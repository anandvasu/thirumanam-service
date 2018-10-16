package com.thirumanam.mongodb.repository;

import java.util.List;

import com.thirumanam.model.SearchCriteria;
import com.thirumanam.model.User;

public interface UserRepositoryCustom {
	
	List<User> searchUserData(SearchCriteria criteria);
}
