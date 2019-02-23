package com.thirumanam.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import com.thirumanam.model.BlockedProfiles;

@Component
public interface BlockedProfileRepository extends MongoRepository<BlockedProfiles, String> {

}
