package com.cellmania.cmreports.common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

public class Encryptor {
	private static MessageDigest m = null;
	
	static {
		try {
			m = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Error getting instance of Algorithim"+e);
			e.printStackTrace();
		}
	}

	/**
	 * Generates a encrypted value for the passed string using the given salt value. 
	 * @param str - String thats will be encrypted
	 * @param salt - Salt value used for encryption
	 * @return String - Encrypted value
	 * @throws UnsupportedEncodingException
	 */
	public static String encrypt(String str,String salt) throws UnsupportedEncodingException {
		if(null==str || null==salt) return null;
		byte[] hash = (str + salt).getBytes("UTF-8");
		if(m!=null) {
			m.update(hash,0,hash.length);
			hash = m.digest();
		}
		return new String(Base64.encodeBase64(hash)).replaceAll("\r\n", "").trim();
	}
	
	/**
	 * Generated a alphaNumeric Salt value based on the passed string. 
	 * @param str - String, used for generating salt value.
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	public static String getSalt(String str) throws UnsupportedEncodingException{
		return new String(Base64.encodeBase64(str.getBytes("UTF-8"))).replaceAll("\r\n", "").trim();
	}
	
	
	// Main method for Testing Encryption 
	public static void main(String[] arg) throws UnsupportedEncodingException{
		String encPwd = encrypt("cmportal11", getSalt("cmreports"));
		System.out.println("Generated: "+encPwd);
		System.out.println("Salt : "+ getSalt("cmreports"));
	}
}
