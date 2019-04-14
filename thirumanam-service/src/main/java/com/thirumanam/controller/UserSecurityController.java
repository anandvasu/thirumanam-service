package com.thirumanam.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.cognitoidp.model.AliasExistsException;
import com.amazonaws.services.cognitoidp.model.ChangePasswordResult;
import com.amazonaws.services.cognitoidp.model.CodeMismatchException;
import com.amazonaws.services.cognitoidp.model.ForgotPasswordResult;
import com.amazonaws.services.cognitoidp.model.LimitExceededException;
import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.amazonaws.services.cognitoidp.model.UserNotConfirmedException;
import com.amazonaws.services.cognitoidp.model.UserNotFoundException;
import com.thirumanam.aws.AWSLoginResponse;
import com.thirumanam.aws.CognitoServiceHelper;
import com.thirumanam.exception.ThirumanamException;
import com.thirumanam.model.AccessCode;
import com.thirumanam.model.DeleteReason;
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
	private CognitoServiceHelper cognitoHelper;
	
		
	@PostMapping("/register")
	public ResponseEntity<Status> registerUser(@RequestBody RegisterUser inputUser) throws URISyntaxException {
		
		Status status = validateUserRegistration(inputUser);
		if (status != null) {
			return ResponseEntity.badRequest().body(status);
		}
		
		List<User> users = userRepository.findByEmail(inputUser.getEmail());
		if (!users.isEmpty()) {
			return ResponseEntity.badRequest().body( Util.populateStatus(400, "Email already exists."));
		}		
				
		String externalId = cognitoHelper.SignUpUser(
				inputUser.getUsername(), 
				inputUser.getPassword(), 
				inputUser.getEmail(), 
				inputUser.getPhCountryCode()+inputUser.getPhonenumber(), 
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
		user.setPhCountryCode(inputUser.getPhCountryCode());
		user.setPhonenumber(inputUser.getPhonenumber());
		user.setExternalId(externalId);
		user.setGender(inputUser.getGender());
		user.setRegisterdBy(inputUser.getRegisteredBy());
		user.setCreatedDate(new Date());
		user.setStatus(ThirumanamConstant.USER_STATUS_ACTIVE);
		
		userRepository.save(user);
		
		// Commented as we don't need to create preference by default. Rather display message on ui.
		/*Preference preference = new Preference();
		preference.setId(profileId);
		preference.setGender(
				(user.getGender().equals(ThirumanamConstant.GENDER_M) ? ThirumanamConstant.GENDER_F: ThirumanamConstant.GENDER_M));
		prefRepository.save(preference);*/
		
		return ResponseEntity.created(new URI("/user")).header(ThirumanamConstant.PROFILEID, profileId).body(
				Util.populateStatus(200, "User registered successfully."));	
	}
	
	@PutMapping("/{profileId}/delete")
	public ResponseEntity<Status> deleteUserProfile(
			@PathVariable("profileId") String profileId, @RequestBody DeleteReason deleteReason) throws URISyntaxException {	
		Status status = validateProfileId(profileId);
		if(status == null) {
			Optional<User> userObj = userRepository.findById(profileId);
			if(userObj.isPresent()) {
				userRepository.delete(userObj.get());
			}
			cognitoHelper.deleteUser(deleteReason.getAccessToken());
		}
		
		return ResponseEntity.ok().body(status);
	}
	
	@PutMapping("/{profileId}/status")
	public ResponseEntity<Response> updateStatus(@PathVariable("profileId") String profileId, 
			@RequestBody UserAccount userAccount) throws URISyntaxException {	
		Response response = new Response();
		try {
			Status status = validateStatus(userAccount.getStatus());
			if(status == null) {
				Optional<User> userObj = userRepository.findById(profileId);
				if(userObj.isPresent()) {						
					User user = userObj.get();
					if(ThirumanamConstant.USER_STATUS_INACTIVE.equals(userAccount.getStatus())) {
						user.setActivateDate(Util.calculateDate(userAccount.getInactiveDays()));
					}
					user.setStatus(userAccount.getStatus());
					userRepository.save(user);					
					response.setSuccess(true);
				} else {
					response.setSuccess(false);
					response.setErrorMessage(ErrorMessageConstants.INVALID_USER);
				}
			} else {
				response.setSuccess(false);
				response.setErrorMessage(status.getMessage());
			}
		} catch (Exception exp) {
			exp.printStackTrace();
			response.setErrorMessage(ErrorMessageConstants.INVALID_USER);
		}
		return ResponseEntity.ok().body(response);
	}
	
	
	@PutMapping("/email")
	public ResponseEntity<Response> updateEmail(@RequestBody UserAccount userAccount) throws URISyntaxException {	
		Response response = new Response();
		try {
			Status status = validateEmail(userAccount.getEmail());
			if(status == null) {
				Optional<User> userObj = userRepository.findById(userAccount.getProfileId());
				if(userObj.isPresent()) {						
					User user = userObj.get();
					user.setEmail(userAccount.getEmail());
					userRepository.save(user);
					cognitoHelper.updateEmail(userAccount.getEmail(), user.getExternalId());
					response.setSuccess(true);
				} else {
					response.setSuccess(false);
					response.setErrorMessage(ErrorMessageConstants.INVALID_USER);
				}
			} else {
				response.setSuccess(false);
				response.setErrorMessage(status.getMessage());
			}
		} catch (AliasExistsException exp) {
			exp.printStackTrace();
			response.setErrorMessage(ErrorMessageConstants.EMAIL_ALREADY_EXISTS);
		}
		return ResponseEntity.ok().body(response);
	}
	
	@GetMapping("/{profileId}/email")
	public ResponseEntity<UserAccount> getEmail(@PathVariable("profileId") String profileId) throws URISyntaxException {	
		UserAccount response = new UserAccount();
		try {
			Status status = validateProfileId(profileId);
			if(status == null) {
				Optional<User> userObj = userRepository.findById(profileId);
				if(userObj.isPresent()) {						
					User user = userObj.get();
					response.setEmail(user.getEmail().replaceAll("(^[^@]{3}|(?!^)\\G)[^@]", "$1*"));					
				} 
			}
		} catch (AliasExistsException exp) {
			exp.printStackTrace();
		}
		return ResponseEntity.ok().body(response);
	}
	
	@GetMapping("/{profileId}/phonenumber")
	public ResponseEntity<UserAccount> getPhoneNumber(@PathVariable("profileId") String profileId) throws URISyntaxException {	
		UserAccount response = new UserAccount();
		try {
			Status status = validateProfileId(profileId);
			if(status == null) {
				Optional<User> userObj = userRepository.findById(profileId);
				if(userObj.isPresent()) {						
					User user = userObj.get();
					response.setPhCountryCode(user.getPhCountryCode());
					response.setPhoneNumber(user.getPhonenumber().replaceAll(".(?=.{4})", "*"));					
				} 
			}
		} catch (AliasExistsException exp) {
			exp.printStackTrace();
		}
		return ResponseEntity.ok().body(response);
	}
	
	@PutMapping("/{profileId}/phonenumber")
	public ResponseEntity<Response> updateMobileNumber(@RequestBody UserAccount userAccount) throws URISyntaxException {	
		Response response = new Response();
		try {
			Status status = validatePhone(userAccount.getPhCountryCode(), userAccount.getPhoneNumber());
			if(status == null) {
				Optional<User> userObj = userRepository.findById(userAccount.getProfileId());
				if(userObj.isPresent()) {		
					User user = userObj.get();
					user.setPhCountryCode(userAccount.getPhCountryCode());
					user.setPhonenumber(userAccount.getPhoneNumber());
					userRepository.save(user);
					cognitoHelper.updatePhoneNumber(userAccount.getPhCountryCode(), userAccount.getPhoneNumber(), user.getExternalId());
					response.setSuccess(true);
				}else {
					response.setSuccess(false);
					response.setErrorMessage(ErrorMessageConstants.INVALID_USER);
				}
			} else {
				response.setSuccess(false);
				response.setErrorMessage(status.getMessage());
			}			
		} catch (AliasExistsException exp) {
			exp.printStackTrace();
			response.setErrorMessage(ErrorMessageConstants.PHONENUMBER_ALREADY_EXISTS);
		}
		return ResponseEntity.ok().body(response);
	}
	
	
	
	private LoginResponse populateUserDetail(
			AWSLoginResponse awsLoginResponse) throws ThirumanamException {
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
					loginResponse.setIdToken(awsLoginResponse.getIdToken());
					loginResponse.setAccessToken(awsLoginResponse.getAccessToken());
					loginResponse.setStatus(user.getStatus());
					if(awsLoginResponse.getRefreshToken() != null) {
						loginResponse.setRefreshToken(awsLoginResponse.getRefreshToken());
					}
					loginResponse.setSuccess(true);
				} else {
					throw new ThirumanamException(
							ErrorMessageConstants.CODE_INVALID_USER_PASSWORD,
						ErrorMessageConstants.INVALID_USER_PASSWORD);
				}
			}			
		} else {
			throw new ThirumanamException(
					ErrorMessageConstants.CODE_INVALID_USER_PASSWORD,
				ErrorMessageConstants.INVALID_USER_PASSWORD);			
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
		} catch (ThirumanamException exp) {
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
		} catch (ThirumanamException exp) {
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
				reponse.setErrorMessage(ErrorMessageConstants.MESSAGE_SERVER_ERROR);
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
					Util.populateStatus(400, "Invalid Code entered. Please try again."));	
		}
		return ResponseEntity.ok().body(
				Util.populateStatus(200, "User registered successfully."));	
	}
			
	private Status validateStatus(String userStatus) {
		Status status = null;
		if(userStatus == null || userStatus.isEmpty()) {
			status = Util.populateStatus(400, "Status is required.");
		}
		return status;
	}
	
	private Status validateProfileId(String profileId) {
		Status status = null;
		if(profileId == null || profileId.isEmpty()) {
			status = Util.populateStatus(400, "ProfileID is required.");
		}
		return status;
	}
	
	private Status validateEmail(String email) {
		Status status = null;
		if(email == null || email.isEmpty()) {
			status = Util.populateStatus(400, "Email is required.");
		}
		return status;
	}
	
	private Status validatePhone(String countryCode, String phoneNumber) {
		Status status = null;
		if(countryCode == null || countryCode.isEmpty()) {
			status = Util.populateStatus(400, "Phone number Country Code is required.");
		}
		
		if(phoneNumber == null || phoneNumber.isEmpty()) {
			status = Util.populateStatus(400, "Phone number is required.");
		}
		
		return status;
	}
	
	private Status validateUserRegistration(RegisterUser user) {
		Status status = null;
		if(user.getFirstName() == null || user.getFirstName().isEmpty()) {
			status = Util.populateStatus(400, "First Name is required.");
			return status;
		}
		if(user.getLastName() == null || user.getLastName().isEmpty()) {
			status = Util.populateStatus(400, "Last Name is required.");
			return status;
		}
		if(user.getDob() == null || user.getDob().isEmpty()) {
			status = Util.populateStatus(400, "Dob is required.");
			return status;
		}
		if(user.getEmail() == null || user.getEmail().isEmpty()) {
			status = Util.populateStatus(400, "Email is required.");
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
		if(user.getPhonenumber() == null || user.getPhonenumber().isEmpty()) {
			status = Util.populateStatus(400, "Mobile is required.");
			return status;
		}
		if(user.getGender() == null || user.getGender().isEmpty()) {
			status = Util.populateStatus(400, "Gender is required.");
			return status;
		}
		/*if(user.getRegisteredBy() == null || user.getRegisteredBy().isEmpty()) {
			status = Util.populateStatus("t-409", "Registered By is required.");
			return status;
		}*/
		return status;
	}	
}