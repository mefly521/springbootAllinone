package com.demo.front.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 区划代码
 **/
@Data
@TableName(value = "area_code_2021")
public class AreaCode {


    @TableId(type = IdType.NONE)
    private Long code;
    private String name;
    private Integer level;
    private Long pcode;
    private String memo;

}
