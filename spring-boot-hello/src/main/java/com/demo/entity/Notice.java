package com.demo.entity;

import lombok.Data;

import java.util.List;

/**
 * @author mifei
 * @create 2020-07-30 13:59
 **/
@Data
public class Notice {
    private int status;
    private Object msg;
    private List<DataBean> data;
}
