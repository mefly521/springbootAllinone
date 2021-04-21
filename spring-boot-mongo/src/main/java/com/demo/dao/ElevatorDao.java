package com.demo.dao;

import com.demo.pojo.FaultTypeCount;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.descending;

/**
 * @author mifei
 * @create 2020-03-30 9:41
 **/
@Component
public class ElevatorDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	/**
	 * 使用 mongoTemplate 的java代码进行聚合查询
	 */
	public void aggregationUseMongoTemplate() {
		ObjectId objectId = new ObjectId("5aaf772c268a2ae53c7d5f5b");
		// 构建 Aggregation
		Aggregation aggregation = Aggregation.newAggregation(Arrays.asList(
				Aggregation.match(Criteria.where("companyId").is(objectId).and("createTime").gt(0).lte(1611650626786L)),
				Aggregation.group("$faultCode").count().as("count"),
				Aggregation.sort(Sort.Direction.DESC, "count"),
				Aggregation.project("count").and("_id").as("code")
		));

		// 执行查询
		AggregationResults<FaultTypeCount> results = mongoTemplate.aggregate(aggregation, "EV_ELEVATOR_FAULT", FaultTypeCount.class);
		for (FaultTypeCount mappedResult : results.getMappedResults()) {
			System.out.println(mappedResult);
		}
	}


	/**
	 * 使用 mongo compass 自动生成的java代码进行聚合查询
	 */
	public void aggregationFromCompass() {
		MongoCollection<Document> collection = mongoTemplate.getCollection("EV_ELEVATOR_FAULT");
		AggregateIterable<Document> result = collection.aggregate(Arrays.asList(
				match(and(gt("createTime", 0L), lte("createTime", 1611650626786L))),
				group("$faultCode", sum("count", 1L)),
				sort(descending("count"))));
		Iterator it = result.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}

	public List<LinkedHashMap> select() {
		List<LinkedHashMap> apis = mongoTemplate.findAll(LinkedHashMap.class, "EV_ELEVATOR");
		return apis;
	}

	/**
	 * 更新对象
	 */
	public void update() {
		List<LinkedHashMap> apis = mongoTemplate.findAll(LinkedHashMap.class, "api");
		long i = 0;

		for (LinkedHashMap api : apis) {
			if (api.get("url") == null) {
				continue;
			}
			String url = api.get("url") + "";
			if (!url.startsWith("/")) {
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

	public static void main(String[] args) {
		System.out.println(Double.parseDouble("150") / 60);
	}
}
