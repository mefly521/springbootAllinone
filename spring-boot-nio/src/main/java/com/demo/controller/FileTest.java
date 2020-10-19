package com.demo.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 打印目录下的文件名
 *
 * @author mifei
 * @create 2020-06-16 15:39
 **/
public class FileTest {

	public static void main(String[] args) {
		File file = new File("D:\\code\\aliyun\\microservices\\service-cbo\\src\\main\\java\\com\\bit\\module\\cbo\\controller");
		if (file.isDirectory()) {
			String[] names = file.list();
			for (String name : names) {
				System.out.println(name);
			}
		}
	}
}
