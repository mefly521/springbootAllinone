package com.demo.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 ** 配置elasticsearch
 * @author caizh
 * @throws UnknownHostException
 *
 */
@Configuration
public class MyConfig {

	//配置elasticsearch
	@Bean
	public TransportClient client() throws UnknownHostException {

		// elasticsearch tcp端口是9300
		InetSocketTransportAddress node =
				new InetSocketTransportAddress(
						InetAddress.getByName("localhost"),
						9300
				);
		Settings setting = Settings.builder()
				.put("cluster.name","caizh")
				.build();
		TransportClient client =
				new PreBuiltTransportClient(setting);
		// 添加节点
		client.addTransportAddress(node);
		//client.addTransportAddress(node1);
		return client;
	}
}