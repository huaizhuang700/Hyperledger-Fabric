package io.smartgear.fabric.sdk;

import io.smartgear.fabric.sdk.lru.LRUCacheMap;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.hyperledger.fabric.sdk.Enrollment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;


public class SmartGearStore {

    private Log logger = LogFactory.getLog(SmartGearStore.class);

    private static SmartGearStore smartGearStore = null;

    private SmartGearStore() {
    }

    public static SmartGearStore getInstance() { //单例模式构建缓存对象
        if (null == smartGearStore) {
            synchronized (LRUCacheMap.class) { //sync关键字，防止多线程对返回对象进行多次实例化。
                if (null == smartGearStore) {
                    smartGearStore = new SmartGearStore();
                }
            }
        }
        return smartGearStore;
    }


    private LRUCacheMap<String, SmartGearUser> members = LRUCacheMap.getInstance();

    /**
     * 根据用户名获取缓存用户
     *
     * @param name
     * @return
     */
    public SmartGearUser getMember(String name) {
        SmartGearUser smartGearUser = members.get(name);
        return smartGearUser;
    }

    /**
     * 根据用户名称设置缓存，如果不存在的话
     *
     * @param name
     * @param smartGearUser
     */
    public void setMember(String name, SmartGearUser smartGearUser) {
        if (null == members.get(name)) {
            members.put(name, smartGearUser);
        }
    }


    /**
     * 根据证书和私钥获取用户
     *
     * @param name
     * @param org
     * @param mspId
     * @param privateKeyFile
     * @param certificateFile
     * @return user
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws InvalidKeySpecException
     */
    public SmartGearUser getMember(String name, String org, String mspId, File privateKeyFile,
                                   File certificateFile) throws IOException {

        try {
            SmartGearUser smartGearUser = members.get(name);
            if (null != smartGearUser) {
                return smartGearUser;
            }

            smartGearUser = new SmartGearUser(name, org, null);
            smartGearUser.setMspId(mspId);
            String certificate = new String(IOUtils.toByteArray(new FileInputStream(certificateFile)), "UTF-8");
            PrivateKey privateKey = getPrivateKeyFromBytes(IOUtils.toByteArray(new FileInputStream(privateKeyFile)));
            smartGearUser.setEnrollment(new SmartGearStoreEnrollment(privateKey, certificate));

            members.put(name, smartGearUser);
            return smartGearUser;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;

        } catch (ClassCastException e) {
            e.printStackTrace();
            throw e;
        }

    }

    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    //读取证书文件工具方法
    static PrivateKey getPrivateKeyFromBytes(byte[] data) throws IOException {
        final Reader pemReader = new StringReader(new String(data));
        final PrivateKeyInfo pemPair;
        try (PEMParser pemParser = new PEMParser(pemReader)) {
            pemPair = (PrivateKeyInfo) pemParser.readObject();
        }

        PrivateKey privateKey = new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).getPrivateKey(pemPair);
        System.out.println("私钥:" + privateKey);
        return privateKey;
    }


    static final class SmartGearStoreEnrollment implements Enrollment, Serializable {

        private static final long serialVersionUID = -2784835212445309006L;
        private final PrivateKey privateKey;
        private final String certificate;

        SmartGearStoreEnrollment(PrivateKey privateKey, String certificate) {

            this.certificate = certificate;

            this.privateKey = privateKey;
        }

        @Override
        public PrivateKey getKey() {

            return privateKey;
        }

        @Override
        public String getCert() {
            return certificate;
        }

    }








    /*=================================================================================*/
    /*======================以下为channel部分的代码，上部为用户==============================*/
    /*=================================================================================*/

//    public void saveChannel(Channel channel) throws Exception {
//
//        setValue("channel." + channel.getName(), Hex.toHexString(channel.serializeChannel()));
//
//    }
//
//    Channel getChannel(HFClient client, String name) throws Exception{
//        Channel ret = null;
//
//        String channelHex = getValue("channel." + name);
//        if (channelHex != null) {
//
//            ret = client.deSerializeChannel(Hex.decode(channelHex));
//
//        }
//        return ret;
//    }

//    public void storeClientPEMTLSKey(SmartGearOrg smartGearOrg, String key) {
//        setValue("clientPEMTLSKey." + smartGearOrg.getName(), key);
//
//    }
//
//    public String getClientPEMTLSKey(SmartGearOrg smartGearOrg) {
//        return getValue("clientPEMTLSKey." + smartGearOrg.getName());
//
//    }
//
//    public void storeClientPEMTLCertificate(SmartGearOrg smartGearOrg, String certificate) {
//        setValue("clientPEMTLSCertificate." + smartGearOrg.getName(), certificate);
//
//    }
//
//    public String getClientPEMTLSCertificate(SmartGearOrg smartGearOrg) {
//        return getValue("clientPEMTLSCertificate." + smartGearOrg.getName());
//
//    }

}
