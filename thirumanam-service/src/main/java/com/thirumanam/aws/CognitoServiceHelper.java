package com.thirumanam.aws;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AdminUpdateUserAttributesRequest;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.ChangePasswordRequest;
import com.amazonaws.services.cognitoidp.model.ChangePasswordResult;
import com.amazonaws.services.cognitoidp.model.CodeMismatchException;
import com.amazonaws.services.cognitoidp.model.ConfirmForgotPasswordRequest;
import com.amazonaws.services.cognitoidp.model.ConfirmForgotPasswordResult;
import com.amazonaws.services.cognitoidp.model.ConfirmSignUpRequest;
import com.amazonaws.services.cognitoidp.model.ConfirmSignUpResult;
import com.amazonaws.services.cognitoidp.model.DeleteUserRequest;
import com.amazonaws.services.cognitoidp.model.DeleteUserResult;
import com.amazonaws.services.cognitoidp.model.ForgotPasswordRequest;
import com.amazonaws.services.cognitoidp.model.ForgotPasswordResult;
import com.amazonaws.services.cognitoidp.model.LimitExceededException;
import com.amazonaws.services.cognitoidp.model.ResendConfirmationCodeRequest;
import com.amazonaws.services.cognitoidp.model.SignUpRequest;
import com.amazonaws.services.cognitoidp.model.SignUpResult;
import com.amazonaws.services.cognitoidp.model.UpdateUserAttributesRequest;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jose.util.ResourceRetriever;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.thirumanam.model.AWSConstants;

@Component
public class CognitoServiceHelper {
	
   
    @Autowired
    private JWTConfiguration jwtConfiguration;
    
           
    @Bean
    public ConfigurableJWTProcessor<SecurityContext> configurableJWTProcessor() throws MalformedURLException {
         ResourceRetriever resourceRetriever = 
              new DefaultResourceRetriever(2000,//jwtConfiguration.getConnectionTimeout()
                   2000);//jwtConfiguration.getReadTimeout());
         //https://cognito-idp.{region}.amazonaws.com/{userPoolId}/.well-known/jwks.json.
         URL jwkSetURL= new URL(jwtConfiguration.getCognitoIdentityPoolUrl());
         //Creates the JSON Web Key (JWK)
         JWKSource<SecurityContext> keySource= new RemoteJWKSet<SecurityContext>(jwkSetURL, resourceRetriever);
         ConfigurableJWTProcessor<SecurityContext> jwtProcessor= new DefaultJWTProcessor<SecurityContext>();
         //RSASSA-PKCS-v1_5 using SHA-256 hash algorithm
         JWSKeySelector<SecurityContext> keySelector = 
        		 new JWSVerificationKeySelector<SecurityContext>(JWSAlgorithm.RS256, keySource);
         jwtProcessor.setJWSKeySelector(keySelector);
         return jwtProcessor;
     }
		
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
                .withRegion(Regions.fromName(jwtConfiguration.getREGION()))
                .build();

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setClientId(jwtConfiguration.getCLIENTAPP_ID());
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
	                .withRegion(Regions.fromName(jwtConfiguration.getREGION()))
	                .build();	     	        
	        		        
	        ConfirmSignUpRequest confirmSignUpRequest = new ConfirmSignUpRequest();
	        confirmSignUpRequest.setUsername(username);
	        confirmSignUpRequest.setConfirmationCode(code);
	        confirmSignUpRequest.setClientId(jwtConfiguration.getCLIENTAPP_ID());

            ConfirmSignUpResult confirmSignUpResult = cognitoIdentityProvider.confirmSignUp(confirmSignUpRequest);
            System.out.println("confirmSignupResult=" + confirmSignUpResult.toString());
	        
