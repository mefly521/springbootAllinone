package com.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.forest.AmapClient;
import com.dtflys.forest.http.ForestResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ForestTests {
    @Autowired
    private AmapClient client;

    /**
     * forest 组件,更简洁,更方便的调用http接口
     */
    @Test
    public void test() {
        // 调用接口
        Map result = client.getLocation("124.730329", "31.463683");
        System.out.println(JSON.toJSONString(result, true));
    }

    @Test
    public void login() {
        String userName = "dangjian";
        Map map = new HashMap();
        map.put("userName", userName);
        map.put("ipAddress", "");
        map.put("clientType", "WINPC");
        // 调用接口
        ForestResponse<String> response = client.login(map);
        System.out.println(response.getResult());
    }
}
