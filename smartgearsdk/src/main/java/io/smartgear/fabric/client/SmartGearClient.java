package io.smartgear.fabric.client;

import com.alibaba.fastjson.JSON;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import io.smartgear.fabric.bean.ResultObject;
import io.smartgear.fabric.bean.Transition;
import io.smartgear.fabric.bean.UsualUser;
import io.smartgear.fabric.exception.HFException;
import io.smartgear.fabric.sdk.SmartGearUser;
import io.smartgear.fabric.utils.HFClientOperateEnum;
import io.smartgear.fabric.utils.LoggerUtils;
import io.smartgear.fabric.utils.TransitionTypeEnum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.UUID;


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
 * @Package: io.smartgear.fabric.client
 * @version: 1.0 SmartGearClient
 * @author: Mason
 * @Description: 关于SmartGear的业务级别的代码封装
 * @Date: 18-7-31 18:07
 */
public class SmartGearClient {

    private static final Log logger = LogFactory.getLog(HFClient.class);

    /**
     * 注册用户信息，当ca和fabric全部返回成功才返回成功
     * @param usualUser fabric注册的用户VO类
     * @param password 注册ca所需要的密码（登记证书使用）
     * @return
     * @throws HFException
     */
    public static ResultObject simpleRegisterUsualUser(UsualUser usualUser, String password)throws HFException {
        //定义返回结果对象
        ResultObject resultObject = null;

        String uuidString = "0xsg" + UUID.randomUUID().toString().replaceAll("-","");
        //创建ca客户端，用等注册、登记ca
        CaClient caClient = CaClient.getInstance();

        //注册ca对象
        //ca注册成功返回注册成功的ca用户实体
        SmartGearUser user = caClient.registerUser(uuidString, password);

        //return resultObject if failed to register
        if (null == user){
            logger.info(LoggerUtils.loggerString("Failed to register usual user, maybe the parameters is invalid.","registerUsualUser", usualUser.getJoinPhoneNum()));
            resultObject = new ResultObject();
            resultObject.setCode(10041);
            resultObject.setMessage("Failed to register usual user, maybe the parameters is invalid.");
            return resultObject;
        }

        //补全用户注册添加信息
        usualUser.setCreateTime(System.currentTimeMillis()/1000);
        usualUser.setUserId(uuidString);
        usualUser.setIsDelete(0);

        //转换成json
        String jsonString = JSON.toJSONString(usualUser);

        //获取fabric的客户端
        HFClient hfClient = new HFClient();
        resultObject = hfClient.invoke(user,HFClientOperateEnum.SIMPLE_REGISTER_FABRIC,jsonString);
        return resultObject;
    }


    /**
     * 修改用户信息
     * @param usualUser 需要修改的用户对象
     * @param password 用户ca证书的密码
     * @return
     * @throws HFException
     */
    public static ResultObject updateUsualUserInfo(UsualUser usualUser, String password)throws HFException{
        //定义返回对象
        ResultObject resultObject = null;

        //创建ca的客户端
        CaClient caClient = CaClient.getInstance();

        //获取调用fabric的用户证书对象
        SmartGearUser user = caClient.getUser(usualUser.getUserId(), password);

        //设置修改时间
        usualUser.setUpdateTime(System.currentTimeMillis()/1000);

        //转化json
        String jsonString = JSON.toJSONString(usualUser);

        //get the client from Fabric sdk
        HFClient hfClient = new HFClient();
        resultObject = hfClient.invoke(user,HFClientOperateEnum.UPDATE_USUAL_USER_FABRIC,jsonString);

        return resultObject;
    }

    /**
     * 查询用户信息
     * @param userId    用户Id，联盟链上操作人主键
     * @param password  用户密码，可能用于证书的颁发（如果缓存中不存在用户会用ca进行证书的重新颁发）
     * @return
     * @throws HFException
     */
    public static ResultObject queryUsualUserInfo(String userId, String password)throws HFException{
        //定义返回对象
        ResultObject resultObject = null;

        //创建ca的客户端
        CaClient caClient = CaClient.getInstance();

        //获取调用fabric的用户证书对象
        SmartGearUser user = caClient.getUser(userId, password);

        HFClient hfClient = new HFClient();
        return hfClient.query(user, HFClientOperateEnum.QUERY_USUAL_USER_FABRIC,userId);
    }


