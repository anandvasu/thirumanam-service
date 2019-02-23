package com.thirumanam.model;

import java.util.ArrayList;
import java.util.List;

public class BlockedProfiles {

	private String id;
	private List<BlockedProfile> profiles = new ArrayList<BlockedProfile>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<BlockedProfile> getProfiles() {
		return profiles;
	}
	public void setProfiles(List<BlockedProfile> profiles) {
		this.profiles = profiles;
	}
}