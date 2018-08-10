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
 * 18-7-31            Mason      1.0            ADD
 *
 * @Package: io.smartgear.fabric.utils
 * @version: 1.0 LoggerUtils
 * @author: Mason
 * @Description: 日志格式化输出类
 * @Date: 18-7-31 18:21
 */
public class LoggerUtils {


    public static String loggerString(String message, String methodName, String parameters, String... elseInfo){
        StringBuffer sb = new StringBuffer(); //定义字符串操作对象

        if (!StringUtils.isEmpty(message)){
            sb.append(message).append(" ");
        }

        if (!StringUtils.isEmpty(methodName)){
            sb.append("==>The Method name is: ").append(methodName).append(" ");
        }

        if (!StringUtils.isEmpty(parameters)){
            sb.append("==>The Parameters is: ").append(parameters).append(" ");
        }

        for (String str : elseInfo) {
            sb.append("==>The Else info is: ").append(str).append(" ");
        }

        return sb.toString();
    }

}
