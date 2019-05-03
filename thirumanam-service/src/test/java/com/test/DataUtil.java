package com.test;

import com.thirumanam.model.RegisterUser;
import com.thirumanam.model.User;

public class DataUtil {
	
	private static String getDateofBirth(int incrNumber) {		
		String dob = "12/12/1990";
		if(incrNumber % 2 == 0 ) {
			dob = "18/02/1995";
		}
		return dob;
	}
	
	public static RegisterUser getRegisterUser(int incrNumber, int imageInt) {
		RegisterUser user = new RegisterUser();
		user.setFirstName("First Name"+incrNumber);
		user.setLastName("Last Name"+ incrNumber);
		user.setEmail("test"+incrNumber+"@gmail.com");
		user.setDob(getDateofBirth(incrNumber));
		user.setMobile("9376548612");
		user.setGender((incrNumber % 2 == 0) ? "F" : "M");
		user.setReligion(1);
		user.setMtongue(250);
		user.setPhCountryCode("+91");
		user.setPhonenumber("9376548612");
		return user;
	}
	
	public static User getFullUserDetail(int incrNumber, String profileId, boolean featureProfile) {
		
		User inputUser = new User();
		inputUser.setId(profileId);
		// Personal Detail
		inputUser.setFirstName("First Name"+incrNumber);
		inputUser.setLastName("Last Name"+ incrNumber);
		inputUser.setDob(getDateofBirth(incrNumber));
		inputUser.setmStatus("NM");
		inputUser.setWeight(80);
		inputUser.setHeightCm(165);
		inputUser.setFamilyType("NU");
		inputUser.setFamilyValue("TL");
		inputUser.setBodyType("SM");
		inputUser.setDisabled("N");
		inputUser.setFoodHabit("V");
		
		// Religion Detail
		inputUser.setReligion(1);
		inputUser.setCaste(2);
		inputUser.setSubcaste("Test Caste");
		inputUser.setGothram(2);
		inputUser.setDhosham("R");
		
		//Set Location Detail
		inputUser.setCountry("IN");
		inputUser.setPstate(58);
		inputUser.setDistrict(31);
		inputUser.setCity("Villupuram");
		
		//Set Professional Detail
		inputUser.setEducation("AE");
		inputUser.setOccupation(30);
		inputUser.setEmployment("P");
		inputUser.setIncome(100);
		inputUser.setFP(featureProfile);	
		
		inputUser.setAbout("Test about bride");
		
		return inputUser;		
	}
}