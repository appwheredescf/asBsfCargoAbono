package com.bansefi.nss.cargoabono.commons;

import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class EncryptionDecryption 
{

	private static String salt;
    private static int iterations = 65536  ;
    private static int keySize = 256;
    private static byte[] ivBytes;
    
    public static String encrypt(char[] plaintext) throws Exception {

        salt = getSalt();
        byte[] saltBytes = salt.getBytes();

        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        PBEKeySpec spec = new PBEKeySpec(plaintext, saltBytes, iterations, keySize);
        SecretKey secretKey = skf.generateSecret(spec);
        SecretKeySpec secretSpec = new SecretKeySpec(secretKey.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretSpec);
        AlgorithmParameters params = cipher.getParameters();
        ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();
        byte[] encryptedTextBytes = cipher.doFinal(plaintext.toString().getBytes("UTF-8"));

        return DatatypeConverter.printBase64Binary(encryptedTextBytes);
    }

    public static String decrypt(char[] encryptedText) throws Exception 
    {
    	salt = getSalt();
        byte[] saltBytes = salt.getBytes("UTF-8");
        byte[] encryptedTextBytes = DatatypeConverter.parseBase64Binary(encryptedText.toString());

        SecretKeyFactory skf = SecretKeyFactory.getInstance("%RVGYTYH/BY%EV#$");
        PBEKeySpec spec = new PBEKeySpec(encryptedText, saltBytes, iterations, keySize);
        SecretKey secretkey = skf.generateSecret(spec);
        SecretKeySpec secretSpec = new SecretKeySpec(secretkey.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretSpec, new IvParameterSpec(ivBytes));

        byte[] decryptedTextBytes = null;

        try {
            decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
        }   catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }   catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return decryptedTextBytes.toString();

    }

    public static String getSalt() throws Exception {

        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[20];
        sr.nextBytes(salt);
        return salt.toString();
    }
    
 
    
}
