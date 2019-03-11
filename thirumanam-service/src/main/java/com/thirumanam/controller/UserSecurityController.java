package com.thirumanam.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.cognitoidp.model.AliasExistsException;
import com.amazonaws.services.cognitoidp.model.ChangePasswordResult;
import com.amazonaws.services.cognitoidp.model.CodeMismatchException;
import com.amazonaws.services.cognitoidp.model.ConfirmForgotPasswordResult;
import com.amazonaws.services.cognitoidp.model.ForgotPasswordResult;
import com.amazonaws.services.cognitoidp.model.LimitExceededException;
import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.amazonaws.services.cognitoidp.model.UserNotConfirmedException;
import com.amazonaws.services.cognitoidp.model.UserNotFoundException;
import com.thirumanam.aws.AWSLoginResponse;
import com.thirumanam.aws.CognitoHelper;
import com.thirumanam.model.AccessCode;
import com.thirumanam.model.ForgotPasswordResponse;
import com.thirumanam.model.LoginRequest;
import com.thirumanam.model.LoginResponse;
import com.thirumanam.model.Preference;
import com.thirumanam.model.RegisterUser;
import com.thirumanam.model.ResetPasswordResponse;
import com.thirumanam.model.Response;
import com.thirumanam.model.Status;
import com.thirumanam.model.ThirumanamUtil;
import com.thirumanam.model.UpdatePasswordRequest;
import com.thirumanam.model.User;
import com.thirumanam.model.UserAccount;
import com.thirumanam.mongodb.repository.PreferenceRepository;
import com.thirumanam.mongodb.repository.SequenceRepository;
import com.thirumanam.mongodb.repository.UserRepository;
import com.thirumanam.util.ErrorMessageConstants;
import com.thirumanam.util.ThirumanamConstant;
import com.thirumanam.util.Util;

