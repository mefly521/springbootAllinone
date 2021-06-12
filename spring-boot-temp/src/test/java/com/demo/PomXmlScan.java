package com.demo;

import cn.hutool.core.io.FileUtil;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mifei
 * @create 2021-04-12 17:29
 **/
public class PomXmlScan {
	private static Map<String, List<String>> allProjects = new HashMap<>();
	private static Map<String, Map> allChildren = new HashMap<>();
	private static Map<String, String> pomProject = new HashMap<>();
	private static Map<String, List<String>> repetition = new HashMap<>();

	public void scan(String path, boolean ex) throws Exception {
		File[] ls = FileUtil.ls(path);
		for (File l : ls) {
			if (l.getName().equals("pom.xml")) {
				print(l, ex);
			}
			if (l.isDirectory()) {
				scan(l.getPath(), ex);
			}
		}
	}

	public void all(String path) throws Exception {
		scan(path, false);
		scan(path, true);
		System.out.println("共有项目:"+allProjects.size());
		allProjects.forEach((k, v) -> {
			if (repetition.containsKey(k)) {
				return;
			}
			System.out.println("=" + k + "	{" + pomProject.get(k) + "}");
			if (v != null) {
				System.out.println("	>>" + v);
			}
			if (allChildren.get(k).get("parent") != null) {
				Map parent = (Map) allChildren.get(k).get("parent");
				printPrent(parent, "");
			}
		});
	}

	public void printPrent(Map map, String prefix) {
		prefix = prefix + "	";
		System.out.println(prefix + "--" + map.get("name"));
		if (map.get("parent") != null) {
			Map parent = (Map) map.get("parent");
			printPrent(parent, prefix);
		}
	}

	private void print(File xmlFile, boolean ex) throws Exception {
		SAXReader reader = new SAXReader();
		// 通过read方法读取一个文件 转换成Document对象
		Document document = reader.read(xmlFile);
		Element root = document.getRootElement();
		// 获取supercars元素节点中，子节点为carname的元素节点(可以看到只能获取第一个carname元素节点)
		Element pxname = root.element("artifactId");
		String pname = pxname.getText();

		if (!ex) { //first
			if (root.element("packaging") != null) {
				String type = root.element("packaging").getText();
				pomProject.put(pname, type);
			}
			allProjects.put(pname, null);

			Map map = new HashMap();
			map.put("name", pname);
			map.put("parent", null);
			allChildren.put(pname, map);
		} else {    //second
			if (root.element("parent") != null) {
				Element parent = root.element("parent");
				String artifactId = parent.element("artifactId").getText();
				Map map = allChildren.get(pname);
				map.put("parent", allChildren.get(artifactId));
				allChildren.put(pname, map);
			}

			List<String> list = new ArrayList<>();
			if (root.element("dependencies") != null) {
				List<Element> childElements = root.element("dependencies").elements();
				for (Element child : childElements) {
					String thisV = child.elementText("artifactId");
					if (allProjects.containsKey(thisV)) {
						list.add(thisV);
						repetition.put(thisV, null);
					}
				}
			}
			if (list.size() > 0) {
				if (allProjects.get(pname) != null) {
					List temp = allProjects.get(pname);
					list.addAll(temp);
				}
				allProjects.put(pname, list);
			}
		}


	}

	public static void main(String[] args) throws Exception {
		new PomXmlScan().all("D:\\code\\lift\\commons\\push");
	}
}
