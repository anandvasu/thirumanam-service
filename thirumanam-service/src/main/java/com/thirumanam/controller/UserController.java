package com.thirumanam.controller;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	
	private void updateProfileCompPercent(User user) {
		int counter = 0;
		counter = (user.getFirstName() != null) ? (counter + 1) : counter; //1
		counter = (user.getLastName() != null) ? (counter + 1) : counter;//2
		counter = (user.getAge() != 0) ? (counter + 1) : counter; //3
		counter = (user.getEmail() != null) ? (counter + 1) : counter; //4
		counter = (user.getGender() != null) ? (counter + 1) : counter; //5
		counter = (user.getRegisterBy() != null) ? (counter + 1) : counter; //6
		counter = (user.getCountry() != null) ? (counter + 1) : counter; //7
		counter = (user.getPstate() != null) ? (counter + 1) : counter; //8
		counter = (user.getDistrict() != null) ? (counter + 1) : counter; //9
		counter = (user.getCity() != null) ? (counter + 1) : counter; //10
		counter = (user.getmStatus() != null) ? (counter + 1) : counter; //11
		counter = (user.getFamilyType() != null) ? (counter + 1) : counter; //12
		counter = (user.getFamilyValue() != null) ? (counter + 1) : counter; //13
		counter = (user.getFoodHabit() != null) ? (counter + 1) : counter; //14
		counter = (user.getBodyType() != null) ? (counter + 1) : counter; //15
		counter = (user.getHeight() != 0) ? (counter + 1) : counter; //16
		counter = (user.getEducation() != null) ? (counter + 1) : counter; //17
		counter = (user.getEmployment() != null) ? (counter + 1) : counter; //18
		counter = (user.getIncome() != null) ? (counter + 1) : counter; //19
		counter = (user.getImage() != null) ? (counter + 1) : counter; //20
		
		double percent = (counter * 100) / 20;
		int percentCeil = (int) Math.ceil(percent);
		user.setProfileCompPercent(Integer.toString(percentCeil));
	}
	
	public static String getRandomNumberString() {
	    // It will generate 6 digit random Number.
	    // from 0 to 999999
	    Random rnd = new Random();
	    int number = rnd.nextInt(999999);

	    // this will convert any number sequence into 6 character.
	    return String.format("%06d", number);
	}
	
	@RequestMapping(value = "/external/{externalId}", method = RequestMethod.GET)
	public ResponseEntity<User> getUserByExternalId(@PathVariable("externalId") String externalId) {
		List<User> userObj = userRepository.findByExternalId(externalId);
		User user = userObj.get(0);
		updateProfileCompPercent(user);
		return ResponseEntity.ok().body(user);
	}
	
	@RequestMapping(value = "/{profileId}", method = RequestMethod.GET)
	public ResponseEntity<User> getUser(@PathVariable("profileId") String profileId) {
		Optional<User> userObj = userRepository.findById(profileId);
		User user = userObj.get();
		updateProfileCompPercent(user);
		return ResponseEntity.ok().body(user);
	}
		
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
		String profileString = "TM" + getRandomNumberString();
		user.setId(profileString);
		
		userRepository.save(user);		
		
		logger.info("User Registration Successfull");
		return ResponseEntity.created(new URI("/user")).header("PROFILEID", profileString).body(
				Util.populateStatus(profileString, "User registered successfully."));	
	}
	
	@PutMapping("/profile/summary")
	public ResponseEntity<User> getProfileSummary(@RequestParam("profileId") String profileId) throws URISyntaxException {
		Optional<User> userObj = userRepository.findById(profileId);
		User user = userObj.get();

		return ResponseEntity.ok().body(user);
	}
	
	@PutMapping("/profile")
	public ResponseEntity<Status> updateProfile(@RequestBody User inputUser) throws URISyntaxException {
		
		Optional<User> userObj = userRepository.findById(inputUser.getId());
		User user = userObj.get();
		
		user.setCountry(inputUser.getCountry());
		user.setPstate(inputUser.getPstate());
		user.setCity(inputUser.getCity());
		user.setDistrict(inputUser.getDistrict());
		
		user.setmStatus(inputUser.getmStatus());
		user.setWeight(inputUser.getWeight());
		user.setHeight(inputUser.getHeight());
		user.setFamilyType(inputUser.getFamilyType());
		user.setFamilyValue(inputUser.getFamilyValue());
		user.setDisabled(inputUser.getDisabled());
		user.setDisInfo(inputUser.getDisInfo());
				
		user.setEducation(inputUser.getEducation());
		user.setEmployment(inputUser.getEmployment());
		user.setIncome(inputUser.getIncome());
		
		userRepository.save(user);
		
		return ResponseEntity.created(new URI("/user")).body(Util.populateStatus("t-200", "User registered successfully."));	
	}
	
	@PostMapping("/list")
	public ResponseEntity<List<User>> searchUser(@RequestBody SearchCriteria searchCriteria) {
		long totalUsers = searchCriteria.getTotalDocs();
		if(totalUsers == 0) {
			totalUsers = userRepositoryImpl.getSearchCount(searchCriteria);	
		}
		
		int skipnumber = (searchCriteria.getPageNumber() == 1) ? 0 : ((searchCriteria.getPageNumber()-1) * 10);
		int numberOfDocs = (skipnumber +10 < totalUsers) ? 10 : (int)(totalUsers-skipnumber);		
		
		List<User> usersList = userRepositoryImpl.searchUserData(searchCriteria, skipnumber, numberOfDocs);	
				
		logger.info("User Size in new class" + usersList.size());				   
		return ResponseEntity.ok()
							 .header("X-TOTAL-DOCS", Long.toString(totalUsers))
							 .body(usersList);
	}
	
	@PostMapping("/image")
	public ResponseEntity uploadProfileImage(@RequestParam("imageFile") MultipartFile imageFile, 
			@RequestParam("profileId") String profileId) {
		try {
			
			InputStream inStream = imageFile.getInputStream();
			//byte[] buffer = new byte[inStream.available()];
			//inStream.read(buffer);
			
			Optional<User> userObj = userRepository.findById(profileId);
			User user = userObj.get();
			user.setImage(Base64.getEncoder().encodeToString(IOUtils.toByteArray(inStream)));
			userRepository.save(user);
			
			/*File targetFile = new File("uploaded.jpg");
			OutputStream outStream = new FileOutputStream(targetFile);
			outStream.write(buffer);
			outStream.close();	*/	
			
			inStream.close();
			
		} catch (Exception exp) {
			exp.printStackTrace();
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