package com.thirumanam.model;

public class User {
	
	private String id;	
	private String firstName;
	private String lastName;	
	private int age;	
	private int bDay;
	private int bMonth;
	private int bYear;
	private String email;
	private String password;
	private String gender;
	private String registerBy;
	private String registerByName;
	private String dob;
	private String profileCompPercent;		
	private String country;
	private String pstate;	
	private String district;		
	private String city;
	
	private String mStatus;	
	private String familyType;
	private String familyValue;
	private String foodHabit;	
	private String disabled;	
	private String disInfo;
	private String bodyType;	
	private int height;
	private int weight;
	
	private String education;
	private String employment;
	private String income;
	
	private String mCountryCode;
	private String mobile;		
	
	private String status;	
	private String image;
	private String externalId;	
	private boolean isFP;
	
	
	public void setProfileCompPercent(String profileCompPercent) {			
		this.profileCompPercent = profileCompPercent;
	}
	
	public String getProfileCompPercent() {		
		return profileCompPercent;
	}
	
	public boolean isFP() {
		return isFP;
	}
	public void setFP(boolean isFP) {
		this.isFP = isFP;
	}
	
	public String getBodyType() {
		return bodyType;
	}
	public void setBodyType(String bodyType) {
		this.bodyType = bodyType;
	}
	
	public String getFoodHabit() {
		return foodHabit;
	}
	public void setFoodHabit(String foodHabit) {
		this.foodHabit = foodHabit;
	}
	
	public String getExternalId() {
		return externalId;
	}
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
	
	public String getPstate() {
		return pstate;
	}
	public void setPstate(String pstate) {
		this.pstate = pstate;
	}
	
	public String getmStatus() {
		return mStatus;
	}
	public void setmStatus(String mStatus) {
		this.mStatus = mStatus;
	}
	public String getFamilyType() {
		return familyType;
	}
	public void setFamilyType(String familyType) {
		this.familyType = familyType;
	}
	public String getFamilyValue() {
		return familyValue;
	}
	public void setFamilyValue(String familyValue) {
		this.familyValue = familyValue;
	}	
	public String getDisabled() {
		return disabled;
	}
	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}
	public String getDisInfo() {
		return disInfo;
	}
	public void setDisInfo(String disInfo) {
		this.disInfo = disInfo;
	}
	public String getEmployment() {
		return employment;
	}
	public void setEmployment(String employment) {
		this.employment = employment;
	}
	public String getIncome() {
		return income;
	}
	public void setIncome(String income) {
		this.income = income;
	}	
	
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
		
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getmCountryCode() {
		return mCountryCode;
	}
	public void setmCountryCode(String mCountryCode) {
		this.mCountryCode = mCountryCode;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getRegisterBy() {
		return registerBy;
	}
	public void setRegisterBy(String registerBy) {
		this.registerBy = registerBy;
	}
	public String getRegisterByName() {
		return registerByName;
	}
	public void setRegisterByName(String registerByName) {
		this.registerByName = registerByName;
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
}