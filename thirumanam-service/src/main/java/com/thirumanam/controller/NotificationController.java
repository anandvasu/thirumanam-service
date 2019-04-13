package com.thirumanam.controller;

import java.net.URISyntaxException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thirumanam.model.Notification;
import com.thirumanam.model.Status;
import com.thirumanam.mongodb.repository.NotificationRepository;
import com.thirumanam.util.Util;

@RestController
@RequestMapping("/matrimony/notification/")
public class NotificationController {
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	@PutMapping("/{profileId}")
	public ResponseEntity<Status> updateNotification(
			@PathVariable("profileId") String profileId, @RequestBody Notification inputNotification) throws URISyntaxException {	
		Status status = validateProfileId(profileId);
		if(status == null) {
			Optional<Notification> notificationObj = notificationRepository.findById(profileId);
			Notification notification = null;
			if(notificationObj.isPresent()) {
				if(inputNotification.getDailyMatch() == 0 && 
						inputNotification.getProductPromotion() == 0 && inputNotification.getWeeklyMatch() == 0) {
					notificationRepository.delete(notificationObj.get());
				} else {
					notification = new Notification();					
				}
			} else {
				if(inputNotification.getDailyMatch() == 1 || 
						inputNotification.getProductPromotion() == 1 || inputNotification.getWeeklyMatch() == 1) {
					notification = new Notification();
				}
			}
			if(notification != null) {
				notification.setId(profileId);
				notification.setWeeklyMatch(inputNotification.getWeeklyMatch());
				notification.setDailyMatch(inputNotification.getDailyMatch());
				notification.setProductPromotion(inputNotification.getProductPromotion());
				notificationRepository.save(notification);
			}
		}
		
		return ResponseEntity.ok().body(
				Util.populateStatus(200, "Notification settings updated successfully."));	
	}
	
	@GetMapping("/{profileId}")
	public ResponseEntity<Notification> getNotification(
			@PathVariable("profileId") String profileId) {	
		Status status = validateProfileId(profileId);
		Notification notification = null;
		if(status == null) {
			Optional<Notification> notificationObj = notificationRepository.findById(profileId);
			
			if(notificationObj.isPresent()) {
				notification = notificationObj.get();
			} else {
				notification = new Notification();
			}			
		}		
		return ResponseEntity.ok().body(notification);	
	}
	
	private Status validateProfileId(String profileId) {
		Status status = null;
		if(profileId == null || profileId.isEmpty()) {
			status = Util.populateStatus(400, "ProfileID is required.");
		}
		return status;
	}
}