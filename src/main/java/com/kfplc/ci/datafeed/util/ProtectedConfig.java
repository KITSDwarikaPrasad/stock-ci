package com.kfplc.ci.datafeed.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * This is the class for decrypting the protected configurations like database passwords etc
 */
public class ProtectedConfig {

	private static final byte[] SALT = {
			(byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
			(byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
	};


	
	/**
	 * Method to decrypt the password
	 * @param property
	 * @return decrypted password as String
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	protected static String decrypt(String property) throws GeneralSecurityException, IOException {
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
		SecretKey key = keyFactory.generateSecret(new PBEKeySpec(getPrivateKey()));
		Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
		pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
		return new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
	}

	private static byte[] base64Decode(String property) throws IOException {
		// NB: This class is internal, and you probably should use another impl
		return new BASE64Decoder().decodeBuffer(property);
	}
	
	/**
	 * Method to read the private key from the user's home directory
	 * @return char array
	 * @throws IOException
	 */
	private static char[] getPrivateKey() throws IOException {
		FileReader fileReader = null;
		BufferedReader br =  null;
		char [] chArr = null;
		try {

			//String pkDir = System.getProperty("user.home").concat("/.vault-pass");
			String pkDir = ConfigReader.getProperty("VAULT_PASS_PATH");
			br = new BufferedReader(new FileReader(pkDir));
			String sFirtsLine = null;
			sFirtsLine = null;
			if ((sFirtsLine = br.readLine()) != null) {
				chArr = sFirtsLine.toCharArray();
			}

		} finally {
			if( fileReader != null ) {
				fileReader.close();
			}
			if(br != null) {
				br.close();
			}
		}

		return chArr;
	}

}