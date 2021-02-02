package com.demo;


import com.demo.service.UserServiceImpl;
import com.demo.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HelloApplication {

	public static void main(String[] args) {
		//调用者自己创建对象
		UserService userService = new UserServiceImpl();
		userService.test();

		//初始化SPring容器，加载配置文件
		ApplicationContext appCon = new ClassPathXmlApplicationContext("spring-config.xml");
		//通过容器获得test实例
		UserService tt = (UserService) appCon.getBean("testDIDao");
		tt.test();
	}
}
