package com.thirumanam.mongodb.repository;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.thirumanam.model.SearchCriteria;
import com.thirumanam.model.User;

@Component
public class UserRepositoryImpl implements UserRepositoryCustom {
	
	private final MongoTemplate mongoTemplate;
	
	@Autowired
	public UserRepositoryImpl(MongoTemplate aMongoTemplate) {
		mongoTemplate = aMongoTemplate;
	}
	
	@Override
	public List<User> searchUserData(SearchCriteria criteria) {
  
		Query query = new Query();
		query.addCriteria(Criteria.where("age").gt(criteria.getAgeGreater()).lt(criteria.getAgeLess()));
		return mongoTemplate.find(query, User.class);
	}		
}
