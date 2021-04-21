package com.demo;

import com.demo.entity.TestBean;
import com.demo.service.TestServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloApplicationTests {
	@Autowired
	private TestServiceImpl testServiceImpl;

	@Test
	public void contextLoads() {

		TestBean testBean = new TestBean();
		testBean.setName("test");
		testServiceImpl.save(testBean);
	}
	@Test
	public void findAll() {
		Iterable<TestBean> list = testServiceImpl.findAll();
	}
}
