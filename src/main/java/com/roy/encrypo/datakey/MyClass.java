package com.roy.encrypo.datakey;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.util.Arrays;

public class MyClass {
    public static void main(String[] args) throws Exception {
        KeyUtil keyUtil = new KeyUtilAesImpl();
        final SecretKey secretKey = keyUtil.generateKey();
        System.out.println("Secret Key: "+ Arrays.toString(secretKey.getEncoded()));
        String originalString = "Hello, World!";
        System.out.println("Original String: " + originalString);
        IvParameterSpec iv = keyUtil.generateIv();
        System.out.println("iv: "+ Arrays.toString(iv.getIV()));
        String encryptedString = keyUtil.encrypt(originalString, secretKey, iv);
        System.out.println("Encrypted String: " + encryptedString);
        String decryptedString = keyUtil.decrypt(encryptedString, secretKey, iv);
        System.out.println("Decrypted String: " + decryptedString);
    }
}
