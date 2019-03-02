package com.thirumanam.model;

public class LoginResponse {
	
	private String idToken;	
	private String refrehToken;
	private String gender;
	private String firstName;	
	private String lastName;
	private String profileId;
	private String profilePerCompleted;	
	
	public String getIdToken() {
		return idToken;
	}
	public void setIdToken(String idToken) {
		this.idToken = idToken;
	}
	public String getRefrehToken() {
		return refrehToken;
	}
	public void setRefrehToken(String refrehToken) {
		this.refrehToken = refrehToken;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getProfileId() {
		return profileId;
	}
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}
	public String getProfilePerCompleted() {
		return profilePerCompleted;
	}
	public void setProfilePerCompleted(String profilePerCompleted) {
		this.profilePerCompleted = profilePerCompleted;
	}
}