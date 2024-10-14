package com.roy.encrypo.service;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.DecryptResult;
import com.amazonaws.services.kms.model.GenerateDataKeyRequest;
import com.amazonaws.services.kms.model.GenerateDataKeyResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class CryptographyServiceKmsImpl implements CryptographyService {
    private static final String SEMICOLON = ":";
    @Value("${app.crypto.keyspec.algo}")
    private String KEY_SPEC_ALGORITHM;
    @Value("${app.crypto.keyspec}")
    private String KEY_SPEC;
    @Value("${app.crypto.transformation.algo}")
    private String TRANSFORMATION_ALGORITHM;
    @Value("${AWS_KMS_KEY_ID}")
    private String AWS_KEY_ID;

    @Autowired
    AWSKMS awsKmsClient;

    /**
     * Encrypt
     *
     * @param input Plain Text
     * @return Encrypted Data along with the encrypted DEK and IV in the following format: [DEK]:[IV]:[Encrypted Text]
     * @see Exception
     * @throws NoSuchPaddingException     If the padding scheme is not available
     * @throws NoSuchAlgorithmException     If the algorithm is not available
     * @throws InvalidAlgorithmParameterException If the algorithm parameters are invalid
     * @throws InvalidKeyException        If the key is invalid
     * @throws IllegalBlockSizeException   If the block size is invalid
     * @throws BadPaddingException        If the padding is invalid
     */
    public String encrypt(String input) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // Generate a Data Encryption Key (DEK) and encrypt it with AWS KMS
        GenerateDataKeyRequest dataKeyRequest = new GenerateDataKeyRequest();
        dataKeyRequest.setKeyId(AWS_KEY_ID);
        dataKeyRequest.setKeySpec(KEY_SPEC);

        GenerateDataKeyResult generateDataKeyResult = awsKmsClient.generateDataKey(dataKeyRequest);
        byte[] plaintextDek = generateDataKeyResult.getPlaintext().array();
        byte[] encryptedDek = generateDataKeyResult.getCiphertextBlob().array();

        // Generate IV
        byte[] iv = generateIV();
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // Encrypt input
        Cipher cipher = Cipher.getInstance(TRANSFORMATION_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(plaintextDek, KEY_SPEC_ALGORITHM), ivSpec);
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(cipherText)
                + SEMICOLON + Base64.getEncoder().encodeToString(iv)// iv
                + SEMICOLON + Base64.getEncoder().encodeToString(encryptedDek); // Encrypted DEK
    }

    /**
     * Decrypt
     *
     * @param cipherText Encrypted Data in the following format: [DEK]:[IV]:[Encrypted Text]
     * @return Decrypted Data
     * @throws NoSuchPaddingException     If the padding scheme is not available
     * @throws NoSuchAlgorithmException     If the algorithm is not available
     * @throws InvalidAlgorithmParameterException If the algorithm parameters are invalid
     * @throws InvalidKeyException        If the key is invalid
     * @throws IllegalBlockSizeException   If the block size is invalid
     * @throws BadPaddingException        If the padding is invalid
     */
    public String decrypt(String cipherText) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String[] parts = cipherText.split(SEMICOLON);

        // Generate an IV
        byte[] iv = Base64.getDecoder().decode(parts[1]);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // Decrypt DEK using AWS KMS
        DecryptRequest decryptRequest = new DecryptRequest();
        decryptRequest.setKeyId(AWS_KEY_ID);
        decryptRequest.setCiphertextBlob(ByteBuffer.wrap(Base64.getDecoder().decode(parts[2])));
        DecryptResult decryptResult = awsKmsClient.decrypt(decryptRequest);
        byte[] decryptedDek = decryptResult.getPlaintext().array();  // Decrypted DEK

        // Decrypt using the decrypted DEK and IV
        Cipher cipher = Cipher.getInstance(TRANSFORMATION_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptedDek, KEY_SPEC_ALGORITHM), ivSpec);
        byte[] plainText = cipher.doFinal(Base64.getDecoder()
                .decode(parts[0]));
        return new String(plainText);
    }

    /**
     * Generate IV (Initialization Vector)
     *
     * @return IV
     */
    public static byte[] generateIV() {
        byte[] iv = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        return iv;
    }
}
