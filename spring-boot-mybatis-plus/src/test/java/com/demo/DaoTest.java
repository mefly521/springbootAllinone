package com.demo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.dao.TestMapper;
import com.demo.entity.TestBean;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DaoTest {

	@Autowired
	private TestMapper testMapper;


	/**
	 * 自动读取变更表,生成同步sql ,将测试库的数据同步到生产库
	 */
	@Test
	public void generalAddInsertSql() {
		TestBean testBean = new TestBean();
		testBean.setName("ttt");
		testMapper.insert(testBean);
		testBean.setName(testBean.getId()+"/");
		testMapper.updateById(testBean);
		System.out.println(testBean);
	}

	/**
	 * select
	 * and 拼接
	 */
	@Test
	public void and() {
		TestBean testBean = new TestBean();
		testBean.setName("ttt");
		List<TestBean> testBeans = testMapper.selectList(new QueryWrapper<TestBean>().ne("name", "123").likeRight("name", "abc"));
	}
}
