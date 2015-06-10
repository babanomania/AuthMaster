package com.shouvikbasu.demo.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.shouvikbasu.client.utils.AuthConstants;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try 
		{
		
			String user_id = (String) request.getAttribute( AuthConstants.USER_ID );
			if( StringUtils.isEmpty(user_id) ){
				response.sendError( 401 );
				return;
			}
			
			request.getRequestDispatcher( "index.jsp" ).forward(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
