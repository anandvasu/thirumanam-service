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
	private List<LabelValue> education  = new ArrayList<LabelValue>();	
	private List<LabelValue> maritalStatus  = new ArrayList<LabelValue>();
	private List<String> foodHabits = new ArrayList<String>();	
	private List<LabelValue> countries  = new ArrayList<LabelValue>();
	private List<LabelValue> states = new ArrayList<LabelValue>();
	private List<LabelValue> religions = new ArrayList<LabelValue>();
	private List<LabelValue> castes = new ArrayList<LabelValue>();
	private List<LabelValue> gothrams = new ArrayList<LabelValue>();
	
	public List<LabelValue> getEducation() {
		return education;
	}
	public void setEducation(List<LabelValue> education) {
		this.education = education;
	}
	
	public List<LabelValue> getGothrams() {
		return gothrams;
	}
	public void setGothrams(List<LabelValue> gothrams) {
		this.gothrams = gothrams;
	}
	public List<String> getFoodHabits() {
		return foodHabits;
	}
	public void setFoodHabits(List<String> foodHabits) {
		this.foodHabits = foodHabits;
	}
	public List<LabelValue> getCountries() {
		return countries;
	}
	public void setCountries(List<LabelValue> countries) {
		this.countries = countries;
	}
	public List<LabelValue> getStates() {
		return states;
	}
	public void setStates(List<LabelValue> states) {
		this.states = states;
	}
	public List<LabelValue> getReligions() {
		return religions;
	}
	public void setReligions(List<LabelValue> religions) {
		this.religions = religions;
	}
	public List<LabelValue> getCastes() {
		return castes;
	}
	public void setCastes(List<LabelValue> castes) {
		this.castes = castes;
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
	
	public long getTotalDocs() {
		return totalDocs;
	}
	public void setTotalDocs(long totalDocs) {
		this.totalDocs = totalDocs;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}	
	public List<LabelValue> getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(List<LabelValue> maritalStatus) {
		this.maritalStatus = maritalStatus;
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