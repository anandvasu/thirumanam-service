package com.thirumanam.model;

import java.util.ArrayList;
import java.util.List;

public class Preference {

	private String id;
	private String gender;		
	private int ageFrom;
	private int ageTo;
	private List<String> mStatus;	
	private int minHeight;
	private int maxHeight;
	private List<String> foodHabits = new ArrayList<String>();
	private List<String> countries  = new ArrayList<String>();
	private List<String> states = new ArrayList<String>();
	private List<Integer> religions = new ArrayList<Integer>();
	private List<String> castes = new ArrayList<String>();
	
	public String getGender() {
		return gender;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getAgeFrom() {
		return ageFrom;
	}
	public void setAgeFrom(int ageFrom) {
		this.ageFrom = ageFrom;
	}
	public int getAgeTo() {
		return ageTo;
	}
	public void setAgeTo(int ageTo) {
		this.ageTo = ageTo;
	}
	public List<String> getmStatus() {
		return mStatus;
	}
	public void setmStatus(List<String> mStatus) {
		this.mStatus = mStatus;
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
	public void setGender(String gender) {
		this.gender = gender;
	}	
}