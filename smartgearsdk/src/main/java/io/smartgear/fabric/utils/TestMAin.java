package io.smartgear.fabric.utils;


import io.smartgear.fabric.sdk.SmartGearStore;
import io.smartgear.fabric.sdk.SmartGearUser;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;

public class TestMAin {

    //读取证书文件工具方法
    static PrivateKey getPrivateKeyFromBytes(byte[] data) throws IOException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {
        final Reader pemReader = new StringReader(new String(data));

        final PrivateKeyInfo pemPair;
        final SubjectPublicKeyInfo publicKey;
        try (PEMParser pemParser = new PEMParser(pemReader)) {
            pemPair = (PrivateKeyInfo) pemParser.readObject();
//            publicKey = (SubjectPublicKeyInfo)pemParser.readObject();
        }

        PrivateKey privateKey = new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).getPrivateKey(pemPair);

        System.out.println("私钥:" + privateKey);

        return privateKey;
    }

    public static void main(String[] args) throws Exception {
        SmartGearUser admin; //定义证书颁发的管理员对象
        admin = FabricCaUtils.getFabricCaAdminUser(); //获取证书颁发的管理员对象
        String src = "test ric";

        String certificate = admin.getEnrollment().getCert();
//
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

        X509Certificate x509Certificate = (X509Certificate)certificateFactory.generateCertificate(new ByteArrayInputStream(certificate.getBytes()));

        PublicKey publicKey = x509Certificate.getPublicKey();

        System.out.println(publicKey);

    }

}
