package net.adi.messenger.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

/**
 * This class is for encryption and decryption of password. 
 * Simple arithmetic operation is used for encryption.
 * 
 * @author Aditya Bakle
 * @version 1.0.0
 */
public class  EncodeDecode {
	
	/**
	 * Number of time to hash a password with the salt.
	 */
	private static final int PASSWORD_ENCODE_MULTIPLIER = 256;
	
	/**
	 * This method encodes the string passed to numeric value.
	 * @param str String value to be Encoded.
	 * @return Encoded <code>String</code> for the passed value.
	 */
	@Deprecated
	public static String encode(String str){
		char[] strCh = str.toCharArray();
		StringBuffer strBuf = new StringBuffer();
		for(char c : strCh){
			int b = c*21;
			String strtmp = ""+b;
			strBuf.append(strtmp.length()+""+strtmp);
		}
		return strBuf.toString();
	}
	
	/**
	 * This methods decodes the encrypted string passed. 
	 * @param str String value to be decoded.
	 * @return decoded <code>String</code> for the passed value.
	 */
	@Deprecated
	public static String decode(String str) {
		char[] strCh = str.toCharArray();
		StringBuffer strBuf = new StringBuffer();
		String strTmp = "";
		int count=0;
		for(char c : strCh){
			if(count==0) {
				count = Integer.parseInt(""+c);
				if(!"".equals(strTmp)){
					strBuf.append((char)(Integer.parseInt(strTmp)/21));
				}
				strTmp="";
			}
			else {
				count--;
				strTmp += c;
			}
		}
		strBuf.append((char)(Integer.parseInt(strTmp)/21));
		return strBuf.toString();
	}
	
	/*public static void main(String str[]){
		System.out.println(decode(encode("adi")));
	}*/
	
	public static String encodePassword(final String password, String user) {
		byte[] hash = (user + password).getBytes();
		for (int i = 0; i < PASSWORD_ENCODE_MULTIPLIER; i += 1) {
			hash = sha512(hash);
		}
		return new Base64(128).encodeToString(hash).trim();
	}
	
	/**
	 * Function to sha512 encode a byte array.
	 * 
	 * @param aby
	 *            to be encoded.
	 * @return the digest.
	 */
	private static byte[] sha512(final byte[] aby) {
		byte[] result = null;
		try {
			final MessageDigest m = MessageDigest.getInstance("SHA-512");
			m.update(aby, 0, aby.length);
			result = m.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
	}
}
