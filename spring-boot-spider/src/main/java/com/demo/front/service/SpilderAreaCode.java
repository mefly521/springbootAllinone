package com.demo.front.service;

import cn.hutool.core.util.StrUtil;
import com.demo.front.entity.News;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mifei
 * @create 2019-09-06 9:48
 **/
@Service
@Slf4j
public class SpilderAreaCode {

    @Autowired
    private MongoTemplate mongoTemplate;


    private String imgPath = "D:\\temp\\01";
    private String collectionName = "spilder_news";


    public Document getDoc(String url) throws Exception {
        //log.info("开始连接");
        Document doc = null;
        try {
            Connection.Response execute = Jsoup.connect(url).maxBodySize(Integer.MAX_VALUE).timeout(500000).execute();
            String html = execute.body();
            //log.info("连接完成");
            doc = Jsoup.parse(html);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return doc;
    }


    public Map<String,String> printList(String url) throws Exception {
        Document doc = getDoc(url);
        if (doc == null) {
            return null;
        }
        Elements elements = doc.select("table").select("tbody").select("tr");
        Map<String,String> map = new HashMap<>(elements.size());
        for (int i = 3; i < elements.size(); i++) {
            Element element = elements.get(i);
            Elements tds = element.select("td");
            if (tds.size() < 3) {
                continue;
            }
            String code = tds.get(1).text();
            if (!StringUtils.isEmpty(code) ){
                map.put(code,tds.get(2).text());
            }
            //System.out.println(tds.get(1).text());
            //System.out.println(tds.get(2).text());
        }

        return map;
    }



    public void printList() throws Exception {
        List<String> list = new ArrayList<>();
        list.add("http://www.mca.gov.cn/article/sj/xzqh/2020/2020/202003061536.html");
        list.add("http://www.mca.gov.cn/article/sj/xzqh/2020/2020/202003301019.html");
        list.add("http://www.mca.gov.cn///article/sj/xzqh/2020/2020/202007170301.html");
        list.add("http://www.mca.gov.cn///article/sj/xzqh/2020/2020/2020072804001.html");
        list.add("http://www.mca.gov.cn///article/sj/xzqh/2020/2020/2020072805001.html");
        list.add("http://www.mca.gov.cn//article/sj/xzqh/2020/202006/202008310601.shtml");
        list.add("http://www.mca.gov.cn//article/sj/xzqh/2020/2020/20200908007001.html");
        list.add("http://www.mca.gov.cn//article/sj/xzqh/2020/2020/2020092500801.html");

        for (int i = 0; i < list.size()-1; i++) {
            Map<String,String> a = printList(list.get(i));
            Map<String,String> b = printList(list.get(i+1));
            System.out.println(i);
            System.out.println("a > b");//删除
            compire(a, b);
            System.out.println("b > a");//新增
            compire(b, a);
        }

    }

    private void compire(Map<String, String> list, Map<String, String> list2){
        for(Map.Entry<String, String> entry : list.entrySet()){
            String code = entry.getKey();
            String name = entry.getValue();
            if (list2.get(code) == null) {
                System.out.println(code);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new SpilderAreaCode().printList();
        //new SpilderService().printImage();
    }
}
