package com.demo.controller;

import com.demo.entity.DataBean;
import com.demo.entity.Notice;
import com.demo.entity.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HelloController {

    @Autowired
    private UserServiceImpl userService;


    @RequestMapping("/")
    public String index() {
        //userService.test(1, 0);
        return "Hello Spring Boot 2.0!";
    }

    @RequestMapping("/notice/list/{num}/{id}")
    public Object test(@PathVariable(value = "num") Long num, @PathVariable(value = "id") Long id) throws InterruptedException {
        Notice notice = new Notice();
        notice.setMsg("test");
        DataBean dataBean = new DataBean();
        dataBean.setNoticeId(1);
        dataBean.setNoticeContent("一个demo");
        List list = new ArrayList<>();
        list.add(dataBean);
        notice.setData(list);
        R<Notice> r = new R();
        r.setData(notice);
        return r;
    }
}