	        return true;
	  }
	 
	 public boolean resendAccessCode(String username) {
		 AnonymousAWSCredentials awsCreds = new AnonymousAWSCredentials();
	        AWSCognitoIdentityProvider cognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder
	                .standard()
	                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
	                .withRegion(Regions.fromName(jwtConfiguration.getREGION()))
	                .build();
	        try {
	        ResendConfirmationCodeRequest resendConfirmationCodeRequest
	        	= new ResendConfirmationCodeRequest();
	        resendConfirmationCodeRequest.setUsername(username);
	        resendConfirmationCodeRequest.setClientId(jwtConfiguration.getCLIENTAPP_ID());
	        cognitoIdentityProvider.resendConfirmationCode(resendConfirmationCodeRequest);
	        } catch (Exception exp) {
	        	 System.out.println(exp);
	        	 return false;
	        }
		 return true;
	 }
	 
	 public DeleteUserResult deleteUser(String accessToken) {
		 BasicAWSCredentials basicCredentials = new BasicAWSCredentials(
					jwtConfiguration.getAccessKey(), jwtConfiguration.getSecretKey());
	        AWSCognitoIdentityProvider cognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder
	                .standard()
	                .withCredentials(new AWSStaticCredentialsProvider(basicCredentials))
	                .withRegion(Regions.fromName(jwtConfiguration.getREGION()))
	                .build();
	        DeleteUserRequest deleteUserRequest = new DeleteUserRequest();
	        deleteUserRequest.setAccessToken(accessToken);
	        return cognitoIdentityProvider.deleteUser(deleteUserRequest);	       
	  }
	 
	 public ForgotPasswordResult resetPassword(String username) throws LimitExceededException{
	        AnonymousAWSCredentials awsCreds = new AnonymousAWSCredentials();
	        AWSCognitoIdentityProvider cognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder
	                .standard()
	                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
	                .withRegion(Regions.fromName(jwtConfiguration.getREGION()))
	                .build();

	        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
	        forgotPasswordRequest.setUsername(username);
	        forgotPasswordRequest.setClientId(jwtConfiguration.getCLIENTAPP_ID());
	        return cognitoIdentityProvider.forgotPassword(forgotPasswordRequest);	       
	  }
	 
	 
	 public ConfirmForgotPasswordResult updatePassword(String username, String newpw, String code) {
	        AnonymousAWSCredentials awsCreds = new AnonymousAWSCredentials();
	        AWSCognitoIdentityProvider cognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder
	                .standard()
	                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
	                .withRegion(Regions.fromName(jwtConfiguration.getREGION()))
	                .build();

	        ConfirmForgotPasswordRequest confirmPasswordRequest = new ConfirmForgotPasswordRequest();
	        confirmPasswordRequest.setUsername(username);
	        confirmPasswordRequest.setPassword(newpw);
	        confirmPasswordRequest.setConfirmationCode(code);
	        confirmPasswordRequest.setClientId(jwtConfiguration.getCLIENTAPP_ID());
	        return cognitoIdentityProvider.confirmForgotPassword(confirmPasswordRequest);	        
	    }
	 
	 public void updateEmail(String email, String userName) {
			BasicAWSCredentials basicCredentials = new BasicAWSCredentials(
					jwtConfiguration.getAccessKey(), jwtConfiguration.getSecretKey());
	        AWSCognitoIdentityProvider cognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder
	                .standard()
	                .withCredentials(new AWSStaticCredentialsProvider(basicCredentials))
	                .withRegion(Regions.fromName(jwtConfiguration.getREGION()))
	                .build();
	        AdminUpdateUserAttributesRequest attrRequest = new AdminUpdateUserAttributesRequest();
	        List<AttributeType> attributeTypeList = new ArrayList<AttributeType>();
	        	attributeTypeList.add(createAttributeType(AWSConstants.ATRBT_EMAIL, email));
	        	attributeTypeList.add(createAttributeType(AWSConstants.ATRBT_EMAIL_VERIFIED, AWSConstants.STRING_TRUE));	       
	        attrRequest.setUserPoolId(jwtConfiguration.getPOOL_ID());
	        attrRequest.setUsername(userName);
	        attrRequest.setUserAttributes(attributeTypeList);
	        cognitoIdentityProvider.adminUpdateUserAttributes(attrRequest);
	 }
	 
	 public void updatePhoneNumber(String countryCode, String phoneNumber, String userName) {
		 BasicAWSCredentials basicCredentials = new BasicAWSCredentials(
					jwtConfiguration.getAccessKey(), jwtConfiguration.getSecretKey());
	        AWSCognitoIdentityProvider cognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder
	                .standard()
	                .withCredentials(new AWSStaticCredentialsProvider(basicCredentials))
	                .withRegion(Regions.fromName(jwtConfiguration.getREGION()))
	                .build();
	        AdminUpdateUserAttributesRequest attrRequest = new AdminUpdateUserAttributesRequest();
	        List<AttributeType> attributeTypeList = new ArrayList<AttributeType>();
	        if(countryCode.equals("+1U")) {
	        	countryCode = "+1";
	        }
	        attributeTypeList.add(createAttributeType(AWSConstants.ATRBT_PHONE_NUMBER, countryCode+phoneNumber));	     	       
	        attrRequest.setUserPoolId(jwtConfiguration.getPOOL_ID());
	        attrRequest.setUsername(userName);
	        attrRequest.setUserAttributes(attributeTypeList);
	        cognitoIdentityProvider.adminUpdateUserAttributes(attrRequest);
	 }
	 
	 /**
      * Helper method to update user attributes
      *
      * @param username represents the username in the cognito user pool
      * @param password represents the password in the cognito user pool
      * @return returns the AWSLoginResponse after the validation
      */
	 public void updateAttributes(Map<String, String> attributes, String accessToken) {
	   AnonymousAWSCredentials awsCreds = new AnonymousAWSCredentials();
        AWSCognitoIdentityProvider cognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(Regions.fromName(jwtConfiguration.getREGION()))
                .build();
        UpdateUserAttributesRequest updateAttrRequest = new UpdateUserAttributesRequest();
        updateAttrRequest.setAccessToken(accessToken);
        Set<String> keys = attributes.keySet();
        List<AttributeType> attributeTypeList = new ArrayList<AttributeType>();
        for(String key : keys) {
        	attributeTypeList.add(createAttributeType(key, attributes.get(key)));	        	
        }
        updateAttrRequest.setUserAttributes(attributeTypeList);
        cognitoIdentityProvider.updateUserAttributes(updateAttrRequest);
	 }
	
	
	/**
     * Helper method to validate the user
     *
     * @param username represents the username in the cognito user pool
     * @param password represents the password in the cognito user pool
     * @return returns the AWSLoginResponse after the validation
     */
    public AWSLoginResponse validateUser(String username, String password) {
        AuthenticationHelper helper = new AuthenticationHelper(jwtConfiguration.getPOOL_ID(), 
        		jwtConfiguration.getCLIENTAPP_ID(), "", jwtConfiguration.getREGION());
        return helper.performSRPAuthentication(username, password);
    }
    
    public AWSLoginResponse validateUserWithRefToken(String refreshToken) {
        AuthenticationHelper helper = new AuthenticationHelper(
        		jwtConfiguration.getPOOL_ID(), 
        		jwtConfiguration.getCLIENTAPP_ID(), 
        		"", 
        		jwtConfiguration.getREGION());
        return helper.performRefreshTokenAuthentication(refreshToken);
    }    
       
    public ChangePasswordResult changePassword(String accessToken, String currentPassword, String newPassword) {
    	AnonymousAWSCredentials awsCreds = new AnonymousAWSCredentials();
    	AWSCognitoIdentityProvider cognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder
	                .standard()
	                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
	                .withRegion(Regions.fromName(jwtConfiguration.getREGION()))
	                .build();
    	ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
    	changePasswordRequest.setPreviousPassword(currentPassword);
    	changePasswordRequest.setProposedPassword(newPassword);
    	changePasswordRequest.setAccessToken(accessToken);
    	return cognitoIdentityProvider.changePassword(changePasswordRequest);
    }
}