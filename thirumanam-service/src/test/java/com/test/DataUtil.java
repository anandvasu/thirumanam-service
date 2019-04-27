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
		user.setPhCountryCode("+91");
		user.setPhonenumber("9376548612");
		return user;
	}
	
	public static User getPersonDetail(int incrNumber, String profileId) {
		
		User inputUser = new User();
		inputUser.setId(profileId);
		inputUser.setmStatus("NM");
		inputUser.setWeight(80);
		inputUser.setHeightCm(165);
		inputUser.setFamilyType("NU");
		inputUser.setFamilyValue("TL");
		inputUser.setBodyType("SM");
		inputUser.setDisabled("N");
		inputUser.setFoodHabit("V");
		
		return inputUser;		
	}
}