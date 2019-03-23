package com.thirumanam.model;

public class MessageSummary {
	
	int sentItemsCount;
	int acceptedCount;
	int pendingCount;
	int awitingReplyCount;
	
	public int getAcceptedCount() {
		return acceptedCount;
	}
	public void setAcceptedCount(int acceptedCount) {
		this.acceptedCount = acceptedCount;
	}
	public int getPendingCount() {
		return pendingCount;
	}
	public void setPendingCount(int pendingCount) {
		this.pendingCount = pendingCount;
	}
	public int getAwitingReplyCount() {
		return awitingReplyCount;
	}
	public void setAwitingReplyCount(int awitingReplyCount) {
		this.awitingReplyCount = awitingReplyCount;
	}
	public int getSentItemsCount() {
		return sentItemsCount;
	}
	public void setSentItemsCount(int sentItemsCount) {
		this.sentItemsCount = sentItemsCount;
	}
}