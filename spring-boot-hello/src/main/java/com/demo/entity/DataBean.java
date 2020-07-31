package com.demo;

import lombok.Data;

/**
 * @author mifei
 * @create 2020-07-30 13:59
 **/
@Data
public class DataBean {
    private int noticeId;
    private String noticeTitle;
    private Object noticeImg;
    private long noticeCreateTime;
    private long noticeUpdateTime;
    private String noticeContent;
}
