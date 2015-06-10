package com.shouvikbasu.authserver;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shouvikbasu.authserver.util.ClientUtils;
import com.shouvikbasu.authserver.util.PasswordValidator;

@WebServlet("/login")
public class login extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try
		{
			
			String userid = request.getParameter( "userid" );
			String passwd = request.getParameter( "passwd" );
			String client_id = request.getParameter( "clientId" );
			
			if(! PasswordValidator.checkCredentials(userid, passwd) ){
				request.getRequestDispatcher( "login.jsp" );
				
			}else{
				
				if( ClientUtils.checkIfValidClientId(client_id) ){
					
					String access_code = ClientUtils.getAccessCode( userid, client_id );
					String redirectionUrl = ClientUtils.getRedirectionUrl(client_id);
					
					response.setStatus( 200 );
					response.sendRedirect(redirectionUrl + "?code=" + access_code);				
					
				}else{
					response.sendError( 401,  "please provide a valid client id in request to authorise" );
				}
			}
		
		}catch(Exception ex){
			ex.printStackTrace();
			response.sendError( 500,  "error occured in server" );
		}
		
	}

}
