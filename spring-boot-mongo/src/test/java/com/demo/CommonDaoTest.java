package com.demo;

import com.demo.dao.CommonDao;
import com.demo.pojo.User;
import com.demo.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedHashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommonDaoTest {

	@Autowired
	private CommonDao commonDao;

	@Test
	public void update() throws Exception {
		List<LinkedHashMap> apis = commonDao.select();
    }
	/**
	 * 使用 mongoTemplate 的java代码进行聚合查询
	 */
	@Test
	public void aggregationUseMongoTemplate() throws Exception {
		commonDao.aggregationUseMongoTemplate();
	}

	/**
	 * 使用 mongo compass 自动生成的java代码进行聚合查询
	 */
	@Test
	public void aggregationFromCompass() throws Exception {
		commonDao.aggregationFromCompass();
	}
}