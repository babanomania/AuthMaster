package com.shouvikbasu.client.utils;

import java.io.ByteArrayOutputStream;
import java.net.URI;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.json.JSONObject;

public class OAuth2Utils {
	
	private static final String client_id = AuthClientProperties.getInstance().getString( "client_id" );
	private static final String client_secret = AuthClientProperties.getInstance().getString( "client_secret" );
	private static final String authserver_url = AuthClientProperties.getInstance().getString( "authserver_url" );
	
	private static final String access_token_url = authserver_url + AuthConstants.PATH_TOKEN;
	private static final String check_token_url = authserver_url + AuthConstants.PATH_CHECK_TOKEN;
	private static final String refresh_token_url = authserver_url + AuthConstants.PATH_REFRESH_TOKEN;
	private static final String revoke_token_url = authserver_url + AuthConstants.PATH_REVOKE_TOKEN;
	
	public static JSONObject getAccessToken( String accessCode ) throws Exception {
		
		String authorisation_header_str = client_id + ":" + client_secret;
		String authorisation_header_endoded = AuthConstants.BASIC_SPACE + new String( Base64.encodeBase64( authorisation_header_str.getBytes() ) );
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpUriRequest access_token_Post;
		
		access_token_Post = RequestBuilder.post().setUri(new URI(access_token_url))
								                .addParameter( AuthConstants.CODE, accessCode )
								                .addHeader( AuthConstants.Authorization, authorisation_header_endoded )
								                .build();
		
        CloseableHttpResponse access_token_response = httpclient.execute(access_token_Post);
		HttpEntity access_token_entity = access_token_response.getEntity();
		ByteArrayOutputStream baos_at = new ByteArrayOutputStream();
		IOUtils.copy( access_token_entity.getContent(), baos_at );
		
		EntityUtils.consume( access_token_entity );
		
		int responseStatus = access_token_response.getStatusLine().getStatusCode();
		if( responseStatus != 200  ){
			
			System.out.println(baos_at.toString());
			return null;
			
		}else{
			
			String access_token_json_response = baos_at.toString();
			JSONObject access_token_obj = new JSONObject(access_token_json_response);
			
			return access_token_obj ;
		}
		
	}
	
	public static JSONObject getRefreshToken( String accessCode, String refreshToken ) throws Exception {
		
		String authorisation_header_str = client_id + ":" + client_secret;
		String authorisation_header_endoded = AuthConstants.BASIC_SPACE + new String( Base64.encodeBase64( authorisation_header_str.getBytes() ) );
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpUriRequest access_token_Post;
		
		access_token_Post = RequestBuilder.post().setUri(new URI(refresh_token_url))
								                .addParameter(AuthConstants.CODE, accessCode )
								                .addParameter(AuthConstants.REFRESH_TOKEN, refreshToken )
								                .addHeader( AuthConstants.Authorization, authorisation_header_endoded )
								                .build();
		
        CloseableHttpResponse access_token_response = httpclient.execute(access_token_Post);
		HttpEntity access_token_entity = access_token_response.getEntity();
		ByteArrayOutputStream baos_at = new ByteArrayOutputStream();
		IOUtils.copy( access_token_entity.getContent(), baos_at );
		
		EntityUtils.consume( access_token_entity );
		
		String access_token_json_response = baos_at.toString();
		JSONObject access_token_obj = new JSONObject(access_token_json_response);
		String access_token = access_token_obj.getString(AuthConstants.ACCESS_TOKEN);
		
		if( StringUtils.isEmpty(access_token) || access_token_response.getStatusLine().getStatusCode() != 200  ){
			System.out.println(baos_at.toString());
			return null;
		}else{
			return access_token_obj ;
		}
		
	}
	
