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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.thirumanam.model.Preference;
import com.thirumanam.model.SearchCriteria;
import com.thirumanam.model.Status;
import com.thirumanam.model.User;
import com.thirumanam.mongodb.repository.PreferenceRepository;
import com.thirumanam.mongodb.repository.SequenceRepository;
import com.thirumanam.mongodb.repository.UserRepository;
import com.thirumanam.mongodb.repository.UserRepositoryImpl;
import com.thirumanam.util.ThirumanamConstant;
import com.thirumanam.util.Util;

@RestController
@RequestMapping("/thirumanam/user")
public class UserController {

	//private Logger logger = LoggerFactory.getLogger(UserController.class);
	Logger logger = LogManager.getLogger(UserController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	PreferenceRepository prefRepository;
	
	@Autowired
	private UserRepositoryImpl userRepositoryImpl;
	
	@Autowired
	private SequenceRepository sequenceRepository;
	
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
		counter = ((user.getHeightCm() != 0) || (user.getHeightInch() != 0 )) ? (counter + 1) : counter; //16
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
		String profileId = ThirumanamConstant.PROFILE_ID_PREFIX + sequenceRepository.getNextProfileId();
		user.setId(profileId);
		
		userRepository.save(user);		
		
		Preference preference = new Preference();
		preference.setId(profileId);
		preference.setGender(
				(user.getGender().equals(ThirumanamConstant.GENDER_M) ? ThirumanamConstant.GENDER_F: ThirumanamConstant.GENDER_M));
		prefRepository.save(preference);
		
		logger.info("User Registration Successfull");
		return ResponseEntity.created(new URI("/user")).header("PROFILEID", profileId).body(
				Util.populateStatus(profileId, "User registered successfully."));	
	}
	
	private SearchCriteria buildSearchCriteria(Preference preference) {
		SearchCriteria searchCriteria = new SearchCriteria();
		searchCriteria.setAgeLess(preference.getAgeTo());
		searchCriteria.setAgeGreater(preference.getAgeFrom());
		searchCriteria.setMinHeight(preference.getMinHeight());
		searchCriteria.setMaxHeight(preference.getMaxHeight());
		searchCriteria.setGender(preference.getGender());	
		searchCriteria.setReligions(preference.getReligions());
		searchCriteria.setCountries(preference.getCountries());
		searchCriteria.setStates(preference.getStates());
		searchCriteria.setMaritalStatus(preference.getmStatus());
		return searchCriteria;
	}
	
	@GetMapping("/{profileId}/preference/matches")
	public ResponseEntity<List<User>> getMyProfileMatches(@PathVariable("profileId") String profileId) {		
		long totalUsers = 0;
		List<User> usersList = null;
		Optional<Preference> prefObj  = prefRepository.findById(profileId);
		if(prefObj.isPresent()) {
			SearchCriteria searchCriteria = buildSearchCriteria(prefObj.get());
			
			if(totalUsers == 0) {
				totalUsers = userRepositoryImpl.getSearchCount(searchCriteria);	
			}			
			usersList = userRepositoryImpl.searchUserData(searchCriteria, 0, 3);	
					
			logger.info("User Size in new class" + usersList.size());	
		}
		return ResponseEntity.ok()
							 .header("X-TOTAL-DOCS", Long.toString(totalUsers))
							 .body(usersList);
	}
	
	@PutMapping("/profile/summary")
	public ResponseEntity<User> getProfileSummary(@RequestParam("profileId") String profileId) throws URISyntaxException {
		Optional<User> userObj = userRepository.findById(profileId);
		User user = userObj.get();

		return ResponseEntity.ok().body(user);
	}
	
	@PutMapping("/profile/personal")
	public ResponseEntity<Status> updatePersonalDetail(@RequestBody User inputUser) throws URISyntaxException {
		
		Optional<User> userObj = userRepository.findById(inputUser.getId());
		User user = userObj.get();
		user.setmStatus(inputUser.getmStatus());
		user.setWeight(inputUser.getWeight());
		user.setHeightCm(inputUser.getHeightCm());
		user.setHeightInch(inputUser.getHeightInch());
		user.setFamilyType(inputUser.getFamilyType());
		user.setFamilyValue(inputUser.getFamilyValue());
		user.setBodyType(inputUser.getBodyType());
		user.setDisabled(inputUser.getDisabled());
		user.setFoodHabit(inputUser.getFoodHabit());
		user.setDisablityInfo(inputUser.getDisablityInfo());				
		userRepository.save(user);
		
		return ResponseEntity.noContent().build();	
	}
	
