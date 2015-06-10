package com.shouvikbasu.authserver;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.shouvikbasu.authserver.util.ClientUtils;
import com.shouvikbasu.client.utils.AuthConstants;

@WebServlet(AuthConstants.PATH_AUTHORIZE)
public class Authorize extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
	
		try 
		{
			Properties authProps = new Properties();
			authProps.load( getServletContext().getResourceAsStream( "/WEB-INF/AuthMaster.properties" ) );
			
			int maxClients = Integer.parseInt( authProps.getProperty( "maxClients" ) );
			for( int counter=1;  counter<=maxClients; counter++ ){
				
				String clientId = authProps.getProperty( "client." + counter + ".clientId" ).trim();
				String clientSecret = authProps.getProperty( "client." + counter + ".clientSecret" ).trim();
				String redirectionUrl = authProps.getProperty( "client." + counter + ".redirectionUrl" ).trim();
				
				ClientUtils.registerClient( clientId, clientSecret, redirectionUrl );
			}
			
		} catch (Exception e) {
			throw new ServletException(e);
		}
		
		super.init();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String client_id = request.getParameter( AuthConstants.CLIENT_ID );
		if( !StringUtils.isEmpty(client_id) && ClientUtils.checkIfValidClientId(client_id) ){
			request.getRequestDispatcher( "login.jsp" ).forward(request, response);
			
		}else{
			response.sendError( 401,  "please provide a valid client id in request to authorise" );
		}
	}

}
