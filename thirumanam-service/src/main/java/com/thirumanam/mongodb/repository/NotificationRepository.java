package com.thirumanam.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.thirumanam.model.Notification;

public interface NotificationRepository extends MongoRepository<Notification, String> {

}
