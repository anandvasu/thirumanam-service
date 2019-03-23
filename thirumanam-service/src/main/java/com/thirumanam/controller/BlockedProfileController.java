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

import com.thirumanam.model.BlockedProfile;
import com.thirumanam.model.BlockedProfiles;
import com.thirumanam.model.Status;
import com.thirumanam.model.User;
import com.thirumanam.mongodb.repository.BlockedProfileRepository;
import com.thirumanam.mongodb.repository.UserRepositoryImpl;

@RestController
@RequestMapping("/matrimony/blocked")
public class BlockedProfileController {

	@Autowired
	private BlockedProfileRepository blockedProfileRepository;
	
	@Autowired
	private UserRepositoryImpl userRepositoryImpl;
	
	@RequestMapping("/list/{profileId}")
	public ResponseEntity<List<User>> getVisitedProfiles(@PathVariable("profileId") String profileId, @RequestParam("pageNo") int pageNo) {
		List<User> shortListedProfilesList = new ArrayList<User>();
		Map<String, Date> profileMap = new HashMap<String,Date>();
		Optional<BlockedProfiles> sProfiles = blockedProfileRepository.findById(profileId);
		
		int blockedCount = 0;
		
		List<String> profileIds = new ArrayList<String>();
		
		if(sProfiles.isPresent()) {
			BlockedProfiles blockedProfiles = sProfiles.get();
			List<BlockedProfile> profiles = blockedProfiles.getProfiles();
			for(BlockedProfile profile: profiles) {
				if(!profileIds.contains(profile.getId())) {
					profileIds.add(profile.getId());
					profileMap.put(profile.getId(), profile.getBlockedDate());
				}
			}
			
			int skipCount = (pageNo-1) * 10;
			
			if(!profileIds.isEmpty()) {
				shortListedProfilesList = userRepositoryImpl.findUsersById(profileIds, skipCount, 10);
				blockedCount = profileIds.size();
			}
		}
		return ResponseEntity.ok()
				 .header("X-TOTAL-DOCS", Integer.toString(blockedCount))
				 .body(shortListedProfilesList);
	}	
	
	@RequestMapping(value = "/{profileId}", method = RequestMethod.PUT)
	public ResponseEntity<Status> blockProfile(
			@PathVariable("profileId") String profileId, 
			@RequestParam("blockedProfileId") String userId) throws URISyntaxException {
		BlockedProfiles blockedProfiles = null;
		BlockedProfile profile = new BlockedProfile();
		profile.setId(userId);
		profile.setBlockedDate(new Date());
		Optional<BlockedProfiles> sProfiles = blockedProfileRepository.findById(profileId);
		if(sProfiles.isPresent()) {
			blockedProfiles = sProfiles.get();			
		} else {
			blockedProfiles = new BlockedProfiles();
			blockedProfiles.setId(profileId);
		}
		blockedProfiles.getProfiles().add(profile);			
		blockedProfileRepository.save(blockedProfiles);		
		return ResponseEntity.noContent().build();		
	}
	
	@RequestMapping(value = "unblock/{profileId}", method = RequestMethod.PUT)
	public ResponseEntity<Status> unblockProfile(
			@PathVariable("profileId") String profileId, 
			@RequestParam("unBlockProfileId") String unBlockProfileId) throws URISyntaxException {
		BlockedProfiles blockedProfiles = null;
		Optional<BlockedProfiles> sProfiles = blockedProfileRepository.findById(profileId);
		if(sProfiles.isPresent()) {
			blockedProfiles = sProfiles.get();		
			List<BlockedProfile> profiles = blockedProfiles.getProfiles();
			for(BlockedProfile profile: profiles) {
				if(unBlockProfileId.equals(profile.getId())) {
					profiles.remove(profile);
					break;
				}
			}
		}
		blockedProfileRepository.save(blockedProfiles);		
		return ResponseEntity.noContent().build();		
	}
}