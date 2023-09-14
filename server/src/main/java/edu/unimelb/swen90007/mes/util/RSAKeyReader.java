package edu.unimelb.swen90007.mes.util;

import org.springframework.core.io.ResourceLoader;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAKeyReader {

    private static final String PRIVATE_KEY_FILENAME = "private_key.der";
    private static final String PUBLIC_KEY_FILENAME = "public_key.der";

    public static RSAPrivateKey readPrivateKey() throws Exception {
        ClassLoader classLoader = ResourceLoader.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(PRIVATE_KEY_FILENAME);
        byte[] keyBytes = inputStream.readAllBytes();
        inputStream.close();
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) kf.generatePrivate(spec);
    }

    public static RSAPublicKey readPublicKey() throws Exception {
        ClassLoader classLoader = ResourceLoader.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(PUBLIC_KEY_FILENAME);
        byte[] keyBytes = inputStream.readAllBytes();
        inputStream.close();
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) kf.generatePublic(spec);
    }

    public static void main(String[] args) throws Exception {
        RSAPrivateKey privateKey = readPrivateKey();
        PublicKey publicKey = readPublicKey();
        System.out.println(privateKey);
        System.out.println(publicKey);
    }

}
