package com.roy.encrypo.datakey;

public class MyClass {
    public static void main(String[] args) throws Exception {
        KeyUtil keyUtil = new KeyUtilKmsImpl();
        String originalString = "Hello, World!";
        System.out.println("Original String: " + originalString);
        String encryptedString = keyUtil.encrypt(originalString);
        System.out.println("Encrypted String: " + encryptedString);
        String decryptedString = keyUtil.decrypt(encryptedString);
        System.out.println("Decrypted String: " + decryptedString);
    }
}
