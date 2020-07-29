package com.demo;

import com.demo.dao.UserRepositoryImpl;
import com.demo.pojo.User;
import com.demo.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDaoTest {

	@Autowired
	private UserService userService;

	@Test
	public void testSaveUser() throws Exception {
        List<User> allUser = userService.getAllUser();
        System.out.println(allUser);
    }

    @Test
    public void add() throws Exception {
        userService.addUser();
        System.out.println(123);
    }


}