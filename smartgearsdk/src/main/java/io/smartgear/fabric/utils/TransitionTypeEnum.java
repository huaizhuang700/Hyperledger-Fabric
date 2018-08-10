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
 * @version: 1.0 TransitionEnum
 * @author: Mason
 * @Description: 交易类型枚举类==>交易类型 0:创建，1：解抵，2：抵押，3：交易（租车），4：分红
 * @Date: 18-8-7 10:21
 */
public enum TransitionTypeEnum {
    //解抵操作
    TRANSITION_RESOLVE(1),

    //抵押操作
    TRANSITION__MORTGAGE(2),

    //用户交易
    TRANSITION_USER(3),

    //用户分红
    TRANSITION_SHARE(4);

    public static final int TRANSITION_RESOLVE_VALUE = 1;

    public static final int TRANSITION__MORTGAGE_VALUE = 2;

    public static final int TRANSITION_USER_VALUE = 3;

    public static final int TRANSITION_SHARE_VALUE = 4;


    private int typeValue;

    private TransitionTypeEnum(int typeValue) {
        this.typeValue = typeValue;
    }

    public int getValue(){
        return this.typeValue;
    }


}
