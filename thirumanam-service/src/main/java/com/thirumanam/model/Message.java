package com.thirumanam.model;

import java.util.Date;

public class Message {
	
	private String partnerMatrimonyId;
	private Date date;
	private String status;
	private String readStatus;
	
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
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}