@RestController
@RequestMapping("/matrimony/identity/")
public class UserSecurityController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private SequenceRepository sequenceRepository;
	
	@Autowired
	PreferenceRepository prefRepository;	
	
	@Autowired
	private CognitoHelper cognitoHelper;
	
		
	@PostMapping("/register")
	public ResponseEntity<Status> registerUser(@RequestBody RegisterUser inputUser) throws URISyntaxException {
		
		Status status = validateUserRegistration(inputUser);
		if (status != null) {
			return ResponseEntity.badRequest().body(status);
		}
		
		List<User> users = userRepository.findByEmail(inputUser.getEmail());
		if (!users.isEmpty()) {
			return ResponseEntity.badRequest().body( Util.populateStatus("t-500", "Email already exists."));
		}		
				
		String externalId = cognitoHelper.SignUpUser(
				inputUser.getUsername(), 
				inputUser.getPassword(), 
				inputUser.getEmail(), 
				inputUser.getMobile(), 
				inputUser.getLastName(), 
				inputUser.getFirstName());
		
		//Split Day, Month, Year. Calculate age. Update all of these into User object.
		User user = new User();
		String[] data = inputUser.getDob().split("/");
		LocalDate birthday = LocalDate.of(Integer.parseInt(data[2]), Integer.parseInt(data[1]), Integer.parseInt(data[0]));
		user.setbDay(Integer.parseInt(data[0]));
		user.setbMonth(Integer.parseInt(data[1]));
		user.setbYear(Integer.parseInt(data[2]));
		user.setAge(Period.between(birthday, LocalDate.now()).getYears());
		String profileId = ThirumanamConstant.PROFILE_ID_PREFIX + sequenceRepository.getNextProfileId();
		user.setId(profileId);
		user.setFirstName(inputUser.getFirstName());
		user.setLastName(inputUser.getLastName());
		user.setEmail(inputUser.getEmail());
		user.setExternalId(externalId);
		user.setGender(inputUser.getGender());
		user.setRegisterdBy(inputUser.getRegisteredBy());
		
		userRepository.save(user);		
		
		Preference preference = new Preference();
		preference.setId(profileId);
		preference.setGender(
				(user.getGender().equals(ThirumanamConstant.GENDER_M) ? ThirumanamConstant.GENDER_F: ThirumanamConstant.GENDER_M));
		prefRepository.save(preference);
		
		return ResponseEntity.created(new URI("/user")).header("PROFILEID", profileId).body(
				Util.populateStatus(profileId, "User registered successfully."));	
	}
	
	@PutMapping("/account")
	public ResponseEntity<Response> updateAccountDetail(@RequestBody UserAccount userAccount) throws URISyntaxException {	
		Response response = new Response();
		try {
			Map<String,String> attributes = new HashMap<String,String>();
			attributes.put("email", userAccount.getEmail());
			attributes.put("phone_number", userAccount.getPhoneNumber());
			//cognitoHelper.updateAttributes(attributes, userAccount.getAccessToken());
			cognitoHelper.updateEmailandPhoneNumber(userAccount.getEmail(), userAccount.getPhoneNumber());
			response.setSuccess(true);
		} catch (AliasExistsException exp) {
			exp.printStackTrace();
			response.setErrorMessage(ErrorMessageConstants.EMAIL_ALREADY_EXISTS);
		}
		return ResponseEntity.ok().body(response);
	}
	
	
	
	private LoginResponse populateUserDetail(AWSLoginResponse awsLoginResponse) {
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setUserConfirmed(awsLoginResponse.isUserConfirmed());
		if(awsLoginResponse.getExternalId() != null) {
			if(awsLoginResponse.isUserConfirmed()) {
				List<User> userObj = userRepository.findByExternalId(awsLoginResponse.getExternalId());
				if(!userObj.isEmpty()) {
					User user = userObj.get(0);
					loginResponse.setProfileId(user.getId());
					loginResponse.setFirstName(user.getFirstName());
					loginResponse.setLastName(user.getLastName());
					loginResponse.setProfilePerCompleted(ThirumanamUtil.updateProfileCompPercent(user));
					loginResponse.setGender(user.getGender());
				}
				loginResponse.setIdToken(awsLoginResponse.getIdToken());
				loginResponse.setAccessToken(awsLoginResponse.getAccessToken());
				if(awsLoginResponse.getRefreshToken() != null) {
					loginResponse.setRefreshToken(awsLoginResponse.getRefreshToken());
				}
			}
			loginResponse.setSuccess(true);
		} else {
			loginResponse.setSuccess(false);			
		}
		return loginResponse;
	}
	
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest login) throws URISyntaxException {
		LoginResponse loginResponse = null;
		try {
			AWSLoginResponse awsLoginResponse = cognitoHelper.validateUser(login.getUsername(), login.getPassword());	
			loginResponse = populateUserDetail(awsLoginResponse);	
		} catch (final UserNotConfirmedException exp) {
			loginResponse = new LoginResponse();
			loginResponse.setSuccess(true);
		} catch (final NotAuthorizedException exp) {
			loginResponse = new LoginResponse();
			loginResponse.setErrorMessage(ErrorMessageConstants.INVALID_USER_PASSWORD);
		} catch (UserNotFoundException exp) {
			loginResponse = new LoginResponse();
			loginResponse.setErrorMessage(ErrorMessageConstants.INVALID_USER_PASSWORD);
		}
		return ResponseEntity.ok().body(loginResponse);
	}
	
	@PostMapping("/login/refreshtoken")
	public ResponseEntity<LoginResponse> refreshTokenLogin (@RequestBody LoginRequest login) throws URISyntaxException {
		LoginResponse loginResponse = null;
		try {
			AWSLoginResponse awsLoginResponse = cognitoHelper.validateUserWithRefToken(login.getRefreshToken());	
			loginResponse = populateUserDetail(awsLoginResponse);	
		} catch (final UserNotConfirmedException exp) {
			loginResponse = new LoginResponse();
			loginResponse.setSuccess(true);
		} catch (final NotAuthorizedException exp) {
			loginResponse = new LoginResponse();
			loginResponse.setErrorMessage(ErrorMessageConstants.INVALID_USER_PASSWORD);
		} catch (UserNotFoundException exp) {
			loginResponse = new LoginResponse();
			loginResponse.setErrorMessage(ErrorMessageConstants.INVALID_USER_PASSWORD);
		}
		return ResponseEntity.ok().body(loginResponse);
	}
	
	@PostMapping("/password/forgot")
	public ResponseEntity<ForgotPasswordResponse> forgotPassword(@RequestBody AccessCode accessCode) throws URISyntaxException {	
		ForgotPasswordResponse resetPwdResponse = new ForgotPasswordResponse();
		try {
			ForgotPasswordResult forgotPwdResult = cognitoHelper.resetPassword(accessCode.getUsername());	
			resetPwdResponse.setDeliveryMedium(forgotPwdResult.getCodeDeliveryDetails().getDeliveryMedium());
			resetPwdResponse.setDestination(forgotPwdResult.getCodeDeliveryDetails().getDestination());
			resetPwdResponse.setSuccess(true);
		} catch (LimitExceededException exp) {
			resetPwdResponse.setLimitExceed(true);
			exp.printStackTrace();
		}
		return ResponseEntity.ok().body(resetPwdResponse);
	}
	
	@PostMapping("/password/reset")
	public ResponseEntity<ResetPasswordResponse> 
		resetPassword(@RequestBody AccessCode accessCode) throws URISyntaxException {	
		ResetPasswordResponse resetPwdResponse = new ResetPasswordResponse();
		try {
			ConfirmForgotPasswordResult updatePasswordResult =
					cognitoHelper.updatePassword(
								accessCode.getUsername(), 
								accessCode.getPassword(),
								accessCode.getAccessCode());				
			resetPwdResponse.setSuccess(true);
			resetPwdResponse.setCodeMatched(true);
		} catch (LimitExceededException exp) {
			resetPwdResponse.setLimitExceed(true);
			exp.printStackTrace();
		} catch (CodeMismatchException exp) {
			exp.printStackTrace();
		}
		return ResponseEntity.ok().body(resetPwdResponse);
	}
	
	@PostMapping("/accesscode/resend")
	public ResponseEntity<String> resendAccessCode(@RequestBody AccessCode accessCode) throws URISyntaxException {		
		if(!cognitoHelper.resendAccessCode(accessCode.getUsername())) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}		
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/password/change")
	public ResponseEntity<Response> changePassword(@RequestBody UpdatePasswordRequest updaterPwdRequest)
			throws URISyntaxException {		
		Response reponse = new Response();
		try {
			ChangePasswordResult changePasswordResult = cognitoHelper.changePassword(
					updaterPwdRequest.getAccessToken(),
					updaterPwdRequest.getCurrentPassword(), 
					updaterPwdRequest.getNewPassword());				
			if(changePasswordResult.getSdkHttpMetadata().getHttpStatusCode() == 200) {
				reponse.setSuccess(true);
			} else {
				reponse.setErrorMessage(ErrorMessageConstants.SERVER_ERROR);
			}
		} catch (NotAuthorizedException exp) {
			reponse.setErrorMessage(ErrorMessageConstants.INVALID_CURRENT_PASSWORD);
			exp.printStackTrace();
		}
		
		return ResponseEntity.ok().body(reponse);
	}
	
	@PostMapping("/accesscode/verify")
	public ResponseEntity<Status> confirmAccessCode(@RequestBody AccessCode accessCode) throws URISyntaxException {	
		try {
			if(!cognitoHelper.VerifyAccessCode(accessCode.getUsername(), accessCode.getAccessCode())) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		} catch (CodeMismatchException exp) {
			return ResponseEntity.ok().body(
					Util.populateStatus("400", "Invalid Code entered. Please try again."));	
		}
		return ResponseEntity.ok().body(
				Util.populateStatus("200", "User registered successfully."));	
	}
	
	private Status validateUserRegistration(RegisterUser user) {
		Status status = null;
		if(user.getFirstName() == null || user.getFirstName().isEmpty()) {
			status = Util.populateStatus("t-400", "First Name is required.");
			return status;
		}
		if(user.getLastName() == null || user.getLastName().isEmpty()) {
			status = Util.populateStatus("t-401", "Last Name is required.");
			return status;
		}
		if(user.getDob() == null || user.getDob().isEmpty()) {
			status = Util.populateStatus("t-402", "Dob is required.");
			return status;
		}
		if(user.getEmail() == null || user.getEmail().isEmpty()) {
			status = Util.populateStatus("t-403", "Email is required.");
			return status;
		}
		/*if(user.getCountry() == null || user.getCountry().isEmpty()) {
			status = Util.populateStatus("t-404", "Country is required.");
		}
		if(user.getState() == null || user.getState().isEmpty()) {
			status = Util.populateStatus("t-405", "State is required.");
		}
		if(user.getCity() == null || user.getCity().isEmpty()) {
			status = Util.populateStatus("t-406", "City is required.");
		}*/
		if(user.getMobile() == null || user.getMobile().isEmpty()) {
			status = Util.populateStatus("t-407", "Mobile is required.");
			return status;
		}
		if(user.getGender() == null || user.getGender().isEmpty()) {
			status = Util.populateStatus("t-408", "Gender is required.");
			return status;
		}
		/*if(user.getRegisteredBy() == null || user.getRegisteredBy().isEmpty()) {
			status = Util.populateStatus("t-409", "Registered By is required.");
			return status;
		}*/
		return status;
	}	
}