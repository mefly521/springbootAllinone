package com.demo.service;

import org.springframework.stereotype.Service;

/**
 * @author mifei
 * @create 2020-07-30 16:25
 **/
@Service
public class UserServiceImpl implements UserService {

	private String name;

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 测试方法
	 *
	 * @param c
	 */
	@Override
	public void test(int c) {
		System.out.println("hello " + this.name);
	}


}
