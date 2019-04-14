package com.thirumanam.model;

import java.util.List;

public class MyMatchResponse extends Response {
	
	private boolean isPrefernceExists;
	List<User> userList;
	
	public boolean isPrefernceExists() {
		return isPrefernceExists;
	}
	public void setPrefernceExists(boolean isPrefernceExists) {
		this.isPrefernceExists = isPrefernceExists;
	}
	public List<User> getUserList() {
		return userList;
	}
	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
}