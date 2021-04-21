package com.demo.service;
import com.demo.entity.TestBean;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

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
 * @time: 2019/9/17
 */
public interface TestService {
	Iterable<TestBean> findAll();

	void save(List<TestBean> list);

	void save(TestBean bean);

	List<TestBean> findByName(String text);

	List<TestBean> findByNameOrDesc(String name,String desc);
}