    /**
     * 交易方法操作
     * @param userId    交易人联盟链主键
     * @param password  交易人证书密钥
     * @param transition    交易实体
     * @return
     * @throws HFException
     */
    public static ResultObject transition(String userId, String password, Transition transition) throws HFException{
        ResultObject resultObject = new ResultObject();
        logger.info(LoggerUtils.loggerString("Transition commit info","transition", transition.toString()));

        //创建ca的客户端
        CaClient caClient = CaClient.getInstance();
        //获取调用fabric的用户证书对象
        SmartGearUser user = caClient.getUser(userId, password);
        logger.info(LoggerUtils.loggerString("Transition commit user","transition", user.getName()));

        //把交易信息转化为json
        String jsonString = JSON.toJSONString(transition);

        //创建HF客户端
        HFClient hfClient = new HFClient();
        switch (transition.getTransitionType()){
            case TransitionTypeEnum.TRANSITION__MORTGAGE_VALUE :{
                logger.info(LoggerUtils.loggerString("Transition commit MORTGAGE","transition", user.getName()));
                resultObject = hfClient.invoke(user,HFClientOperateEnum.TRANSITION_MORTGAGE_FABRIC,jsonString);
                return resultObject;
            }
            case TransitionTypeEnum.TRANSITION_RESOLVE_VALUE :{
                logger.info(LoggerUtils.loggerString("Transition commit RESOLVE","transition", user.getName()));
                resultObject = hfClient.invoke(user,HFClientOperateEnum.TRANSITION_RESOLVE_MORTGAGE_FABRIC,jsonString);
                return resultObject;
            }
            case TransitionTypeEnum.TRANSITION_USER_VALUE :{
                break;
            }
            case TransitionTypeEnum.TRANSITION_SHARE_VALUE :{
                break;
            }
            default :{
                logger.error(LoggerUtils.loggerString("Transition commit error, except the value is 1,2,3,4.But got the value is "+transition.getTransitionType(),"transition",""));
                resultObject.setCode(10022);
                resultObject.setMessage("Transition commit error, except the value is 1,2,3,4.But got the value is "+transition.getTransitionType());
                return resultObject;
            }
        }


        return resultObject;
    }


    /**
     * 查询用户资产账户
     * @param userId
     * @param password
     * @return
     * @throws HFException
     */
    public static ResultObject queryUserAccount(String userId, String password)throws HFException{
        ResultObject resultObject = null;

        logger.info(LoggerUtils.loggerString("query user account","queryUserAccount", userId));

        //创建ca的客户端
        CaClient caClient = CaClient.getInstance();
        //获取调用fabric的用户证书对象
        SmartGearUser user = caClient.getUser(userId, password);
        logger.info(LoggerUtils.loggerString("query user account","queryUserAccount", user.getName()));
        HFClient hfClient = new HFClient();

        resultObject = hfClient.query(user,HFClientOperateEnum.QUERY_USER_ACCOUNT_FABRIC,userId);

        return resultObject;
    }

    public static ResultObject queryBlockByTxId(String userId, String password,String txId)throws HFException{
        ResultObject resultObject = null;

        CaClient caClient = CaClient.getInstance();
        //获取调用fabric的用户证书对象
        SmartGearUser user = caClient.getUser(userId, password);
        logger.info(LoggerUtils.loggerString("query user account","queryUserAccount", user.getName()));
        HFClient hfClient = new HFClient();

        resultObject = hfClient.query(user,txId);

        return resultObject;
    }

    //测试证书
    public ResultObject testCert(String userId, String password) throws HFException{
        ResultObject resultObject = new ResultObject();
        CaClient caClient = CaClient.getInstance();
        SmartGearUser user = caClient.getUser(userId, password);
        HFClient hfClient = new HFClient();
        resultObject = hfClient.query(user,HFClientOperateEnum.Test_USER_CERT,"123");



        return resultObject;
    }

    public static void main(String[] args)throws HFException {
//        UsualUser usualUser = new UsualUser();
//        usualUser.setCreateTime(1243512312);
//        usualUser.setPhoneNum("13247158487");
//        System.out.println(JSONObject.fromObject(usualUser).toString());
//        ResultObject resultObject = SmartGearClient.simpleRegisterUsualUser(usualUser,"123456");
//        System.out.println(resultObject);
//        System.out.println(resultObject.getResultObj());
//        System.out.println(resultObject.getCode());
//        System.out.println(resultObject.getTxId());

//        SmartGearClient.queryUsualUserInfo("0xsg1d0d77118b294fb0871d66aed16d7f98","123456");






//        Transition transition = new Transition();
//        transition.setUserId("0xsg1d0d77118b294fb0871d66aed16d7f98");
//        transition.setTransitionType(TransitionTypeEnum.TRANSITION__MORTGAGE);
//        transition.setWheelCount(1000);
//        transition.setGearCount(3000);
//        transition.setTransitionTime(1533630438000l);
//        System.out.println(SmartGearClient.transition("0xsg1d0d77118b294fb0871d66aed16d7f98","123456",transition).getResultObj());


//        Transition transition = new Transition();
//        transition.setUserId("0xsg1d0d77118b294fb0871d66aed16d7f98");
//        transition.setTransitionType(TransitionTypeEnum.TRANSITION_RESOLVE);
//        transition.setWheelCount(500);
//        transition.setTransitionTime(1533630458000l);
//        System.out.println(SmartGearClient.transition("0xsg1d0d77118b294fb0871d66aed16d7f98","123456",transition).getResultObj());




        SmartGearClient.queryBlockByTxId("0xsg1d0d77118b294fb0871d66aed16d7f98","123456","98ae0fae9c949a0e186ef3c8d86a3dbce563c6f6f05d438e1046f7cf605f492c");




//        System.out.println(SmartGearClient.testCert("0xsg1d0d77118b294fb0871d66aed16d7f98","123456").getResultObj());



    }

}
