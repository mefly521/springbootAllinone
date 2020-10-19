package com.demo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HttpClientTests {


    private void login2(JSONObject obj) {
    }

    private String host = "http://xxx:8085";

    @Test
    public void getMqConfig() {
        //String token = "xxx";
        String token = getToken();
        Map data = new HashMap();
        data.put("optional", "/admin/API/BRM/Config/GetMqConfig?token=" + token);

        Map map = new HashMap();
        map.put("clientType", "WINPC");
        map.put("clientMac", "xxx");
        map.put("clientPushId", "");
        map.put("project", "PSDK");
        map.put("method", "BRM.Config.GetMqConfig");
        map.put("data", data);
        JSONObject res = http(host + "/GetMqConfig?token=" + token, map);

    }

    @Test
    public void getAlarmTypes() {
        String token = "xxx";
        Map data = new HashMap();
        String url = "/GetAlarmTypes?token=" + token;
        data.put("locale", "zh_CN");
        data.put("optional", url);

        Map map = new HashMap();
        map.put("clientType", "WINPC");
        map.put("clientMac", "xxx");
        map.put("clientPushId", "");
        map.put("project", "PSDK");
        map.put("method", "BRM.Alarm.GetAlarmTypes");
        map.put("data", data);
        JSONObject res = http(host + url, map);
    }

    @Test
    public void getAlarmList() {
        //String token = "xxx";
        String token = getToken();
        try {
            Thread.sleep(31_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Map orderInfo = new HashMap();
        orderInfo.put("orderType", "1");
        orderInfo.put("direction", "1");

        Map pageInfo = new HashMap();
        pageInfo.put("pageSize", "20");
        pageInfo.put("pageNo", "1");

        Map searchInfo = new HashMap();
        searchInfo.put("alarmStatus", new int[1]);//报警状态，1=报警产生，2=报警消失
        searchInfo.put("handleUser", "");//报警处理人(用户名)
        searchInfo.put("deviceCode", "");
        searchInfo.put("handleStatus", "");//处理状态，1=处理中，2=已解决，3=误报，4=忽略，5=未解决
        searchInfo.put("endHandleDate", "" + System.currentTimeMillis());
        searchInfo.put("endAlarmDate", "");
        searchInfo.put("alarmId", "");
        searchInfo.put("alarmGrade", "");
        searchInfo.put("alarmType", "");
        searchInfo.put("startHandleDate", DateUtil.yesterday().millisecond());
        searchInfo.put("orgCode", "");
        searchInfo.put("channelId", "");
        searchInfo.put("startAlarmDate", "");
        searchInfo.put("alarmCode", "");

        Map data = new HashMap();
        data.put("optional", "/QueryAlarms?token=" + token);
        data.put("orderInfo", orderInfo);
        data.put("pageInfo", pageInfo);
        data.put("searchInfo", searchInfo);

        Map map = new HashMap();
        map.put("clientType", "WINPC");
        map.put("clientMac", "xxx");
        map.put("clientPushId", "");
        map.put("project", "PSDK");
        map.put("method", "BRM.Alarm.QueryAlarms");
        map.put("data", data);
        System.out.println("入参:"+JSON.toJSONString(map, true));
        JSONObject res = http(host + "/QueryAlarms?token=" + token, map);

    }

    @Test
    public void getTokenTest() {
        getToken();
    }

    public String getToken() {
        String userName = "xxx";
        String password = "xxx";

        Map map = new HashMap();
        map.put("userName", userName);
        map.put("ipAddress", "");
        map.put("clientType", "WINPC");
        JSONObject res = http(host + "/authorize", map);

        String realm = res.getString("realm");
        String temp = SecureUtil.md5(password);
        temp = SecureUtil.md5(userName + temp);
        temp = SecureUtil.md5(temp);
        temp = SecureUtil.md5(userName + ":" + realm + ":" + temp);
        String signature = SecureUtil.md5(temp + ":" + res.getString("randomKey"));

        Map<String, String> input2 = new HashMap<>();
        input2.put("userName", userName);
        input2.put("randomKey", res.getString("randomKey"));
        input2.put("mac", "");
        input2.put("encryptType", "MD5");
        input2.put("ipAddress", "");
        input2.put("signature", signature);
        input2.put("clientType", "WINPC");
        JSONObject res2 = http(host + "/authorize", input2);
        return (String) res2.get("token");
    }

    public JSONObject http(String url, Map input) {
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建Post请求
        HttpPost httpPost = new HttpPost(url);
        // 我这里利用阿里的fastjson，将Object转换为json字符串;
        // (需要导入com.alibaba.fastjson.JSON包)
        String jsonString = JSON.toJSONString(input);
        StringEntity entity = new StringEntity(jsonString, "UTF-8");
        // post请求是将参数放在请求体里面传过去的;这里将entity放入post请求体中
        httpPost.setEntity(entity);
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                String resStr = EntityUtils.toString(responseEntity);
                System.out.println("响应内容为:" + resStr);
                JSONObject obj = JSONObject.parseObject(resStr);
                System.out.println("响应内容为:" + JSON.toJSONString(obj, true));
                return obj;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
