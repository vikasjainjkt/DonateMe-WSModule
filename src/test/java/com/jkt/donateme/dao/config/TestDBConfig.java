package com.jkt.donateme.dao.config;

import junit.framework.TestCase;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.jkt.donateme.model.User;
import com.mongodb.Mongo;

public class TestDBConfig extends TestCase {

	private MongoTemplate mongoTemplate;

	@SuppressWarnings("deprecation")
	@Override
	protected void setUp() throws Exception {
		mongoTemplate = new MongoTemplate(new Mongo("127.0.0.1", 27017),
				"yourdb");
		mongoTemplate.remove(new Query(), "users");
	}

	public void testMongoDBConfigDataPersist() {
		User user = new User("mkyong", "password123");
		mongoTemplate.save(user);
		Query searchUserQuery = new Query(Criteria.where("username").is(
				"mkyong"));
		User savedUser = mongoTemplate.findOne(searchUserQuery, User.class);
		assertEquals("Username should be equal", user.getUsername(),
				savedUser.getUsername());
		assertEquals("Password Should be equal", user.getPassword(),
				savedUser.getPassword());
	}

	public void testMongoDBConfigRecordNotFound() {
		User user = new User("mkyong", "password123");
		mongoTemplate.save(user);
		Query searchUserQuery = new Query(Criteria.where("username").is(
				"myname"));
		User savedUser = mongoTemplate.findOne(searchUserQuery, User.class);
		assertNull("User should be null", savedUser);

	}

	public void testMongoDBConfigUpdateRecord() {
		User user = new User("mkyong", "password123");
		mongoTemplate.save(user);

		Update update = new Update();
		update.set("password", "newPassword");
		Query searchUserQuery = new Query(Criteria.where("username").is(
				"mkyong"));
		mongoTemplate.updateFirst(searchUserQuery, update, User.class);
		User savedUser = mongoTemplate.findOne(searchUserQuery, User.class);
		assertEquals("Password Should be equal", "newPassword",
				savedUser.getPassword());
	}

	
}
