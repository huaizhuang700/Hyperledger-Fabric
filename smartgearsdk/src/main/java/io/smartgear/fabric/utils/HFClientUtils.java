package io.smartgear.fabric.utils;

import io.smartgear.fabric.sdk.SmartGearStore;
import io.smartgear.fabric.sdk.SmartGearUser;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import java.util.Collection;


public class HFClientUtils {

    public static HFClient newInstance() throws Exception {

        HFClient hfclient = HFClient.createNewInstance();
        setupClient(hfclient);

        return hfclient;
    }

    public static void setupClient(HFClient hfclient) throws Exception {

        SmartGearUser someTestUSER = FabricCaUtils.getFabricCaAdminUser(); //获取证书颁发的管理员对象
        someTestUSER.setMspId(FabricCaConfigUtils.HF_CA_MSPID);

        hfclient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
        hfclient.setUserContext(someTestUSER);
    }


    public static void main(String[] args) throws Exception{
        HFClient hfclient = HFClientUtils.newInstance();
        Channel channel = hfclient.getChannel("mychannel");

        Collection<Peer> test= channel.getPeers();

        for (Peer c:test) {
            System.out.println(c.getName());
        }
    }
}
