package com.shouvikbasu.client.utils;

public class AuthConstants {

	public static final String CODE = "code";
	public static final String STATE = "state";
	public static final String Authorization = "Authorization";
	public static final String BASIC_SPACE = "Basic ";
	
	public static final String USER_ID = "user_id";
	public static final String ACCESS_CODE = "access_code";
	public static final String REFRESH_TOKEN = "refresh_token";
	public static final String ACCESS_TOKEN = "access_token";
	public static final String CLIENT_ID = "client_id";
	public static final String EXPIRES_IN = "expires_in";
	
	public static final String PATH_TOKEN = "/token";
	public static final String PATH_REVOKE_TOKEN = "/revoke_token";
	public static final String PATH_REFRESH_TOKEN = "/refresh_token";
	public static final String PATH_CHECK_TOKEN = "/check_token";
	public static final String PATH_AUTHORIZE = "/authorize";
	
	public static final String OK_STATE = "OK";
	public static final String EXPIRED_STATE = "EXPIRED";
	
	public static final long TWO_HOURS_VALID = 2l * 60l * 60l * 1000l;

}
