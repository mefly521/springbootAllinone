package com.demo.other;

import org.junit.Test;

import java.util.Comparator;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 方法引用:若Lambda 体中的内容有方法已经实现了，我们可以使用"方法引用"
 * 《可以理解为方法引用是 Lambda表达式的另外一种表现形式)
 * 主要有三种语法格式:
 * 对象::实例方法名
 * 类::静态方法名
 * 类::实例方法名
 * <p>
 * Lambda 体中调用方法的参数列表与返回值类型，要与函数式接口中抽象方法的函数列表和返回值类型保持一致!
 * 若Lambda参数列表中的第一参数是实例方法的调用者，而第二个参数是实例方法的参数时，可以使用ClassName :: method
 **/
public class MethodTests {

	//数组引用:
	@Test
	public void test7() {
		Function<Integer, String[]> fun = (x) -> new String[x];
		String[] strs = fun.apply(10);
		System.out.println(strs.length);
		Function<Integer, String[]> fun2 = String[]::new;
		String[] strs2 = fun2.apply(20);
		System.out.println(strs2.length);
	}

	//构造器引用
	@Test
	public void test5() {
		Supplier<Employee> sup = () -> new Employee();
		//构造器引用方式
		Supplier<Employee> sup2 = Employee::new;
		Employee emp = sup2.get();
		System.out.println(emp);
	}


	//类::实例方法名
	@Test
	public void test4() {
		BiPredicate<String, String> bp = (x, y) -> x.equals(y);
		BiPredicate<String, String> bp2 = String::equals;

		boolean test = bp2.test("123", "222");
		System.out.println(test);
	}

	@Test
	public void test() {
		Consumer<String> con = (x) -> System.out.println(x);
		Consumer<String> con1 = System.out::println;
	}

	@Test
	public void test2() {
		Employee emp = new Employee();
		emp.setAge(22);

		Supplier<String> sup = () -> emp.getName();
		String str = sup.get();
		System.out.println(str);
		Supplier<Integer> sup2 = emp::getAge;
		Integer num = sup2.get();
		System.out.println(num);
	}

	//类::静态方法名
	@Test
	public void test3() {
		Comparator<Integer> com = (x, y) -> Integer.compare(x, y);
		Comparator<Integer> com1 = Integer::compare;
	}


}
