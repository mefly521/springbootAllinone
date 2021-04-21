package com.demo;

import com.demo.other.Employee;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class LambdaTests {

	// 语法格式一:无参数,无返回值
	// o-> System.out.print1n("Hello Lambda! ");
	@Test
	public void test1() {
		int num = 0;
		//原来
		Runnable r = new Runnable() {
			@Override
			public void run() {
				System.out.println("Hello Wor1d! " + num);
			}
		};
		//现在
		Runnable r1 = () -> System.out.println("Hello Lambda! ");
		r1.run();
	}

	//语法格式二:有一个参数,并且无返回值
	//()->System.out.print1n(x)
	@Test
	public void test2() {
		Consumer<String> a = e -> System.out.println(e);
		a.accept("test");

	}

	//语法格式四:有两个以上的参数，有返回值，并且 Lambda 体中有多条语句

	// 语法格式五:若 Lambda 体中只有一条语句，return和大括号都可以省略不写
	// Comparator<Integer> com = (x, y) -> Integer. compare(x， y);
	@Test
	public void test3() {
		Comparator<Integer> com = (x, y) -> {
			System.out.println("函数式接口");
			return Integer.compare(x, y);
		};
		int compare = com.compare(1, 2);
		System.out.println(compare);

		// Arrays.asList()
	}

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

		// 需求:获取当前公司中员工年龄大于35的员工信息
		employees.stream()
				.filter(e -> e.getSalary() > 5000)
				.forEach(e -> System.out.println(e));

	}

}
