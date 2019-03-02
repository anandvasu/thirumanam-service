package com.thirumanam.controller;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.apache.commons.io.IOUtils;
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

import com.thirumanam.model.BlockedProfile;
import com.thirumanam.model.BlockedProfiles;
import com.thirumanam.model.Preference;
import com.thirumanam.model.SearchCriteria;
import com.thirumanam.model.ShortListedProfile;
import com.thirumanam.model.ShortListedProfiles;
import com.thirumanam.model.Status;
import com.thirumanam.model.ThirumanamUtil;
import com.thirumanam.model.User;
import com.thirumanam.model.VisitedProfiles;
import com.thirumanam.model.Visitor;
import com.thirumanam.mongodb.repository.BlockedProfileRepository;
import com.thirumanam.mongodb.repository.PreferenceRepository;
import com.thirumanam.mongodb.repository.ShortlistedProfileRepository;
import com.thirumanam.mongodb.repository.UserRepository;
import com.thirumanam.mongodb.repository.UserRepositoryImpl;
import com.thirumanam.mongodb.repository.VisitedProfileRepository;
import com.thirumanam.util.Util;

@RestController
@RequestMapping("/matrimony/user")
public class UserSearchController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	PreferenceRepository prefRepository;
	
	@Autowired
	private UserRepositoryImpl userRepositoryImpl;
	
	@Autowired
	private ShortlistedProfileRepository shortlistedProfileRepository;
	
	@Autowired
	private BlockedProfileRepository blockedProfileRepository;
	
	@Autowired
	private VisitedProfileRepository visitedProfileRepository;
	
	
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
		ThirumanamUtil.updateProfileCompPercent(user);
		return ResponseEntity.ok().body(user);
	}
	
	@RequestMapping(value = "/self/{profileId}", method = RequestMethod.GET)
	public ResponseEntity<User> getProfileSelf(@PathVariable("profileId") String profileId) {
		Optional<User> userObj = userRepository.findById(profileId);
		User user = userObj.get();		
		return ResponseEntity.ok().body(user);
	}
	
	@RequestMapping(value = "/{profileId}", method = RequestMethod.GET)
	public ResponseEntity<User> getUser(@PathVariable("profileId") String profileId, @RequestParam("userId") String loggedInUserId) {
		Optional<User> userObj = userRepository.findById(profileId);
		User user = userObj.get();
		Optional<VisitedProfiles> vProfiles = visitedProfileRepository.findById(profileId);
		VisitedProfiles visitedProfile = null;
		if(vProfiles.isPresent()) {
			visitedProfile = vProfiles.get();
			List<Visitor> vistoryList = visitedProfile.getProfiles();
			boolean isUserAlreadyExists = false;
			for(Visitor visitor: vistoryList) {
				if(visitor.getId().equals(loggedInUserId)) {
					isUserAlreadyExists = true;
					break;
				}
			}
			if(!isUserAlreadyExists) {
				Visitor visitor = new Visitor();
				visitor.setId(loggedInUserId);
				visitor.setVisitedDate(new Date());
				visitedProfile.getProfiles().add(0, visitor);
				visitedProfileRepository.save(visitedProfile);
			}
		} else {
			visitedProfile = new VisitedProfiles();			
			visitedProfile.setId(profileId);
			Visitor visitor = new Visitor();
			visitor.setId(loggedInUserId);
			visitor.setVisitedDate(new Date());
			visitedProfile.getProfiles().add(0, visitor);
			visitedProfileRepository.save(visitedProfile);
		}		
		
		
		return ResponseEntity.ok().body(user);
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
		
		
		//Get Blocked Profiles 
		List<String> blockedProfileIds = new ArrayList<String>();
		Optional<BlockedProfiles> bProfiles = blockedProfileRepository.findById(profileId);	
		if(bProfiles.isPresent()) {
			BlockedProfiles blockedProfiles = bProfiles.get();
			List<BlockedProfile> profiles = blockedProfiles.getProfiles();
			for(BlockedProfile profile: profiles) {
				blockedProfileIds.add(profile.getId());
			}			
		}
		
		//Get shortlisted Profiles 
		Optional<ShortListedProfiles> sProfiles = shortlistedProfileRepository.findById(profileId);	
		if(sProfiles.isPresent()) {
			ShortListedProfiles sListedProfiles = sProfiles.get();
			List<ShortListedProfile> profiles = sListedProfiles.getProfiles();
			for(ShortListedProfile profile: profiles) {
				//add shortlisted profiles to blocked list
				blockedProfileIds.add(profile.getId());
			}			
		}
		
		Optional<Preference> prefObj  = prefRepository.findById(profileId);
		if(prefObj.isPresent()) {
			SearchCriteria searchCriteria = buildSearchCriteria(prefObj.get());
			searchCriteria.setBlockedProfiles(blockedProfileIds);
			
			if(totalUsers == 0) {
				totalUsers = userRepositoryImpl.getSearchCount(searchCriteria);	
			}			
			usersList = userRepositoryImpl.searchUserData(searchCriteria, 0, 3);						
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
						   
		return ResponseEntity.ok()
							 .header("X-TOTAL-DOCS", Long.toString(totalUsers))
							 .body(usersList);
	}
	
	@PostMapping("/image")
	public ResponseEntity<String> uploadProfileImage(@RequestParam("imageFile") MultipartFile imageFile, 
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
	public ResponseEntity<Status> uploadHoroscopeImage(@RequestParam("horoscopeImage") MultipartFile horoscopeImage, 
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
}