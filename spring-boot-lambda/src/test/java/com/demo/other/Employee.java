package com.demo.other;

import lombok.Data;

/**
 * @author mifei
 * @create 2021-03-25 13:38
 **/
@Data
public class Employee {
	private String name;
	private int age;
	private double salary;

	public Employee() {
	}

	public Employee(String name, int age,double salary) {
		super();
		this.name = name;
		this.age = age;
		this.salary = salary;
	}

}
