package com.thirumanam.mongodb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.thirumanam.model.User;

public interface UserRepository extends MongoRepository<User, String>, UserRepositoryCustom {
	
	List<User> findByEmail(String email);
	List<User> findByFirstName(String firstName);
	List<User> findByExternalId(String externalId);
}