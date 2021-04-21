package com.demo.service;

import demo.service.TestService;
import org.apache.dubbo.config.annotation.Service;
/**
 * @author mifei
 * @create 2021-01-13 15:29
 **/
@Service(version = "1.0.0",interfaceClass = TestService.class)
public class TestServiceImpl implements TestService {
	@Override
	public void ins() {
		System.out.println("insert");
	}

	@Override
	public void del() {
		System.out.println("delete");
	}

	@Override
	public void upd() {
		System.out.println("update");
	}

	@Override
	public void sel() {
		System.out.println("select");
	}
}
