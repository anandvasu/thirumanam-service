package com.thirumanam.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.cognitoidp.model.CodeMismatchException;
import com.thirumanam.aws.AWSLoginResponse;
import com.thirumanam.aws.CognitoHelper;
import com.thirumanam.model.AccessCode;
import com.thirumanam.model.LoginRequest;
import com.thirumanam.model.LoginResponse;
import com.thirumanam.model.Preference;
import com.thirumanam.model.RegisterUser;
import com.thirumanam.model.Status;
import com.thirumanam.model.ThirumanamUtil;
import com.thirumanam.model.User;
import com.thirumanam.mongodb.repository.PreferenceRepository;
import com.thirumanam.mongodb.repository.SequenceRepository;
import com.thirumanam.mongodb.repository.UserRepository;
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
	
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest login) throws URISyntaxException {		
		LoginResponse loginResponse = new LoginResponse();
		AWSLoginResponse awsLoginResponse = cognitoHelper.ValidateUser(login.getUsername(), login.getPassword());	
		if(awsLoginResponse.getExternalId() != null) {
			List<User> userObj = userRepository.findByExternalId(awsLoginResponse.getExternalId());
			if(!userObj.isEmpty()) {
				User user = userObj.get(0);
				loginResponse.setProfileId(user.getId());
				loginResponse.setFirstName(user.getFirstName());
				loginResponse.setLastName(user.getLastName());
				loginResponse.setProfilePerCompleted(ThirumanamUtil.updateProfileCompPercent(user));
			}
			loginResponse.setIdToken(awsLoginResponse.getIdToken());
			loginResponse.setRefrehToken(awsLoginResponse.getRefreshToken());
			loginResponse.setAuthSuccess(true);
		} else {
			loginResponse.setAuthSuccess(false);
			loginResponse.setUserConfirmed(awsLoginResponse.getUserConfirmed());
		}		
		return ResponseEntity.ok().body(loginResponse);
	}
	
	@PostMapping("/accesscode/resend")
	public ResponseEntity<String> resendAccessCode(@RequestBody AccessCode accessCode) throws URISyntaxException {		
		if(!cognitoHelper.resendAccessCode(accessCode.getUsername())) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}		
		return ResponseEntity.ok().build();
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