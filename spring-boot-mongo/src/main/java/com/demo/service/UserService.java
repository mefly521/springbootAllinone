package com.demo.service;

import com.demo.pojo.User;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author mifei
 * @create 2020-03-30 10:50
 **/
@Service
public class UserService {
    @Resource
    MongoTemplate mongotemplate;

    public List<User> getAllUser(){
        return mongotemplate.findAll(User.class);
    }

    //@Transactional
    public boolean addUser(){
        User user = new User();
        user.setId(5L);
        mongotemplate.save(user);
        int a = 1/0;    //事务测试代码

        User user2 = new User();
        user2.setId(6L);
        mongotemplate.save(user2);
        return true;
    }
}
