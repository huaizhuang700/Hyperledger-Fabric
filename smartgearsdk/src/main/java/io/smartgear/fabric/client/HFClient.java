package io.smartgear.fabric.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.smartgear.fabric.bean.ResultObject;
import io.smartgear.fabric.exception.HFException;
import io.smartgear.fabric.sdk.FabricManager;
import io.smartgear.fabric.sdk.OrgManager;
import io.smartgear.fabric.sdk.SmartGearUser;
import io.smartgear.fabric.utils.FabricCaUtils;
import io.smartgear.fabric.utils.HFClientOperateEnum;
import io.smartgear.fabric.utils.LoggerUtils;
import io.smartgear.fabric.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.TransactionException;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


public class HFClient {

    private static final Log logger = LogFactory.getLog(HFClient.class);

    private static List<Object> sumTime = new ArrayList<>();
    private static FabricManager fabricManager = null;

    /**
     * 获取channel-artifacts配置路径
     *
     * @return /WEB-INF/classes/fabric/channel-artifacts/
     */
    public static String getChannleArtifactsPath() {
        String directorys = FabricManager.class.getClassLoader().getResource("fabric").getFile();
        logger.debug("directorys = " + directorys);
        File directory = new File(directorys);
        logger.debug("directory = " + directory.getPath());
        String ccPath = System.getProperty("user.dir");
        return ccPath + "/src/main/resources/fabric/channel-artifacts/";
    }

    /**
     * 获取crypto-config配置路径
     *
     * @return /WEB-INF/classes/fabric/crypto-config/
     */
    public static String getCryptoConfigPath() {
        String directorys = FabricManager.class.getClassLoader().getResource("fabric").getFile();
        logger.debug("directorys = " + directorys);
        File directory = new File(directorys);
        logger.debug("directory = " + directory.getPath());
        String ccPath = System.getProperty("user.dir");
        return ccPath + "/src/main/resources/fabric/crypto-config/";
    }


    public static FabricManager getFabricManage(SmartGearUser user) throws CryptoException, InvalidArgumentException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, TransactionException, InvalidArgumentException {
        OrgManager orgManager = new OrgManager();
        orgManager
                .init("Org1", true, false)
                .setUser(user, getCryptoConfigPath(), getChannleArtifactsPath())
//		    .setCA("", "http://192.168.1.208:7054") 设置ca服务器，获取用户以及登记用户证书
                .setPeers("Org1", "Org1MSP", "org1.wheel.io")
                .addPeer("peer0.org1.wheel.io", "peer0.org1.wheel.io", "grpcs://192.168.1.213:7051", "grpcs://192.168.1.213:7053", false)
                .addPeer("peer0.org2.wheel.io", "peer0.org2.wheel.io", "grpcs://192.168.1.211:7051", "grpcs://192.168.1.211:7053", false)
                .setOrderers("wheel.io")
                .addOrderer("orderer0.wheel.io", "grpcs://192.168.1.216:7050")
                .setChannel("mychannel")
                .setChainCode("smartchaincode", "/opt/gopath", "github.com/hyperledger/fabric/example/chaincode/go/smartgear_v1.0", "9.3", 90000, 120)
//		    .setChainCode("myc", "/opt/gopath", "github.com/hyperledger/fabric/example/chaincode/go/chaincode_example02", "1.0", 90000, 120)
                .setBlockListener(map -> {
                    logger.debug(map.get("code"));
                    logger.debug(map.get("data"));
                })
                .add();

        FabricManager fabricManager = orgManager.use("Org1");
        return fabricManager;
    }

    public static FabricManager obtain(SmartGearUser user) throws CryptoException, InvalidArgumentException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, TransactionException, InvalidArgumentException {
        if (null == fabricManager) {
            synchronized (HFClient.class) {
                if (null == fabricManager) {
                    fabricManager = getFabricManage(user);
                }
            }
        }
        return fabricManager;
    }


