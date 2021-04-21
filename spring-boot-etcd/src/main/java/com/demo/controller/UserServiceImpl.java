package com.demo.controller;

import com.demo.entity.Notice;
import com.demo.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mifei
 * @create 2020-07-30 16:25
 **/
@Service
public class UserServiceImpl implements UserService {

	/**测试方法
	 * @param c
	 */
	@Override
	public void test(int c){
		new Notice();
		Notice notice = new Notice();
		Notice n = new Notice();
		List list = new ArrayList();


	}
}
