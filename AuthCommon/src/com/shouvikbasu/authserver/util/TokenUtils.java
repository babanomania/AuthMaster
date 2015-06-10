package com.shouvikbasu.authserver.util;

import java.util.GregorianCalendar;

import org.json.JSONObject;

import com.shouvikbasu.client.utils.AuthConstants;

public class TokenUtils {

	//2 hours validity
	private static long token_valid_seconds = AuthConstants.TWO_HOURS_VALID;
	
	public static boolean checkIfValidAccessToken( String accessToken ){
		return TokenStore.getInstance().isValidAccessToken(accessToken);
	}
	
	public static boolean checkIfValidRefreshToken( String refreshToken ){
		return TokenStore.getInstance().isValidRefreshToken(refreshToken);
	}
	
	public static void revokeTokens( String accessToken, String refreshToken ) throws Exception{
		
		String old_access_code = TokenStore.getInstance().getAccessCodeByRefreshToken(refreshToken);
		ClientUtils.revokeAccessCode( old_access_code );
		TokenStore.getInstance().removeTokens(accessToken, refreshToken);
	}

	public static String getRefreshTokenString( String oldRefreshToken ) throws Exception{
		
		String access_token = RandUtils.generateValue();
		String refresh_token = RandUtils.generateValue();
		String expires_in = String.valueOf( GregorianCalendar.getInstance().getTimeInMillis() + token_valid_seconds );
		String user_id = TokenStore.getInstance().getUserIdByRefreshToken(oldRefreshToken);
		String old_access_token = TokenStore.getInstance().getAccessTokenByefreshToken(oldRefreshToken);
		String old_access_code = TokenStore.getInstance().getAccessCodeByRefreshToken(oldRefreshToken);
		
		TokenStore.getInstance().removeTokens( old_access_token, oldRefreshToken );
		TokenStore.getInstance().storeToken( old_access_code, access_token, refresh_token, expires_in, user_id );
		
		JSONObject jsObj = new JSONObject();
		jsObj.put( AuthConstants.ACCESS_TOKEN, access_token );
		jsObj.put( AuthConstants.EXPIRES_IN, expires_in );
		jsObj.put( AuthConstants.USER_ID, user_id );
		jsObj.put( AuthConstants.REFRESH_TOKEN, refresh_token );
		
		return jsObj.toString();
	}

	public static String getAccessTokenString( String access_code ) throws Exception{
		
		String access_token = RandUtils.generateValue();
		String refresh_token = RandUtils.generateValue();
		String expires_in = String.valueOf( GregorianCalendar.getInstance().getTimeInMillis() + token_valid_seconds );
		String user_id = ClientUtils.getUserIdByAccessCode(access_code);
		
		TokenStore.getInstance().storeToken( access_code, access_token, refresh_token, expires_in, user_id);
		
		JSONObject jsObj = new JSONObject();
		jsObj.put( AuthConstants.ACCESS_TOKEN, access_token );
		jsObj.put( AuthConstants.EXPIRES_IN, expires_in );
		jsObj.put( AuthConstants.USER_ID, user_id );
		jsObj.put( AuthConstants.REFRESH_TOKEN, refresh_token );
		
		return jsObj.toString();
	}
	
	public static String getCheckAccessTokenString( String access_token ){
		
		long currentInstance = GregorianCalendar.getInstance().getTimeInMillis();
		long tokenExpiresIn = Long.valueOf( TokenStore.getInstance().getExpiresInByAccessToken(access_token) );
		long expires_in_long = currentInstance - tokenExpiresIn;
		
		String refresh_token = TokenStore.getInstance().getRefreshTokenByAccessToken(access_token);
		String user_id = TokenStore.getInstance().getUserIdByAccessToken(access_token);
		String expires_in = String.valueOf( expires_in_long > 0l ? expires_in_long : null );
		String state = expires_in_long > 0l ? AuthConstants.OK_STATE : AuthConstants.EXPIRED_STATE;
		
		JSONObject jsObj = new JSONObject();
		jsObj.put( AuthConstants.USER_ID, user_id );
		jsObj.put( AuthConstants.EXPIRES_IN, expires_in );
		jsObj.put( AuthConstants.STATE, state );		
		jsObj.put( AuthConstants.REFRESH_TOKEN, refresh_token );
		
		return jsObj.toString();
	}
}
