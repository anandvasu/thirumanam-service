package com.thirumanam.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import com.thirumanam.model.VisitedProfiles;

@Component
public interface VisitedProfileRepository extends MongoRepository<VisitedProfiles, String> {

}