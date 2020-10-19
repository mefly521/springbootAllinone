package com.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.FileWriter;
import java.io.IOException;

/**
 * @author mifei
 * @create 2019-11-08 13:17
 **/
@Data
@TableName("test")
public class TestBean {

    @TableId(value = "id",type = IdType.AUTO)
    private String id;
    private String name;


}
