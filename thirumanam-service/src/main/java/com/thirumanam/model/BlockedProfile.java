package com.thirumanam.model;

import java.util.Date;

public class BlockedProfile {

	private String id;
	private Date blockedDate;	
	
	public Date getBlockedDate() {
		return blockedDate;
	}
	public void setBlockedDate(Date blockedDate) {
		this.blockedDate = blockedDate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}	
}