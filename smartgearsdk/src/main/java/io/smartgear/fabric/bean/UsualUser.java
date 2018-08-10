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
 * @version: 1.0 UsualUser
 * @author: Mason
 * @Description: Fabric联盟链普通用户实体，需要用该实体来进行链上用户进行添加、删除、修改等操作s
 * @Date: 18-7-30 11:00
 */
public class UsualUser {

    //用户的唯一身份标志
    private String userId;

    //用户注册的手机号
    private String phoneNum;

    //用户名称
    private String userName;

    //用户身份号码
    private String userIdCardNum;

    //用户身份类型  1:身份证
    private String userIdType;

    //用户联系的手机号,可能与用户注册的手机号不一样
    private String joinPhoneNum;

    //用户的资产账户
    private String userAccountId;

    //用户来源
    private int comeFromCompany;

    //用户身份认证激励标志
    private int identityReward;

    //Gear币账户绑定励标志
    private int gearAccountReward;

    //备注激励（其他激励方式：备用）
    private int remarkReward;

    //用户的gear币地址
    private String gearAddress;

    //用户身份证照片,主要存放照片hash值
    private String userIdCardImage;

    //用户驾驶证照片，主要存放照片的hash值
    private String userDriverImage;

    //创建时间以后不予以更改
    private long createTime;

    //修改时间
    private long updateTime;

    //用户信用积分
    private int creditScore;

    //是否注销
    private int isDelete;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIdCardNum() {
        return userIdCardNum;
    }

    public void setUserIdCardNum(String userIdCardNum) {
        this.userIdCardNum = userIdCardNum;
    }

    public String getUserIdType() {
        return userIdType;
    }

    public void setUserIdType(String userIdType) {
        this.userIdType = userIdType;
    }

    public String getJoinPhoneNum() {
        return joinPhoneNum;
    }

    public void setJoinPhoneNum(String joinPhoneNum) {
        this.joinPhoneNum = joinPhoneNum;
    }

    public String getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(String userAccountId) {
        this.userAccountId = userAccountId;
    }

    public int getComeFromCompany() {
        return comeFromCompany;
    }

    public void setComeFromCompany(int comeFromCompany) {
        this.comeFromCompany = comeFromCompany;
    }

    public int getIdentityReward() {
        return identityReward;
    }

    public void setIdentityReward(int identityReward) {
        this.identityReward = identityReward;
    }

    public int getGearAccountReward() {
        return gearAccountReward;
    }

    public void setGearAccountReward(int gearAccountReward) {
        this.gearAccountReward = gearAccountReward;
    }

    public int getRemarkReward() {
        return remarkReward;
    }

    public void setRemarkReward(int remarkReward) {
        this.remarkReward = remarkReward;
    }

    public String getGearAddress() {
        return gearAddress;
    }

    public void setGearAddress(String gearAddress) {
        this.gearAddress = gearAddress;
    }

    public String getUserIdCardImage() {
        return userIdCardImage;
    }

    public void setUserIdCardImage(String userIdCardImage) {
        this.userIdCardImage = userIdCardImage;
    }

    public String getUserDriverImage() {
        return userDriverImage;
    }

    public void setUserDriverImage(String userDriverImage) {
        this.userDriverImage = userDriverImage;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(int creditScore) {
        this.creditScore = creditScore;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }
}
