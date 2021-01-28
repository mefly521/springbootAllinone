package com.demo.controller;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
//通过这个注解，可以不用写gettersetter方法
@Data
//通过这个注解可以声明一个文档，指定其所在的索引库和type
@Document(indexName = "testdoct", type = "testbean")
public class TestBean implements Serializable {
	public TestBean() {
	}

	public TestBean(long id, String name, Integer age, String sex, String desc) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.sex = sex;
		this.desc = desc;
	}

	// 必须指定一个id，
	@Id
	private long id;
	// 这里配置了分词器，字段类型，可以不配置，默认也可
	@Field(analyzer = "ik_smart", type = FieldType.text)
	private String name;
	private Integer age;
	@Field(analyzer = "ik_smart", type = FieldType.text)
	private String sex;
	@Field(analyzer = "ik_smart", type = FieldType.text)
	private String desc;
}