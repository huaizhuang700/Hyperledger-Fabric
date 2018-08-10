package io.smartgear.fabric.sdk.lru;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * All rights Reserved, Designed By zxbit.cn
 *
 * @version V1.0
 * @Title: LRUCacheMap
 * @Package io.smartgear.fabric.ca.sdkintegration.lru
 * @Description: LRUMap的集合==>用单例模式实例化返回实例，保证多线程下的缓存配置.
 * @author: 致行科技
 * @date: 18-7-6
 * @Copyright: All rights Reserved, Designed By zxbit.cn
 * 注意：本内容仅限于北京中睿致行科技技术股份有限公司传阅，禁止外泄以及用于其他的商业目的
 */
public class LRUCacheMap<K, V> {

    private static LRUCacheMap lruCacheMap = null; //单例模式返回的对象

    private final int MAX_CACHE_SIZE; //Map最大容量

    private Entry<K, V> head; //头部节点对象
    private Entry<K, V> tail; //尾部节点对象

    private HashMap<K, Entry<K, V>> cache; //缓存变量

    private final Lock lruLock = new ReentrantLock(); //线程锁，实现线程安全

    private LRUCacheMap(int maxCacheSize){ //构造方法，传入值为缓存的Map大小
        this.MAX_CACHE_SIZE = maxCacheSize;
        cache = new HashMap<>();
    }

    public static LRUCacheMap getInstance(){
        if (lruCacheMap == null) {
            synchronized (LRUCacheMap.class){ //sync关键字，防止多线程对返回对象进行多次实例化。
                if(lruCacheMap == null){
                    lruCacheMap = new LRUCacheMap(5000);
                }
            }
        }
        return lruCacheMap;
    }

    public V get(K key) {
        Entry<K, V> entry = getEntry(key);
        if (entry == null) {
            return null;
        }
        moveToHead(entry);
        return entry.value;
    }

    public void remove(K key) {
        Entry<K, V> entry = getEntry(key);
        if(lruLock.tryLock()){
            try{
                if (entry != null) {
                    if (entry == head) {
                        Entry<K, V> next = head.next;
                        head.next = null;
                        head = next;
                        head.pre = null;
                    } else if (entry == tail) {
                        Entry<K, V> prev = tail.pre;
                        tail.pre = null;
                        tail = prev;
                        tail.next = null;
                    } else {
                        entry.pre.next = entry.next;
                        entry.next.pre = entry.pre;
                    }
                    cache.remove(key);
                }
            }catch (Exception e){
                throw new RuntimeException("error for get lock");
            }finally {
                lruLock.unlock();
            }
        }
    }

    public void put(K key, V value) {
        Entry<K, V> entry = getEntry(key);
        if(lruLock.tryLock()){
            try{
                if (entry == null) {
                    if (cache.size() >= MAX_CACHE_SIZE) {
                        cache.remove(tail.key);
                        removeTail();
                    }
                    entry = new Entry<>();
                    entry.key = key;
                    entry.value = value;
                    moveToHead(entry);
                    cache.put(key, entry);
                } else {
                    entry.value = value;
                    moveToHead(entry);
                }
            }catch (Exception e){
                throw new RuntimeException("error for get lock");
            }finally {
                lruLock.unlock();
            }
        }
    }

    private void removeTail() {
        if(lruLock.tryLock()){
            try{
                if (tail != null) {
                    Entry<K, V> prev = tail.pre;
                    if (prev == null) {
                        head = null;
                        tail = null;
                    } else {
                        tail.pre = null;
                        tail = prev;
                        tail.next = null;
                    }
                }
            }catch (Exception e){
                throw new RuntimeException("error for get lock");
            }finally {
                lruLock.unlock();
            }
        }

    }

    private void moveToHead(Entry<K, V> entry) {
       if(lruLock.tryLock()){
           try{
                if (entry == head) {
                    return;
                }
                if (entry.pre != null) {
                    entry.pre.next = entry.next;
                }
                if (entry.next != null) {
                    entry.next.pre = entry.pre;
                }
                if (entry == tail) {
                    Entry<K, V> prev = entry.pre;
                    if (prev != null) {
                        tail.pre = null;
                        tail = prev;
                        tail.next = null;
                    }
                }

                if (head == null || tail == null) {
                    head = tail = entry;
                    return;
                }

                entry.next = head;
                head.pre = entry;
                entry.pre = null;
                head = entry;
           }catch (Exception e){
               throw new RuntimeException("error for get lock");
           }finally {
               lruLock.unlock();
           }
       }

    }


    private static class Entry<K, V> {
        Entry<K, V> pre;
        Entry<K, V> next;
        K key;
        V value;
    }

    private Entry<K, V> getEntry(K key) {
        return cache.get(key);
    }
}
