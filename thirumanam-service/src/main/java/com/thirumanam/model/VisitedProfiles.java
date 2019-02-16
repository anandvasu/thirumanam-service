package com.thirumanam.model;

import java.util.ArrayList;
import java.util.List;

public class VisitedProfiles {
	
	private String id;
	private List<Visitor> profiles = new ArrayList<Visitor>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<Visitor> getProfiles() {
		return profiles;
	}
	public void setProfiles(List<Visitor> profiles) {
		this.profiles = profiles;
	}
}