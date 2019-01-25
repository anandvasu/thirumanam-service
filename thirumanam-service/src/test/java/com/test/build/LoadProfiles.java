package com.test.build;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.thirumanam.model.User;

public class LoadProfiles {
	
	private String getDateofBirth(int incrNumber) {
		String dob = "12/12/1990";
		return dob;
	}
	
	private User getUser(int incrNumber) {
		User user = new User();
		user.setFirstName("Test User First Name"+incrNumber);
		user.setLastName("Test User Last Name"+ incrNumber);
		user.setEmail("test"+incrNumber+"@gmail.com");
		user.setCountry("IND");
		user.setDob(getDateofBirth(incrNumber));
		user.setMobile("9376548612");
		user.setGender((incrNumber % 2 == 0) ? "F" : "M");
		user.setPstate("TN");
		user.setDistrict("VP");
		user.setCity("Villupuram");
		user.setmStatus("NM");
		user.setHeight(165);
		user.setFamilyType("NU");
		user.setFamilyValue("TL");
		user.setFoodHabit("V");
		user.setBodyType("AG");
		user.setEducation("BE");
		user.setEmployment("P");
		user.setIncome("12354544");
		user.setRegisterBy("S");		
		return user;
	}
	
	
	public static void main(String s[]) {
		LoadProfiles loadProfiles = new LoadProfiles();
		//Client client = ClientBuilder.newClient();
		Client client = ClientBuilder.newBuilder().build();
		WebTarget webTarget 
		  = client.target("http://localhost:8085/");
		WebTarget employeeWebTarget 
		  = webTarget.path("/thirumanam/user//register");
		Invocation.Builder invocationBuilder 
		  = employeeWebTarget.request(MediaType.APPLICATION_JSON);		
		
		for(int i=0; i<1000; i++) {
			Response response 
			  = invocationBuilder.post(Entity.entity(loadProfiles.getUser(i), MediaType.APPLICATION_JSON));
			System.out.println("Response Status" + response.getStatus());
		}
	}
}
