package com.precious.kit;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.precious.Const;
 
/**
 * AES 256 加解密
 * @author biezhi
 *
 */
public class MagicCrypt {
	
	/**
	 * 私钥
	 */
	//-----类别常数-----
	/**
	 * 预设的Initialization Vector，为16 Bits的0
	 */
	private static final IvParameterSpec DEFAULT_IV = new IvParameterSpec(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
	/**
	 * 加密演算法使用AES
	 */
	private static final String ALGORITHM = "AES";
	/**
	 * AES使用CBC模式与PKCS5Padding
	 */
	private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
 
	//-----物件变数-----
	/**
	 * 取得AES加解密的密鑰
	 */
	private Key key;
	/**
	 * AES CBC模式使用的Initialization Vector
	 */
	private IvParameterSpec iv;
	/**
	 * Cipher 物件
	 */
	private Cipher cipher;
 
	//-----建构子-----
	/**
	 * 建构子，使用128 Bits的AES密钥(计算任意长度密钥的MD5)和预设IV
	 *
	 * @param key 传入任意长度的AES密钥
	 */
	public MagicCrypt(final String key) {
		this(key, 256);
	}
 
	/**
	 * 建构子，使用128 Bits或是256 Bits的AES密钥(计算任意长度密钥的MD5或是SHA256)和预设IV
	 *
	 * @param key 传入任意长度的AES密钥
	 * @param bit 传入AES密钥长度，数值可以是128、256 (Bits)
	 */
	public MagicCrypt(final String key, final int bit) {
		this(key, bit, null);
	}
 
	/**
	 * 建构子，使用128 Bits或是256 Bits的AES密钥(计算任意长度密钥的MD5或是SHA256)，用MD5计算IV值
	 *
	 * @param key 传入任意长度的AES密钥
	 * @param bit 传入AES密钥长度，数值可以是128、256 (Bits)
	 * @param 传入任意长度的的IV字串
	 */
	public MagicCrypt(final String key, final int bit, final String iv) {
		if (bit == 256) {
			this.key = new SecretKeySpec(getHash("SHA-256", key), ALGORITHM);
		} else {
			this.key = new SecretKeySpec(getHash("MD5", key), ALGORITHM);
		}
		if (iv != null) {
			this.iv = new IvParameterSpec(getHash("MD5", iv));
		} else {
			this.iv = DEFAULT_IV;
		}
 
		init();
	}
 
	//-----物件方法-----
	/**
	 * 取得字串的杂骤值
	 *
	 * @param algorithm 传入算法
	 * @param text 传入字符串
	 * @return 返回hash
	 */
	private byte[] getHash(final String algorithm, final String text) {
		try {
			return getHash(algorithm, text.getBytes("UTF-8"));
		} catch (final Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}
 
	/**
	 * 获取Hash值
	 *
	 * @param algorithm 传入算法
	 * @param data 传入数据
	 * @return 返回hash
	 */
	private byte[] getHash(final String algorithm, final byte[] data) {
		try {
			final MessageDigest digest = MessageDigest.getInstance(algorithm);
			digest.update(data);
			return digest.digest();
		} catch (final Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}
 
	/**
	 * 初始化密钥
	 */
	private void init() {
		try {
			cipher = Cipher.getInstance(TRANSFORMATION);
		} catch (final Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}
 
	/**
	 * 加密文字
	 *
	 * @param str 传入要加密的文字
	 * @return 返回加密后的文字
	 */
	public String encrypt(final String str) {
		try {
			return encrypt(str.getBytes("UTF-8"));
		} catch (final Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}
 
	/**
	 * 加密数据
	 *
	 * @param data 传入要加密的数据
	 * @return 返回加密后的文字
	 */
	public String encrypt(final byte[] data) {
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key, iv);
			final byte[] encryptData = cipher.doFinal(data);
			return new String(Base64.getEncoder().encode(encryptData), "UTF-8");
		} catch (final Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}
 
	/**
	 * 解密文字
	 *
	 * @param str 传入要解密的文字
	 * @return 返回解密后的文字
	 */
	public String decrypt(final String str) {
		try {
			return decrypt(Base64.getDecoder().decode(str));
		} catch (final Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}
 
	/**
	 * 解密文字
	 *
	 * @param data 传入要解密的数据
	 * @return 返回解密后的文字
	 */
	public String decrypt(final byte[] data) {
		try {
			cipher.init(Cipher.DECRYPT_MODE, key, iv);
			final byte[] decryptData = cipher.doFinal(data);
			return new String(decryptData, "UTF-8");
		} catch (final Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}
 
	/**
	 * AES加密
	 * @param str
	 * @return
	 */
	public static String aesEncrypt(String str){
		MagicCrypt magicCrypt = new MagicCrypt(Const.AES_SLAT);
		return magicCrypt.encrypt(str);
	}
	
	/**
	 * AES加密
	 * @param key
	 * @param str
	 * @return
	 */
	public static String aesEncrypt(final String key, final String str){
		MagicCrypt magicCrypt = new MagicCrypt(key);
		return magicCrypt.encrypt(str);
	}
	
	/**
	 * AES解密
	 * @param str
	 * @return
	 */
	public static String aesDecrypt(String str){
		MagicCrypt magicCrypt = new MagicCrypt(Const.AES_SLAT);
		return magicCrypt.decrypt(str);
	}
	
	public static void main(String[] args) {
		String str = "15821086508";
		System.out.println("加密前数据： " + str);
		// 加密
		String pwd = MagicCrypt.aesEncrypt(str);
		
		System.out.println("加密后数据：" + pwd);
		
		String repwd = MagicCrypt.aesDecrypt(pwd);
		
		System.out.println("解密数据：" + repwd);
	}
}