package com.thirumanam.model;

import java.util.ArrayList;
import java.util.List;

public class ShortListedProfiles {
	
	private String id;
	private List<ShortListedProfile> profiles = new ArrayList<ShortListedProfile>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<ShortListedProfile> getProfiles() {
		return profiles;
	}
	public void setProfiles(List<ShortListedProfile> profiles) {
		this.profiles = profiles;
	}
}