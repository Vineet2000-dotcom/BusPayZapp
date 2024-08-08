package com.jio.cohorts.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Configuration
public class PasswordDecryptor {
    private static final Logger logger = LogManager.getLogger(PasswordDecryptor.class);

    public String decrypt(String padding, String cipherText, String key) {

        byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        byte[] decodedKey = Base64.getDecoder().decode(key);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        Cipher cipher = null;

        try {
            cipher = Cipher.getInstance(padding);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
        }

        try {
            cipher.init(Cipher.DECRYPT_MODE, originalKey, ivParameterSpec);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException e) {
            logger.error(e.getMessage());
        }

        byte[] plainText = new byte[0];
        try {
            plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            logger.error(e.getMessage());
        }

        return new String(plainText);
    }
}
