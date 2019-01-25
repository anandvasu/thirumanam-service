package com.test.build;

import java.io.File;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

import com.test.Message;
import com.thirumanam.model.User;

public class LoadFeaturedProfiles {
	private String getDateofBirth(int incrNumber) {
		String dob = "12/12/1990";
		return dob;
	}
	
	private User getUser(int incrNumber, int imageInt) {
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
		user.setFP(true);		
		return user;
	}
	
	
	public static void main(String s[]) {
		LoadFeaturedProfiles loadProfiles = new LoadFeaturedProfiles();
		//Client client = ClientBuilder.newClient();
		Client client = ClientBuilder.newBuilder().build();
		WebTarget webTarget 
		  = client.target("http://localhost:8085/");
		
		
		final Client multipartClient = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
		    WebTarget multipartTarget 
			  = multipartClient.target("http://localhost:8085/");
		
		WebTarget employeeWebTarget 
		  = webTarget.path("/thirumanam/user/register");
		Invocation.Builder invocationBuilder 
		  = employeeWebTarget.request(MediaType.APPLICATION_JSON);
		
		int imageInt = 1;
		//PROFILEID
		for(int i=10340; i<10380; i++) {
			Response response 
			  = invocationBuilder.post(Entity.entity(loadProfiles.getUser(i, imageInt), MediaType.APPLICATION_JSON));
		
			Message message = response.readEntity(Message.class);
			
			try {
				String fileName = "image"+imageInt+".jpg";
				File targetFile = new File(fileName);
				
				FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("imageFile", targetFile);
				//FormDataMultiPart multipart = new FormDataMultiPart();
				//multipart.bodyPart(fileDataBodyPart);
				FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
			    final FormDataMultiPart multipart = (FormDataMultiPart) formDataMultiPart.field("profileId", message.getCode()).bodyPart(fileDataBodyPart);
							   			    
				WebTarget imageUploadTarget 
				  = multipartTarget.path("/thirumanam/user/image");
				Invocation.Builder imageUploadInvocationBuilder 
				  = imageUploadTarget.request(MediaType.MULTIPART_FORM_DATA_TYPE);
				
				Response response1 
				  = imageUploadInvocationBuilder.post(Entity.entity(multipart, multipart.getMediaType()));
				System.out.println("Response1 Status" + response1.getStatus());
			} catch (Exception exp) {
				exp.printStackTrace();
			}
			
			imageInt ++;
			if(imageInt == 20) {
				imageInt = 1;
			}
			System.out.println("Response Status" + response.getStatus());
			
		}
	}
}
