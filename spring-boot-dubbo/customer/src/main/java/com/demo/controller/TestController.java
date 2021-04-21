package com.demo.controller;

import demo.service.TestService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mifei
 * @create 2021-01-13 16:49
 **/
@RestController
public class TestController {

	@Reference(version = "1.0.0")
	private TestService testServiceImpl;

	@RequestMapping("/ins")
	public String ins(){
		testServiceImpl.ins();
		return "ins";
	}

	@RequestMapping("/del")
	public String del(){
		testServiceImpl.del();
		return "del";
	}

	@RequestMapping("/upd")
	public String upd(){
		testServiceImpl.upd();
		return "upd";
	}

	@RequestMapping("/sel")
	public String sel(){
		testServiceImpl.sel();
		return "sel";
	}
}