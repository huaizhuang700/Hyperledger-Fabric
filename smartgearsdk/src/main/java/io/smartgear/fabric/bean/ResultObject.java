package io.smartgear.fabric.bean;

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
 * @Package: io.smartgear.fabric.bean
 * @version: 1.0 ResultObject
 * @author: Mason
 * @Description: 返回对象，该类是sdk统一返回对象
 * @Date: 18-7-30 15:02
 */
public class ResultObject {
    //200 统一的成功返回码
    private int code;

    private String message;

    private String txId;

    private Object resultObj;

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResultObj() {
        return resultObj;
    }

    public void setResultObj(Object resultObj) {
        this.resultObj = resultObj;
    }
}
