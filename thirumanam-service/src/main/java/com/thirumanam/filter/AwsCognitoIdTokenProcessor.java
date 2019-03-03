package com.thirumanam.filter;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;

@Component
public class AwsCognitoIdTokenProcessor {
	
	private static final String ROLE_PREFIX = "ROLE_";
    private static final String EMPTY_PWD = "";
    private static final String BEARER_PREFIX = "Bearer ";
    
    @Autowired
    private ConfigurableJWTProcessor<SecurityContext> configurableJWTProcessor;

	public void processIdToken(HttpServletRequest request) {
		String idToken = request.getHeader("Authorization");
		if (idToken != null) {

            JWTClaimsSet claimsSet = null;
            
            try {
            claimsSet = configurableJWTProcessor.process(stripBearerToken(idToken), null);
            } catch (Exception exp) {
            	exp.fillInStackTrace();
            }

            if (!isIssuedCorrectly(claimsSet)) {
               // throw new Exception(String.format("Issuer %s in JWT token doesn't match cognito idp %s", claimsSet.getIssuer(), jwtConfiguration.getCognitoIdentityPoolUrl()));
            }

            if (!isIdToken(claimsSet)) {
                //throw new Exception("JWT Token doesn't seem to be an ID Token");
            }            
        }
	}
	
	 private String stripBearerToken(String token) {
	        return token.startsWith(BEARER_PREFIX) ? token.substring(BEARER_PREFIX.length()) : token;
	    }

	    private boolean isIssuedCorrectly(JWTClaimsSet claimsSet) {
	    	return true;
	       // return claimsSet.getIssuer().equals(jwtConfiguration.getCognitoIdentityPoolUrl());
	    }

	    private boolean isIdToken(JWTClaimsSet claimsSet) {
	        return claimsSet.getClaim("token_use").equals("id");
	    }

	    private static <T, U> List<U> convertList(List<T> from, Function<T, U> func) {
	        return from.stream().map(func).collect(Collectors.toList());
	    }

}
