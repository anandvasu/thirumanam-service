package com.thirumanam.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.thirumanam.model.MessageList;

public interface MessageRepository extends MongoRepository<MessageList, String> {

}
