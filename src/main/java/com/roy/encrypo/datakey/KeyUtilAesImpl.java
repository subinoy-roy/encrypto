package com.roy.encrypo.datakey;

import com.amazonaws.services.kms.AWSKMSClient;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.DecryptResult;
import com.amazonaws.services.kms.model.GenerateDataKeyRequest;
import com.amazonaws.services.kms.model.GenerateDataKeyResult;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class KeyUtilAesImpl implements KeyUtil {
    private static final String SEMICOLON = ":";
    public static final String ALGORITHM = "AES";
    public static final int keySize = 128;
    public static String algorithm = "AES/CBC/PKCS5Padding";
    private final String AWS_KEY_ID = "%%%";

    public String encrypt(String input) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // Encrypt key using AWS KMS
        GenerateDataKeyRequest dataKeyRequest = new GenerateDataKeyRequest();
        dataKeyRequest.setKeyId(AWS_KEY_ID);
        dataKeyRequest.setKeySpec("AES_256");
        AWSKMSClient awskmsClient = new AWSKMSClient();
        GenerateDataKeyResult generateDataKeyResult = awskmsClient.generateDataKey(dataKeyRequest);
        byte[] plaintextDek = generateDataKeyResult.getPlaintext().array();
        byte[] encryptedDek = generateDataKeyResult.getCiphertextBlob().array();

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(plaintextDek, ALGORITHM));
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(cipherText)+SEMICOLON+
                Base64.getEncoder().encodeToString(encryptedDek);
    }

    public String decrypt(String cipherText) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String[] parts = cipherText.split(SEMICOLON);
        SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(parts[1]),ALGORITHM);

        AWSKMSClient awskmsClient = new AWSKMSClient();
        DecryptRequest decryptRequest = new DecryptRequest();
        decryptRequest.setKeyId(AWS_KEY_ID);
        decryptRequest.setCiphertextBlob(ByteBuffer.wrap(Base64.getDecoder().decode(parts[1])));

        DecryptResult decryptResult = awskmsClient.decrypt(decryptRequest);
        byte[] decryptedDek = decryptResult.getPlaintext().array();  // Decrypted DEK


        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] plainText = cipher.doFinal(Base64.getDecoder()
                .decode(parts[0]));
        return new String(plainText);
    }
}
