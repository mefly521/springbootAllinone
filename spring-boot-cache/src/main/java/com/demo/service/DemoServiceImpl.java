/**
 * Copyright (c) 2016-2019  All rights reserved.
 */

package com.demo.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service("demoService")
public class DemoServiceImpl  {

    private static final Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);

    /**
     * 使用静态方法当参数
     * @return
     */
    @Cacheable(value="cache:template", key="T(com.demo.utils.RequestThreadBinder).getComId()+':'+#root.args[0]")
    public String test(int id){
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        System.out.println("没有缓存");
        return "123";
    }

    /**
     * 使用缓存
     * @return
     */
    @Cacheable(value="demo")
    public List<String> getListCacheable(){
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        System.out.println("没有缓存");
        return list;
    }

    /**
     * 和 @Cacheable 不同的是，它每次都会触发真实方法的调用
     * @return
     */
    @CachePut(value="cache:column")
    public List<String> getListCachePut(int i){
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        System.out.println("模拟经过db");
        return list;
    }
    /**
     * 清除缓存,
     * @return
     */
    @CacheEvict(value={"cache:template","cache:column"})
    public List<String> getListCacheEvict(int i){
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        System.out.println("模拟经过db");
        return list;
    }
}
