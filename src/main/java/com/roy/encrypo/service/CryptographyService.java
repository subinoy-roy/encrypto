package com.roy.encrypo.service;

public interface CryptographyService {
    public String encrypt(String input) throws Exception;
    public String decrypt(String cipherText) throws Exception;
}
