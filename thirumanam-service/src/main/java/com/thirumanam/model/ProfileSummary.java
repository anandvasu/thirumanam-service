package com.thirumanam.model;

public class ProfileSummary {

	private String id;	
	private String firstName;
	private String lastName;	
	private int age;	
	private int bDay;
	private int bMonth;
	private int bYear;
	private String gender;
	private String education;
	private String city;
	private String thumbImage;	
	private boolean isInterestSent;
	private boolean isShortlisted;
	private boolean isBlocked;
	
	public String getThumbImage() {
		return thumbImage;
	}
	public void setThumbImage(String thumbImage) {
		this.thumbImage = thumbImage;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getbDay() {
		return bDay;
	}
	public void setbDay(int bDay) {
		this.bDay = bDay;
	}
	public int getbMonth() {
		return bMonth;
	}
	public void setbMonth(int bMonth) {
		this.bMonth = bMonth;
	}
	public int getbYear() {
		return bYear;
	}
	public void setbYear(int bYear) {
		this.bYear = bYear;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public boolean isInterestSent() {
		return isInterestSent;
	}
	public void setInterestSent(boolean isInterestSent) {
		this.isInterestSent = isInterestSent;
	}
	public boolean isShortlisted() {
		return isShortlisted;
	}
	public void setShortlisted(boolean isShortlisted) {
		this.isShortlisted = isShortlisted;
	}
	public boolean isBlocked() {
		return isBlocked;
	}
	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}
}