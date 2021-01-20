package com.demo.dao;

import cn.hutool.core.util.StrUtil;
import com.demo.pojo.User;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author mifei
 * @create 2020-03-30 9:41
 **/
@Component
public class CommonDao {

	@Autowired
	private MongoTemplate mongoTemplate;
	public List<LinkedHashMap> select() {
		List<LinkedHashMap> apis = mongoTemplate.findAll(LinkedHashMap.class, "App");
		return apis;
	}

	/**
	 * 更新对象
	 *
	 */
	public void update() {
		List<LinkedHashMap> apis = mongoTemplate.findAll(LinkedHashMap.class, "api");
		long i = 0;

		for (LinkedHashMap api : apis) {
			if (api.get("url")== null) {
				continue;
			}
			String url = api.get("url")+"";
			if (!url.startsWith("/")){
				System.out.println(api.get("url"));
			}
		}

//		for (LinkedHashMap api : apis) {
//			if (api.get("url") == null || api.get("baseUrl")== null) {
//				continue;
//			}
//			String url = api.get("url")+"";
//			String baseUrl = api.get("baseUrl")+"";
//			if (!url.startsWith("/")){
//				url = "/" + url;
//			}
//			if (baseUrl.endsWith("/vol")) {
//				baseUrl = StrUtil.subBefore(baseUrl, "/vol",true);
//				url = "/vol" + url;
//				Query query = new Query(Criteria.where("cid").is(api.get("cid")));
//				Update update = new Update().set("url", url).set("baseUrl", baseUrl);
//				//更新查询返回结果集的第一条
//				UpdateResult result = mongoTemplate.updateFirst(query, update, LinkedHashMap.class,"api");
//				i = i+result.getModifiedCount();
//			}
//			if (baseUrl.endsWith("/cms")) {
//				baseUrl = StrUtil.subBefore(baseUrl, "/cms",true);
//				url = "/cms" + url;
//				Query query = new Query(Criteria.where("cid").is(api.get("cid")));
//				Update update = new Update().set("url", url).set("baseUrl", baseUrl);
//				//更新查询返回结果集的第一条
//				UpdateResult result = mongoTemplate.updateFirst(query, update, LinkedHashMap.class,"api");
//				i = i+result.getModifiedCount();
//			}
//		}
//		System.out.println(i);
	}

}
