package com.demo;

import com.demo.entity.R;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 4种内置接口
 */
public class InterfaceTests {
	//Predicate<T>断言型接口:
	@Test
	public void test4() {
		List<String> list = Arrays.asList("Hello", "atguigu", "Lambda", "ww", "ok");
		List<String> strList = filterStr(list, (s) -> s.length() > 3);
		strList.forEach(e -> System.out.println(e));
	}

	//需求:将满足条件的字符串,放入集合中
	public List<String> filterStr(List<String> list, Predicate<String> pre) {
		List<String> strList = new ArrayList<>();
		for (String str : list) {
			if (pre.test(str)) {
				strList.add(str);
			}
		}
		return strList;
	}

	//Function<T, R>函数型接口:
	@Test
	public void test3() {
		String newStr = strHandler("  我大尚硅谷戚武", (str) -> str.substring(0, 5));
		System.out.println(newStr);
	}

	//需求:用于处理字符串
	public String strHandler(String str, Function<String, String> fun) {
		return fun.apply(str);
	}

	//Supplier<T>供给型接口:
	@Test
	public void test2() {
		List<Integer> numList = getNumList(10, () -> (int) (Math.random() * 104));
		for (Integer num : numList) {
			System.out.println(num);
		}
	}

	//需求:产生指定个数的整数,并放入集合中
	public List<Integer> getNumList(int num, Supplier<Integer> sup) {
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < num; i++) {
			Integer n = sup.get();
			list.add(n);
		}
		return list;
	}


	//Consumer 接口 没有返回值
	@Test
	public void test1() {
		happy(10000, (m) -> System.out.println("每次消费:" + m + "元"));
	}

	public void happy(double money, Consumer<Double> con) {
		con.accept(money);
	}


}
