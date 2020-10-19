package com.demo.front.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.demo.front.entity.News;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
public class SpilderService {

    @Autowired
    private MongoTemplate mongoTemplate;


    private String imgPath = "D:\\temp\\01";
    private String collectionName = "spilder_news";


    public Document getDoc(String url) throws Exception {
        //log.info("开始连接");
        Document doc = null;
        try {
            Connection.Response execute = Jsoup.connect(url).timeout(500000).execute();
            String html = execute.body();
            //log.info("连接完成");
            doc = Jsoup.parse(html);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        Thread.sleep(1000);
        return doc;
    }

    /**
     * 打印新闻标题和内容
     *
     * @throws Exception
     */
    public void printNews() throws Exception {
        for (int i = 0; i < 50; i++) {
            String jrgz = "http://www.tj.gov.cn/sy/jrgz/";
            String tjxw = "http://www.tj.gov.cn/sy/tjxw/";
            String index = "index.html";
            if (i > 0) {
                index = "index_" + i + ".html";
            }
            log.info(jrgz + index);
            printList(jrgz + index, 0);
            log.info(tjxw + index);
            printList(tjxw + index, 1);
        }
    }

    public void printList(String url, int type) throws Exception {
        Document doc = getDoc(url);
        if (doc == null) {
            return;
        }
        Elements elements = doc.select("div[class=list-item clear]");
        for (Element element : elements) {
            String res = "";
            log.info(element.select("div").select("span").select("a").text());
            String jump = element.select("div").select("span").select("a").attr("onclick");
            if (jump.indexOf("../") > 0) {
                jump = jump.substring("jumpToDetail('./".length(), jump.length() - 2);
                log.info(element.select("div").select("span[class=list-item-date]").text());
                log.info(jump);
                String sub = url.substring(0, url.lastIndexOf("/"));
                sub = sub.substring(0, sub.lastIndexOf("/")); //去除2层
                res = sub + jump;
            } else {
                jump = jump.substring("jumpToDetail('./".length(), jump.length() - 2);
                log.info(element.select("div").select("span[class=list-item-date]").text());
                log.info(jump);
                String sub = url.substring(0, url.lastIndexOf("/") + 1);
                res = sub + jump;
            }
            printContext(res, type);
        }
    }


    /**
     * 打印新闻内容
     *
     * @param url
     * @param type
     * @throws Exception
     */
    public void printContext(String url, int type) throws Exception {
        log.info("内容=" + url);//来源 时间
        Document doc = getDoc(url);
        if (doc == null) {
            return;
        }
        News news = new News();
        news.setUrl(url);
        //news.setContext(doc.select("div[class=xw-txt]").select("div").html());
        news.setTitle(doc.select("div[class=qt-title]").text());
        String source = doc.select("div[class=xw-xtitle clear]").select("span").first().text();
        source = StrUtil.removePrefix(source, "来源：");
        String date = doc.select("div[class=xw-xtitle clear]").select("span[class=p-title p-fbsj]").text();
        date = StrUtil.removePrefix(date, "发布时间：");
        news.setSource(source);
        news.setDate(date);
        news.setType(type);
        save(news);
        log.info(news.getSource());//来源 时间
        //log.info(news.getContext());
        log.info(news.getTitle());//标题
    }


    public void save(News news) {
        Criteria where = Criteria.where("type").is(news.getType()).and("title").is(news.getTitle());
        Query query = new Query(where);

        List list = mongoTemplate.find(query, News.class, collectionName);
        if (list.size() == 0) {
            mongoTemplate.save(news, collectionName);
        }
    }

    public void saveImage(News news) {
        mongoTemplate.save(news, collectionName);
    }

    public void cleanImage() {
        Criteria where = Criteria.where("type").is(News.TYPE_IMAGE);
        Query query = new Query(where);
        mongoTemplate.remove(query, collectionName);
    }

    public void printImage() throws Exception {
        cleanImage();
        String url = "http://www.tj.gov.cn";
        Document doc = getDoc(url);
        Elements elements = doc.select("div[class=box_img]").select("ul").select("li");
        List<News> list = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            log.info(element.select("a").attr("href"));//url
            String imageUrl = element.select("a").select("img").attr("src");//图片
            //downLoadImage(url + imageUrl.substring(1));
            imageUrl = url + imageUrl.substring(1);
            News news = new News();
            news.setImgSrc(imageUrl);
            list.add(news);
        }
        elements = doc.select("div[class=box_tab right clear-fix]").select("a");
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            String newsUrl = element.attr("href");
            newsUrl = url + newsUrl.substring(1);
            News news = list.get(i);
            news.setTitle(element.select("span").text());
            news.setType(News.TYPE_IMAGE);
            news.setUrl(newsUrl);
            log.info(news.getTitle());//标题
            saveImage(news);
        }
    }

    /**
     * 下载图片到本地
     *
     * @param imgUrl
     * @throws Exception
     */
    public void downLoadImage(String imgUrl) throws Exception {
        String filename = imgUrl.substring(imgUrl.lastIndexOf("/"));
        //创建文件目录
        File files = new File(imgPath);
        if (!files.exists()) {
            files.mkdirs();
        }
        //获取下载地址
        URL url = new URL(imgUrl);
        //链接网络地址
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        //获取链接的输出流
        InputStream is = connection.getInputStream();
        //创建文件，fileName为编码之前的文件名
        File file = new File(imgPath + filename);
        //根据输入流写入文件
        FileOutputStream out = new FileOutputStream(file);
        int i = 0;
        while ((i = is.read()) != -1) {
            out.write(i);
        }
        out.close();
        is.close();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("2020-07-16 07:55".compareTo("2020-07-16 07:54"));
        //new SpilderService().printNews();
        //new SpilderService().printImage();
    }
}
