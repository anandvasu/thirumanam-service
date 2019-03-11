package com.thirumanam.filter;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.BadJWTException;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.thirumanam.aws.JWTConfiguration;
import com.thirumanam.exception.ThirumanamException;
import com.thirumanam.util.ErrorMessageConstants;

@Component
public class AwsCognitoIdTokenProcessor {	
  
    @Autowired
    private ConfigurableJWTProcessor<SecurityContext> configurableJWTProcessor;    
    
    @Autowired
    private JWTConfiguration jwtConfiguration;
   
	public void processIdToken(HttpServletRequest request, Map<String,String> unAuthApis) throws ThirumanamException {
		try {
		String idToken = request.getHeader(JWTConfiguration.HEADER);
		if (idToken != null) {

            JWTClaimsSet claimsSet  = configurableJWTProcessor.process(stripBearerToken(idToken), null);           

            if (!isIssuedCorrectly(claimsSet)) {
            	throw new ThirumanamException(
            			ErrorMessageConstants.CODE_INVALID_JWT_ISSUER, 
            			ErrorMessageConstants.MESSAGE_INVALID__JWT_ISSUER);
            	//TODO:Log proper message;
            }

            if (!isIdToken(claimsSet)) {
            	throw new ThirumanamException(
            			ErrorMessageConstants.CODE_INVALID_JWT_ISSUER, 
            			ErrorMessageConstants.MESSAGE_INVALID__JWT_ISSUER);
            }          
            throw new ThirumanamException(
        			ErrorMessageConstants.CODE_EXPIRED_JWT, 
        			ErrorMessageConstants.MESSAGE_EXPIRED_JWT);
        } else {
        	 String requestURI = request.getRequestURI();
        	 String requestMethod = request.getMethod();
        	 System.out.println(unAuthApis.get(requestURI));
        	 System.out.println(unAuthApis.get(requestURI));
        	 
        	 if(unAuthApis.get(requestURI) == null || !requestMethod.equals(unAuthApis.get(requestURI))) {
        		 throw new ThirumanamException(ErrorMessageConstants.CODE_INVALID_JWT_ISSUER, ErrorMessageConstants.MESSAGE_INVALID__JWT_ISSUER);
        	 }
        }
		} catch (BadJWTException exp) {
			if(ErrorMessageConstants.MESSAGE_EXPIRED_JWT.equals(exp.getCause().getMessage())) {
				throw new ThirumanamException(
	        			ErrorMessageConstants.CODE_EXPIRED_JWT, 
	        			ErrorMessageConstants.MESSAGE_EXPIRED_JWT);
			}
		} catch (Exception exp) {
			throw new ThirumanamException(
        			ErrorMessageConstants.CODE_INVALID_JWT_ISSUER, 
        			ErrorMessageConstants.MESSAGE_INVALID__JWT_ISSUER);
		}
	}
	
	private String stripBearerToken(String token) {
	    return token.startsWith(JWTConfiguration.BEARER_PREFIX) ? 
	    		token.substring(JWTConfiguration.BEARER_PREFIX.length()) : token;
	}

	private boolean isIssuedCorrectly(JWTClaimsSet claimsSet) {	    
		System.out.println("Claim Set:" + claimsSet.getIssuer());
		System.out.println("jwtConfiguration:" + jwtConfiguration.getJwkUrl());
		//return true;
		return claimsSet.getIssuer().equals(jwtConfiguration.getJwkUrl());
	}
	
	private boolean isIdToken(JWTClaimsSet claimsSet) {
	    return claimsSet.getClaim(JWTConfiguration.TOKEN_USE).equals(JWTConfiguration.ID);
	}
}