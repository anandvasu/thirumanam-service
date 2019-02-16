package com.thirumanam.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thirumanam.mongodb.repository.VisitedProfileRepository;

@RestController
@RequestMapping("/thirumanam/visitedprofiles")
public class VisitedProfileController {

	
	@RequestMapping("/list/{profileId}")
	public ResponseEntity<List<String>> getFeaturedProfiles(@PathVariable("profileId") String profileId) {
		List<String> profileList = new ArrayList<String>();
		return ResponseEntity.ok().body(profileList);
	}	
}
