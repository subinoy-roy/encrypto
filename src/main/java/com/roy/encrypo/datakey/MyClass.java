package com.roy.encrypo.datakey;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.util.Arrays;

public class MyClass {
    public static void main(String[] args) throws Exception {
        KeyUtil keyUtil = new KeyUtilAesImpl();
        String originalString = "Hello, World!";
        System.out.println("Original String: " + originalString);
        String encryptedString = keyUtil.encrypt(originalString);
        System.out.println("Encrypted String: " + encryptedString);
        String decryptedString = keyUtil.decrypt(encryptedString);
        System.out.println("Decrypted String: " + decryptedString);
    }
}
