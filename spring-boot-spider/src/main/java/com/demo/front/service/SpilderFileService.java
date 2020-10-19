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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mifei
 * @create 2019-09-06 9:48
 **/
@Service
@Slf4j
public class SpilderFileService {

    @Autowired
    private MongoTemplate mongoTemplate;


    private String imgPath = "D:\\temp\\01";
    private String collectionName = "spilder_news";


    public void localFile() throws Exception {
        File file = new File("D:\\temp\\00\\bookmarks_2020_9_11.html");
        Document document = Jsoup.parse(file, "utf-8");
        /*
         */
        Elements rows = document.getElementsByTag("DT");
        for (Element row : rows) {
            if (row.select("h3").size()==1){
                System.out.println("==="+row.select("h3").text());
            }

            if (row.select("a").size()==1){
                System.out.println(""+row.select("a").text());
                System.out.println(""+row.select("a").attr("href"));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new SpilderFileService().localFile();
    }
}
