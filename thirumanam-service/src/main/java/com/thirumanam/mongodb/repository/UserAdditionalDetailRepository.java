package com.thirumanam.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.thirumanam.model.UserInfo;

public interface UserAdditionalDetailRepository extends MongoRepository<UserInfo, String> {

}