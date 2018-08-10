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
 * 18-7-30            Mason      1.0            ADD
 *
 * @Package: io.smartgear.fabric.utils
 * @version: 1.0 Test
 * @author: Mason
 * @Description: 添加描述
 * @Date: 18-7-30 10:29
 */
public enum HFClientOperateEnum {

    //简单注册
    SIMPLE_REGISTER_FABRIC("simpleRegister"),

    //用户认证
    UPDATE_USUAL_USER_FABRIC("updateUser"),

    //交易抵押
    TRANSITION_MORTGAGE_FABRIC("transitionMortgage"),

    //交易解抵
    TRANSITION_RESOLVE_MORTGAGE_FABRIC("transitionResolveMortgage"),

    //查询用户
    QUERY_USUAL_USER_FABRIC("queryUsualUser"),

    //查询用户资产
    QUERY_USER_ACCOUNT_FABRIC("queryUserAccount"),

    //测试用户证书
    Test_USER_CERT("testCertificate");

    private String name;

    private HFClientOperateEnum(String name) {
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

}
