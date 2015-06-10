package com.shouvikbasu.client.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.shouvikbasu.client.utils.AuthClientProperties;
import com.shouvikbasu.client.utils.AuthConstants;
import com.shouvikbasu.client.utils.OAuth2Utils;

public abstract class BaseSecurityFilter implements Filter {

	protected String authMaster_AuthUrl = null;
	protected String clientId = null;

	protected String loginUrl = null;
	protected String logoutUrl = null;

	public void destroy() {
		// TODO Auto-generated method stub
	}

	public void init(FilterConfig fConfig) throws ServletException {
		
		loginUrl = AuthClientProperties.getInstance().getString( "this_loginUrl" );
		logoutUrl = AuthClientProperties.getInstance().getString( "this_logoutUrl" );
		
		clientId = AuthClientProperties.getInstance().getString( "client_id" );
		
		String authserver_url = AuthClientProperties.getInstance().getString( "authserver_url" );
		authMaster_AuthUrl =authserver_url + AuthConstants.PATH_AUTHORIZE;
			
	}
	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		if (request instanceof HttpServletRequest) {

			try {

				String url = ((HttpServletRequest) request).getRequestURL().toString();
				if (loginUrl.equals(url)) {
					doLogin( (HttpServletRequest) request, (HttpServletResponse) response, chain );
					
				}else if(logoutUrl.equals(url)){
					doLogout( (HttpServletRequest) request, (HttpServletResponse) response, chain );
					
				}else{

					HttpSession session = ((HttpServletRequest) request).getSession(true);
					Cookie[] cookies = ((HttpServletRequest) request).getCookies();
					 
					String access_token = null;
					
					if (cookies != null) {
						
						for (int i = 0; i < cookies.length; i++) {
						
							if (cookies[i].getName().equals(AuthConstants.ACCESS_TOKEN)) {
							  access_token = cookies[i].getValue(); 
						    }
						}
					}
					
					String access_code = (String) session.getAttribute(AuthConstants.ACCESS_CODE);
					String userId = (String) session.getAttribute(AuthConstants.USER_ID);
	
					if( StringUtils.isEmpty(access_token) || 
						StringUtils.isEmpty(access_code) || 
						StringUtils.isEmpty(userId) ){
						
						sendToAuthorise(((HttpServletResponse) response));
						
					}else{
					
						boolean token_valid = OAuth2Utils.checkAccessToken( access_token, access_code, userId,
																			((HttpServletRequest) request),
																			((HttpServletResponse) response));
						if (!token_valid) {
							session.invalidate();
							((HttpServletResponse) response).sendError(401);
							
						}else{
							chain.doFilter(request, response);
						}
					}
				}

			} catch (Exception ex) {

				ex.printStackTrace();
				((HttpServletResponse) response).sendError(500);
			}

		}else{
			chain.doFilter(request, response);
		}

	}

	private void doLogin(HttpServletRequest request, HttpServletResponse response, FilterChain chain ) throws Exception{
		
		String access_code = request.getParameter(AuthConstants.CODE);
		if( access_code == null ){
			response.sendError( 302 );
			return;
		}
		
		JSONObject access_token_obj = OAuth2Utils.getAccessToken( access_code );
		if( access_token_obj == null ){
			response.sendError(500);
			return;
		}
		
		String access_token = access_token_obj.getString(AuthConstants.ACCESS_TOKEN);
		String refresh_token = access_token_obj.getString(AuthConstants.REFRESH_TOKEN);
		String user_id = access_token_obj.getString(AuthConstants.USER_ID);
				
		if( access_token == null || access_token == "" ){
			response.sendError( 404 );
			return;
		}else{
			System.out.println( "access token " + access_token );
		}
		
		Cookie c_access_token = new Cookie( AuthConstants.ACCESS_TOKEN, access_token );
		Cookie c_refresh_token = new Cookie( AuthConstants.REFRESH_TOKEN, refresh_token ); 
		response.addCookie(c_access_token);
		response.addCookie(c_refresh_token);
		
		HttpSession session = request.getSession(true);
		session.setAttribute( AuthConstants.ACCESS_CODE, access_code );
		session.setAttribute( AuthConstants.USER_ID, user_id );
		
		request.setAttribute( AuthConstants.USER_ID, user_id );
		
		chain.doFilter(request, response);
	}
	
	private void doLogout( HttpServletRequest request, HttpServletResponse response, FilterChain chain ) throws ServletException, IOException {
		
		try 
		{
			HttpSession session = request.getSession(true);
			String access_token = null;
			String refresh_token = null;
			String access_code = (String) session.getAttribute( AuthConstants.ACCESS_CODE );
			
			Cookie[] cookies = ((HttpServletRequest) request).getCookies();
			
			if (cookies != null) {
				
				for (int i = 0; i < cookies.length; i++) {
				
					if(cookies[i].getName().equals(AuthConstants.ACCESS_TOKEN)) {
						access_token = cookies[i].getValue();
						
					}else if(cookies[i].getName().equals(AuthConstants.REFRESH_TOKEN)) {
						refresh_token = cookies[i].getValue();
					}
					
				}
			}
			
			if( OAuth2Utils.revokeToken(access_code, access_token, refresh_token ) ){
				session.invalidate();
				
				if (cookies != null) {
					
					for (int i = 0; i < cookies.length; i++) {
					
						if(cookies[i].getName().equals(AuthConstants.ACCESS_TOKEN)) {
							cookies[i].setValue("");
							cookies[i].setMaxAge(0);
							
						}else if(cookies[i].getName().equals(AuthConstants.REFRESH_TOKEN)) {
							cookies[i].setValue("");
							cookies[i].setMaxAge(0);
						}
						
					}
				}
				
				chain.doFilter(request, response);
				
			}else{
				response.sendError(500);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void sendToAuthorise( HttpServletResponse response ) throws IOException{
		String login_url = authMaster_AuthUrl + "?client_id=" + clientId;
		response.sendRedirect(login_url);
	}
}
