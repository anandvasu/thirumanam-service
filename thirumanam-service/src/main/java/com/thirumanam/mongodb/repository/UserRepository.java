package com.thirumanam.mongodb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.thirumanam.model.User;

public interface UserRepository extends MongoRepository<User, String>, UserRepositoryCustom {
	
	public List<User> findByEmail(String email);
	public List<User> findByFirstName(String firstName);
}