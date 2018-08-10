package io.smartgear.fabric.sdk;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 描述：中继组织对象
 */
class IntermediateOrg {

    private static final Log log = LogFactory.getLog(IntermediateOrg.class);

    /**
     * orderer 排序服务器所在根域名，如：example.com
     */
    private String ordererDomainName;
    /**
     * orderer 排序服务器集合
     */
    private List<SmartGearOrderer> orderers = new LinkedList<>();

    /**
     * 当前指定的组织名称，如：Org1
     */
    private String orgName;
    /**
     * 当前指定的组织名称，如：Org1MSP
     */
    private String orgMSPID;
    /**
     * 当前指定的组织所在根域名，如：org1.example.com
     */
    private String orgDomainName;
    /**
     * orderer 排序服务器集合
     */
    private List<SmartGearPeer> peers = new LinkedList<>();

    /**
     * 是否开启TLS访问
     */
    private boolean openTLS;

    /**
     * 频道对象
     */
    private SmartGearChannel channel;

    /**
     * 智能合约对象
     */
    private IntermediateChainCodeID chaincode;
    /**
     * 事件监听
     */
    private BlockListener blockListener;

    /**
     * channel-artifacts所在路径
     */
    private String channelArtifactsPath;
    /**
     * crypto-config所在路径
     */
    private String cryptoConfigPath;

    /**
     * 当前指定的组织节点ca名称
     */
    private String caName;
    /**
     * 当前指定的组织节点ca访问地址（http://110.131.116.21:7054）
     */
    private String caLocation;
    /**
     * 是否开启CA TLS访问
     */
    private boolean openCATLS;

    private HFClient client;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    void init() {
//        Properties properties = null;
//        if (openCATLS) {
//            File caCert = Paths.get(cryptoConfigPath, "/peerOrganizations/", getOrgDomainName(), String.format("/tlsca/tlsca.%s-cert.pem", getOrgDomainName())).toFile();
//            if (!caCert.exists() || !caCert.isFile()) {
//                throw new RuntimeException("TEST is missing cert file " + caCert.getAbsolutePath());
//            }
//            properties = new Properties();
//            properties.setProperty("pemFile", caCert.getAbsolutePath());
//            // properties.setProperty("allowAllHostNames", "true"); // 仅用于测试环境
//        }

//        HFCAInfo info = caClient.info(); // 检查一下我们是否连接

//        IntermediateUser admin = fabricStore.getMember("admin", orgName);
//        if (!admin.isEnrolled()) {
//            admin.setEnrollment(caClient.enroll(admin.getName(), "adminpw"));
//            admin.setMspId(orgMSPID);
//        }
//        addUser(admin);
//
//        IntermediateUser user1 = fabricStore.getMember(username, orgName);
//        if (!user1.isRegistered()) {
//            RegistrationRequest rr = new RegistrationRequest(user1.getName(), "org1.department1");
//            user1.setEnrollmentSecret(caClient.register(rr, admin));
//        }
//        if (!user1.isEnrolled()) {
//            user1.setEnrollment(caClient.enroll(user1.getName(), user1.getEnrollmentSecret()));
//            user1.setMspId(orgMSPID);
//        }
//        addUser(user1);

//        setPeerAdmin();
    }

//    private void setPeerAdmin() throws IOException {
//        // 一个特殊的用户，可以创建通道，连接对等点，并安装链码
//
//    }

    void setCaName(String caName) {
        this.caName = caName;
    }

    String getCALocation() {
        return caLocation;
    }

    void setCALocation(String caLocation) {
        this.caLocation = caLocation;
    }

    String getOrdererDomainName() {
        return ordererDomainName;
    }

    void setOrdererDomainName(String ordererDomainName) {
        this.ordererDomainName = ordererDomainName;
    }

    /**
     * 新增排序服务器
     */
    void addOrderer(String name, String location) {
        orderers.add(new SmartGearOrderer(name, location));
    }

    /**
     * 获取排序服务器集合
     */
    List<SmartGearOrderer> getOrderers() {
        return orderers;
    }

    void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /**
     * 设置会员id信息并将用户状态更新至存储配置对象
     *
     * @param orgMSPID 会员id
     */
    void setOrgMSPID(String orgMSPID) {
        this.orgMSPID = orgMSPID;
    }

    String getOrgDomainName() {
        return orgDomainName;
    }

    void setOrgDomainName(String orgDomainName) {
        this.orgDomainName = orgDomainName;
    }

    /**
     * 新增节点服务器
     */
    void addPeer(String peerName, String peerEventHubName, String peerLocation, String peerEventHubLocation, boolean isEventListener) {
        peers.add(new SmartGearPeer(peerName, peerEventHubName, peerLocation, peerEventHubLocation, isEventListener));
    }

    /**
     * 获取排序服务器集合
     */
    List<SmartGearPeer> getPeers() {
        return peers;
    }

    void setChannel(SmartGearChannel channel) {
        this.channel = channel;
    }

    SmartGearChannel getChannel() {
        return channel;
    }

    void setChainCode(IntermediateChainCodeID chaincode) {
        this.chaincode = chaincode;
    }

    IntermediateChainCodeID getChainCode() {
        return chaincode;
    }

    void setChannelArtifactsPath(String channelArtifactsPath) {
        this.channelArtifactsPath = channelArtifactsPath;
    }

    String getChannelArtifactsPath() {
        return channelArtifactsPath;
    }

    void setCryptoConfigPath(String cryptoConfigPath) {
        this.cryptoConfigPath = cryptoConfigPath;
    }

    String getCryptoConfigPath() {
        return cryptoConfigPath;
    }

    void setBlockListener(BlockListener blockListener) {
        this.blockListener = blockListener;
    }

    BlockListener getBlockListener() {
        return blockListener;
    }

    /**
     * 设置是否开启TLS
     *
     * @param openTLS 是否
     */
    void openTLS(boolean openTLS) {
        this.openTLS = openTLS;
    }

    /**
     * 获取是否开启TLS
     *
     * @return 是否
     */
    boolean openTLS() {
        return openTLS;
    }

    /**
     * 设置是否开启CATLS
     *
     * @param openCATLS 是否
     */
    void openCATLS(boolean openCATLS) {
        this.openCATLS = openCATLS;
    }

    /**
     * 获取是否开启CATLS
     *
     * @return 是否
     */
    boolean openCATLS() {
        return openCATLS;
    }

    void setClient(HFClient client) throws CryptoException, InvalidArgumentException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.client = client;
        log.debug("Create instance of HFClient");
        client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
        log.debug("Set Crypto Suite of HFClient");
    }

    HFClient getClient() {
        return client;
    }

    /**
     * 从指定路径中获取后缀为 _sk 的文件，且该路径下有且仅有该文件
     *
     * @param directory 指定路径
     * @return File
     */
    private File findFileSk(File directory) {
        File[] matches = directory.listFiles((dir, name) -> name.endsWith("_sk"));
        if (null == matches) {
            throw new RuntimeException(String.format("Matches returned null does %s directory exist?", directory.getAbsoluteFile().getName()));
        }
        if (matches.length != 1) {
            throw new RuntimeException(String.format("Expected in %s only 1 sk file but found %d", directory.getAbsoluteFile().getName(), matches.length));
        }
        return matches[0];
    }
}