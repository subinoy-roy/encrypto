package com.roy.encrypo.service;

import com.roy.encrypo.dto.Payload;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CryptographyServiceKmsImplTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void encryptDecrypt() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Payload payload = new Payload();
        payload.setPayload("Hello World");
        Payload encryptedPayload = this.restTemplate.postForObject("/encrypt", payload, Payload.class);
        Payload decryptedPayload = this.restTemplate.postForObject("/decrypt", encryptedPayload, Payload.class);
        Assertions.assertEquals(payload.getPayload(),decryptedPayload.getPayload());
    }
}