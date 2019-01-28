package com.thirumanam.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.thirumanam.model.Preference;

public interface PreferenceRepository extends MongoRepository<Preference, String> {
	
}