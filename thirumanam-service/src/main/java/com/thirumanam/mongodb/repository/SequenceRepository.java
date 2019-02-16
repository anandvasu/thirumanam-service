package com.thirumanam.mongodb.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.thirumanam.model.Sequence;

@Component
public class SequenceRepository implements SequenceCustom {
	
	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public int getNextProfileId() {
		Query query = new Query(Criteria.where("name").is("ProfileId"));
        Update update = new Update().inc("sequence", 1);
        Sequence sequence = mongoTemplate.findAndModify(query, update, Sequence.class); 
        return sequence.getSequence();
	}
}
