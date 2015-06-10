package com.shouvikbasu.client.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AuthClientProperties {

	private Properties clientProps = new Properties();
	private static AuthClientProperties _instance = new AuthClientProperties();
	
	private AuthClientProperties() {
	}
	
	public static AuthClientProperties getInstance() throws IllegalArgumentException {
		
		if( _instance == null ){
			throw new IllegalArgumentException( "AuthClientProperties not initialised" );
		}else{
			return _instance;
		}
		
	}
	
	public void initialise( InputStream inStream ) throws IOException{
		clientProps.load(inStream);
	}
	
	public String getString( String key ){
		return clientProps.get(key) == null ? null : clientProps.get(key).toString().trim() ;
	}
}
