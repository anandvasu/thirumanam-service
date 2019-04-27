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

import com.test.DataUtil;

public class LoadFeaturedProfiles {		
	
	public static void main(String s[]) {

		Client client = ClientBuilder.newBuilder().build();
		WebTarget webTarget 
		  = client.target("http://localhost:8085/");
		
		
		final Client multipartClient = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
		    WebTarget multipartTarget 
			  = multipartClient.target("http://localhost:8085/");
		
		WebTarget employeeWebTarget 
		  = webTarget.path("/matrimony/identity/register");
		Invocation.Builder invocationBuilder 
		  = employeeWebTarget.request(MediaType.APPLICATION_JSON);
		
		int imageInt = 1;
		//PROFILEID
		for(int i=10340; i<10380; i++) {
			Response response 
			  = invocationBuilder.post(Entity.entity(DataUtil.getRegisterUser(i, imageInt), MediaType.APPLICATION_JSON));			
			String profileID = (String)response.getHeaders().get("profileid").get(0);
			System.out.println("User Registration Status:" + response.getStatus());
	
			try {
				if (response.getStatus() == 201) {
					//Update Image
					String fileName = "image"+imageInt+".jpg";
					File targetFile = new File(fileName);
					
					FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("imageFile", targetFile);
					FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
				    final FormDataMultiPart multipart = (FormDataMultiPart) formDataMultiPart.field("profileId", profileID).bodyPart(fileDataBodyPart);
								   			    
					WebTarget imageUploadTarget 
					  = multipartTarget.path("/matrimony/user/image");
					Invocation.Builder imageUploadInvocationBuilder 
					  = imageUploadTarget.request(MediaType.MULTIPART_FORM_DATA_TYPE);
					
					response 
					  = imageUploadInvocationBuilder.post(Entity.entity(multipart, multipart.getMediaType()));
					System.out.println("Image Upload Status:" + response.getStatus());
					formDataMultiPart.close();
					//Update Personal Information
					if(response.getStatus() == 200) {						
						WebTarget persontalDetailTarget 
						  = webTarget.path("/matrimony/user/profile/personal");
						Invocation.Builder personalDetInvbuilder 
						  = persontalDetailTarget.request(MediaType.APPLICATION_JSON);
						response 
						  = personalDetInvbuilder.post(Entity.entity(DataUtil.getPersonDetail(i, profileID), MediaType.APPLICATION_JSON));	
						System.out.println("Personal Detail Update Status:" + response.getStatus());
					}
				}
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
