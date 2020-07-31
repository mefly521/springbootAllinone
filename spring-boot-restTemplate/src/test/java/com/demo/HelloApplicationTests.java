package com.demo;

import com.demo.entity.Notice;
import com.demo.entity.R;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloApplicationTests {

    @Test
    public void contextLoads() {
        System.out.println("Hello Spring Boot 2.0!");
    }

    @Test
    public void restTemplateGetTest() {
        RestTemplate restTemplate = new RestTemplate();
        R<Notice> result = restTemplate.getForObject("http://localhost:8088/notice/list/1/5"
                , R.class);
        Notice notice = result.getData();
        System.out.println(notice);
    }

    @Test
    public void t2() {
        //exchange 用来处理返回值中有泛型的
        String url = "http://localhost:8088/notice/list/1/5";
        ParameterizedTypeReference<R<Notice>> responseBodyType = new ParameterizedTypeReference<R<Notice>>() {
        };
        RestTemplate restTemplate = new RestTemplate();
        // 请求头
        HttpHeaders headers = new HttpHeaders();
        MimeType mimeType = MimeTypeUtils.parseMimeType("application/json");
        MediaType mediaType = new MediaType(mimeType.getType(), mimeType.getSubtype(), Charset.forName("UTF-8"));
        // 请求体
        headers.setContentType(mediaType);
        // 发送请求
        HttpEntity entity = new HttpEntity<>(null, headers);
        ResponseEntity<R<Notice>> resultEntity = restTemplate.exchange(url, HttpMethod.GET, entity, responseBodyType);
        System.out.println(resultEntity.getBody());
    }
}