    /**
     * The method to call Fabric that for operate data. for ex:insert or update
     * @param user The user of to call fabric
     * @param hfClientOperateEnum  The enum of to call fabric
     * @param invokeJson  The data of to call fabric that operate
     * @return
     * @throws HFException  The error is maybe happen
     */
    public ResultObject invoke(SmartGearUser user, HFClientOperateEnum hfClientOperateEnum, String invokeJson) throws HFException {

        //The result of return
        ResultObject resultObject = new ResultObject();

        //it is not empty to make sure the parameters
        if (StringUtils.isEmpty(invokeJson)) {
            logger.warn(LoggerUtils.loggerString("The input parameters is empty, please check input value and try again","invoke",invokeJson));

            //return the result if parameters is empty
            resultObject.setCode(10021);
            resultObject.setMessage("The input parameters is empty, please check input value and try again");
            return resultObject;
        }
        try {
            //set value for fabric
            String[] argument2 = new String[]{invokeJson};
            //operate the fabric
            Map<String, String> tempResult = HFClient.obtain(user).invoke(hfClientOperateEnum.getName(), argument2);
            //set the value of return

            JSONObject jsonObject = JSON.parseObject(tempResult.get("data"));

            resultObject.setCode(jsonObject.getInteger("messageCode"));
            resultObject.setResultObj(jsonObject.getString("messageContent"));
            resultObject.setTxId(tempResult.get("txid"));
        } catch (Exception e) {
            throw new HFException(HFException.getMessage("[invoke] Blocks Invoke Exception -> ERROR error during invoke. ", hfClientOperateEnum, invokeJson), e);
        }

        return resultObject;
    }

//	public void upgrade(SmartGearUser user){
//		long a = System.currentTimeMillis();
//		try {
//			String[] argument = new String[] {"{\"phoneNum\": \"13608370406\"}"};
//			System.out.println(Fabric_demo.obtain(user).upgrade(argument));
//			long b = System.currentTimeMillis();
//			sumTime.add(b-a);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

    //区块链按照key统一查询操作
    public ResultObject query(SmartGearUser user, HFClientOperateEnum hfClientOperateEnum, String queryKey) throws HFException {
        ResultObject resultObject = new ResultObject(); //定义返回对象
        Map<String, String> resultMap = null; //定义查询结果集
        if (StringUtils.isEmpty(queryKey)) {    //判断传入的查询参数是否为空
            resultObject.setCode(40011);
            resultObject.setMessage("传入查询参数为空");
            return resultObject;
        }

        try {
            String[] argument0 = new String[]{queryKey}; //设置查询体
            resultMap = HFClient.obtain(user).query(hfClientOperateEnum.getName(), argument0); //进行查询操作
        } catch (Exception e) {
            throw new HFException(HFException.getMessage("[query] Blocks Exception -> ERROR error during query. ", hfClientOperateEnum, queryKey), e);
        }

        JSONObject jsonObject = JSON.parseObject(resultMap.get("data"));
        resultObject.setCode(jsonObject.getInteger("messageCode"));
        resultObject.setResultObj(jsonObject.getString("messageContent"));
        resultObject.setTxId(resultMap.get("txid"));

        return resultObject;
    }


    //txid查询交易信息
    public ResultObject query(SmartGearUser user, String txId)throws HFException{
        ResultObject resultObject = new ResultObject();

        Map<String, String> resultMap = null; //定义查询结果集

        try{
            resultMap = HFClient.obtain(user).queryTransitionByTransactionID(txId);
        }catch (Exception e){
            throw new HFException(HFException.getMessage("[query] Blocks Exception -> ERROR error during query. ", null, txId), e);
        }

        JSONObject jsonObject = JSON.parseObject(resultMap.get("data"));
        resultObject.setCode(jsonObject.getInteger("messageCode"));
        resultObject.setResultObj(jsonObject.getString("messageContent"));
        resultObject.setTxId(resultMap.get("txid"));

        return resultObject;
    }
    //删除操作，隐藏
//	public void delete(){
//		long a = System.currentTimeMillis();
//		try {
//			String[] argument2 = new String[] {"a"};
//			System.out.println(Fabric_demo.obtain().invoke("delete",argument2));
//			long b = System.currentTimeMillis();
//			sumTime.add(b-a);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//}

    //查询历史记录
    public void getHistoryQuery(SmartGearUser user, String queryHistoryStr) {
        long a = System.currentTimeMillis();
        try {

            String[] argument0 = new String[]{queryHistoryStr};
            System.out.println(HFClient.obtain(user).invoke("getHistoryQuery", argument0));
            long b = System.currentTimeMillis();
            sumTime.add(b - a);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

////
//	    @Override
//	    	public void run() {
//	    		// TODO Auto-generated method stub
//	    		//query();
//				try{
//					invoke(FabricCaUtils.getFabricCaAdminUser());
//				}catch (Exception e){
//					e.getMessage();
//				}
//	    	}


    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException, Exception {

        HFClient f = new HFClient();

//			f.invoke(FabricCaUtils.getFabricCaAdminUser(),HFClientOperateEnum.SIMPLE_REGISTER_FABRIC,"{\"userId\":\"123141324512323\",\"phoneNum\":\"1234567123\"}");
//			System.out.println(f.query(FabricCaUtils.getFabricCaAdminUser(),HFClientOperateEnum.QUERY_USUAL_USER_FABRIC,"72da05d8-1d5c-4da0-b604-c2cb0cd28198").getResultObj());
        System.out.println(f.query(FabricCaUtils.getFabricCaAdminUser(), HFClientOperateEnum.QUERY_USUAL_USER_FABRIC, "123141324512323").getResultObj());
//			f.upgrade(FabricCaUtils.getFabricCaAdminUser());

    }
}
