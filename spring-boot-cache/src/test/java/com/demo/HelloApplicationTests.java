package com.demo;

import com.demo.controller.UserInfo;
import com.demo.service.DemoServiceImpl;
import com.demo.utils.RequestThreadBinder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloApplicationTests {

	@Autowired
	private DemoServiceImpl demoService;
	@Test
	public void test() {
		UserInfo userInfo = new UserInfo();
		userInfo.setComId(1L);
		RequestThreadBinder.bindUser(userInfo);
		demoService.test(12);
	}

	@Test
	public void testCache() {
		List list1 = demoService.getListCacheable();
		List list2 = demoService.getListCacheable();
	}
	@Test
	public void testCachePut() {
		List list1 = demoService.getListCachePut(1);
	}

	@Test
	public void CacheEvict() {
		List list1 = demoService.getListCacheEvict(1);
		List list2 = demoService.getListCacheEvict(1);
	}
}
