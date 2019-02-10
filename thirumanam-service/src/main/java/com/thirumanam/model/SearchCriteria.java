package com.thirumanam.model;

import java.util.ArrayList;
import java.util.List;

public class SearchCriteria {

	private long totalDocs;	
	private int minHeight;	
	private int maxHeight;
	private int pageNumber;
	private int ageGreater;
	private int ageLess;
	private String gender;	
	private String city;
	private String state;
	private String country;
	private List<String> education  = new ArrayList<String>();	
	private List<String> maritalStatus  = new ArrayList<String>();
	private List<String> foodHabits = new ArrayList<String>();	
	private List<String> countries  = new ArrayList<String>();
	private List<String> states = new ArrayList<String>();
	private List<Integer> religions = new ArrayList<Integer>();
	private List<String> castes = new ArrayList<String>();
	private List<String> gothrams = new ArrayList<String>();
	private List<String> district = new ArrayList<String>();
	private List<String> bodyTypes = new ArrayList<String>();
	private List<String> employments = new ArrayList<String>();
	
	public List<String> getEmployments() {
		return employments;
	}
	public void setEmployments(List<String> employments) {
		this.employments = employments;
	}
	public List<String> getBodyTypes() {
		return bodyTypes;
	}
	public void setBodyTypes(List<String> bodyTypes) {
		this.bodyTypes = bodyTypes;
	}
	public List<String> getDistrict() {
		return district;
	}
	public void setDistrict(List<String> district) {
		this.district = district;
	}
	public long getTotalDocs() {
		return totalDocs;
	}
	public void setTotalDocs(long totalDocs) {
		this.totalDocs = totalDocs;
	}
	public int getMinHeight() {
		return minHeight;
	}
	public void setMinHeight(int minHeight) {
		this.minHeight = minHeight;
	}
	public int getMaxHeight() {
		return maxHeight;
	}
	public void setMaxHeight(int maxHeight) {
		this.maxHeight = maxHeight;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
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
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
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
	public List<String> getEducation() {
		return education;
	}
	public void setEducation(List<String> education) {
		this.education = education;
	}
	public List<String> getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(List<String> maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	public List<String> getFoodHabits() {
		return foodHabits;
	}
	public void setFoodHabits(List<String> foodHabits) {
		this.foodHabits = foodHabits;
	}
	public List<String> getCountries() {
		return countries;
	}
	public void setCountries(List<String> countries) {
		this.countries = countries;
	}
	public List<String> getStates() {
		return states;
	}
	public void setStates(List<String> states) {
		this.states = states;
	}
	public List<Integer> getReligions() {
		return religions;
	}
	public void setReligions(List<Integer> religions) {
		this.religions = religions;
	}
	public List<String> getCastes() {
		return castes;
	}
	public void setCastes(List<String> castes) {
		this.castes = castes;
	}
	public List<String> getGothrams() {
		return gothrams;
	}
	public void setGothrams(List<String> gothrams) {
		this.gothrams = gothrams;
	}
}