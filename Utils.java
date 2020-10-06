package sitejabber;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Utils 
{
	private static final String method = "AES/CBC/PKCS5Padding";
	private static int IV_SIZE = 16;

	public Utils()
	{
	}

	public String decrypt(String string, String key) throws Exception
	{
		return this.decrypt(string.getBytes(StandardCharsets.UTF_8), key.getBytes(StandardCharsets.UTF_8));
	}

	public String decrypt(byte[] string, byte[] key) throws Exception
	{
		string = Base64.getDecoder().decode(string);

		byte[] iv = Arrays.copyOfRange(string, 0, IV_SIZE);
		int macLength = hmacLength(key);
		byte[] hash = Arrays.copyOfRange(string, IV_SIZE, IV_SIZE + macLength);
		byte[] ciphertext = Arrays.copyOfRange(string, IV_SIZE + macLength, string.length);
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		key = digest.digest(key);

		byte[] hmac = generateHMAC(key, ciphertext);
		if (Arrays.equals(hash, hmac))
		{
			byte[] decrypt = decrypt(key, iv, ciphertext);
			return new String(decrypt, StandardCharsets.UTF_8);
		}
		else
		{
			throw new RuntimeException("Incorrect HMAC");
		}
	}

	public String encrypt(String string, String key) throws Exception
	{
		return this.encrypt(string.getBytes(StandardCharsets.UTF_8), key.getBytes(StandardCharsets.UTF_8));
	}

	public String encrypt(byte[] string, byte[] key) throws Exception
	{
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		key = digest.digest(key);

		byte[] iv = generateIV();
		byte[] ciphertext = encrypt(key, iv, string);
		byte[] hash = generateHMAC(key, ciphertext);
		
		return Base64.getEncoder().encodeToString(concat(iv, concat(hash, ciphertext)));
	}

	private byte[] concat(byte[] first, byte[] second)
	{
		byte[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

	private byte[] decrypt(byte[] skey, byte[] iv, byte[] data) throws Exception
	{
		SecretKeySpec key = new SecretKeySpec(skey, "AES");
		AlgorithmParameterSpec param = new IvParameterSpec(iv);
		Cipher cipher = Cipher.getInstance(method);
		cipher.init(Cipher.DECRYPT_MODE, key, param);
		return cipher.doFinal(data);
	}

	private byte[] encrypt(byte[] skey, byte[] iv, byte[] data) throws Exception
	{
		SecretKeySpec key = new SecretKeySpec(skey, "AES");
		Cipher cipher = Cipher.getInstance(method);
		AlgorithmParameterSpec param = new IvParameterSpec(iv);
		cipher.init(Cipher.ENCRYPT_MODE, key, param);
		return cipher.doFinal(data);
	}

	private byte[] generateHMAC(byte[] skey, byte[] data) throws Exception
	{
		SecretKeySpec key = new SecretKeySpec(skey, "HmacSHA256");
		Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		sha256_HMAC.init(key);
		return sha256_HMAC.doFinal(data);
	}

	private byte[] generateIV() throws Exception
	{
		byte[] iv = new byte[IV_SIZE];
		SecureRandom randomSecureRandom = SecureRandom.getInstance("SHA1PRNG");
		randomSecureRandom.nextBytes(iv);
		return iv;
	}

	private int hmacLength(byte[] skey) throws Exception
	{
		SecretKeySpec key = new SecretKeySpec(skey, "HmacSHA256");
		Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		sha256_HMAC.init(key);
		return sha256_HMAC.getMacLength();
	}
}