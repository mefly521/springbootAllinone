package com.demo.dao;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.demo.pojo.FaultTypeCount;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import org.bson.BsonNull;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Accumulators.avg;
import static com.mongodb.client.model.Sorts.descending;

/**
 * @author mifei
 * @create 2020-03-30 9:41
 **/
@Component
public class CommonDao {

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
	 * 26  工作效率统计-维修次数 按班组分组
	 */
	public void test(String companyId, long startTime, long endTime) {

		//List<Map> test = mongoTemplate.findAll(Map.class, "test");
		Map map = new HashMap();
		map.put("begin", DateUtil.beginOfDay(DateUtil.yesterday()));
		map.put("end",  DateUtil.beginOfDay(DateUtil.date()));

		map.put("end2", DateUtil.beginOfDay(DateUtil.yesterday()));
		map.put("begin2",  DateUtil.beginOfDay(DateUtil.date()).getTime());

		//mongoTemplate.insert(map, "test");
		ObjectId objectId = new ObjectId("60175519a13096307cfbc522");
		// 构建 Aggregation
		Aggregation aggregation = Aggregation.newAggregation(Arrays.asList(
				Aggregation.match(Criteria.where("begin").exists(true)),
				//Aggregation.group("$faultCode").count().as("count"),
				Aggregation.project("end","workGroupId").andInclude("begin").andExpression("(end - begin)/1000").as("res")
						.andExpression("(([0] + begin2)-end2)/1000", new Date(0)).as("res2"),
				Aggregation.group("workGroupId").avg("res2").as("avg")
						.count().as("ff")
						.sum("res2").as("bf")
//				Aggregation.sort(Sort.Direction.DESC, "count"),
//				Aggregation.project("count").and("_id").as("code")
		));
		AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, "test", Map.class);
		for (Map mappedResult : results.getMappedResults()) {
			System.out.println(mappedResult);
		}
	}

	/**
	 * 13 故障代码排名
	 */
	public void faultCodeRange(String companyId, long startTime, long endTime) {
		MongoCollection<Document> collection = mongoTemplate.getCollection("EV_ELEVATOR_FAULT");
		AggregateIterable<Document> result = collection.aggregate(Arrays.asList(
				match(and(and(gt("createTime", startTime), lte("createTime", endTime)), ne("faultCode",
						new BsonNull()),eq("companyId",
				new ObjectId("5aaf772c268a2ae53c7d5f5b")))),
				group("$faultCode", sum("count", 1L)),
				sort(descending("count")),
				project(fields(excludeId(), include("count"), computed("code", "$_id")))));

		Iterator it = result.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
	/**
	 * 14 月度故障统计-发生故障数
	 */
	public void faultSumByMonth(String companyId, long startTime, long endTime) {
		MongoCollection<Document> collection = mongoTemplate.getCollection("EV_ELEVATOR_FAULT");
		AggregateIterable<Document> result = collection.aggregate(Arrays.asList(match(and(and(gt("reportTime", 1601481600000L), lte("reportTime", 1611650626786L)), eq("companyId",
				new ObjectId("5aaf772c268a2ae53c7d5f5b")))),
				group(eq("$dateToString", and(eq("format", "%Y-%m-%d"), eq("date", eq("$add", Arrays.asList(new java.util.Date(0L), "$reportTime", 28800000L))))), sum("count", 1L))));
		Iterator it = result.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}

	/**
	 * 15 月度故障统计-发生故障数
	 */
	public void autoFaultSumByMonth(String companyId, long startTime, long endTime) {
		MongoCollection<Document> collection = mongoTemplate.getCollection("EV_ELEVATOR_FAULT");
		AggregateIterable<Document> result = collection.aggregate(Arrays.asList(
				match(and(eq("faultSourceType", 4L), and(gt("reportTime", 1601481600000L), lte("reportTime", 1604160000000L)), eq("companyId",
						new ObjectId("5aaf772c268a2ae53c7d5f5b")))),
				group(eq("$dateToString", and(eq("format", "%Y-%m-%d"), eq("date", eq("$add", Arrays.asList(new java.util.Date(0L), "$reportTime", 28800000L))))), sum("count", 1L))));

		Iterator it = result.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}

	/**
	 * 25  急修概览-困人电梯台数
	 */
	public void trappedPersonSumByMonth(String companyId, long startTime, long endTime) {
		MongoCollection<Document> collection = mongoTemplate.getCollection("EV_DECLARE");
		AggregateIterable<Document> result = collection.aggregate(Arrays.asList(match(and(ne("status", 10L), exists("endRepairTime", true), eq("isTrappedPerson", true), and(gt("createTime", 1601481600000L), lte("createTime", 1611650626786L)), eq("companyId",
				new ObjectId("5aaf772c268a2ae53c7d5f5b")))),
				group("$elevatorId"),
				group(1L, sum("abc", 1L))));

		Iterator it = result.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}


	/**
	 * 26  工作效率统计-维修次数 按班组分组
	 */
	public void WorkEfficiencyStatistics(String companyId, long startTime, long endTime) {

		ObjectId objectId = new ObjectId("5aaf772c268a2ae53c7d5f5b");
		Aggregation aggregation = Aggregation.newAggregation(Arrays.asList(
				Aggregation.match(Criteria.where("endRepairTime").exists(true)
						.and("companyId").is(objectId)
						.and("status").ne(10).and("createTime").gt(1601481600000L).lt(1611650626786L)),
				Aggregation.project("workGroupId").andExpression("(endRepairTime - beginRepairTime)/1000").as("finishTime")
						.andExpression("(setOutTime - ([0] + faultDate))/1000", new Date(0)).as("responseTime"),
				Aggregation.group("workGroupId").avg("finishTime").as("avgFinishTime")
						.avg("responseTime").as("avgResponseTime")
						.count().as("sum")
		));
		AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, "EV_DECLARE", Map.class);
		List<ObjectId> list = new ArrayList<>();
		for (Map mappedResult : results.getMappedResults()) {
			System.out.println(mappedResult);
			list.add((ObjectId)mappedResult.get("_id"));
		}
		List<Map> groups = mongoTemplate.find(new Query(Criteria.where("_id").in(list)), Map.class, "EV_WORK_GROUP");
		for (Map mappedResult : results.getMappedResults()) {
			System.out.println(mappedResult);
			ObjectId workGroupId = (ObjectId)mappedResult.get("_id");
			for (Map group : groups) {
				if ( ((ObjectId)group.get("_id")).equals(workGroupId)){
					mappedResult.put("workGroupName",group.get("teamName"));
				}
			}
		}
		System.out.println(results.getMappedResults());

	}


	/**
	 * 28  月度急修统计-发生急修数量统计
	 */
	public void declareSumByMonth(String companyId, long startTime, long endTime) {
		MongoCollection<Document> collection = mongoTemplate.getCollection("EV_DECLARE");
		AggregateIterable<Document> result = collection.aggregate(Arrays.asList(
				match(and(ne("status", 10L),and(gt("createTime", 1601481600000L), lte("createTime", 1611650626786L)), eq("companyId",
				new ObjectId("5aaf772c268a2ae53c7d5f5b")))),
				group(eq("$dateToString", and(eq("format", "%Y-%m-%d"), eq("date", eq("$add", Arrays.asList(new java.util.Date(0L), "$createTime", 28800000L))))), sum("count", 1L))));

		Iterator it = result.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}

	/**
	 * 29  月度急修统计-实际急修完成数量统计
	 */
	public void declareFinishSumByMonth(String companyId, long startTime, long endTime) {
		MongoCollection<Document> collection = mongoTemplate.getCollection("EV_DECLARE");
		AggregateIterable<Document> result = collection.aggregate(Arrays.asList(
				match(and(ne("status", 10L), exists("endRepairTime", true), and(gt("createTime", 1601481600000L), lte("createTime", 1611650626786L)), eq("companyId",
						new ObjectId("5aaf772c268a2ae53c7d5f5b")))),
				group(eq("$dateToString", and(eq("format", "%Y-%m-%d"), eq("date", eq("$add", Arrays.asList(new java.util.Date(0L), "$createTime", 28800000L))))), sum("count", 1L))));

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
		JSONArray jsonArray = new JSONArray(" [{\n" +
				"    $match: {\n" +
				"        \"status\": {\n" +
				"            $ne: 10\n" +
				"        },\n" +
				"        \"endRepairTime\": {\n" +
				"            $exists: true\n" +
				"        },\n" +
				"        \"isTrappedPerson\": true,\n" +
				"        \"createTime\": {\n" +
				"            $gt: 1601481600000,\n" +
				"            $lte: 1611650626786\n" +
				"        },\n" +
				"        \"companyId\": ObjectId('5aaf772c268a2ae53c7d5f5b')\n" +
				"    }\n" +
				"}, {\n" +
				"    $group: {\n" +
				"        _id: \"$elevatorId\"\n" +
				"    }\n" +
				"}, {\n" +
				"    $group: {\n" +
				"        _id: 1,\n" +
				"        count: {\n" +
				"            $sum: 1\n" +
				"        }\n" +
				"    }\n" +
				"}]") ;
//		for (JSONObject o : jsonArray) {
//			System.out.println(o);
//		}
	}
}
