package com.demo;

import com.demo.other.Employee;
import org.junit.Test;

import java.util.*;
import java.util.function.Consumer;

public class ListTest {
	// list 过滤
	@Test
	public void test() {
		List<Employee> employees = Arrays.asList(
				new Employee("张三", 18, 9999.99),
				new Employee("李四", 38, 5555.99),
				new Employee("王五", 50, 6666.66),
				new Employee("赵六", 16, 3333.33),
				new Employee("田七", 8, 7777.77)
		);

		Collections.sort(employees, (e1, e2) -> {
			if (e1.getAge() == e2.getAge()) {
				return e1.getName().compareTo(e2.getName());
			} else {
				return Integer.compare(e1.getAge(), e2.getAge());
			}
		});
		System.out.println(employees);
	}

}
