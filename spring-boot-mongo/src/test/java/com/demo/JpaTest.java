package com.demo;

import com.demo.dao.UserJpaRepository;
import com.demo.pojo.PushUser;
import com.demo.pojo.User;
import com.demo.service.UserService;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JpaTest {

	@Autowired
	private UserJpaRepository userJpaRepository;

	@Test
	public void testSaveUser() throws Exception {
		Optional<PushUser> byId = userJpaRepository.findById(new ObjectId("5d3a8ef4e4b0f6b26beda8bc"));
		System.out.println(byId.get());

		PushUser pushUser = new PushUser();
		pushUser.setId(new ObjectId("5d3a8ef4e4b0f6b26beda8bc"));
		pushUser.setTerminalId(new ObjectId("5c35a61ce4b03454c38b2a1b"));
		pushUser.setTerminalPort(1L);
		Set<ObjectId> ids = new HashSet();
		ids.add(new ObjectId("5aa733d4e4b0274d66f17e9c"));
		pushUser.setUserIds(ids);
		pushUser.setAppId(new ObjectId("5c0f13c90c879831740123a2"));
		pushUser.setAction(999101L);
		pushUser.setUpdateAt(new Date());

		userJpaRepository.updateWithPullAllUserIdsByAppIdAndTerminalIdAndTerminalPort
				(pushUser,
						new ObjectId("5c0f13c90c879831740123a2"),
						new ObjectId("5c35a61ce4b03454c38b2a1b"), 1L);
	}


}