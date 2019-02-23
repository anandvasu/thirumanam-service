package com.thirumanam.model;

import java.util.Date;

public class ShortListedProfile {
	
	private String id;
	private Date shortlistedDate;
	
	public Date getShortlistedDate() {
		return shortlistedDate;
	}
	public void setShortlistedDate(Date shortlistedDate) {
		this.shortlistedDate = shortlistedDate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}	
}