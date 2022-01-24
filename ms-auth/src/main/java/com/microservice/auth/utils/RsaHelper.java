package com.microservice.auth.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

public class RsaHelper {

    private static final int KeySize = 1024;

    RSAPublicKey publicKey;
    RSAPrivateKey privateKey;

    String PemPublicKey = "";
    String PemPrivateKey = "";

    String publicExponentHex;// 16进制
    String privateExponentHex;// 16进制
    String ModulusHex;// 16进制

    Cipher cipherEncrypt;
    Cipher cipherDecrypt;

    public RsaHelper() throws Exception {
        KeyPair keyPair = creatKeyPair();
        this.publicKey = ((RSAPublicKey) keyPair.getPublic());
        this.privateKey = ((RSAPrivateKey) keyPair.getPrivate());
        this.Init(this.publicKey, this.privateKey);
    }

    public RsaHelper(String modulus, String publicExponent, String privateExponent) {
        this.publicKey = getPublicKey(modulus, publicExponent);
        this.privateKey = getPrivateKey(modulus, privateExponent);
        this.Init(this.publicKey, this.privateKey);
    }

    public void Init(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
        this.PemPublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        this.PemPrivateKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());

        this.publicExponentHex = publicKey.getPublicExponent().toString(16);
        this.ModulusHex = publicKey.getModulus().toString(16);
        this.privateExponentHex = privateKey.getPrivateExponent().toString(16);

        try {
            cipherEncrypt = initCipherEncrypt(this.publicKey);
            cipherDecrypt = initCipherDecrypt(this.privateKey);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }


    public String encrypt(String msg) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        return Base64.getEncoder().encodeToString(cipherEncrypt.doFinal(msg.getBytes()));
    }

    public String decrypt(String msg) throws Exception {
        byte[] bsinput = Base64.getDecoder().decode(msg);
        byte[] decrypted = cipherDecrypt.doFinal(bsinput);
        return new String(decrypted, "UTF-8");
    }

    public String getPublicExponentHex() {
        return this.publicExponentHex;
    }

    public String getModulusHex() {
        return this.ModulusHex;
    }

    public String getPemPublicKey() {
        return this.PemPublicKey;
    }

    public String getPemPrivateKey() {
        return this.PemPrivateKey;
    }

    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer("");
        // PemPublicKey, PemPrivateKey
        sb.append("String PemPublicKey = \"" + PemPublicKey + "\"; \n");
        sb.append("String PemPrivateKey = \"" + PemPrivateKey + "\"; \n");

        // modulus, publicExponent, privateExponent
        sb.append("String publicExponent = \"" + this.publicExponentHex + "\"; \n");
        sb.append("String modulus = \"" + this.ModulusHex + "\"; \n");
        sb.append("String privateExponent = \"" + this.privateExponentHex + "\"; \n");
        return sb.toString();
    }

    /*****************************************************************************************************************/

    public static KeyPair creatKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(KeySize);
        return keyPairGenerator.generateKeyPair();
    }

    public static RSAPublicKey getPublicKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus, 16);
            BigInteger b2 = new BigInteger(exponent, 16);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus, 16);
            BigInteger b2 = new BigInteger(exponent, 16);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Cipher initCipherEncrypt(PublicKey publicKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        Cipher cipherEncrypt = Cipher.getInstance("RSA");
        cipherEncrypt.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipherEncrypt;
    }

    public static Cipher initCipherDecrypt(PrivateKey privateKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        Cipher cipherDecrypt = Cipher.getInstance("RSA");
        cipherDecrypt.init(Cipher.DECRYPT_MODE, privateKey);
        return cipherDecrypt;
    }

    /**Test*********************************************************************************************************/
    public static void main(String[] args) throws Exception {
        RsaHelper rsaHelper = new RsaHelper();
        System.out.println(rsaHelper.toString());
    }
}
