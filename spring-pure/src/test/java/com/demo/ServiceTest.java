package com.demo;

import com.demo.service.UserMongoServiceImpl;
import com.demo.service.UserService;
import com.demo.service.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author mifei
 * @create 2021-02-01 15:56
 **/

// 配置Spring中的测试环境
@RunWith(SpringJUnit4ClassRunner.class)
// 指定Spring的配置文件路径
@ContextConfiguration(locations = {"classpath*:/spring-config.xml"})
public class ServiceTest {

	// 如果变量名字和xml 中配的不一样, 就必须用Qualifier

	@Autowired
	private UserService userMongoService;

	@Autowired
	private UserService userService;

	@Test
	public void testGet() {
		userService.test();
		userMongoService.test();
	}

}
