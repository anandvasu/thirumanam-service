package com.thirumanam.model;

public class Notification {
	
	private String id;
	private int productPromotion;	
	private int weeklyMatch;	
	private int dailyMatch;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getDailyMatch() {
		return dailyMatch;
	}
	public void setDailyMatch(int dailyMatch) {
		this.dailyMatch = dailyMatch;
	}
	public int getProductPromotion() {
		return productPromotion;
	}
	public void setProductPromotion(int productPromotion) {
		this.productPromotion = productPromotion;
	}
	public int getWeeklyMatch() {
		return weeklyMatch;
	}
	public void setWeeklyMatch(int weeklyMatch) {
		this.weeklyMatch = weeklyMatch;
	}
}