package constant;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CnstMethods {
	/**
	 * Return the MD5 hash of the String in param
	 * @param s String to hash
	 * @return 32 char hash value or null if there is a NoSuchAlgoritMException
	 */
	public static String MD5(String s){
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			//Create a byte array with the hash
			byte[] hash=md.digest(s.getBytes());
			StringBuilder hashString = new StringBuilder();
			//Build the String with the bytes by Hex values
			for (int i = 0; i < hash.length; i++)
			{
				String hex = Integer.toHexString(hash[i]);
		        if (hex.length() == 1){
		                hashString.append('0');
		                hashString.append(hex.charAt(hex.length() - 1));
		        }
		        else
		                hashString.append(hex.substring(hex.length() - 2));
			}
			return hashString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
}
