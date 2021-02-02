package com.demo.service;

import org.springframework.stereotype.Service;

/**
 * @author mifei
 * @create 2020-07-30 16:25
 **/
@Service("userService")
public class UserServiceImpl implements UserService {

	private String name;

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 测试方法
	 */
	@Override
	public void test() {
		System.out.println("hello user :" + this.name);
	}


}
