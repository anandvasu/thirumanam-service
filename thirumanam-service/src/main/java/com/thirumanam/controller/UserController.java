package com.thirumanam.controller;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.thirumanam.model.SearchCriteria;
import com.thirumanam.model.Status;
import com.thirumanam.model.User;
import com.thirumanam.mongodb.repository.PreferenceRepository;
import com.thirumanam.mongodb.repository.UserRepository;
import com.thirumanam.mongodb.repository.UserRepositoryImpl;
import com.thirumanam.util.ErrorMessageConstants;
import com.thirumanam.util.ThirumanamConstant;
import com.thirumanam.util.Util;

@RestController
@RequestMapping("/matrimony/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	PreferenceRepository prefRepository;
	
	@Autowired
	private UserRepositoryImpl userRepositoryImpl;
	
	@PostMapping("/profile/personal")
	public ResponseEntity<Status> createPersonalDetail(
			@RequestBody User inputUser) throws URISyntaxException {
		
		Optional<User> userObj = userRepository.findById(inputUser.getId());
		User user = userObj.get();
		if(!user.isConfirmed() ) {
			user.setmStatus(inputUser.getmStatus());
			user.setWeight(inputUser.getWeight());
			user.setHeightCm(inputUser.getHeightCm());
			user.setHeightInch(inputUser.getHeightInch());
			user.setFamilyType(inputUser.getFamilyType());
			user.setFamilyValue(inputUser.getFamilyValue());
			user.setBodyType(inputUser.getBodyType());
			user.setDisabled(inputUser.getDisabled());
			user.setFoodHabit(inputUser.getFoodHabit());
			user.setDisablityInfo(inputUser.getDisablityInfo());				
			userRepository.save(user);
			return ResponseEntity.noContent().build();	
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
					Util.populateStatus(ErrorMessageConstants.CODE_UNAUTHORIZED,
							ErrorMessageConstants.MESSAGE_UNAUTHORIZED));
		}		
	}
	
	@PutMapping("/profile/personal")
	public ResponseEntity<Status> updatePersonalDetail(@RequestBody User inputUser) throws URISyntaxException {
		
		Optional<User> userObj = userRepository.findById(inputUser.getId());
		User user = userObj.get();
		if(!user.isConfirmed() ) {
			user.setmStatus(inputUser.getmStatus());
			user.setWeight(inputUser.getWeight());
			user.setHeightCm(inputUser.getHeightCm());
			user.setHeightInch(inputUser.getHeightInch());
			user.setFamilyType(inputUser.getFamilyType());
			user.setFamilyValue(inputUser.getFamilyValue());
			user.setBodyType(inputUser.getBodyType());
			user.setDisabled(inputUser.getDisabled());
			user.setFoodHabit(inputUser.getFoodHabit());
			user.setDisablityInfo(inputUser.getDisablityInfo());	
			userRepository.save(user);
			return ResponseEntity.noContent().build();	
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
					Util.populateStatus(ErrorMessageConstants.CODE_UNAUTHORIZED,
							ErrorMessageConstants.MESSAGE_UNAUTHORIZED));
		}
	}
	
	@PutMapping("/profile/religion")
	public ResponseEntity<Status> updateReligionDetail(@RequestBody User inputUser) throws URISyntaxException {
		
		Optional<User> userObj = userRepository.findById(inputUser.getId());
		User user = userObj.get();		
		if(!user.isConfirmed() ) {
			user.setCaste(inputUser.getCaste());
			user.setSubcaste(inputUser.getSubcaste());
			user.setGothram(inputUser.getGothram());
			user.setDhosham(inputUser.getDhosham());	
			userRepository.save(user);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
					Util.populateStatus(ErrorMessageConstants.CODE_UNAUTHORIZED,
							ErrorMessageConstants.MESSAGE_UNAUTHORIZED));
		}		
	}
	
	@PutMapping("/profile/location")
	public ResponseEntity<Status> updateLocationDetail(@RequestBody User inputUser) throws URISyntaxException {		
		Optional<User> userObj = userRepository.findById(inputUser.getId());
		User user = userObj.get();	
		if(!user.isConfirmed() ) {
			user.setCountry(inputUser.getCountry());
			user.setPstate(inputUser.getPstate());
			user.setCity(inputUser.getCity());
			user.setDistrict(inputUser.getDistrict());	
			userRepository.save(user);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
					Util.populateStatus(ErrorMessageConstants.CODE_UNAUTHORIZED,
							ErrorMessageConstants.MESSAGE_UNAUTHORIZED));
		}
	}
	
	@PutMapping("/profile/professional")
	public ResponseEntity<Status> updateProfessionalDetail(@RequestBody User inputUser) throws URISyntaxException {		
		Optional<User> userObj = userRepository.findById(inputUser.getId());
		User user = userObj.get();			
		if(!user.isConfirmed() ) {
			user.setEducation(inputUser.getEducation());
			user.setEmployment(inputUser.getEmployment());
			user.setIncome(inputUser.getIncome());						
			userRepository.save(user);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
					Util.populateStatus(ErrorMessageConstants.CODE_UNAUTHORIZED,
							ErrorMessageConstants.MESSAGE_UNAUTHORIZED));
		}
	}	
	
	/*@PutMapping("/profile")
	public ResponseEntity<Status> updateProfile(
			@RequestBody User inputUser) throws URISyntaxException {
		
		Optional<User> userObj = userRepository.findById(inputUser.getId());
		User user = userObj.get();
		
		user.setCountry(inputUser.getCountry());
		user.setPstate(inputUser.getPstate());
		user.setCity(inputUser.getCity());
		user.setDistrict(inputUser.getDistrict());
		
		user.setmStatus(inputUser.getmStatus());
		user.setWeight(inputUser.getWeight());
		user.setHeightCm(inputUser.getHeightCm());
		user.setHeightInch(inputUser.getHeightInch());
		user.setFamilyType(inputUser.getFamilyType());
		user.setFamilyValue(inputUser.getFamilyValue());
		user.setBodyType(inputUser.getBodyType());
		user.setDisabled(inputUser.getDisabled());
		user.setFoodHabit(inputUser.getFoodHabit());
		user.setDisablityInfo(inputUser.getDisablityInfo());
				
		user.setEducation(inputUser.getEducation());
		user.setEmployment(inputUser.getEmployment());
		user.setIncome(inputUser.getIncome());		
				
		userRepository.save(user);
		
		return ResponseEntity.noContent().build();	
	}*/
	
	@PutMapping("/completeprofile")
	public ResponseEntity<Status> updateCompleteProfile(@RequestBody User inputUser, 
			@RequestAttribute(ThirumanamConstant.USER_AUTHORIZED) boolean isUserAuthorized) throws URISyntaxException {
		
		System.out.print("isUserAuthorized:" + isUserAuthorized);
		
		Optional<User> userObj = userRepository.findById(inputUser.getId());
		User user = userObj.get();
		user.setFirstName(inputUser.getFirstName());
		user.setLastName(inputUser.getLastName());
		user.setbDay(inputUser.getbDay());
		user.setbMonth(inputUser.getbMonth());
		user.setbYear(inputUser.getbYear());
		user.setmStatus(inputUser.getmStatus());
		user.setWeight(inputUser.getWeight());
		user.setHeightCm(inputUser.getHeightCm());
		user.setHeightInch(inputUser.getHeightInch());
		user.setFamilyType(inputUser.getFamilyType());
		user.setFamilyValue(inputUser.getFamilyValue());
		user.setBodyType(inputUser.getBodyType());
		user.setFoodHabit(inputUser.getFoodHabit());
		user.setDisabled(inputUser.getDisabled());
		user.setDisablityInfo(inputUser.getDisablityInfo());
		user.setBodyType(inputUser.getBodyType());		
		user.setCountry(inputUser.getCountry());
		user.setPstate(inputUser.getPstate());
		user.setCity(inputUser.getCity());
		user.setDistrict(inputUser.getDistrict());
				
		user.setEducation(inputUser.getEducation());
		user.setEmployment(inputUser.getEmployment());
		user.setIncome(inputUser.getIncome());		
				
		userRepository.save(user);
		
		return ResponseEntity.noContent().build();	
	}
	
	@PostMapping("/list")
	public ResponseEntity<List<User>> searchUser(@RequestBody SearchCriteria searchCriteria) {
		long totalUsers = searchCriteria.getTotalDocs();
		if(totalUsers == 0) {
			totalUsers = userRepositoryImpl.getSearchCount(searchCriteria);	
		}
		
		int skipnumber = (searchCriteria.getPageNumber() == 1) ? 0 : ((searchCriteria.getPageNumber()-1) * 10);
		int numberOfDocs = (skipnumber +10 < totalUsers) ? 10 : (int)(totalUsers-skipnumber);		
		
		List<User> usersList = userRepositoryImpl.searchUserData(searchCriteria, skipnumber, numberOfDocs);	
						   
		return ResponseEntity.ok()
							 .header("X-TOTAL-DOCS", Long.toString(totalUsers))
							 .body(usersList);
	}
	
	@PostMapping("/image")
	public ResponseEntity<String> uploadProfileImage(@RequestParam("imageFile") MultipartFile imageFile, 
			@RequestParam("profileId") String profileId) {
		try {
			
			Optional<User> userObj = userRepository.findById(profileId);
			User user = userObj.get();
			InputStream inStream = imageFile.getInputStream();	
			byte[] imageArray = IOUtils.toByteArray(inStream);
			user.setImage(Base64.getEncoder().encodeToString(imageArray));
			
			//compressImage
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageArray));
			BufferedImage compressedImage = resize(image, 200, 200);
			File outputFile = new File(user.getId()+".png");
			ImageIO.write(compressedImage, "png", outputFile);
			user.setThumbImage(Base64.getEncoder().encodeToString(IOUtils.toByteArray(new FileInputStream(outputFile))));
			userRepository.save(user);			
			inStream.close();
			
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return ResponseEntity.ok().build();
	}
	
	
	@PostMapping("/horoscope")
	public ResponseEntity<Status> uploadHoroscopeImage(
			@RequestParam("horoscopeImage") MultipartFile horoscopeImage, 
			@RequestParam("profileId") String profileId) {
		try {			
			InputStream inStream = horoscopeImage.getInputStream();
			Optional<User> userObj = userRepository.findById(profileId);
			User user = userObj.get();
			user.setHoroscope(Base64.getEncoder().encodeToString(IOUtils.toByteArray(inStream)));
			if (user.getHoroscope().getBytes().length > 1048576) {
				Status status = Util.populateStatus(420, "Horoscope size greater than 1MB. Please reduce the file size and then upload.");
				return ResponseEntity.badRequest().body(status);
			}
			userRepository.save(user);			
			inStream.close();
			
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return ResponseEntity.ok().build();
	}	
	
	 private static BufferedImage resize(BufferedImage img, int height, int width) {
	        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	        Graphics2D g2d = resized.createGraphics();
	        g2d.drawImage(tmp, 0, 0, null);
	        g2d.dispose();
	        return resized;
	 }
}