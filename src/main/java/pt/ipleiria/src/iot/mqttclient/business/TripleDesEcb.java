package pt.ipleiria.src.iot.mqttclient.business;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class TripleDesEcb {
    public static byte[] encryptMessage(byte[] message, byte[] keyBytes)
            throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, NoSuchProviderException {
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        SecretKey secretKey = new SecretKeySpec(keyBytes, "DESede");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(message);
    }

    public static byte[] decryptMessage(byte[] encryptedMessage, byte[] keyBytes) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchProviderException {

        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        SecretKey secretKey = new SecretKeySpec(keyBytes, "DESede");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(encryptedMessage);
    }
}
