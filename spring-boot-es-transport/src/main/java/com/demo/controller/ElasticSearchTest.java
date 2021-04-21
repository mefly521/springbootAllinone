package com.demo.controller;


import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ElasticSearchTest {

	private TransportClient client;

	@Before
	public void getConnect() throws UnknownHostException {
		String clustername = "my-application";
		Settings settings = Settings.builder().put("cluster.name", clustername)
				.put("client.transport.sniff", true) //自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中
				.build();
		client = new PreBuiltTransportClient(settings);//初始化client较老版本发生了变化，此方法有几个重载方法，初始化插件等。
		//此步骤添加IP，至少一个，其实一个就够了，因为添加了自动嗅探配置
		client.addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.0.110"), 9300));
	}


	/**
	 * 添加一个索引
	 */
	@Test
	public void index() {
		Map<String,Object> infoMap = new HashMap<>();
		infoMap.put("name", "广告信息11");
		infoMap.put("title", "我的广告22");
		infoMap.put("createTime", new Date());
		infoMap.put("count", 1022);
		IndexResponse indexResponse = client.prepareIndex("test", "info","100").setSource(infoMap).execute().actionGet();
		System.out.println("id:"+indexResponse.getId());
	}


	/**
	 * 获取索引
	 */
	@Test
	public void get() {
		GetResponse response = client.prepareGet("test", "info", "100")
				.execute().actionGet();
		System.out.println("response.getId():"+response.getId());
		System.out.println("response.getSourceAsString():"+response.getSourceAsString());
	}


	/**
	 * 查询
	 */
	@Test
	public void query() {
		//term查询
//        QueryBuilder queryBuilder = QueryBuilders.termQuery("age", 50) ;
		//range查询
		QueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("count").gt(50);
		SearchResponse searchResponse = client.prepareSearch("test")
				.setTypes("info")
				.setQuery(rangeQueryBuilder)
//                .addSort("age", SortOrder.DESC)
				.setSize(20)
				.execute()
				.actionGet();
		SearchHits hits = searchResponse.getHits();
		System.out.println("查到记录数："+hits.getTotalHits());
		SearchHit[] searchHists = hits.getHits();
		if(searchHists.length>0){
			for(SearchHit hit:searchHists){
				String name =  (String) hit.getSourceAsMap().get("name");
				String title = (String)hit.getSourceAsMap().get("title");
				System.out.format("name:%s ,title :%s \n",name ,title);
			}
		}
	}

}
