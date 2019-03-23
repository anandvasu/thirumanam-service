package com.thirumanam.model;

import java.util.ArrayList;
import java.util.List;

public class MessageList {
	
	private String id;
	private List<Message> inbox = new ArrayList<Message>();
	private List<Message> sentItems = new ArrayList<Message>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}	
	
	public List<Message> getInbox() {
		return inbox;
	}
	public void setInbox(List<Message> inbox) {
		this.inbox = inbox;
	}
	public List<Message> getSentItems() {
		return sentItems;
	}
	public void setSentItems(List<Message> sentItems) {
		this.sentItems = sentItems;
	}
}