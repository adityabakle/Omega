package net.adi.messenger.common;

/**
 * This class is for encryption and decryption of password. 
 * Simple arithmetic operation is used for encryption.
 * 
 * @author Aditya Bakle
 * @version 1.0.0
 */
public class  EncodeDecode {
	
	/**
	 * This method encodes the string passed to numeric value.
	 * @param str String value to be Encoded.
	 * @return Encoded <code>String</code> for the passed value.
	 */
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
}
