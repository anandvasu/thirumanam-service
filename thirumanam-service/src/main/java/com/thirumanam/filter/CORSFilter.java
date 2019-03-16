package com.thirumanam.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.thirumanam.exception.ThirumanamException;
import com.thirumanam.util.ThirumanamConstant;

@Component
public class CORSFilter implements Filter {
	
	@Value("${unauthapis}")
	private String unAuthApis;
	
	private Map<String,String> unAuthUrlMap = new HashMap<String,String>();
	
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
		
		if(unAuthApis != null) {
			String [] uris = unAuthApis.split(",");
			for(int i=0;i<uris.length;i++) {
				String [] urnEntry = uris[i].split(":");
				unAuthUrlMap.put(urnEntry[0], urnEntry[1]);
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
		
		try {	
			String requestURI = request.getRequestURI();
	   	 	String requestMethod = request.getMethod();
	   	 	if(unAuthUrlMap.get(requestURI) == null || !requestMethod.equals(unAuthUrlMap.get(requestURI))) {
	   	 		boolean isTokenValid = idTokenProcessor.processIdToken(request);
	   	 		request.setAttribute(ThirumanamConstant.USER_AUTHORIZED, isTokenValid);
	   	 	}
			chain.doFilter(servletRequest, servletResponse);
		} catch (ThirumanamException exp) { 
			StringBuilder sb = new StringBuilder();
			sb.append("{")
			.append("\"code\":\"")
			.append(exp.getCode())
			.append("\",")
			.append("\"message\":\"")
			.append(exp.getMessage())
			.append("\"}");
			response.setStatus(401);			
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.println(sb.toString());
			out.flush();
			out.close();
			return;
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
}   