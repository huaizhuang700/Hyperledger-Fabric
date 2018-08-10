package io.smartgear.fabric.exception;

import io.smartgear.fabric.utils.HFClientOperateEnum;
import io.smartgear.fabric.utils.StringUtils;

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
 * 18-7-30            Mason      1.0            ADD
 *
 * @Package: io.smartgear.fabric.exception
 * @version: 1.0 HFException
 * @author: Mason
 * @Description: 添加描述
 * @Date: 18-7-30 15:31
 */
public class HFException extends Exception {

    public HFException(String message) {
        super(message);
    }

    public HFException(String message, Exception e) {

        super(message,e);
    }

    public static String  getMessage(String message, HFClientOperateEnum hfClientOperateEnum, String parameter){
        if (null != hfClientOperateEnum){
            message += "The method  ==>" + hfClientOperateEnum.getName();
        }
        if (!StringUtils.isEmpty(parameter)){
            message += "The Patameters  ==>" + parameter;
        }

        return message;
    }

}
