package com.thirumanam.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.thirumanam.model.Preference;
import com.thirumanam.model.Status;
import com.thirumanam.mongodb.repository.PreferenceRepository;
import com.thirumanam.util.Util;

@RestController
@RequestMapping("/matrimony/preference")
public class PreferenceController {
	
	@Autowired
	PreferenceRepository prefRepository;

	@PutMapping
	public ResponseEntity<Status> createUpdatePreferences(@RequestBody Preference inputPreference) throws URISyntaxException {		
		Optional<Preference> prefObj  = prefRepository.findById(inputPreference.getId());	
		Preference preference = null;
		if(prefObj.isPresent()) {
			preference = prefObj.get();
			preference.setAgeFrom(inputPreference.getAgeFrom());
			preference.setAgeTo(inputPreference.getAgeTo());
			preference.setmStatus(inputPreference.getmStatus());
			preference.setMaxHeight(inputPreference.getMaxHeight());
			preference.setMinHeight(inputPreference.getMinHeight());
			preference.setFoodHabits(inputPreference.getFoodHabits());
			preference.setReligions(inputPreference.getReligions());
			preference.setCastes(inputPreference.getCastes());
			preference.setCountries(inputPreference.getCountries());
			preference.setStates(inputPreference.getStates());
		} else {
			preference = inputPreference;
		}		
		prefRepository.save(preference);
		return ResponseEntity.created(new URI("/thirumanam/preference")).body(Util.populateStatus(200, "Contact created successfully."));		
	}
	
	@RequestMapping(value = "/{profileId}", method = RequestMethod.GET)
	public ResponseEntity<Preference> getAllMatchingProfile(@PathVariable("profileId") String profileId) {		
		Optional<Preference> prefObj = prefRepository.findById(profileId);
		Preference preference = new Preference();
		if(prefObj.isPresent()) {
			preference = prefObj.get();
		}		
		return ResponseEntity.ok().body(preference);
	}	
}
