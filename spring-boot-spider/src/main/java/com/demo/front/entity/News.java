package com.demo.front.entity;

import lombok.Data;

/**
 * @author mifei
 * @create 2020-07-16 9:05
 **/
@Data
public class News {
    public static final int TYPE_IMAGE = 2;//轮播

    private String source;
    private String date;
    private String url;
    private String imgSrc;
    private String title;
    private String context;
    private int type;  // 0 关注 1 新闻 2轮播

}
