package com.roy.encrypo.datakey;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.NoSuchAlgorithmException;

public interface KeyUtil {
    public SecretKey generateKey() throws NoSuchAlgorithmException;
    public String encrypt(String input, SecretKey key, IvParameterSpec iv) throws Exception;
    public String decrypt(String cipherText, SecretKey key, IvParameterSpec iv) throws Exception;
    public IvParameterSpec generateIv();
}
