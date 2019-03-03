package com.thirumanam.aws;

public class AWSLoginResponse {

	private String idToken;
	private String refreshToken;
	private String accessToken;
	private String externalId;
	private String userConfirmed;
	
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public String getUserConfirmed() {
		return userConfirmed;
	}
	public void setUserConfirmed(String userConfirmed) {
		this.userConfirmed = userConfirmed;
	}
	public String getIdToken() {
		return idToken;
	}
	public void setIdToken(String idToken) {
		this.idToken = idToken;
	}	
	
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public String getExternalId() {
		return externalId;
	}
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
}