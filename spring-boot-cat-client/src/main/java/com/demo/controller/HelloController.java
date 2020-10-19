package com.demo.controller;

import com.demo.common.CatTransaction;
import com.demo.entity.DataBean;
import com.demo.entity.Notice;
import com.demo.entity.R;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Event;
import com.dianping.cat.message.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/hello")
    @CatTransaction(type = "URL",name = "hello")
    public String simpleCall() throws InterruptedException {
        Thread.sleep(1500);
        System.out.println(System.currentTimeMillis());
        return "success";
    }

// 非aop的方法 写得比较麻烦,但性能好
//    public String simpleCall() {
//        Transaction t = Cat.newTransaction("URL", "pageName");
//
//        try {
//            Cat.logEvent("URL.Server", "serverIp", Event.SUCCESS, "ip=${serverIp}");
//            Cat.logMetricForCount("metric.key");
//            Cat.logMetricForDuration("metric.key", 5);
//            Thread.sleep(1500);
//            System.out.println(System.currentTimeMillis());
//            t.setStatus(Transaction.SUCCESS);
//        } catch (Exception e) {
//            t.setStatus(e);
//            Cat.logError(e);
//        } finally {
//            t.complete();
//        }
//        return "success";
//    }
}