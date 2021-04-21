package com.demo.dao;

import com.demo.entity.TestBean;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author: pyfysf
 * <p>
 * @qq: 337081267
 * <p>
 * @CSDN: http://blog.csdn.net/pyfysf
 * <p>
 * @blog: http://wintp.top
 * <p>
 * @email: pyfysf@163.com
 * <p>
 * 继承CRUD，第一个泛型是实体类类型，第二个泛型是id的类型
 */
public interface TestDao extends CrudRepository<TestBean, Long> {
	List<TestBean> findByName(String name);

	List<TestBean> findByNameOrDesc(String text);
}