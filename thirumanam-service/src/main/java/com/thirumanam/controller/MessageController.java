package com.thirumanam.controller;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thirumanam.model.Message;
import com.thirumanam.model.MessageList;
import com.thirumanam.model.MessageSummary;
import com.thirumanam.model.Status;
import com.thirumanam.model.User;
import com.thirumanam.mongodb.repository.MessageRepository;
import com.thirumanam.mongodb.repository.UserRepositoryImpl;
import com.thirumanam.util.ThirumanamConstant;

@RestController
@RequestMapping("/matrimony/message")
public class MessageController {
	
	@Autowired
	private MessageRepository messageRepository;	
	
	@Autowired
	private UserRepositoryImpl userRepositoryImpl;
	
	@GetMapping("/{userId}/summary")
	public ResponseEntity<MessageSummary> getMessageSummary(
			@PathVariable("userId") String userId) throws URISyntaxException {
		MessageSummary messageSummary = new MessageSummary();
		Optional<MessageList> messageListObj = messageRepository.findById(userId);
		int pendingCount = 0;
		int awitingReply = 0;
		int acceptedCount = 0;
		if(messageListObj.isPresent()) {
			List<Message> messages = messageListObj.get().getInbox();			
			for(Message message:messages ) {
				if(message.getStatus().equals(ThirumanamConstant.MESSAGE_STATUS_PENDING)) {
					pendingCount = pendingCount + 1;
				} else if (message.getStatus().equals(ThirumanamConstant.MESSAGE_STATUS_ACCEPTED)) {
					acceptedCount = acceptedCount + 1;
				}
			}
			messages = messageListObj.get().getSentItems();
			for(Message message:messages ) {
				if(message.getStatus().equals(ThirumanamConstant.MESSAGE_STATUS_AWAITING_REPLY)) {
					awitingReply = awitingReply + 1;
				} 
			}
			messageSummary.setSentItemsCount(messageListObj.get().getSentItems().size());
		}
		messageSummary.setAcceptedCount(acceptedCount);
		messageSummary.setAwitingReplyCount(awitingReply);
		messageSummary.setPendingCount(pendingCount);
		
		return ResponseEntity.ok().body(messageSummary);
	}
	
	@GetMapping("/{userId}/inbox")
	public ResponseEntity<List<User>> getInboxMessages(
			@PathVariable("userId") String userId,
			@QueryParam("status") String status,
			@QueryParam("pageNo") int pageNo) throws URISyntaxException {

		int profileCount = 0;
		List<Message> messagesList = new ArrayList<Message>();
		List<User> messageProfiles = new ArrayList<User>();
		Optional<MessageList> messageListObj = messageRepository.findById(userId);
		if(messageListObj.isPresent()) {
			List<Message> messages = messageListObj.get().getInbox();
			List<String> profileIds = new ArrayList<String>();
			if(status == null) {
				for(Message message:messages) {
					profileIds.add(message.getPartnerMatrimonyId());
				}
			} else {
				for(Message message:messages) {
					if(message.getStatus().equals(status)) {
						messagesList.add(message);
						profileIds.add(message.getPartnerMatrimonyId());
					}
				}
			}
			
			int skipCount = (pageNo-1) * 10;
			
			if(!profileIds.isEmpty()) {
				messageProfiles = userRepositoryImpl.findUsersById(profileIds, skipCount, 10);
				profileCount = profileIds.size();
			}
		}
			
		return ResponseEntity.ok()
				 .header("X-TOTAL-DOCS", Integer.toString(profileCount))
				 .body(messageProfiles);
	}
	
	@GetMapping("/{userId}/sentItems")
	public ResponseEntity<List<User>> getSentItems(
			@PathVariable("userId") String userId,
			@QueryParam("status") String status,
			@QueryParam("pageNo") int pageNo) throws URISyntaxException {
		int profileCount = 0;
		List<Message> messagesList = new ArrayList<Message>();
		List<User> messageProfiles = new ArrayList<User>();
		Optional<MessageList> messageListObj = messageRepository.findById(userId);
		if(messageListObj.isPresent()) {
			List<Message> messages = messageListObj.get().getSentItems();	
			List<String> profileIds = new ArrayList<String>();
			if(ThirumanamConstant.MESSAGE_STATUS_ALL.equals(status)) {
				for(Message message:messages) {
					profileIds.add(message.getPartnerMatrimonyId());
				}
			} else {
				for(Message message:messages) {
					if(message.getStatus().equals(status)) {
						messagesList.add(message);
						profileIds.add(message.getPartnerMatrimonyId());
					}
				}
			}
			
			int skipCount = (pageNo-1) * 10;
			
			if(!profileIds.isEmpty()) {
				messageProfiles = userRepositoryImpl.findUsersById(profileIds, skipCount, 10);
				profileCount = profileIds.size();
			}
		}
		return ResponseEntity.ok()
				 .header("X-TOTAL-DOCS", Integer.toString(profileCount))
				 .body(messageProfiles);		
	}
	
	@PostMapping("/{userId}")
	public ResponseEntity<Status> sendMessage(
			@RequestBody Message message, 
			@PathVariable("userId") String fromUserId) {
		
		String id = UUID.randomUUID().toString();
		message.setId(id);
		//Save To Sent Items
		Optional<MessageList> messageListObj = messageRepository.findById(fromUserId);
		MessageList messageList = null;
		if(messageListObj.isPresent()) {
			messageList = messageListObj.get();
		} else {
			messageList = new MessageList();
			messageList.setId(fromUserId);			
		}		
		message.setSentDate(new Date());
		message.setStatus(ThirumanamConstant.MESSAGE_STATUS_AWAITING_REPLY);		
		messageList.getSentItems().add(message);
		messageRepository.save(messageList);
		
		//Save To Inbox
		messageListObj = messageRepository.findById(message.getPartnerMatrimonyId());
		if(messageListObj.isPresent()) {
			messageList = messageListObj.get();
		} else {
			messageList = new MessageList();
			messageList.setId(message.getPartnerMatrimonyId());
		}
		message.setPartnerMatrimonyId(fromUserId);
		message.setSentDate(new Date());
		message.setStatus(ThirumanamConstant.MESSAGE_STATUS_PENDING);
		messageList.getInbox().add(message);
		messageRepository.save(messageList);		
		
		return ResponseEntity.noContent().build();	
	}	
	
	@PutMapping("/{userId}")
	public ResponseEntity<Status> updateMessageStatus(
			@RequestBody Message inputMessage, 
			@PathVariable("userId") String fromUserId) {
		//Save To Inbox
		Optional<MessageList> messageListObj = messageRepository.findById(fromUserId);

		if(messageListObj.isPresent()) {
			List<Message> messages = messageListObj.get().getInbox();
			for(Message message:messages) {
				if(inputMessage.getPartnerMatrimonyId().equals(message.getPartnerMatrimonyId())) {
					message.setStatus(inputMessage.getStatus());
					message.setResponseDate(new Date());
					break;
				}
			}
			messageRepository.save(messageListObj.get());
		} 		
		
		//Save To Inbox
		messageListObj = messageRepository.findById(inputMessage.getPartnerMatrimonyId());

		if(messageListObj.isPresent()) {
			List<Message> messages = messageListObj.get().getSentItems();
			for(Message message:messages) {
				if(message.getPartnerMatrimonyId().equals(fromUserId)) {
					message.setStatus(inputMessage.getStatus());
					message.setResponseDate(new Date());
					break;
				}
			}
			messageRepository.save(messageListObj.get());
		} 		
		return ResponseEntity.noContent().build();	
	}		
}