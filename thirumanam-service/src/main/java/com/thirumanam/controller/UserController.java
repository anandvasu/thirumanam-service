package com.thirumanam.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Base64;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.thirumanam.model.SearchCriteria;
import com.thirumanam.model.Status;
import com.thirumanam.model.User;
import com.thirumanam.mongodb.repository.UserRepository;
import com.thirumanam.mongodb.repository.UserRepositoryImpl;
import com.thirumanam.util.Util;

@RestController
@RequestMapping("/thirumanam/user")
public class UserController {

	//private Logger logger = LoggerFactory.getLogger(UserController.class);
	Logger logger = LogManager.getLogger(UserController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserRepositoryImpl userRepositoryImpl;
		
	@PostMapping("/register")
	public ResponseEntity<Status> registerUser(@RequestBody User user) throws URISyntaxException {
		
		Status status = validateUserRegistration(user);
		if (status != null) {
			return ResponseEntity.badRequest().body(status);
		}
		
		List<User> users = userRepository.findByEmail(user.getEmail());
		if (!users.isEmpty()) {
			logger.info("{}", users.size());
			logger.info("{}{}", "foo", "bar");
			return ResponseEntity.badRequest().body( Util.populateStatus("t-500", "Email already exists."));
		}		
		
		//Split Day, Month, Year. Calculate age. Update all of these into User object.
		String[] data = user.getDob().split("/");
		LocalDate birthday = LocalDate.of(Integer.parseInt(data[2]), Integer.parseInt(data[1]), Integer.parseInt(data[0]));
		user.setbDay(Integer.parseInt(data[0]));
		user.setbMonth(Integer.parseInt(data[1]));
		user.setbYear(Integer.parseInt(data[2]));
		user.setAge(Period.between(birthday, LocalDate.now()).getYears());
		
		userRepository.save(user);
		
		
		logger.info("User Registration Successfull");
		return ResponseEntity.created(new URI("/user")).body(Util.populateStatus("t-200", "User registered successfully."));	
	}
	
	@PostMapping("/list")
	public ResponseEntity<List<User>> searchUser(@RequestBody SearchCriteria searchCriteria) {
		List<User> usersList = userRepositoryImpl.searchUserData(searchCriteria);
		
		String image = "";
		
		try {
			File targetFile = new File("uploaded.jpg");
			FileInputStream in = new FileInputStream(targetFile);
			image = Base64.getEncoder().encodeToString(org.apache.commons.io.IOUtils.toByteArray(in));
		} catch (Exception exp) {
			
		}
		
		for(User user : usersList) {
			user.setThumbImage(image);
		}
		
		
		logger.info("User Size in new class" + usersList.size());
		return ResponseEntity.ok().body(usersList);
	}
	
	@RequestMapping ("/image")
	public ResponseEntity uploadProfileImage(@RequestParam MultipartFile imageFile) {
		try {
			
			InputStream inStream = imageFile.getInputStream();
			byte[] buffer = new byte[inStream.available()];
			inStream.read(buffer);
			
			File targetFile = new File("uploaded.jpg");
			OutputStream outStream = new FileOutputStream(targetFile);
			outStream.write(buffer);
			outStream.close();
			inStream.close();
			
		} catch (Exception exp) {
			
		}
		return ResponseEntity.ok().build();
	}
	
	private Status validateUserRegistration(User user) {
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
		if(user.getRegisterBy() == null || user.getRegisterBy().isEmpty()) {
			status = Util.populateStatus("t-409", "Registered By is required.");
			return status;
		}
		return status;
	}	
}