package com.thirumanam.model;

public class ForgotPasswordResponse extends Response {

	private boolean limitExceed;
	private String deliveryMedium;
	private String destination;
	
	public boolean isLimitExceed() {
		return limitExceed;
	}

	public void setLimitExceed(boolean limitExceed) {
		this.limitExceed = limitExceed;
	}
	
	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	public String getDeliveryMedium() {
		return deliveryMedium;
	}

	public void setDeliveryMedium(String deliveryMedium) {
		this.deliveryMedium = deliveryMedium;
	}	
}