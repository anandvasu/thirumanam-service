package com.thirumanam.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.thirumanam.model.UserAdditionalDetial;

public interface UserAdditionalDetailRepository extends MongoRepository<UserAdditionalDetial, String> {

}