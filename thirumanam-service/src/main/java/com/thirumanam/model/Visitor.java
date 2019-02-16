package com.thirumanam.model;

import java.util.Date;

public class Visitor {
	
	private String id;
	private Date visitedDate;
			
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getVisitedDate() {
		return visitedDate;
	}
	public void setVisitedDate(Date visitedDate) {
		this.visitedDate = visitedDate;
	}
}