	public static boolean revokeToken( String accessCode, String accessToken, String refreshToken ) throws Exception {
		
		String authorisation_header_str = client_id + ":" + client_secret;
		String authorisation_header_endoded = AuthConstants.BASIC_SPACE + new String( Base64.encodeBase64( authorisation_header_str.getBytes() ) );
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpUriRequest access_token_Post;
		
		access_token_Post = RequestBuilder.post().setUri(new URI(revoke_token_url))
								                .addParameter(AuthConstants.CODE, accessCode )
								                .addParameter(AuthConstants.ACCESS_TOKEN, accessToken )
								                .addParameter(AuthConstants.REFRESH_TOKEN, refreshToken )
								                .addHeader( AuthConstants.Authorization, authorisation_header_endoded )
								                .build();
		
        CloseableHttpResponse access_token_response = httpclient.execute(access_token_Post);
		HttpEntity access_token_entity = access_token_response.getEntity();
		ByteArrayOutputStream baos_at = new ByteArrayOutputStream();
		IOUtils.copy( access_token_entity.getContent(), baos_at );
		
		EntityUtils.consume( access_token_entity );
				
		if( access_token_response.getStatusLine().getStatusCode() != 200  ){
			System.out.println(baos_at.toString());
			return false;
		}else{
			return true ;
		}
		
	}
	
	public static boolean checkAccessToken( String accessToken, String accessCode, String userId, 
											HttpServletRequest request, HttpServletResponse response ) throws Exception {
		
		String authorisation_header_str = client_id + ":" + client_secret;
		String authorisation_header_endoded = AuthConstants.BASIC_SPACE + new String( Base64.encodeBase64( authorisation_header_str.getBytes() ) );
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpUriRequest access_token_Post;
		
		access_token_Post = RequestBuilder.post().setUri(new URI(check_token_url))
								                .addHeader(AuthConstants.ACCESS_TOKEN, accessToken)
								                .addParameter(AuthConstants.CODE, accessCode )
								                .addHeader( AuthConstants.Authorization, authorisation_header_endoded )
								                .build();
		
        CloseableHttpResponse access_token_response = httpclient.execute(access_token_Post);
		HttpEntity access_token_entity = access_token_response.getEntity();
		ByteArrayOutputStream baos_at = new ByteArrayOutputStream();
		IOUtils.copy( access_token_entity.getContent(), baos_at );
		
		EntityUtils.consume( access_token_entity );
		
		String access_token_json_response = baos_at.toString();
		System.out.println( access_token_json_response );
		
		if( access_token_response.getStatusLine().getStatusCode() != 200 ){
			return false;
		}
		
		JSONObject access_token_obj = new JSONObject(access_token_json_response);
		
		String as_resp_user_id = access_token_obj.getString(AuthConstants.USER_ID);
		String as_resp_state = access_token_obj.getString(AuthConstants.STATE);
		String as_resp_refresh_token = access_token_obj.getString(AuthConstants.REFRESH_TOKEN);
		
		if( access_token_response.getStatusLine().getStatusCode() == 200 &&
			as_resp_user_id.equals( userId ) ){
			
			if( "OK".equals(as_resp_state) ){
				
				Cookie c_access_token = new Cookie( AuthConstants.ACCESS_TOKEN, accessToken );
				Cookie c_refresh_token = new Cookie( AuthConstants.REFRESH_TOKEN, as_resp_refresh_token ); 
				((HttpServletResponse) response).addCookie(c_access_token);
				((HttpServletResponse) response).addCookie(c_refresh_token);
				
				return true;
			
			}else if( "EXPIRED".equals(as_resp_state) ){
				
				Cookie[] cookies = ((HttpServletRequest) request).getCookies();
				String refreshToken = null;
				
				if (cookies != null) {
					
					for (int i = 0; i < cookies.length; i++) {
					
						if(cookies[i].getName().equals(AuthConstants.REFRESH_TOKEN)) {
							refreshToken = cookies[i].getValue();
						}
						
					}
				}
				
				JSONObject re_access_token_obj = getRefreshToken(accessCode, refreshToken);
				
				String access_token = re_access_token_obj.getString(AuthConstants.ACCESS_TOKEN);
				String refresh_token = re_access_token_obj.getString(AuthConstants.REFRESH_TOKEN);
				String user_id = re_access_token_obj.getString(AuthConstants.USER_ID);
				
				Cookie c_access_token = new Cookie( AuthConstants.ACCESS_TOKEN, access_token );
				Cookie c_refresh_token = new Cookie( AuthConstants.REFRESH_TOKEN, refresh_token ); 
				response.addCookie(c_access_token);
				response.addCookie(c_refresh_token);
				
				HttpSession session = request.getSession();
				session.setAttribute( AuthConstants.USER_ID, user_id );
				session.setAttribute( AuthConstants.ACCESS_CODE, accessCode );
				
				return true;
			}
			
		}
		
		return false;
		
	}

}
