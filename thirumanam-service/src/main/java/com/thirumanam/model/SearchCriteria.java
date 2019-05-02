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
	private String income;	
	private boolean pageClick;	
	
	private List<String> bodyTypes = new ArrayList<String>();
	private List<String> showProfile = new ArrayList<String>();	
	
	private List<String> maritalStatus  = new ArrayList<String>();	
	
	private List<Integer> mtongues = new ArrayList<Integer>();	
	
	private List<String> countries  = new ArrayList<String>();
	private List<Integer> states = new ArrayList<Integer>();
	private List<Integer> districts = new ArrayList<Integer>();	
	
	private List<Integer> religions = new ArrayList<Integer>();
	private List<Integer> castes = new ArrayList<Integer>();
	private List<Integer> gothrams = new ArrayList<Integer>();
	private List<String> dhoshams = new ArrayList<String>();
		
	private List<String> educations = new ArrayList<String>();	
	private List<String> employments = new ArrayList<String>();
	private List<String> occupations = new ArrayList<String>();	
	
	private List<String> foodHabits = new ArrayList<String>();	
	private List<String> smokingHabits = new ArrayList<String>();		
	private List<String> drinkingHabits = new ArrayList<String>();	
	
	private List<String> blockedProfiles = new ArrayList<String>();
	
	public boolean isPageClick() {
		return pageClick;
	}
	public void setPageClick(boolean pageClick) {
		this.pageClick = pageClick;
	}
	
	public List<String> getShowProfile() {
		return showProfile;
	}
	public void setShowProfile(List<String> showProfile) {
		this.showProfile = showProfile;
	}
	
	public List<Integer> getMtongues() {
		return mtongues;
	}
	public void setMtongues(List<Integer> mtongues) {
		this.mtongues = mtongues;
	}
	
	public List<String> getSmokingHabits() {
		return smokingHabits;
	}
	public void setSmokingHabits(List<String> smokingHabits) {
		this.smokingHabits = smokingHabits;
	}
	public List<String> getDrinkingHabits() {
		return drinkingHabits;
	}
	public void setDrinkingHabits(List<String> drinkingHabits) {
		this.drinkingHabits = drinkingHabits;
	}
	
	public String getIncome() {
		return income;
	}
	public void setIncome(String income) {
		this.income = income;
	}
	
	public List<String> getOccupations() {
		return occupations;
	}
	public void setOccupations(List<String> occupations) {
		this.occupations = occupations;
	}
	
	public List<String> getEducations() {
		return educations;
	}
	public void setEducations(List<String> educations) {
		this.educations = educations;
	}
	
	public List<Integer> getDistricts() {
		return districts;
	}
	public void setDistricts(List<Integer> districts) {
		this.districts = districts;
	}
	public List<String> getDhoshams() {
		return dhoshams;
	}
	public void setDhoshams(List<String> dhoshams) {
		this.dhoshams = dhoshams;
	}	
		
	public List<String> getBlockedProfiles() {
		return blockedProfiles;
	}
	public void setBlockedProfiles(List<String> blockedProfiles) {
		this.blockedProfiles = blockedProfiles;
	}
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
	public List<Integer> getStates() {
		return states;
	}
	public void setStates(List<Integer> states) {
		this.states = states;
	}
	public List<Integer> getReligions() {
		return religions;
	}
	public void setReligions(List<Integer> religions) {
		this.religions = religions;
	}
	public List<Integer> getCastes() {
		return castes;
	}
	public void setCastes(List<Integer> castes) {
		this.castes = castes;
	}
	public List<Integer> getGothrams() {
		return gothrams;
	}
	public void setGothrams(List<Integer> gothrams) {
		this.gothrams = gothrams;
	}
}