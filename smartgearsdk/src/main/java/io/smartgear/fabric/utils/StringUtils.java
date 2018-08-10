package io.smartgear.fabric.utils;

/**
 * Copyright 2018 by zxbit.cn
 * All rights reserved.
 * <p>
 * This software is the confidential and proprietary information of
 * ZXBIT ("Confidential Information").  You shall not disclose such
 * Confidential Information and shall use it only in accordance with
 * the terms of the license agreement you entered into with ZXBIT.
 * <p>
 * Modification History:
 * Date             Author      Version         Description
 * ------------------------------------------------------------------
 * 18-8-7            Mason      1.0            ADD
 *
 * @Package: io.smartgear.fabric.utils
 * @version: 1.0 StringUtils
 * @author: Mason
 * @Description: 字符串工具类
 * @Date: 18-8-7 14:00
 */
public class StringUtils {

    /**
     * 判断字符串是否为空，如果为空返回true，如果不为空则返回false
     * @param parameter
     * @return
     */
    public static boolean isEmpty(String parameter){
        boolean flag = true;
        if (null != parameter && !("".equals(parameter))){
            flag = false;
        }
        return flag;
    }

}
