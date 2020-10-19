package com.demo;

import com.alibaba.fastjson.JSON;
import com.demo.utils.JwtUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloApplicationTests {

	@Test
	public void contextLoads() {
		System.out.println("Hello Spring Boot 2.0!");
	}
	@Test
	public void jwtTest() throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("id", "10001");
		map.put("name", "zhangsan");
		String token = JwtUtils.createToken(map);
		System.out.println(token);
		Map<String, String> res = JwtUtils.verifyToken(token);
		System.out.println(JSON.toJSONString(res));
	}
}
