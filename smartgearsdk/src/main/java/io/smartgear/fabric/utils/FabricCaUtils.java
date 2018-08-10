package io.smartgear.fabric.utils;

import io.smartgear.fabric.exception.HFException;
import io.smartgear.fabric.sdk.SmartGearStore;
import io.smartgear.fabric.sdk.SmartGearUser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.fabric.sdk.security.CryptoPrimitives;
import org.hyperledger.fabric_ca.sdk.HFCAClient;

import java.io.File;
import java.net.MalformedURLException;


public class FabricCaUtils {

    private static final Log log = LogFactory.getLog(FabricCaUtils.class);

    private static HFCAClient client  = null;

    private static SmartGearUser admin = null;

    private static CryptoPrimitives crypto;
    /*
     * Fabric初始化CA的客户端工具
     */
    public static HFCAClient getFabricCaClient(){
        try{
            if(null == client){
                client = HFCAClient.createNewInstance(FabricCaConfigUtils.HF_CA_URL,null);
                try{
                    crypto = new CryptoPrimitives();
                    crypto.init();
                }catch (Exception e){
                    log.error("init HFCAClient failed exception:" + e.getStackTrace());
                    return null;
                }
                client.setCryptoSuite(crypto);
            }
        }catch (MalformedURLException e){
            log.error("init HFCAClient failed exception:" + e.getStackTrace());
            return client;
        }
        return client;
    }


    /*
     * 通过Store的证书方式来获取指定用户
     */
    public static SmartGearUser getFabricCaAdminUser() throws Exception{
        SmartGearStore smartGearStore = SmartGearStore.getInstance();
        if (null == admin){
            admin = smartGearStore.getMember("Admin@org1.wheel.io","Org1","Org1MSP",getCertificatePrivateKey(),getCertificate());
        }
        return admin;
    }


    //根据文件路径读取管理员的签名证书
    private static File getCertificatePrivateKey() {
        String directorys = "";
        directorys = System.getProperty("user.dir") + "/src/main/resources/fabric/crypto-config/peerOrganizations/org1.wheel.io/users/Admin@org1.wheel.io/msp/keystore/81002a83966d5e952e64bbfd61b331d7b5d030fc851c6bdb54b9b0fce4e50192_sk";
        File directory = new File(directorys);
        return directory;
    }

    //根据文件路径读取管理员的证书
    private static File getCertificate() {
        String directorys = "";
        directorys = System.getProperty("user.dir") + "/src/main/resources/fabric/crypto-config/peerOrganizations/org1.wheel.io/users/Admin@org1.wheel.io/msp/signcerts/Admin@org1.wheel.io-cert.pem";
        File directory = new File(directorys);
        return directory;
    }

}
