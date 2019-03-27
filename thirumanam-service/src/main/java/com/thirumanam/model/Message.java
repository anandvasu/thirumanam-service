package com.thirumanam.model;

import java.util.Date;

public class Message {
	
	private String id;		
	private String partnerMatrimonyId;
	private Date sentDate;
	private Date responseDate;
	private String status;
	private String readStatus;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public Date getSentDate() {
		return sentDate;
	}
	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}
	public Date getResponseDate() {
		return responseDate;
	}
	public void setResponseDate(Date responseDate) {
		this.responseDate = responseDate;
	}
	
	public String getReadStatus() {
		return readStatus;
	}
	public void setReadStatus(String readStatus) {
		this.readStatus = readStatus;
	}
	public String getPartnerMatrimonyId() {
		return partnerMatrimonyId;
	}
	public void setPartnerMatrimonyId(String partnerMatrimonyId) {
		this.partnerMatrimonyId = partnerMatrimonyId;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}