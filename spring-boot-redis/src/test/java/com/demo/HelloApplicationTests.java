package com.demo;

import com.sun.javaws.CacheUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//@RunWith(SpringRunner.class)
//@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringBootTest(classes = HelloApplication.class) // 指定我们SpringBoot工程的Application启动类,1.5.4摒弃了SpringApplicationConfiguration注解
@WebAppConfiguration
public class HelloApplicationTests {

	@Autowired
	private RedisTemplate redisTemplate;
	@Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private DefaultRedisScript<String> redisScript;


    @Test
    public void test3() {
        List<String> keys = Arrays.asList("campaign:id:237");
        String execute = stringRedisTemplate.execute(redisScript, keys, "29","100");
        System.out.println(execute);
    }

    @Test
    public void test2() {
        List<String> keys = Arrays.asList("testLua", "hello lua");
        //Boolean execute = stringRedisTemplate.execute(redisScript, keys, "100");
        //System.out.println(execute);
    }
	@Test
	public void contextLoads() {
		String lua = "return ARGV[1]";
        //Long expire = redisTemplate.getExpire("123");
        String res = excuteLuaForVol(lua,"campaign:id:237", "29", "10");
        System.out.println(res);
	}

    public String excuteLuaForVol(String lua, String key1, String argv1, String argv2) {
        DefaultRedisScript<String> rs = new DefaultRedisScript();
        //设置脚本
        rs.setScriptText(lua);
        RedisSerializer<String> stringRedisSerializer = redisTemplate.getStringSerializer();
        //定义key
        List<String> keyList = new ArrayList();
        keyList.add(key1);
        return (String) redisTemplate.execute(rs, stringRedisSerializer,
                stringRedisSerializer, keyList, argv1, argv2);
    }
}
