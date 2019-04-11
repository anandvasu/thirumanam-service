package com.thirumanam.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JWTConfiguration {
	
	public static final String HEADER = "Authorization";
	public static final String BEARER_PREFIX = "Bearer";
	public static final String ID = "id";
	public static final String TOKEN_USE = "token_use"; 
	
	private static final String COGNITO_IDENTITY_POOL_URL = "https://cognito-idp.%s.amazonaws.com/%s";
	private static final String JSON_WEB_TOKEN_SET_URL_SUFFIX = "/.well-known/jwks.json";

	@Value("${aws.poolId}")
	private String POOL_ID;
	
	@Value("${aws.clientAppId}")
    private String CLIENTAPP_ID;
      
    @Value("${aws.region}")
    private String REGION;
    
    @Value("${aws.secretKey}")
    private String secretKey;
    
    @Value("${aws.accessKey}")
    private String accessKey;
    
    
    public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}	
   
	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	
	public String getPOOL_ID() {
		return POOL_ID;
	}

	public void setPOOL_ID(String pOOL_ID) {
		POOL_ID = pOOL_ID;
	}

	public String getCLIENTAPP_ID() {
		return CLIENTAPP_ID;
	}

	public void setCLIENTAPP_ID(String cLIENTAPP_ID) {
		CLIENTAPP_ID = cLIENTAPP_ID;
	}

	public String getREGION() {
		return REGION;
	}

	public void setREGION(String rEGION) {
		REGION = rEGION;
	}	
	
	 public String getJwkUrl() {
	      return String.format(COGNITO_IDENTITY_POOL_URL,REGION,POOL_ID);
	}

	public String getCognitoIdentityPoolUrl() {
		return String.format(COGNITO_IDENTITY_POOL_URL + JSON_WEB_TOKEN_SET_URL_SUFFIX, REGION, POOL_ID);
		 //return "https://cognito-idp."+REGION+".amazonaws.com/"+POOL_ID+"/.well-known/jwks.json";
	}
}