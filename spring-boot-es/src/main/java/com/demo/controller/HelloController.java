package com.demo.controller;

import com.demo.entity.DataBean;
import com.demo.entity.Notice;
import com.demo.entity.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class HelloController {


    @RequestMapping("/hello")
    public String index() {
		log.info("成功打印日志");
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