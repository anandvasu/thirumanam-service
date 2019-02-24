package com.thirumanam.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thirumanam.model.User;
import com.thirumanam.model.VisitedProfiles;
import com.thirumanam.model.Visitor;
import com.thirumanam.mongodb.repository.UserRepositoryImpl;
import com.thirumanam.mongodb.repository.VisitedProfileRepository;

@RestController
@RequestMapping("/thirumanam/visitedprofiles")
public class VisitedProfileController {
	
	@Autowired
	private VisitedProfileRepository visitedProfileRepository;
	
	@Autowired
	private UserRepositoryImpl userRepositoryImpl;
	
	@RequestMapping("/list/{profileId}")
	public ResponseEntity<List<User>> getVisitedProfiles(@PathVariable("profileId") String profileId, 
			@RequestParam("pageNo") int pageNo) {
		List<User> visitedProfileList = new ArrayList<User>();
		Map<String, Date> profileMap = new HashMap<String,Date>();
		Optional<VisitedProfiles> vProfiles = visitedProfileRepository.findById(profileId);
		
		int peopleViewed = 0;
		
		List<String> profileIds = new ArrayList<String>();
		
		if(vProfiles.isPresent()) {
			VisitedProfiles visitedProfile = vProfiles.get();
			List<Visitor> visitors = visitedProfile.getProfiles();
			for(Visitor visitor: visitors) {
				if(!profileIds.contains(visitor.getId())) {
					profileIds.add(visitor.getId());
					profileMap.put(visitor.getId(), visitor.getVisitedDate());
				}
			}
			
			int skipCount = (pageNo-1) * 10;
			
			if(!profileIds.isEmpty()) {
				visitedProfileList = userRepositoryImpl.findUsersByd(profileIds, skipCount, pageNo);
				peopleViewed = profileIds.size();
			}
		}
		return ResponseEntity.ok()
				 .header("X-TOTAL-DOCS", Integer.toString(peopleViewed))
				 .body(visitedProfileList);
	}	
}
