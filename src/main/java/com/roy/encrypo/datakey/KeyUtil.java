package com.roy.encrypo.datakey;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.NoSuchAlgorithmException;

public interface KeyUtil {
    public String encrypt(String input) throws Exception;
    public String decrypt(String cipherText) throws Exception;
}