	@PutMapping("/profile/religion")
	public ResponseEntity<Status> updateReligionDetail(@RequestBody User inputUser) throws URISyntaxException {
		
		Optional<User> userObj = userRepository.findById(inputUser.getId());
		User user = userObj.get();		
		user.setCaste(inputUser.getCaste());
		user.setSubcaste(inputUser.getSubcaste());
		user.setGothram(inputUser.getGothram());
		user.setDhosham(inputUser.getDhosham());	
		userRepository.save(user);
		
		return ResponseEntity.noContent().build();		
	}
	
	@PutMapping("/profile/location")
	public ResponseEntity<Status> updateLocationDetail(@RequestBody User inputUser) throws URISyntaxException {
		
		Optional<User> userObj = userRepository.findById(inputUser.getId());
		User user = userObj.get();		
		user.setCountry(inputUser.getCountry());
		user.setPstate(inputUser.getPstate());
		user.setCity(inputUser.getCity());
		user.setDistrict(inputUser.getDistrict());	
		userRepository.save(user);
		
		return ResponseEntity.noContent().build();		
	}
	
	@PutMapping("/profile/professional")
	public ResponseEntity<Status> updateProfessionalDetail(@RequestBody User inputUser) throws URISyntaxException {
		
		Optional<User> userObj = userRepository.findById(inputUser.getId());
		User user = userObj.get();				
		user.setEducation(inputUser.getEducation());
		user.setEmployment(inputUser.getEmployment());
		user.setIncome(inputUser.getIncome());		
				
		userRepository.save(user);
		
		return ResponseEntity.noContent().build();		
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
		user.setHeightCm(inputUser.getHeightCm());
		user.setHeightInch(inputUser.getHeightInch());
		user.setFamilyType(inputUser.getFamilyType());
		user.setFamilyValue(inputUser.getFamilyValue());
		user.setBodyType(inputUser.getBodyType());
		user.setDisabled(inputUser.getDisabled());
		user.setFoodHabit(inputUser.getFoodHabit());
		user.setDisablityInfo(inputUser.getDisablityInfo());
				
		user.setEducation(inputUser.getEducation());
		user.setEmployment(inputUser.getEmployment());
		user.setIncome(inputUser.getIncome());		
				
		userRepository.save(user);
		
		return ResponseEntity.created(new URI("/user")).body(Util.populateStatus("t-200", "User registered successfully."));	
	}
	
	@PutMapping("/completeprofile")
	public ResponseEntity<Status> updateCompleteProfile(@RequestBody User inputUser) throws URISyntaxException {
		
		Optional<User> userObj = userRepository.findById(inputUser.getId());
		User user = userObj.get();
		user.setFirstName(inputUser.getFirstName());
		user.setLastName(inputUser.getLastName());
		user.setbDay(inputUser.getbDay());
		user.setbMonth(inputUser.getbMonth());
		user.setbYear(inputUser.getbYear());
		user.setmStatus(inputUser.getmStatus());
		user.setWeight(inputUser.getWeight());
		user.setHeightCm(inputUser.getHeightCm());
		user.setHeightInch(inputUser.getHeightInch());
		user.setFamilyType(inputUser.getFamilyType());
		user.setFamilyValue(inputUser.getFamilyValue());
		user.setBodyType(inputUser.getBodyType());
		user.setFoodHabit(inputUser.getFoodHabit());
		user.setDisabled(inputUser.getDisabled());
		user.setDisablityInfo(inputUser.getDisablityInfo());
		user.setBodyType(inputUser.getBodyType());		
		user.setCountry(inputUser.getCountry());
		user.setPstate(inputUser.getPstate());
		user.setCity(inputUser.getCity());
		user.setDistrict(inputUser.getDistrict());
				
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
			Optional<User> userObj = userRepository.findById(profileId);
			User user = userObj.get();
			user.setImage(Base64.getEncoder().encodeToString(IOUtils.toByteArray(inStream)));
			userRepository.save(user);			
			inStream.close();
			
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return ResponseEntity.ok().build();
	}
	
	
	@PostMapping("/horoscope")
	public ResponseEntity uploadHoroscopeImage(@RequestParam("horoscopeImage") MultipartFile horoscopeImage, 
			@RequestParam("profileId") String profileId) {
		try {
			
			InputStream inStream = horoscopeImage.getInputStream();
			Optional<User> userObj = userRepository.findById(profileId);
			User user = userObj.get();
			user.setHoroscope(Base64.getEncoder().encodeToString(IOUtils.toByteArray(inStream)));
			if (user.getHoroscope().getBytes().length > 1048576) {
				Status status = Util.populateStatus("t-420", "Horoscope size greater than 1MB. Please reduce the file size and then upload.");
				return ResponseEntity.badRequest().body(status);
			}
			userRepository.save(user);			
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