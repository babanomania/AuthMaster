package com.shouvikbasu.authserver.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class RandUtils {

	private static final char[] hexCode = "0123456789abcdef".toCharArray();
	
	public static String generateValue() throws Exception {
        return generateValue(UUID.randomUUID().toString());
    }
	 
	private static String generateValue(String param) throws NoSuchAlgorithmException {
		
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(param.getBytes());
            byte[] messageDigest = algorithm.digest();
            return toHexString(messageDigest);
 
    }
	
	private static String toHexString(byte[] data) {
        if(data == null) {
            return null;
        }
        StringBuilder r = new StringBuilder(data.length*2);
        for ( byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }
}
