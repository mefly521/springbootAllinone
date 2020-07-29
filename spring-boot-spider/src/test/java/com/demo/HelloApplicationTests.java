package com.demo;

import com.demo.front.entity.News;
import com.demo.front.service.SpilderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloApplicationTests {
	@Autowired
	private SpilderService spilderService;
	@Test
	public void printNews() throws Exception {
		spilderService.printNews();
	}

	@Test
	public void printImage() throws Exception {
		spilderService.printImage();
	}
	@Test
	public void test2() throws Exception {
//		Criteria where = Criteria.where("type").is(1).and("title").is(news.getTitle());
//		Query query = new Query(where);
//
//		List list = mongoTemplate.find(query, News.class, collectionName);
	}
}
