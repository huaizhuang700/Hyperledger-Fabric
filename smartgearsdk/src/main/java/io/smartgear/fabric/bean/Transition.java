package io.smartgear.fabric.bean;

import io.smartgear.fabric.utils.TransitionTypeEnum;

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
 * @Package: io.smartgear.fabric.bean
 * @version: 1.0 ResolveMortgage
 * @author: Mason
 * @Description: 交易实体，包含各种交易类型操作
 * @Date: 18-8-7 10:11
 */
public class Transition {

    //用户Id
    private String userId;

    //用户资产账户Id(不填，在链上确定值)
    private String accountId;

    //冻结的wheel币数量
    private float wheelCount;

    //用户冻结的gear币数量
    private float gearCount;

    //交易类型
    private int transitionType;

    //交易来源人
    private String transitionComeFromAddress;

    //交易来源人姓名
    private String transitionComeFromUserName;

    //交易去向人地址
    private String transitionToAddress;

    //交易去向人姓名
    private String transitionToUserName;

    //实时gear与wheel币兑换比例
    private float gearWheelPercent;

    //发生交易时间戳
    private long transitionTime;

    //交易备注
    private String transitionDesc;

    public String getTransitionDesc() {
        return transitionDesc;
    }

    public void setTransitionDesc(String transitionDesc) {
        this.transitionDesc = transitionDesc;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public float getWheelCount() {
        return wheelCount;
    }

    public void setWheelCount(float wheelCount) {
        this.wheelCount = wheelCount;
    }

    public float getGearCount() {
        return gearCount;
    }

    public void setGearCount(float gearCount) {
        this.gearCount = gearCount;
    }

    public int getTransitionType() {
        return transitionType;
    }

    public void setTransitionType(TransitionTypeEnum transitionTypeEnum) {
        this.transitionType = transitionTypeEnum.getValue();
    }

    public String getTransitionComeFromAddress() {
        return transitionComeFromAddress;
    }

    public void setTransitionComeFromAddress(String transitionComeFromAddress) {
        this.transitionComeFromAddress = transitionComeFromAddress;
    }

    public String getTransitionComeFromUserName() {
        return transitionComeFromUserName;
    }

    public void setTransitionComeFromUserName(String transitionComeFromUserName) {
        this.transitionComeFromUserName = transitionComeFromUserName;
    }

    public String getTransitionToAddress() {
        return transitionToAddress;
    }

    public void setTransitionToAddress(String transitionToAddress) {
        this.transitionToAddress = transitionToAddress;
    }

    public String getTransitionToUserName() {
        return transitionToUserName;
    }

    public void setTransitionToUserName(String transitionToUserName) {
        this.transitionToUserName = transitionToUserName;
    }

    public float getGearWheelPercent() {
        return gearWheelPercent;
    }

    public void setGearWheelPercent(float gearWheelPercent) {
        this.gearWheelPercent = gearWheelPercent;
    }

    public long getTransitionTime() {
        return transitionTime;
    }

    public void setTransitionTime(long transitionTime) {
        this.transitionTime = transitionTime;
    }

    public String toString(){
        StringBuffer sb = new StringBuffer();

        sb.append("{");
        sb.append("userId:"+this.userId+",");
        sb.append("wheelCount:"+this.wheelCount+",");
        sb.append("gearCount:"+this.gearCount+",");
        sb.append("transitionType:"+this.transitionType+",");
        sb.append("transitionComeFromAddress:"+this.transitionComeFromAddress+",");
        sb.append("transitionComeFromUserName:"+this.transitionComeFromUserName+",");
        sb.append("transitionToAddress:"+this.transitionToAddress+",");
        sb.append("transitionToUserName:"+this.transitionToUserName+",");
        sb.append("gearWheelPercent:"+this.gearWheelPercent+",");
        sb.append("transitionTime:"+this.transitionTime+",");
        sb.append("}");

        return sb.toString();
    }
}
