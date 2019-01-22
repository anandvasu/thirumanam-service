package com.thirumanam.mongodb.repository;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.thirumanam.model.SearchCriteria;
import com.thirumanam.model.User;
import com.thirumanam.util.FieldConstants;

@Component
public class UserRepositoryImpl implements UserRepositoryCustom {
	
	private final MongoTemplate mongoTemplate;
	
	@Autowired
	public UserRepositoryImpl(MongoTemplate aMongoTemplate) {
		mongoTemplate = aMongoTemplate;
	}
	
	@Override
	public List<User> searchUserData(SearchCriteria searchCriteria, long skipNumber, int noOfDocs) {
  
		Criteria criteria = Criteria.where("age").gt(searchCriteria.getAgeGreater()-1).lt(searchCriteria.getAgeLess()+1);
		
		if(searchCriteria.getCity() != null && !searchCriteria.getCity().isEmpty()) {
			Criteria citryCriteria = Criteria.where(FieldConstants.CITY).is(searchCriteria.getCity());	
			criteria.andOperator(citryCriteria);
		}
		if(searchCriteria.getGender() != null && !searchCriteria.getGender().isEmpty()) {
			criteria.and(FieldConstants.GENDER).in(searchCriteria.getGender());
		}
		if(searchCriteria.getMaritalStatus() != null && !searchCriteria.getMaritalStatus().isEmpty()) {
			criteria.and(FieldConstants.MARITAL_STATUS).in(searchCriteria.getMaritalStatus());
		}
		if(searchCriteria.getEducation() != null && !searchCriteria.getEducation().isEmpty()) {
			Criteria eduCriteria = Criteria.where(FieldConstants.EDUCATION).is(searchCriteria.getEducation());	
			criteria.andOperator(eduCriteria);
		}
				
		Query query = new Query();
		query.addCriteria(criteria);
		if (skipNumber > 0) {
			query.skip(skipNumber);
		}
		query.limit(noOfDocs);	
		
		return mongoTemplate.find(query, User.class);
	}		
	
	@Override
	public long getSearchCount(SearchCriteria searchCriteria) {
  
		Criteria criteria = Criteria.where("age").gt(searchCriteria.getAgeGreater()-1).lt(searchCriteria.getAgeLess()+1);
		
		if(searchCriteria.getCity() != null && !searchCriteria.getCity().isEmpty()) {
			Criteria citryCriteria = Criteria.where(FieldConstants.CITY).is(searchCriteria.getCity());	
			criteria.andOperator(citryCriteria);
		}
		if(searchCriteria.getGender() != null && !searchCriteria.getGender().isEmpty()) {
			criteria.and(FieldConstants.GENDER).in(searchCriteria.getGender());
		}
		if(searchCriteria.getMaritalStatus() != null && !searchCriteria.getMaritalStatus().isEmpty()) {
			criteria.and(FieldConstants.MARITAL_STATUS).in(searchCriteria.getMaritalStatus());
		}
		if(searchCriteria.getEducation() != null && !searchCriteria.getEducation().isEmpty()) {
			Criteria eduCriteria = Criteria.where(FieldConstants.EDUCATION).is(searchCriteria.getEducation());	
			criteria.andOperator(eduCriteria);
		}
				
		Query query = new Query();
		query.addCriteria(criteria);	
		return mongoTemplate.count(query, "user");
	}	
}
