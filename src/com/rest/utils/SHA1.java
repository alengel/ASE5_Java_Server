package com.rest.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * 
 * this class encrypts Strings using the SHA1 algorithm
 * 
 */
public class SHA1 {

	/**
	 * encypts a String using the SHA1 algorithm
	 * 
	 * @param password
	 * @return the encrypted password as a String
	 */
	private static String encryptPassword(String password) {
		String sha1 = "";
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(password.getBytes("UTF-8"));
			sha1 = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sha1;
	}

	/**
	 * converts byte[] to String
	 * 
	 * @param hash
	 * @return String representation of a byte[] hash
	 */
	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	/**
	 * encypts a String using the SHA1 algorithm
	 * 
	 * @param string
	 * @return the encrypted password as a String
	 */
	public static String stringToSHA(String string) {
		return encryptPassword(string);
	}
}
