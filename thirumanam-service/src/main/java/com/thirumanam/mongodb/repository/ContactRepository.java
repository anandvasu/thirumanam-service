package com.thirumanam.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.thirumanam.model.Contact;

public interface ContactRepository extends MongoRepository<Contact, String> {
	
}