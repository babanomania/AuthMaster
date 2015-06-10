package com.shouvikbasu.demo.filters;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;

import com.shouvikbasu.client.filters.BaseSecurityFilter;
import com.shouvikbasu.client.utils.AuthClientProperties;

@WebFilter(urlPatterns = {"/*"}, description = "AuthMaster Security Filter")
public class AuthMasterFilter extends BaseSecurityFilter {

	public void init(FilterConfig fConfig) throws ServletException {
		
		try 
		{
			AuthClientProperties
				.getInstance()
				.initialise( 
								fConfig
									.getServletContext()
									.getResourceAsStream("/WEB-INF/AuthClient.properties") 
							);
			
			super.init(fConfig);
			
		} catch (Exception e) {
			throw new ServletException(e);
		}
		
	}

}
