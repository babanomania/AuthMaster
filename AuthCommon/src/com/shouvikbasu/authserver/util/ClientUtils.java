package com.shouvikbasu.authserver.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import com.shouvikbasu.authserver.exceptions.InvalidAuthHeaderException;

public class ClientUtils {

	private static Map<String, String> clientDetailsMap = new HashMap<String, String>();
	private static Map<String, String> clientSecretMap = new HashMap<String, String>();
	private static Map<String, Map<String, String>> clientAccessCodeMap = new HashMap<String, Map<String, String>>();
	
	private static Map<String, String> accessCodeUserIdMap = new HashMap<String, String>();
	private static Map<String, String> accessCodeClientIdMap = new HashMap<String, String>();
	
	public static void registerClient( String clientId, String clientSecret, String redirectionUrl ){
		clientDetailsMap.put(clientId, redirectionUrl);
		clientSecretMap.put(clientId, clientSecret);
	}

	public static boolean checkIfValidClientId(String clientId) {
		return clientSecretMap.containsKey(clientId);
	}
	
	public static boolean validateClientId(String clientId, String clientSecret) {
		if( clientSecretMap.get(clientId) == null ){
			return false;
		}else{
			return clientSecretMap.get(clientId).equals(clientSecret);
		}
	}
	
	public static boolean checkIfValidAccessCode(String clientId, String pAccessCode) {
		
		if( clientAccessCodeMap.get(clientId) == null ){
			return false;
		}else{
			return clientAccessCodeMap.get(clientId).equals(pAccessCode);
		}
	}

	public static String getRedirectionUrl(String clientId) {
		return clientDetailsMap.get(clientId);
	}

	public static String getAccessCode( String userId, String clientId ) throws Exception {

		synchronized (clientAccessCodeMap) {

			String accessCode = RandUtils.generateValue();
			
			if ( clientAccessCodeMap.get(clientId) != null ) {
				
				Map<String, String> userIdAccessCodeMap = clientAccessCodeMap.get(clientId);
				userIdAccessCodeMap.put(userId, accessCode);
				clientAccessCodeMap.put(clientId, userIdAccessCodeMap);
				accessCodeUserIdMap.put(accessCode, userId);
				accessCodeClientIdMap.put(accessCode, clientId);
				
				return accessCode;

			} else {
				
				HashMap<String, String> userIdAccessCodeMap = new HashMap<String, String>();
				userIdAccessCodeMap.put(userId, accessCode);
				clientAccessCodeMap.put(clientId, userIdAccessCodeMap);
				accessCodeUserIdMap.put(accessCode, userId);
				accessCodeClientIdMap.put(accessCode, clientId);
				
				return accessCode;
			}

		}

	}

	public static void revokeAccessCode( String accessCode ) throws Exception {

		String userId = getUserIdByAccessCode(accessCode);
		String clientId = getClientIdByAccessCode(accessCode);
		
		if( StringUtils.isEmpty(userId) || StringUtils.isEmpty(userId) ){
			return;
		}
		
		synchronized (clientAccessCodeMap) {
			
			if ( clientAccessCodeMap.get(clientId) != null ) {
				
				Map<String, String> userIdAccessCodeMap = clientAccessCodeMap.get(clientId);
				
				if( userIdAccessCodeMap != null ){
					userIdAccessCodeMap.remove(userId);
					clientAccessCodeMap.put(clientId, userIdAccessCodeMap);
				}
				
				if( accessCode != null ){
					accessCodeUserIdMap.remove(accessCode);
					accessCodeClientIdMap.remove(accessCode);
				}
			} 
		}
	}
	
	public static String getUserIdByAccessCode( String accessCode ){
		return accessCodeUserIdMap.get(accessCode);		
	}
	
	public static String getClientIdByAccessCode( String accessCode ){
		return accessCodeClientIdMap.get(accessCode);		
	}
	
	private static final String BASIC = "Basic ";
	public static void validateAuthHeader( HttpServletRequest request ) throws InvalidAuthHeaderException, UnsupportedEncodingException {
				
		String authorisation_header = request.getHeader( "Authorization" );
		String access_code = request.getParameter( "code" );
		
		if( StringUtils.isEmpty( authorisation_header ) || ! authorisation_header.startsWith(BASIC) ){
			throw new InvalidAuthHeaderException("invalid authorisation header, supported types are Basic");
		}
		
		String encodedHeader = authorisation_header.substring( BASIC.length() );
		String decodedHeader = new String( Base64.decodeBase64( encodedHeader.getBytes() ), "UTF-8" );
		if( decodedHeader.indexOf( ":" ) == -1 ){
			throw new InvalidAuthHeaderException("unable to decode authorisation header");
		}
		
		String[] authHeaderArray = decodedHeader.split( ":" );
		String clientId = authHeaderArray[0];
		String clientSecret = authHeaderArray[1];
		
		if( !ClientUtils.validateClientId( clientId, clientSecret) || 
			 ClientUtils.checkIfValidAccessCode( clientId, access_code ) ){
			
			throw new InvalidAuthHeaderException("unable to authenticate client id");
		}
		
	}
}
