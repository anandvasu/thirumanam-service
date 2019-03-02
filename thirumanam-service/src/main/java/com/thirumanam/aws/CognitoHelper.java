package com.thirumanam.aws;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.SignUpRequest;
import com.amazonaws.services.cognitoidp.model.SignUpResult;

@Component
public class CognitoHelper {
	
	@Value("${aws.poolId}")
	private String POOL_ID;
	
	@Value("${aws.clientAppId}")
    private String CLIENTAPP_ID;
      
    @Value("${aws.region}")
    private String REGION;
		
    private AttributeType createAttributeType(String name, 
    		String value ) {
    	AttributeType attributeType = new AttributeType();
		attributeType.setName(name);
	    attributeType.setValue(value);
    	return attributeType;
    }
    
	public String SignUpUser(
			String username, 
			String password, 
			String email, 
			String phonenumber,
			String familyName,
			String givenName
			) {
		
		String externalId = null;
				
        AnonymousAWSCredentials awsCreds = new AnonymousAWSCredentials();
        AWSCognitoIdentityProvider cognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(Regions.fromName(REGION))
                .build();

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setClientId(CLIENTAPP_ID);
        signUpRequest.setUsername(username);
        signUpRequest.setPassword(password);
        
        List<AttributeType> list = new ArrayList<>();  
        list.add(createAttributeType("email", email));
        list.add(createAttributeType("phone_number", phonenumber));
        list.add(createAttributeType("family_name", familyName));
        list.add(createAttributeType("given_name", givenName));   

        signUpRequest.setUserAttributes(list);

        try {
            SignUpResult result = cognitoIdentityProvider.signUp(signUpRequest);
            externalId = result.getUserSub();
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e);
        }
        return externalId;
    }
	
	
	 /**
     * Helper method to validate the user
     *
     * @param username represents the username in the cognito user pool
     * @param password represents the password in the cognito user pool
     * @return returns the AWSLoginResponse after the validation
     */
    public AWSLoginResponse ValidateUser(String username, String password) {
        AuthenticationHelper helper = new AuthenticationHelper(POOL_ID, CLIENTAPP_ID, "", REGION);
        return helper.PerformSRPAuthentication(username, password);
    }
}