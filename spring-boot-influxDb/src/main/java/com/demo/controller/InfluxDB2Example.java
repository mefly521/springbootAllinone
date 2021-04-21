package com.demo.controller;


import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;

public class InfluxDB2Example {
	public static void main(final String[] args) {

		// You can generate a Token from the "Tokens Tab" in the UI
		String token = "UnPDgOQWX2QHsSdmJacNwYPTqns8utlnazWSPlUg1HKTvyHFnT2YXmzK3wnGeIoOl49V7XL_dmweZFZyhkrERg==";
		String bucket = "test";
		String org = "bit";

		InfluxDBClient client = InfluxDBClientFactory.create("http://39.105.118.36:8086", token.toCharArray());

		String data = "mem,host=host1 used_percent=23.43234543";
		try (WriteApi writeApi = client.getWriteApi()) {
			writeApi.writeRecord(bucket, org, WritePrecision.NS, data);
		}
	}
}