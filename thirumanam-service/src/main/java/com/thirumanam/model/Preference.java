package com.thirumanam.model;

public class Preference {

	private String id;
	private SearchCriteria searchCriteria;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public SearchCriteria getSearchCriteria() {
		return searchCriteria;
	}
	public void setSearchCriteria(SearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}
}