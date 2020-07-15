package com.demo;

import com.demo.front.service.SpilderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloApplicationTests {
	@Autowired
	private SpilderService spilderService;
	@Test
	public void test1() throws Exception {
		spilderService.print();
	}
}
