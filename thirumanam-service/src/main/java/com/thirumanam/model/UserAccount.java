package com.thirumanam.model;

public class UserAccount {
	
	private String accessToken;
	private String phoneNumber;
	private String phCountryCode;	
	private String email;
	private String profileId;
	private String status;
	private int inactiveDays;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getInactiveDays() {
		return inactiveDays;
	}
	public void setInactiveDays(int inactiveDays) {
		this.inactiveDays = inactiveDays;
	}
	public String getPhCountryCode() {
		return phCountryCode;
	}
	public void setPhCountryCode(String phCountryCode) {
		this.phCountryCode = phCountryCode;
	}
	
	public String getProfileId() {
		return profileId;
	}
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}	
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}