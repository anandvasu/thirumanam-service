package com.thirumanam.controller;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thirumanam.model.ShortListedProfile;
import com.thirumanam.model.ShortListedProfiles;
import com.thirumanam.model.Status;
import com.thirumanam.model.User;
import com.thirumanam.mongodb.repository.ShortlistedProfileRepository;
import com.thirumanam.mongodb.repository.UserRepositoryImpl;

@RestController
@RequestMapping("/matrimony/shortlisted")
public class ShortlistedProfileController {

	@Autowired
	private ShortlistedProfileRepository shortlistedProfileRepository;
	
	@Autowired
	private UserRepositoryImpl userRepositoryImpl;
	
	@RequestMapping("/list/{profileId}")
	public ResponseEntity<List<User>> getVisitedProfiles(@PathVariable("profileId") String profileId, 
			@RequestParam("pageNo") int pageNo) {
		List<User> shortListedProfilesList = new ArrayList<User>();
		Map<String, Date> profileMap = new HashMap<String,Date>();
		Optional<ShortListedProfiles> sProfiles = shortlistedProfileRepository.findById(profileId);
		
		int shortListedCount = 0;
		
		List<String> profileIds = new ArrayList<String>();
		
		if(sProfiles.isPresent()) {
			ShortListedProfiles shortListedProfiles = sProfiles.get();
			List<ShortListedProfile> profiles = shortListedProfiles.getProfiles();
			for(ShortListedProfile profile: profiles) {
				if(!profileIds.contains(profile.getId())) {
					profileIds.add(profile.getId());
					profileMap.put(profile.getId(), profile.getShortlistedDate());
				}
			}
			
			int skipCount = (pageNo-1) * 10;
			
			if(!profileIds.isEmpty()) {
				shortListedProfilesList = userRepositoryImpl.findUsersById(profileIds, skipCount , 10);
				shortListedCount = profileIds.size();
			}
		}
		return ResponseEntity.ok()
				 .header("X-TOTAL-DOCS", Integer.toString(shortListedCount))
				 .body(shortListedProfilesList);
	}	
	
	@RequestMapping(value = "/{profileId}", method = RequestMethod.PUT)
	public ResponseEntity<Status> shortlistProfile(
			@PathVariable("profileId") String profileId, 
			@RequestParam("shortListedProfileId") String shortListedProfileId) throws URISyntaxException {
		ShortListedProfiles shortListProfiles = null;
		ShortListedProfile profile = new ShortListedProfile();
		profile.setId(shortListedProfileId);
		profile.setShortlistedDate(new Date());
		Optional<ShortListedProfiles> sProfiles = shortlistedProfileRepository.findById(profileId);
		if(sProfiles.isPresent()) {
			shortListProfiles = sProfiles.get();			
		} else {			
			shortListProfiles = new ShortListedProfiles();
			shortListProfiles.setId(profileId);
		}
		shortListProfiles.getProfiles().add(profile);			
		shortlistedProfileRepository.save(shortListProfiles);		
		return ResponseEntity.noContent().build();		
	}
}
