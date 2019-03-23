package com.thirumanam.controller;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thirumanam.model.Message;
import com.thirumanam.model.MessageList;
import com.thirumanam.model.MessageSummary;
import com.thirumanam.model.Status;
import com.thirumanam.mongodb.repository.MessageRepository;
import com.thirumanam.util.ThirumanamConstant;

@RestController
@RequestMapping("/matrimony/message")
public class MessageController {
	
	@Autowired
	private MessageRepository messageRepository;	
	
	@GetMapping("/{userId}/summary")
	public ResponseEntity<MessageSummary> getMessageSummary(
			@PathVariable("userId") String userId) throws URISyntaxException {
		MessageSummary messageSummary = new MessageSummary();
		Optional<MessageList> messageListObj = messageRepository.findById(userId);
		if(messageListObj.isPresent()) {
			messageSummary.setInboxCount(messageListObj.get().getInbox().size());
			messageSummary.setInboxCount(messageListObj.get().getSentItems().size());
		}
		return ResponseEntity.ok().body(messageSummary);
	}
	
	@GetMapping("/{userId}/inbox")
	public ResponseEntity<List<Message>> getInboxMessages(
			@PathVariable("userId") String userId,
			@QueryParam("status") String status) throws URISyntaxException {
		List<Message> messagesList = new ArrayList<Message>();
		Optional<MessageList> messageListObj = messageRepository.findById(userId);
		if(messageListObj.isPresent()) {
			List<Message> messages = messageListObj.get().getInbox();
			if(status == null) {
				messagesList = messages;
			} else {
				for(Message message:messages) {
					if(message.getStatus().equals(status)) {
						messagesList.add(message);
					}
				}
			}
		}
		return ResponseEntity.ok().body(messagesList);
	}
	
	@GetMapping("/{userId}/sentItems")
	public ResponseEntity<List<Message>> getSentItems(
			@PathVariable("userId") String userId,
			@QueryParam("status") String status) throws URISyntaxException {
		List<Message> messagesList = new ArrayList<Message>();
		Optional<MessageList> messageListObj = messageRepository.findById(userId);
		if(messageListObj.isPresent()) {
			List<Message> messages = messageListObj.get().getInbox();
			if(status == null) {
				messagesList = messages;
			} else {
				for(Message message:messages) {
					if(message.getStatus().equals(status)) {
						messagesList.add(message);
					}
				}
			}
		}
		return ResponseEntity.ok().body(messagesList);		
	}
	
	@PostMapping("/{userId}")
	public ResponseEntity<Status> sendMessage(
			@RequestBody Message message, 
			@PathVariable("userId") String fromUserId) {
		//Save To Sent Items
		Optional<MessageList> messageListObj = messageRepository.findById(fromUserId);
		MessageList messageList = null;
		if(messageListObj.isPresent()) {
			messageList = messageListObj.get();
		} else {
			messageList = new MessageList();
			messageList.setId(fromUserId);
		}		
		message.setDate(new Date());
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
		message.setDate(new Date());
		message.setStatus(ThirumanamConstant.MESSAGE_STATUS_PENDING);
		messageList.getSentItems().add(message);
		messageRepository.save(messageList);		
		
		return ResponseEntity.noContent().build();	
	}	
}