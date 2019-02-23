package com.thirumanam.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import com.thirumanam.model.ShortListedProfiles;

@Component
public interface ShortlistedProfileRepository extends MongoRepository<ShortListedProfiles, String> {

}
