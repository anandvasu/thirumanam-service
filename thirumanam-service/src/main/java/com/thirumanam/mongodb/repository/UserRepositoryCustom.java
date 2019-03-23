package com.thirumanam.mongodb.repository;

import java.util.List;

import com.thirumanam.model.SearchCriteria;
import com.thirumanam.model.User;

public interface UserRepositoryCustom {
	
	List<User> searchUserData(SearchCriteria criteria, long skipNumber, int noOfDocs);
	long getSearchCount(SearchCriteria searchCriteria);
	List<User> getFeaturedProfiles();
	List<User> findUsersById(List<String> profileIds, long skipNumber, int noOfDocs);
}
