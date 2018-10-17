package com.thirumanam.model;

public class SearchCriteria {

	private int ageGreater;
	private int ageLess;
	private String gender;	
	private String maritalStatus;
	private String education;
	private String city;
	private String state;
	private String country;
	
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	
	public int getAgeGreater() {
		return ageGreater;
	}
	public void setAgeGreater(int ageGreater) {
		this.ageGreater = ageGreater;
	}
	public int getAgeLess() {
		return ageLess;
	}
	public void setAgeLess(int ageLess) {
		this.ageLess = ageLess;
	}
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
}