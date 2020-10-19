package com.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.dao.AreaCodeMapper;
import com.demo.entity.AreaCode;
import com.demo.entity.DataBean;
import com.demo.entity.Notice;
import com.demo.entity.R;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
public class HelloController {

    @Autowired
    private UserService userService;
    @Autowired
    private AreaCodeMapper areaCodeMapper;


    @RequestMapping("/")
    public String index() {
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

    @RequestMapping("/testa")
    public Object testa()  {
        Page<AreaCode> page = new Page<>(1, 10);
        IPage<AreaCode> agentVOIPage = areaCodeMapper.selectPage(page, new QueryWrapper<>());
        agentVOIPage.getRecords();
        return null;
    }

    @RequestMapping("/test2")
    public Object testb()  {
        PageHelper.startPage(1, 8);
        List<AreaCode> alarmVOS = areaCodeMapper.listPage();
        PageInfo<AreaCode> pageInfo = new PageInfo<>(alarmVOS);
        R<PageInfo> r = new R();
        r.setData(pageInfo);
        return r;
    }

    // 并且
    @RequestMapping("/test4")
    public Object testc()  {
        PageHelper.startPage(1, 8);
        List<AreaCode> alarmVOS = areaCodeMapper.listPage();
        PageInfo<AreaCode> pageInfo = new PageInfo<>(alarmVOS);
        R<PageInfo> r = new R();
        r.setData(pageInfo);
        return r;
    }

}