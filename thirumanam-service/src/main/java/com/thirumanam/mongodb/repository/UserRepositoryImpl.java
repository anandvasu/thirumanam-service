package com.thirumanam.mongodb.repository;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.thirumanam.model.LabelValue;
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
  
		Criteria criteria = buildCriteria(searchCriteria);
				
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
  
		Criteria criteria = buildCriteria(searchCriteria);
				
		Query query = new Query();
		query.addCriteria(criteria);	
		return mongoTemplate.count(query, "user");
	}	
	
	@Override
	public List<User> getFeaturedProfiles() {
		Criteria criteria = Criteria.where("isFP").exists(true);
		Query query = new Query();
		query.addCriteria(criteria);	
		return mongoTemplate.find(query, User.class);		
	}
	
	private List<String> getValues(List<LabelValue> labelValues) {
		List<String> values = new ArrayList<String>();
		for(LabelValue labelValue: labelValues) {
			values.add(labelValue.getValue());
		}
		return values;
	}
	
	private Criteria buildCriteria(SearchCriteria searchCriteria) {
		
			Criteria criteria =  Criteria.where(FieldConstants.GENDER).in(searchCriteria.getGender());
			
			Criteria ageLessCriteria = Criteria.where(FieldConstants.AGE).gt(searchCriteria.getAgeGreater()-1).lt(searchCriteria.getAgeLess()+1);
			criteria.andOperator(ageLessCriteria);
			
			criteria.and(FieldConstants.HEIGHT).gt(searchCriteria.getMinHeight()-1).lt(searchCriteria.getMaxHeight()+1);
						
			if(searchCriteria.getCity() != null && !searchCriteria.getCity().isEmpty()) {
				criteria.and(FieldConstants.CITY).in(searchCriteria.getCity());
			}
			
			if(!searchCriteria.getReligions().isEmpty()) {
				criteria.and(FieldConstants.RELIGION).in(getValues(searchCriteria.getReligions()));
			}
			
			if(!searchCriteria.getCastes().isEmpty()) {
				criteria.and(FieldConstants.CASTE).in(getValues(searchCriteria.getCastes()));
			}
			
			if(!searchCriteria.getGothrams().isEmpty()) {
				criteria.and(FieldConstants.GOTHRAM).in(getValues(searchCriteria.getGothrams()));
			}
			
			if(searchCriteria.getMaritalStatus() != null && !searchCriteria.getMaritalStatus().isEmpty()) {
				criteria.and(FieldConstants.MARITAL_STATUS).in(searchCriteria.getMaritalStatus());
			}
			if(searchCriteria.getEducation() != null && !searchCriteria.getEducation().isEmpty()) {
				Criteria eduCriteria = Criteria.where(FieldConstants.EDUCATION).is(searchCriteria.getEducation());	
				criteria.andOperator(eduCriteria);
			}
			return criteria;
		}
	}
