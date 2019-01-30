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
	private List<LabelValue> countries  = new ArrayList<LabelValue>();
	private List<LabelValue> states = new ArrayList<LabelValue>();
	private List<LabelValue> religions = new ArrayList<LabelValue>();
	private List<LabelValue> castes = new ArrayList<LabelValue>();
	
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public List<String> getmStatus() {
		return mStatus;
	}
	public void setmStatus(List<String> mStatus) {
		this.mStatus = mStatus;
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
}