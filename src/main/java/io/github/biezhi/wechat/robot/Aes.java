package io.github.biezhi.wechat.robot;

import java.security.Key;
import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * aes加密算法
 *
 * @author 图灵机器人
 */
public class Aes {

    private Key key;
    /**
     * AES CBC模式使用的Initialization Vector
     */
    private IvParameterSpec iv;
    /**
     * Cipher 物件
     */
    private Cipher cipher;

    /**
     * 构造方法
     *
     * @param strKet 密钥
     */
    public Aes(String strKey) {
        try {
            this.key = new SecretKeySpec(getHash("MD5", strKey), "AES");
            this.iv = new IvParameterSpec(new byte[]{0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0});
            this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * 加密方法
     * <p>
     * 说明：采用128位
     *
     * @return 加密结果
     */
    public String encrypt(String strContent) {
        try {
            byte[] data = strContent.getBytes("UTF-8");
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] encryptData = cipher.doFinal(data);
            String encryptResult = new String(Base64.getEncoder().encode(encryptData), "UTF-8");
            return encryptResult;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * @param algorithm
     * @param text
     * @return
     */
    private static byte[] getHash(String algorithm, String text) {
        try {
            byte[] bytes = text.getBytes("UTF-8");
            final MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(bytes);
            return digest.digest();
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
