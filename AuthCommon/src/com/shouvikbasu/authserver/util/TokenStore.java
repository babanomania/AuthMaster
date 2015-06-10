package com.shouvikbasu.authserver.util;

import java.util.HashMap;
import java.util.Map;

public class TokenStore {

	private static TokenStore _instance = new TokenStore();
	private static Map<String, TokenStorePojo> storedAccessTokens = new HashMap<String, TokenStore.TokenStorePojo>();
	private static Map<String, TokenStorePojo> storedRefreshTokens = new HashMap<String, TokenStore.TokenStorePojo>();
	
	private TokenStore() {
	}
	
	public static TokenStore getInstance(){
		return _instance;
	}
	
	public void storeToken( String access_code, String access_token, String refresh_token, 
							String expires_in, 	 String user_id ){
		
		TokenStorePojo storePojo = new TokenStorePojo();
		storePojo.setAccess_token(access_token);
		storePojo.setExpires_in(expires_in);
		storePojo.setRefresh_token(refresh_token);
		storePojo.setUser_id(user_id);
		storePojo.setAccess_code(access_code);
		
		storedAccessTokens.put(access_token,storePojo);
		storedRefreshTokens.put(refresh_token,storePojo);
	}

	public void removeTokens( String access_token, String refresh_token ){
		storedAccessTokens.remove(access_token);
		storedRefreshTokens.remove(refresh_token);
	}
	
	public boolean isValidAccessToken( String access_token ){
		return storedAccessTokens.containsKey(access_token);
	}
	
	public boolean isValidRefreshToken( String refresh_token ){
		return storedRefreshTokens.containsKey(refresh_token);
	}
	
	public String getUserIdByAccessToken( String access_token ){
		if( storedAccessTokens.get(access_token) == null ) return null;
		else return storedAccessTokens.get(access_token).getUser_id();
	}
	
	public String getRefreshTokenByAccessToken( String access_token ){
		if( storedAccessTokens.get(access_token) == null ) return null;
		else return storedAccessTokens.get(access_token).getRefresh_token();
	}
	
	public String getAccessCodeByAccessToken( String access_token ){
		if( storedAccessTokens.get(access_token) == null ) return null;
		else return storedAccessTokens.get(access_token).getAccess_code();
	}
	
	public String getAccessCodeByRefreshToken( String refresh_token ){
		if( storedRefreshTokens.get(refresh_token) == null ) return null;
		else return storedRefreshTokens.get(refresh_token).getAccess_code();
	}
	
	public String getExpiresInByAccessToken( String access_token ){
		if( storedAccessTokens.get(access_token) == null ) return null;
		else return storedAccessTokens.get(access_token).getExpires_in();
	}
	
	public String getUserIdByRefreshToken( String refresh_token ){
		if( storedRefreshTokens.get(refresh_token) == null ) return null;
		else return storedRefreshTokens.get(refresh_token).getUser_id();
	}
	
	public String getAccessTokenByefreshToken( String refresh_token ){
		if( storedRefreshTokens.get(refresh_token) == null ) return null;
		else return storedRefreshTokens.get(refresh_token).getAccess_token();
	}
	
	class TokenStorePojo {
		
		 private String access_code; 
		 private String access_token;
		 private String refresh_token; 
		 private String expires_in;
		 private String user_id;
		  
		public String getAccess_code() {
			return access_code;
		}
		public void setAccess_code(String access_code) {
			this.access_code = access_code;
		}
		public String getAccess_token() {
			return access_token;
		}
		public void setAccess_token(String access_token) {
			this.access_token = access_token;
		}
		public String getRefresh_token() {
			return refresh_token;
		}
		public void setRefresh_token(String refresh_token) {
			this.refresh_token = refresh_token;
		}
		public String getExpires_in() {
			return expires_in;
		}
		public void setExpires_in(String expires_in) {
			this.expires_in = expires_in;
		}
		public String getUser_id() {
			return user_id;
		}
		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}
		 
	}
}

