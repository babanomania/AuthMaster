package com.shouvikbasu.authserver;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shouvikbasu.authserver.exceptions.InvalidAuthHeaderException;
import com.shouvikbasu.authserver.util.ClientUtils;
import com.shouvikbasu.authserver.util.TokenUtils;
import com.shouvikbasu.client.utils.AuthConstants;

@WebServlet(AuthConstants.PATH_REVOKE_TOKEN)
public class RevokeToken extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try
		{
			
			String refresh_token = request.getParameter( AuthConstants.REFRESH_TOKEN );
			String access_token = request.getParameter( AuthConstants.ACCESS_TOKEN );
			
			if (! TokenUtils.checkIfValidRefreshToken(refresh_token) ) {
				response.sendError( 401, "invalid refresh token");
		
			}else if (! TokenUtils.checkIfValidAccessToken(access_token) ) {
				response.sendError( 401, "invalid access token");
				
			}else{
				
				try
				{
					ClientUtils.validateAuthHeader(request);
				
				}catch(InvalidAuthHeaderException iahe){
					response.sendError( 401, iahe.getMessage() );
				}
				
				TokenUtils.revokeTokens(access_token, refresh_token);
				response.setStatus(200);
				
			}
		
		}catch(Exception ex){
			ex.printStackTrace();
			response.sendError( 500, "error occured in server");
		}
	}

}
