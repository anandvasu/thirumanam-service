package com.thirumanam.controller;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.thirumanam.model.About;
import com.thirumanam.model.Message;
import com.thirumanam.model.MessageList;
import com.thirumanam.model.Status;
import com.thirumanam.model.User;
import com.thirumanam.model.UserInfo;
import com.thirumanam.model.VisitedProfiles;
import com.thirumanam.model.Visitor;
import com.thirumanam.mongodb.repository.MessageRepository;
import com.thirumanam.mongodb.repository.PreferenceRepository;
import com.thirumanam.mongodb.repository.UserAdditionalDetailRepository;
import com.thirumanam.mongodb.repository.UserRepository;
import com.thirumanam.mongodb.repository.VisitedProfileRepository;
import com.thirumanam.util.ErrorMessageConstants;
import com.thirumanam.util.ThirumanamConstant;
import com.thirumanam.util.Util;

@RestController
@RequestMapping("/matrimony/user")
public class UserController {
	
	@Autowired
	private MessageRepository messageRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	PreferenceRepository prefRepository;
	
	@Autowired
	private VisitedProfileRepository visitedProfileRepository;
	
	@Autowired
	private UserAdditionalDetailRepository userAdditionalDetailRepository;
	
	@RequestMapping(value = "/{profileId}", method = RequestMethod.GET)
	public ResponseEntity getUser(@PathVariable("profileId") String profileId, @RequestParam("userId") String loggedInUserId) {
		Optional<User> userObj = userRepository.findById(profileId);
		User user = null;
		if(userObj.isPresent()) {
			user = userObj.get();
			user.setThumbImage(null);	
			boolean allowPhoto = false;
			if(user.getProtectImage()) {	
				Optional<MessageList> messageListObj = messageRepository.findById(loggedInUserId);
				if(messageListObj.isPresent()) {
					MessageList messageList = messageListObj.get();
					List<Message> sentItems = messageList.getSentItems();
					for(Message message:sentItems) {
						if(message.getPartnerMatrimonyId().equals(profileId) &&
								message.getStatus().equals(ThirumanamConstant.MESSAGE_STATUS_ACCEPTED)) {
							allowPhoto = true;
							user.setProtectImage(false);
							break;
						}
					}
				}	
			} else {
				allowPhoto = true;
			}
			
			if(allowPhoto) {
				Optional<UserInfo> userAddtionalDetailObj = userAdditionalDetailRepository.findById(profileId);
				if(userAddtionalDetailObj.isPresent()) {
					UserInfo userAdditional = userAddtionalDetailObj.get();
					user.setImage(userAdditional.getImage());
				}
			}
			
			Optional<VisitedProfiles> vProfiles = visitedProfileRepository.findById(profileId);
			VisitedProfiles visitedProfile = null;
			if(vProfiles.isPresent()) {
				visitedProfile = vProfiles.get();
				List<Visitor> vistoryList = visitedProfile.getProfiles();
				boolean isUserAlreadyExists = false;
				for(Visitor visitor: vistoryList) {
					if(visitor.getId().equals(loggedInUserId)) {
						isUserAlreadyExists = true;
						break;
					}
				}
				if(!isUserAlreadyExists) {
					Visitor visitor = new Visitor();
					visitor.setId(loggedInUserId);
					visitor.setVisitedDate(new Date());
					visitedProfile.getProfiles().add(0, visitor);
					visitedProfileRepository.save(visitedProfile);
				}
			} else {
				visitedProfile = new VisitedProfiles();			
				visitedProfile.setId(profileId);
				Visitor visitor = new Visitor();
				visitor.setId(loggedInUserId);
				visitor.setVisitedDate(new Date());
				visitedProfile.getProfiles().add(0, visitor);
				visitedProfileRepository.save(visitedProfile);
				
			}		
			
			//Check Interest Sent already or not
			Optional<MessageList> messageListObj = messageRepository.findById(loggedInUserId);
			if(messageListObj.isPresent()) {
				MessageList messageList = messageListObj.get();
				List<Message> messages = messageList.getSentItems();
				for(Message message: messages) {
					if(profileId.equals(message.getPartnerMatrimonyId())) {
						user.setInterestSent(true);
						break;
					}
				}
			}
			return ResponseEntity.ok().body(user);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					Util.populateStatus(ErrorMessageConstants.CODE_BAD_REQUEST,
							ErrorMessageConstants.BAD_REQUEST_ID_NOT_EXIST));
		}		
	}

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
	
	@PutMapping("/profile/about")
	public ResponseEntity<Status> updateAbout(@RequestBody About about) throws URISyntaxException {		
		Optional<User> userObj = userRepository.findById(about.getProfileId());
		User user = userObj.get();			
		if(!user.isConfirmed() ) {
			user.setAbout(about.getAbout());								
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
			user.setOccupation(inputUser.getOccupation());
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
	
	@PutMapping("/profile")
	public ResponseEntity<Status> updateProfile(
			@RequestBody User inputUser) throws URISyntaxException {
		
		Optional<User> userObj = userRepository.findById(inputUser.getId());
		if(userObj.isPresent()) {
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
				
				user.setCaste(inputUser.getCaste());
				user.setSubcaste(inputUser.getSubcaste());
				user.setGothram(inputUser.getGothram());
				user.setDhosham(inputUser.getDhosham());	
				
				user.setCountry(inputUser.getCountry());
				user.setPstate(inputUser.getPstate());
				user.setCity(inputUser.getCity());
				user.setDistrict(inputUser.getDistrict());
				
				user.setEducation(inputUser.getEducation());
				user.setOccupation(inputUser.getOccupation());
				user.setEmployment(inputUser.getEmployment());
				user.setIncome(inputUser.getIncome());	
				
				user.setAbout(inputUser.getAbout());	
				
				userRepository.save(user);
				return ResponseEntity.noContent().build();	
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
						Util.populateStatus(ErrorMessageConstants.CODE_UNAUTHORIZED,
								ErrorMessageConstants.MESSAGE_UNAUTHORIZED));
			}	
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
					Util.populateStatus(ErrorMessageConstants.CODE_UNAUTHORIZED,
							ErrorMessageConstants.MESSAGE_UNAUTHORIZED));
		}
	}
	
	@PutMapping("/completeprofile")
	public ResponseEntity<Status> updateCompleteProfile(@RequestBody User inputUser, 
			@RequestAttribute(ThirumanamConstant.USER_AUTHORIZED) boolean isUserAuthorized) throws URISyntaxException {
		
		System.out.print("isUserAuthorized:" + isUserAuthorized);
		
		Optional<User> userObj = userRepository.findById(inputUser.getId());
		User user = userObj.get();
		user.setFirstName(inputUser.getFirstName());
		user.setLastName(inputUser.getLastName());		
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
		String[] data = inputUser.getDob().split("/");
		LocalDate birthday = LocalDate.of(Integer.parseInt(data[2]), Integer.parseInt(data[1]), Integer.parseInt(data[0]));
		user.setbDay(Integer.parseInt(data[0]));
		user.setbMonth(Integer.parseInt(data[1]));
		user.setbYear(Integer.parseInt(data[2]));
		user.setAge(Period.between(birthday, LocalDate.now()).getYears());
		
		//Update Religion Detail
		user.setReligion(inputUser.getReligion());
		user.setCaste(inputUser.getCaste());
		user.setOtherCaste(inputUser.getOtherCaste());
		user.setSubcaste(inputUser.getSubcaste());
		user.setGothram(inputUser.getGothram());
		user.setDhosham(inputUser.getDhosham());
		
		//Update Location Detail
		user.setCountry(inputUser.getCountry());
		user.setPstate(inputUser.getPstate());
		user.setOtherState(inputUser.getOtherState());
		user.setCity(inputUser.getCity());
		user.setDistrict(inputUser.getDistrict());
		user.setOtherDistrict(inputUser.getOtherDistrict());
				
		//Update Education Detail
		user.setEducation(inputUser.getEducation());
		user.setOccupation(inputUser.getOccupation());
		user.setEmployment(inputUser.getEmployment());
		user.setIncome(inputUser.getIncome());		
		
		//about detail
		user.setAbout(inputUser.getAbout());
				
		userRepository.save(user);
		
		return ResponseEntity.noContent().build();	
	}
			
	@PostMapping("/image")
	public ResponseEntity<Status> uploadProfileImage(@RequestParam("imageFile") MultipartFile imageFile, 
			@RequestParam("profileId") String profileId,
			@RequestParam("protectImage") boolean protectImage) {
		
		try {
			
			Optional<User> userObj = userRepository.findById(profileId);
			if (userObj.isPresent()) {
				User user = userObj.get();				
				InputStream inStream = imageFile.getInputStream();	
				byte[] imageArray = IOUtils.toByteArray(inStream);
				
				//save big image into user additional detail.
				Optional<UserInfo> userAddDetailObj = userAdditionalDetailRepository.findById(profileId);
				UserInfo userAddDetail = null;
				if(userAddDetailObj.isPresent()) {
					userAddDetail = userAddDetailObj.get();
					userAddDetail.setImage(Base64.getEncoder().encodeToString(imageArray));
					userAdditionalDetailRepository.save(userAddDetail);
				} else {
					userAddDetail = new UserInfo();
					userAddDetail.setId(profileId);
				}
				
				userAddDetail.setImage(Base64.getEncoder().encodeToString(imageArray));
				userAdditionalDetailRepository.save(userAddDetail);
				
				//compressImage
				ByteArrayInputStream byteArrInputStream = new ByteArrayInputStream(imageArray);
				BufferedImage image = ImageIO.read(byteArrInputStream);
				BufferedImage compressedImage = resize(image, 60, 60);
				File outputFile = new File(user.getId()+ThirumanamConstant.IMAGE_PNG_WITH_DOT);
				ImageIO.write(compressedImage, ThirumanamConstant.IMAGE_PNG, outputFile);
				InputStream compImageInputStream = new FileInputStream(outputFile);
				user.setThumbImage(Base64.getEncoder().encodeToString(IOUtils.toByteArray(compImageInputStream)));
				user.setProtectImage(protectImage);
				userRepository.save(user);		
				compImageInputStream.close();
				byteArrInputStream.close();
				inStream.close();
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
						Util.populateStatus(ErrorMessageConstants.CODE_BAD_REQUEST,
								ErrorMessageConstants.BAD_REQUEST_ID_NOT_EXIST));
			}
			
		} catch (Exception exp) {
			exp.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					Util.populateStatus(ErrorMessageConstants.CODE_SERVER_ERROR,
							ErrorMessageConstants.MESSAGE_SERVER_ERROR));
			
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