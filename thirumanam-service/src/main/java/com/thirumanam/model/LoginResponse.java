package com.thirumanam.model;

public class LoginResponse {
	
	private String idToken;	
	private String refreshToken;
	private String accessToken;		
	private String gender;
	private String firstName;	
	private String lastName;
	private String profileId;
	private String profilePerCompleted;
	private boolean isAuthSuccess;	
	private String userConfirmed;
	
	public String getUserConfirmed() {
		return userConfirmed;
	}
	public void setUserConfirmed(String userConfirmed) {
		this.userConfirmed = userConfirmed;
	}
	public boolean isAuthSuccess() {
		return isAuthSuccess;
	}
	public void setAuthSuccess(boolean isAuthSuccess) {
		this.isAuthSuccess = isAuthSuccess;
	}		
	
	public String getIdToken() {
		return idToken;
	}
	public void setIdToken(String idToken) {
		this.idToken = idToken;
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
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}