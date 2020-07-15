package com.demo.front.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author mifei
 * @create 2019-09-06 9:48
 **/
@Service
@Slf4j
public class SpilderService {
    private String imgPath = "D:\\temp\\01";
    public Document getDoc(String url) throws Exception {
        log.info("开始连接 ");
        Connection.Response execute = Jsoup.connect(url).timeout(500000).execute();
        String html = execute.body();
        log.info("连接完成");
        Document doc = Jsoup.parse(html);
        return doc;
    }

    /**
     * 打印新闻标题和内容
     * @throws Exception
     */
    public void print() throws Exception {
        for (int i = 0; i < 50; i++) {
            String jrgz = "http://www.tj.gov.cn/sy/jrgz/";
            String tjxw = "http://www.tj.gov.cn/sy/tjxw/";
            String index = "index.html";
            if (i > 0) {
                index = "index" + i + ".html";
            }
            printPage(jrgz+index);
            printPage(tjxw+index);
        }
    }
    public void printPage(String url) throws Exception {
        Document doc = getDoc(url);
        Elements elements = doc.select("div[class=list-item clear]");
        for (Element element : elements) {
            log.info(element.select("div").select("span").select("a").text());
            String jump = element.select("div").select("span").select("a").attr("onclick");
            jump = jump.substring("jumpToDetail('./".length(), jump.length() - 2);
            log.info(element.select("div").select("span[class=list-item-date]").text());
            log.info(jump);
            printContext(url + jump);
        }
    }

    public void printContext(String url) throws Exception {
        Document doc = getDoc(url);
        log.info(doc.select("div[class=qt-title]").text());//标题
        log.info(doc.select("div[class=xw-xtitle clear]").text());//来源 时间
        log.info(doc.select("div[class=xw-txt]").select("div").text());//内容
    }


    public void printImage() throws Exception {
        String url = "http://www.tj.gov.cn";
        Document doc = getDoc(url);
        Elements elements = doc.select("div[class=box_img]").select("ul").select("li");
        for (Element element : elements) {
            log.info(element.select("a").attr("href"));//url
            String imageUrl = element.select("a").select("img").attr("src");//图片
            downLoadImage(url+imageUrl.substring(1));
        }
        elements = doc.select("div[class=box_tab right clear-fix]").select("a");
        for (Element element : elements) {
            log.info(element.select("span").text());//标题
        }

    }
    public void downLoadImage( String imgUrl) throws Exception {
        String filename = imgUrl.substring(imgUrl.lastIndexOf("/"));
        //创建文件目录
        File files = new File(imgPath);
        if (!files.exists()) {
            files.mkdirs();
        }
        //获取下载地址
        URL url = new URL(imgUrl);
        //链接网络地址
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        //获取链接的输出流
        InputStream is = connection.getInputStream();
        //创建文件，fileName为编码之前的文件名
        File file = new File(imgPath +filename);
        //根据输入流写入文件
        FileOutputStream out = new FileOutputStream(file);
        int i = 0;
        while((i = is.read()) != -1){
            out.write(i);
        }
        out.close();
        is.close();
    }

    public static void main(String[] args) throws Exception {
        //new SpilderService().print();
        new SpilderService().printImage();
    }
}
