package com.thirumanam.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thirumanam.model.Contact;
import com.thirumanam.model.Status;
import com.thirumanam.mongodb.repository.ContactRepository;
import com.thirumanam.util.Util;

@RestController
@RequestMapping("/matrimony/contact")
public class ContactController {
	
	@Autowired
	private ContactRepository contactRepository;
	
	@PostMapping("/register")
	public ResponseEntity<Status> createContact(@RequestBody Contact contact) throws URISyntaxException {
		contactRepository.save(contact);
		return ResponseEntity.created(new URI("/thirumanam/contact")).body(Util.populateStatus(200, "Contact created successfully."));		
	}
	
	@RequestMapping("/list")
	public ResponseEntity<List<Contact>> getAllContacts() {
		List<Contact> contacts = contactRepository.findAll();
		return ResponseEntity.ok().body(contacts);
	}	
}