package io.smartgear.fabric.client;

import io.smartgear.fabric.exception.HFException;
import io.smartgear.fabric.sdk.SmartGearStore;
import io.smartgear.fabric.sdk.SmartGearUser;
import io.smartgear.fabric.utils.FabricCaUtils;
import io.smartgear.fabric.utils.FabricCaConfigUtils;
import io.smartgear.fabric.utils.LoggerUtils;
import io.smartgear.fabric.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric_ca.sdk.Attribute;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;
import org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException;

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
 * @Package: io.smartgear.fabric.client
 * @version: 1.0 CaClient
 * @author: Mason
 * @Description: 封装CA常用操作，现阶段只有注册用户，和登记证书。
 * @Date: 18-7-30 10:29
 */
public class CaClient {

    private Log logger = LogFactory.getLog(CaClient.class);


    private HFCAClient client; //ca客户短对象

    private SmartGearStore smartGearStore; //缓存配置对象

    private static CaClient caClient = null; //单例模式构造该对象


    private CaClient(){
        this.client = FabricCaUtils.getFabricCaClient(); //实例化ca的client
        this.smartGearStore = SmartGearStore.getInstance(); //获取缓存配置对象
    }

    public static CaClient getInstance()throws HFException{
        if (caClient == null) {
            synchronized (CaClient.class){ //sync关键字，防止多线程对返回对象进行多次实例化。
                if(caClient == null){
                    caClient = new CaClient();
                    try{

                    }catch (Exception e){
                        throw new HFException(HFException.getMessage("[init] Crypto Exception -> ERROR error during Crypto init. ",null, ""),e);
                    }
                }
            }
        }
        return caClient;
    }

    SmartGearUser registerUser(String userName, String password)throws HFException{

        SmartGearUser admin; //定义证书颁发的管理员对象
        try{
            admin = FabricCaUtils.getFabricCaAdminUser(); //获取证书颁发的管理员对象

        }catch (Exception e){
            throw new HFException(HFException.getMessage("[register] Ca Exception -> ERROR error during registerUser init. ",null, userName),e);
        }

//        client.setCryptoSuite(crypto);

        String userId = userName;
        RegistrationRequest regreq = null;
        try{
            regreq = new RegistrationRequest(userId, FabricCaConfigUtils.HF_CA_AFFILIATION);
        }catch (Exception e){
            throw new HFException(HFException.getMessage("[register] Ca Exception -> ERROR error during registrationRequest init. ",null, userName),e);
        }
        regreq.setEnrollmentID(userId); //证书颁发的唯一ID现阶段和用户ID绑定，可以更改策略
//        regreq.setType("user"); //设置类型    不能设置类型，fabric不识别user的类型
        regreq.setAffiliation(FabricCaConfigUtils.HF_CA_AFFILIATION);
        if (!StringUtils.isEmpty(password)){//非空设置密码
            regreq.setSecret(password);
        }
        regreq.addAttribute(new Attribute("company","zxbit"));
        try {
            password = client.register(regreq, admin); //获取注册返回的密码
        }catch (Exception e){
            throw new HFException(HFException.getMessage("[register] Ca Exception -> ERROR error during registerUser. ",null, userName),e);
        }

        SmartGearUser registerUser = null;
        try{
            registerUser = enrollUser(userId,password);
        }catch (Exception e){
            throw new HFException(HFException.getMessage("[enroll] Ca Exception -> ERROR error during enroll. ",null, userName),e);
        }
        if(null != registerUser){
            smartGearStore.setMember(userId,registerUser);
        }

        return registerUser;
    }

    //获取用户
    //如果缓存中没有则从Ca服务器中重新登记证书
    SmartGearUser getUser(String userName, String secret)throws HFException{


        SmartGearUser user;
        user = smartGearStore.getMember(userName);

        if (null == user){
            try{
                user = enrollUser(userName,secret);
            }catch (InvalidArgumentException e){
                logger.warn(LoggerUtils.loggerString("Enroll usual user method Exception, because the parameters is invalid.","getUser", userName));
                return user;
            }catch (EnrollmentException e){
                throw new HFException(HFException.getMessage("[enroll] Ca Exception -> ERROR error during enroll. ",null, userName),e);
            }
        }

        return user;
    }

    //登记用户证书
    private SmartGearUser enrollUser(String userId, String secret)throws EnrollmentException, InvalidArgumentException {
        SmartGearUser enrollUser;

//        if (null == client.getCryptoSuite()){
//            client.setCryptoSuite(crypto);
//        }

        try{
            Enrollment em = this.client.enroll(userId,secret);
            enrollUser = new SmartGearUser(userId,FabricCaConfigUtils.HF_CA_ORGNAME,null);
            enrollUser.setEnrollmentSecret(secret);
            enrollUser.setEnrollment(em);
            enrollUser.setMspId(FabricCaConfigUtils.HF_CA_MSPID);
            enrollUser.setAffiliation("org1.wheel.io");
            smartGearStore.setMember(userId,enrollUser);
        }catch (EnrollmentException e){
            throw new EnrollmentException("Enroll usual user method Error by CaClient.class enrollUser");
        }catch (InvalidArgumentException e){
            throw new InvalidArgumentException("Enroll usual user method Exception, because the parameters is invalid");
        }

        return enrollUser;
    }
}
