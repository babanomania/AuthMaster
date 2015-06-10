package com.shouvikbasu.authserver;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.shouvikbasu.authserver.util.TokenUtils;
import com.shouvikbasu.client.utils.AuthConstants;

@WebServlet(AuthConstants.PATH_CHECK_TOKEN)
public class CheckToken extends HttpServlet {

	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try
		{
			
			String access_token = request.getHeader( AuthConstants.ACCESS_TOKEN );
			
			if( StringUtils.isEmpty( access_token ) || ! TokenUtils.checkIfValidAccessToken(access_token) ){
				response.sendError( 401, "invalid access_token");
			}else{
				
				OutputStream osResponse = response.getOutputStream();
				BufferedOutputStream bosResponse = new BufferedOutputStream(osResponse);
				bosResponse.write( TokenUtils.getCheckAccessTokenString(access_token).getBytes() );
				bosResponse.flush();
				bosResponse.close();
				
				response.setStatus(200);
				
			}
		
		}catch(Exception ex){
			ex.printStackTrace();
			response.sendError( 500, "error occured in server");
		}
	}

}
