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
import com.thirumanam.util.ThirumanamConstant;

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
		if(!searchCriteria.getBlockedProfiles().isEmpty()) {
			criteria.and(FieldConstants.ID).nin(searchCriteria.getBlockedProfiles());
		}
				
		Query query = new Query();
		query.fields().include(FieldConstants.FIRSTNAME)
					  .include(FieldConstants.LASTNAME)
					  .include(FieldConstants.BDAY)
					  .include(FieldConstants.BMONTH)
					  .include(FieldConstants.BYEAR)
					  .include(FieldConstants.EDUCATION)
					  .include(FieldConstants.CITY)
					  .include(FieldConstants.THUMB_IMAGE)
					  .include(FieldConstants.AGE);
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
		if(!searchCriteria.getBlockedProfiles().isEmpty()) {
			criteria.and(FieldConstants.ID).nin(searchCriteria.getBlockedProfiles());
		}
		
		Query query = new Query();
		query.fields().include(FieldConstants.ID);
		query.addCriteria(criteria);	
		
		return mongoTemplate.count(query, "user");
	}	
	
	@Override
	public List<User> findUsersById(List<String> profileIds, long skipNumber, int noOfDocs) {
		Criteria criteria = Criteria.where("id").in(profileIds);
		Query query = new Query();
		query.fields().include(FieldConstants.FIRSTNAME)
					  .include(FieldConstants.LASTNAME)
					  .include(FieldConstants.AGE)
					  .include(FieldConstants.BDAY)
					  .include(FieldConstants.BMONTH)
					  .include(FieldConstants.BYEAR)
					  .include(FieldConstants.EDUCATION)
					  .include(FieldConstants.CITY)
					  .include(FieldConstants.IMAGE);
		query.addCriteria(criteria);	
		if(skipNumber > 0) {
			query.skip(skipNumber);
		}		
		query.limit(noOfDocs);	
		return mongoTemplate.find(query, User.class);	
	}
	
	@Override
	public List<User> getFeaturedProfiles() {
		Criteria criteria = Criteria.where("isFP").exists(true);
		Query query = new Query();
		query.fields().include(FieldConstants.FIRSTNAME)
					  .include(FieldConstants.LASTNAME)
					  .include(FieldConstants.IMAGE);
		query.addCriteria(criteria);	
		return mongoTemplate.find(query, User.class);		
	}
	
	private Criteria buildCriteria(SearchCriteria searchCriteria) {
		
			Criteria criteria =  Criteria.where(FieldConstants.GENDER).in(searchCriteria.getGender());
			
			//Criteria ageLessCriteria = Criteria.where(FieldConstants.AGE).gt(searchCriteria.getAgeGreater()-1).lt(searchCriteria.getAgeLess()+1);
			//criteria.andOperator(ageLessCriteria);			
			
			if(searchCriteria.getMaxHeight() > 0 || searchCriteria.getMaxHeight() > 0 ) {
				Criteria heightCriteria = new Criteria();
				heightCriteria.orOperator(Criteria.where(FieldConstants.HEIGHT_CM).gt(searchCriteria.getMinHeight()-1).lt(searchCriteria.getMaxHeight()+1), 
						Criteria.where(FieldConstants.HEIGHT_INCH).gt(searchCriteria.getMinHeight()-1).lt(searchCriteria.getMaxHeight()+1));
				criteria.andOperator(heightCriteria);
			}		
			
			if(searchCriteria.getAgeLess() > 0 || searchCriteria.getAgeGreater() > 0 ) {
				criteria.and(FieldConstants.AGE).gt(searchCriteria.getAgeGreater()-1).lt(searchCriteria.getAgeLess()+1);
			}			
						
			if(searchCriteria.getCountries() != null && !searchCriteria.getCountries().isEmpty()) {
				criteria.and(FieldConstants.COUNTRY).in(searchCriteria.getCountries());
			}
			
			if(searchCriteria.getStates() != null && !searchCriteria.getStates().isEmpty()) {
				criteria.and(FieldConstants.STATE).in(searchCriteria.getStates());
			}	
			
			if(searchCriteria.getDistricts() != null && !searchCriteria.getDistricts().isEmpty()) {
				criteria.and(FieldConstants.DISTRICT).in(searchCriteria.getDistricts());
			}
					
			if(!searchCriteria.getReligions().isEmpty()) {
				criteria.and(FieldConstants.RELIGION).in(searchCriteria.getReligions());
			}
			
			if(!searchCriteria.getCastes().isEmpty()) {
				criteria.and(FieldConstants.CASTE).in(searchCriteria.getCastes());
			}
			
			if(!searchCriteria.getGothrams().isEmpty()) {
				criteria.and(FieldConstants.GOTHRAM).in(searchCriteria.getGothrams());
			}
			
			if(!searchCriteria.getDhoshams().isEmpty()) {
				criteria.and(FieldConstants.DHOSHAM).in(searchCriteria.getDhoshams());
			}
			
			if(searchCriteria.getMaritalStatus() != null && !searchCriteria.getMaritalStatus().isEmpty()) {
				criteria.and(FieldConstants.MARITAL_STATUS).in(searchCriteria.getMaritalStatus());
			}
			
			if(searchCriteria.getEducations() != null && !searchCriteria.getEducations().isEmpty()) {
				criteria.and(FieldConstants.EDUCATION).in(searchCriteria.getEducations());				
			}
			
			if(searchCriteria.getFoodHabits() != null && !searchCriteria.getFoodHabits().isEmpty()) {
				criteria.and(FieldConstants.FOODHABIT).in(searchCriteria.getFoodHabits());				
			}
			
			if(searchCriteria.getSmokingHabits() != null && !searchCriteria.getSmokingHabits().isEmpty()) {
				criteria.and(FieldConstants.SMOKINGHABIT).in(searchCriteria.getSmokingHabits());				
			}
			
			if(searchCriteria.getDrinkingHabits() != null && !searchCriteria.getDrinkingHabits().isEmpty()) {
				criteria.and(FieldConstants.DRINKINGHABIT).in(searchCriteria.getDrinkingHabits());				
			}
			
			if(searchCriteria.getBodyTypes() != null && !searchCriteria.getBodyTypes().isEmpty()) {
				criteria.and(FieldConstants.BODYTYPE).in(searchCriteria.getBodyTypes());				
			}
			
			if(searchCriteria.getEmployments() != null && !searchCriteria.getEmployments().isEmpty()) {
				criteria.and(FieldConstants.EMPLOYMENT).in(searchCriteria.getEmployments());				
			}	
			
			if(!searchCriteria.getOccupations().isEmpty()) {
				criteria.and(FieldConstants.OCCUPATION).in(searchCriteria.getOccupations());				
			}	
			
			if(searchCriteria.getShowProfile() != null && 
					searchCriteria.getShowProfile().equals(ThirumanamConstant.SHOW_PROFILE_WITH_PHOTO)) {
				criteria.and(FieldConstants.IMAGE).exists(true);
			}
			
			if(searchCriteria.getShowProfile() != null && 
					searchCriteria.getShowProfile().equals(ThirumanamConstant.SHOW_PROFILE_WITHOUT_PHOTO)) {
				criteria.and(FieldConstants.IMAGE).exists(false);
			}
			
			if(searchCriteria.getIncome() != null && !searchCriteria.getIncome().isEmpty()) {
				if(searchCriteria.getIncome().indexOf(ThirumanamConstant.HYPHEN) > 0) {
					String incomeArr[] = searchCriteria.getIncome().split(ThirumanamConstant.HYPHEN);
					Long lowValue = Long.parseLong(incomeArr[0].trim());
					Long highValue = Long.parseLong(incomeArr[1].trim());
					criteria.and(FieldConstants.INCOME).gt(lowValue).lt(highValue);
				} else {
					criteria.and(FieldConstants.INCOME).gt(Long.parseLong(searchCriteria.getIncome()));	
				}							
			}			
			return criteria;
		}
	}
