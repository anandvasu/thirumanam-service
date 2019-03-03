package com.thirumanam.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CORSFilter implements Filter {
	private Set<String> validOrigins = new HashSet<String>();

	private String allowedOrigins;
	
	@Autowired
	AwsCognitoIdTokenProcessor idTokenProcessor;

	@Override
	public void init(FilterConfig config) throws ServletException {
		// nothing to initialize
		if (allowedOrigins != null && !allowedOrigins.isEmpty()) {
			String[] origins = allowedOrigins.split(",");
			for (String origin : origins) {
				validOrigins.add(origin);
			}
		}
	}

	@Override
	public void destroy() {
		// nothing to cleanup
	}

	
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain chain)
					throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		String origin = request.getHeader("Origin");
		//if (origin != null) 
			//if (validOrigins.contains(origin)) {
				response.addHeader("Access-Control-Allow-Origin", origin);
			//}
			
			System.out.println("origin - "+origin+" validOrigins.contains(origin) "+validOrigins.contains(origin));
			
			response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
			response.addHeader("Access-Control-Allow-Headers",
					"Content-Type, accept, X-TOTAL-DOCS, Authorization");
			response.addHeader("Access-Control-Allow-Credentials", "true");
			response.addHeader("Access-Control-Max-Age", "1209600");
			response.addHeader("Access-Control-Expose-Headers", "X-TOTAL-DOCS");
		//}
		
		System.out.println(" Check the Method before Returning - "+request.getMethod());
				
		
		if (request.getMethod().equals( "OPTIONS" ) ) {
				System.out.println(" Not Going to Authentication Returning Options ");
			 return;
		}
		System.out.println("request URI:" + request.getRequestURI());
	
		idTokenProcessor.processIdToken(request);
		
		chain.doFilter(servletRequest, servletResponse);
	}
}   
