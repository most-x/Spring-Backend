package kr.co.mostx.japi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kr.co.mostx.japi.config.WebConstants;
import org.springframework.web.util.UriUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AesUtil {
    private static String privateKey = WebConstants.PRIVATE_KEY;
    private static String privateIv = WebConstants.PRIVATE_IV;

    public static String aesCBCEncode(String plainText) {
        try {
            byte[] keyBytes = privateKey.getBytes(StandardCharsets.UTF_8);
            byte[] ivBytes = privateIv.getBytes(StandardCharsets.UTF_8);

            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec IV = new IvParameterSpec(ivBytes);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, IV);

            byte[] encryptionByte = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            String base64encode = Base64.getEncoder().encodeToString(encryptionByte);

            return UriUtils.encode(base64encode, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }

    public static DataObject aesCBCDecode(String encodeText) {
        try {
            byte[] keyBytes = privateKey.getBytes(StandardCharsets.UTF_8);
            byte[] ivBytes = privateIv.getBytes(StandardCharsets.UTF_8);

            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec IV = new IvParameterSpec(ivBytes);

            byte[] decodeByte = Base64.getDecoder().decode(encodeText);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, IV);
            String decodeData = new String(cipher.doFinal(decodeByte), StandardCharsets.UTF_8);

            ObjectMapper jsonData = new ObjectMapper();

            return jsonData.registerModule(new JavaTimeModule())
                    .readValue(decodeData, DataObject.class);
        } catch (Exception e) {
            return null;
        }
    }
}
