package com.thirumanam.model;

import java.util.Date;

public class User {
	
	private boolean protectImage;
	private boolean protectHscope;		
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
	private String registerdBy;	
	private String registerByName;
	private String dob;
	private String profileCompPercent;		
	private String country;
	private int pstate;		
	private int district;		
	private String city;	
	private int religion;		
	private int caste;
	private String otherCaste;	
	private String subcaste;	
	private int gothram;			
	private String dhosham;
	private String mStatus;	
	private String familyType;
	private String familyValue;
	private String foodHabit;
	private String drinkingHabit;
	private String smokingHabit;	
	private String disabled;	
	private String disablityInfo;	
	private String bodyType;	
	private int heightInch;	
	private int heightCm;
	private int weight;
	private String horoscope;	
	private String education;
	private String employment;
	private int occupation;		
	private int income;			
	private String mCountryCode;
	private String mobile;			
	private String status;
	private Date activateDate;		
	private String image;
	private String thumbImage;	
	private String externalId;	
	private boolean isFP;	
	private boolean paymentDone;
	private String about;	
	private Date createdDate;
	private boolean confirmed;	
	private boolean isInterestSent;
	private boolean isShortlisted;
	private boolean isBlocked;
	private String phCountryCode;
	private String phonenumber;
	private String otherState;
	private String otherDistrict;
	private int mtongue;
	
	public boolean isProtectHscope() {
		return protectHscope;
	}

	public void setProtectHscope(boolean protectHscope) {
		this.protectHscope = protectHscope;
	}
		
	public boolean getProtectImage() {
		return protectImage;
	}

	public void setProtectImage(boolean protectImage) {
		this.protectImage = protectImage;
	}
	
	public int getIncome() {
		return income;
	}

	public void setIncome(int income) {
		this.income = income;
	}
	
	public int getOccupation() {
		return occupation;
	}

	public void setOccupation(int occupation) {
		this.occupation = occupation;
	}
	
	public int getMtongue() {
		return mtongue;
	}

	public void setMtongue(int mtongue) {
		this.mtongue = mtongue;
	}

	public String getDrinkingHabit() {
		return drinkingHabit;
	}

	public void setDrinkingHabit(String drinkingHabit) {
		this.drinkingHabit = drinkingHabit;
	}

	public String getSmokingHabit() {
		return smokingHabit;
	}

	public void setSmokingHabit(String smokingHabit) {
		this.smokingHabit = smokingHabit;
	}	
		
	public int getGothram() {
		return gothram;
	}

	public void setGothram(int gothram) {
		this.gothram = gothram;
	}
	
	public int getPstate() {
		return pstate;
	}

	public void setPstate(int pstate) {
		this.pstate = pstate;
	}

	public int getDistrict() {
		return district;
	}

	public void setDistrict(int district) {
		this.district = district;
	}

	public int getCaste() {
		return caste;
	}

	public void setCaste(int caste) {
		this.caste = caste;
	}
	
	public String getOtherDistrict() {
		return otherDistrict;
	}

	public void setOtherDistrict(String otherDistrict) {
		this.otherDistrict = otherDistrict;
	}

	public String getOtherState() {
		return otherState;
	}

	public void setOtherState(String otherState) {
		this.otherState = otherState;
	}

	public String getOtherCaste() {
		return otherCaste;
	}

	public void setOtherCaste(String otherCaste) {
		this.otherCaste = otherCaste;
	}
	
	public Date getActivateDate() {
		return activateDate;
	}

	public void setActivateDate(Date activateDate) {
		this.activateDate = activateDate;
	}
	
	public String getPhCountryCode() {
		return phCountryCode;
	}

	public void setPhCountryCode(String phCountryCode) {
		this.phCountryCode = phCountryCode;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
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
		
	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	public String getThumbImage() {
		return thumbImage;
	}

	public void setThumbImage(String thumbImage) {
		this.thumbImage = thumbImage;
	}
		
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getRegisterdBy() {
		return registerdBy;
	}

	public void setRegisterdBy(String registerdBy) {
		this.registerdBy = registerdBy;
	}
		
	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public boolean isPaymentDone() {
		return paymentDone;
	}

	public void setPaymentDone(boolean paymentDone) {
		this.paymentDone = paymentDone;
	}

	public String getHoroscope() {
		return horoscope;
	}

	public void setHoroscope(String horoscope) {
		this.horoscope = horoscope;
	}	
	
	public String getDhosham() {
		return dhosham;
	}

	public void setDhosham(String dhosham) {
		this.dhosham = dhosham;
	}
	
	public String getSubcaste() {
		return subcaste;
	}

	public void setSubcaste(String subcaste) {
		this.subcaste = subcaste;
	}
	
	public String getDisablityInfo() {
		return disablityInfo;
	}

	public void setDisablityInfo(String disablityInfo) {
		this.disablityInfo = disablityInfo;
	}
	
	public int getHeightInch() {
		return heightInch;
	}

	public void setHeightInch(int heightInch) {
		this.heightInch = heightInch;
	}

	public int getHeightCm() {
		return heightCm;
	}

	public void setHeightCm(int heightCm) {
		this.heightCm = heightCm;
	}
	
	
	public int getReligion() {
		return religion;
	}

	public void setReligion(int religion) {
		this.religion = religion;
	}	
		
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
	public String getEmployment() {
		return employment;
	}
	public void setEmployment(String employment) {
		this.employment = employment;
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