package com.shouvikbasu.authserver;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shouvikbasu.authserver.exceptions.InvalidAuthHeaderException;
import com.shouvikbasu.authserver.util.ClientUtils;
import com.shouvikbasu.authserver.util.TokenUtils;
import com.shouvikbasu.client.utils.AuthConstants;

@WebServlet(AuthConstants.PATH_TOKEN)
public class AccessToken extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try
		{
			String access_code = request.getParameter( AuthConstants.CODE );
			
			try
			{
				ClientUtils.validateAuthHeader(request);
			
			}catch(InvalidAuthHeaderException iahe){
				response.sendError( 401, iahe.getMessage() );
			}
			
			OutputStream osResponse = response.getOutputStream();
			BufferedOutputStream bosResponse = new BufferedOutputStream(osResponse);
			bosResponse.write( TokenUtils.getAccessTokenString( access_code ).getBytes() );
			bosResponse.flush();
			bosResponse.close();
			
			response.setStatus(200);
		
		}catch(Exception ex){
			ex.printStackTrace();
			response.sendError( 500, "error occured in server");
		}
	}

}
