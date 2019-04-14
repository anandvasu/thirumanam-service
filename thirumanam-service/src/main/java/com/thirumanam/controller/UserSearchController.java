package com.thirumanam.controller;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thirumanam.model.BlockedProfile;
import com.thirumanam.model.BlockedProfiles;
import com.thirumanam.model.MyMatchResponse;
import com.thirumanam.model.Preference;
import com.thirumanam.model.SearchCriteria;
import com.thirumanam.model.ShortListedProfile;
import com.thirumanam.model.ShortListedProfiles;
import com.thirumanam.model.ThirumanamUtil;
import com.thirumanam.model.User;
import com.thirumanam.mongodb.repository.BlockedProfileRepository;
import com.thirumanam.mongodb.repository.PreferenceRepository;
import com.thirumanam.mongodb.repository.ShortlistedProfileRepository;
import com.thirumanam.mongodb.repository.UserRepository;
import com.thirumanam.mongodb.repository.UserRepositoryImpl;
import com.thirumanam.util.ErrorMessageConstants;
import com.thirumanam.util.ThirumanamConstant;

@RestController
@RequestMapping("/matrimony/list/user")
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
	
	@PostMapping("/")
	public ResponseEntity<List<User>> searchUser(@RequestBody SearchCriteria searchCriteria) {
		long totalUsers = searchCriteria.getTotalDocs();
		if(totalUsers == 0) {
			totalUsers = userRepositoryImpl.getSearchCount(searchCriteria);	
		}
		
		int skipnumber = (searchCriteria.getPageNumber() == 1) ? 0 : ((searchCriteria.getPageNumber()-1) * 10);
		int numberOfDocs = (skipnumber +10 < totalUsers) ? 10 : (int)(totalUsers-skipnumber);		
		
		List<User> usersList = userRepositoryImpl.searchUserData(searchCriteria, skipnumber, numberOfDocs);	
						   
		return ResponseEntity.ok()
							 .header(ThirumanamConstant.HEADER_TOTAL_DOCS, Long.toString(totalUsers))
							 .body(usersList);
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
		ThirumanamUtil.updateProfileCompPercent(user);
		return ResponseEntity.ok().body(user);
	}
	
	@RequestMapping(value = "/self/{profileId}", method = RequestMethod.GET)
	public ResponseEntity<User> getProfileSelf(
			@PathVariable("profileId") String profileId, 
			@RequestAttribute(ThirumanamConstant.USER_AUTHORIZED) boolean isUserAuthorized) {		
		System.out.print("isUserAuthorized:" + isUserAuthorized);
		Optional<User> userObj = userRepository.findById(profileId);
		User user = userObj.get();		
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
	public ResponseEntity<MyMatchResponse> getMyProfileMatches(
			@PathVariable("profileId") String profileId, 
			@RequestAttribute(ThirumanamConstant.USER_AUTHORIZED) boolean isUserAuthorized) {		
		long totalUsers = 0;		
		MyMatchResponse myMatchResponse = new MyMatchResponse();		
		if (isUserAuthorized) {		
			List<User> usersList = null;					
			Optional<Preference> prefObj  = prefRepository.findById(profileId);
			if(prefObj.isPresent()) {
				SearchCriteria searchCriteria = buildSearchCriteria(prefObj.get());
				
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
				
				searchCriteria.setBlockedProfiles(blockedProfileIds);
				
				if(totalUsers == 0) {
					totalUsers = userRepositoryImpl.getSearchCount(searchCriteria);	
					if(totalUsers > 0) {
						usersList = userRepositoryImpl.searchUserData(searchCriteria, 0, 3);							
					} else {
						usersList = new ArrayList<User>();
					}
					myMatchResponse.setUserList(usersList);
				}			
				myMatchResponse.setPrefernceExists(true);
				myMatchResponse.setSuccess(true);				
			} else {
				
			}
		} else {
			myMatchResponse.setErrorMessage(ErrorMessageConstants.MESSAGE_INVALID__JWT_ISSUER);
		}
		return ResponseEntity.ok()
							 .header("X-TOTAL-DOCS", Long.toString(totalUsers))
							 .body(myMatchResponse);
	}
	
	@PutMapping("/profile/summary")
	public ResponseEntity<User> getProfileSummary(@RequestParam("profileId") String profileId) throws URISyntaxException {
		Optional<User> userObj = userRepository.findById(profileId);
		User user = userObj.get();

		return ResponseEntity.ok().body(user);
	}	
}