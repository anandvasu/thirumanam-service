package com.thirumanam.model;

public class ThirumanamUtil {
	
	public static String updateProfileCompPercent(User user) {
		int counter = 0;
		counter = (user.getFirstName() != null) ? (counter + 1) : counter; //1
		counter = (user.getLastName() != null) ? (counter + 1) : counter;//2
		counter = (user.getAge() != 0) ? (counter + 1) : counter; //3
		counter = (user.getEmail() != null) ? (counter + 1) : counter; //4
		counter = (user.getGender() != null) ? (counter + 1) : counter; //5
		counter = (user.getRegisterdBy() != null) ? (counter + 1) : counter; //6
		counter = (user.getCountry() != null) ? (counter + 1) : counter; //7
		counter = (user.getPstate() != null) ? (counter + 1) : counter; //8
		counter = (user.getDistrict() != null) ? (counter + 1) : counter; //9
		counter = (user.getCity() != null) ? (counter + 1) : counter; //10
		counter = (user.getmStatus() != null) ? (counter + 1) : counter; //11
		counter = (user.getFamilyType() != null) ? (counter + 1) : counter; //12
		counter = (user.getFamilyValue() != null) ? (counter + 1) : counter; //13
		counter = (user.getFoodHabit() != null) ? (counter + 1) : counter; //14
		counter = (user.getBodyType() != null) ? (counter + 1) : counter; //15
		counter = ((user.getHeightCm() != 0) || (user.getHeightInch() != 0 )) ? (counter + 1) : counter; //16
		counter = (user.getEducation() != null) ? (counter + 1) : counter; //17
		counter = (user.getEmployment() != null) ? (counter + 1) : counter; //18
		counter = (user.getIncome() != null) ? (counter + 1) : counter; //19
		counter = (user.getImage() != null) ? (counter + 1) : counter; //20
		
		double percent = (counter * 100) / 20;
		int percentCeil = (int) Math.ceil(percent);
		return Integer.toString(percentCeil);
	}
}