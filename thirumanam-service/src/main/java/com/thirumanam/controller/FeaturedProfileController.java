package com.thirumanam.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thirumanam.model.User;
import com.thirumanam.model.UserSummary;
import com.thirumanam.mongodb.repository.UserRepository;
import com.thirumanam.util.CacheConstants;

@RestController
@RequestMapping("/matrimony/featuredprofiles")
public class FeaturedProfileController {
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/list")
	public ResponseEntity<List<UserSummary>> getFeaturedProfiles() {
		List<UserSummary> profileList = CacheConstants.featuredProfileList;
		if(profileList == null) {
			List<User> users = userRepository.getFeaturedProfiles();
			profileList = new ArrayList<UserSummary>();
			for(User user: users) {
				if(user.getImage() != null) {
					UserSummary featuredProfile = new UserSummary();
					featuredProfile.setProfileId(user.getId());
					featuredProfile.setImage(user.getImage());
					featuredProfile.setFirstName(user.getFirstName());
					featuredProfile.setLastName(user.getLastName());
					featuredProfile.setAge(user.getAge());
					featuredProfile.setbDay(user.getbDay());
					featuredProfile.setbMonth(user.getbMonth());
					featuredProfile.setbYear(user.getbYear());
					featuredProfile.setGender(user.getGender());
					featuredProfile.setCity(user.getCity());
					featuredProfile.setEducation(user.getEducation());
					profileList.add(featuredProfile);
				}
			}
			CacheConstants.featuredProfileList = profileList;
		} 
		return ResponseEntity.ok().body(profileList);
	}	
}