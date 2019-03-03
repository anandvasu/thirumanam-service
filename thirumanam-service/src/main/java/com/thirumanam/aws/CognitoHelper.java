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
import com.amazonaws.services.cognitoidp.model.CodeMismatchException;
import com.amazonaws.services.cognitoidp.model.ConfirmForgotPasswordRequest;
import com.amazonaws.services.cognitoidp.model.ConfirmForgotPasswordResult;
import com.amazonaws.services.cognitoidp.model.ConfirmSignUpRequest;
import com.amazonaws.services.cognitoidp.model.ConfirmSignUpResult;
import com.amazonaws.services.cognitoidp.model.ForgotPasswordRequest;
import com.amazonaws.services.cognitoidp.model.ForgotPasswordResult;
import com.amazonaws.services.cognitoidp.model.LimitExceededException;
import com.amazonaws.services.cognitoidp.model.ResendConfirmationCodeRequest;
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
	
	 public boolean VerifyAccessCode(String username, String code) throws CodeMismatchException {
	        AnonymousAWSCredentials awsCreds = new AnonymousAWSCredentials();
	        AWSCognitoIdentityProvider cognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder
	                .standard()
	                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
	                .withRegion(Regions.fromName(REGION))
	                .build();	        
	        		        
	        ConfirmSignUpRequest confirmSignUpRequest = new ConfirmSignUpRequest();
	        confirmSignUpRequest.setUsername(username);
	        confirmSignUpRequest.setConfirmationCode(code);
	        confirmSignUpRequest.setClientId(CLIENTAPP_ID);
	
            ConfirmSignUpResult confirmSignUpResult = cognitoIdentityProvider.confirmSignUp(confirmSignUpRequest);
            System.out.println("confirmSignupResult=" + confirmSignUpResult.toString());
	        
	        return true;
	  }
	 
	 public boolean resendAccessCode(String username) {
		 AnonymousAWSCredentials awsCreds = new AnonymousAWSCredentials();
	        AWSCognitoIdentityProvider cognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder
	                .standard()
	                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
	                .withRegion(Regions.fromName(REGION))
	                .build();
	        try {
	        ResendConfirmationCodeRequest resendConfirmationCodeRequest
	        	= new ResendConfirmationCodeRequest();
	        resendConfirmationCodeRequest.setUsername(username);
	        resendConfirmationCodeRequest.setClientId(CLIENTAPP_ID);
	        cognitoIdentityProvider.resendConfirmationCode(resendConfirmationCodeRequest);
	        } catch (Exception exp) {
	        	 System.out.println(exp);
	        	 return false;
	        }
		 return true;
	 }
	 
	 public ForgotPasswordResult resetPassword(String username) throws LimitExceededException{
	        AnonymousAWSCredentials awsCreds = new AnonymousAWSCredentials();
	        AWSCognitoIdentityProvider cognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder
	                .standard()
	                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
	                .withRegion(Regions.fromName(REGION))
	                .build();

	        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
	        forgotPasswordRequest.setUsername(username);
	        forgotPasswordRequest.setClientId(CLIENTAPP_ID);
	        return cognitoIdentityProvider.forgotPassword(forgotPasswordRequest);	       
	  }
	 
	 
	 public ConfirmForgotPasswordResult updatePassword(String username, String newpw, String code) {
	        AnonymousAWSCredentials awsCreds = new AnonymousAWSCredentials();
	        AWSCognitoIdentityProvider cognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder
	                .standard()
	                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
	                .withRegion(Regions.fromName(REGION))
	                .build();

	        ConfirmForgotPasswordRequest confirmPasswordRequest = new ConfirmForgotPasswordRequest();
	        confirmPasswordRequest.setUsername(username);
	        confirmPasswordRequest.setPassword(newpw);
	        confirmPasswordRequest.setConfirmationCode(code);
	        confirmPasswordRequest.setClientId(CLIENTAPP_ID);
	        return cognitoIdentityProvider.confirmForgotPassword(confirmPasswordRequest);	        
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
        return helper.performSRPAuthentication(username, password);
    }
    
    public AWSLoginResponse validateUserWithRefToken(String refreshToken) {
        AuthenticationHelper helper = new AuthenticationHelper(POOL_ID, CLIENTAPP_ID, "", REGION);
        return helper.performRefreshTokenAuthentication(refreshToken);
    }
}