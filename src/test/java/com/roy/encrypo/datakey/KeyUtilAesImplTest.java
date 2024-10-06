package com.roy.encrypo.datakey;

import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class KeyUtilAesImplTest {
    @Test
    void generateKey() throws NoSuchAlgorithmException {
        KeyUtilAesImpl keyUtilAes = new KeyUtilAesImpl();
        String key = String.valueOf(keyUtilAes.generateKey());
        System.out.println(key);
        assertNotNull(key);
    }
}