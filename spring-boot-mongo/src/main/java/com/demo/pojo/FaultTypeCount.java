package com.demo.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author mifei
 * @create 2019-11-08 13:17
 **/
@Data
public class FaultTypeCount implements Serializable {

    private String code;
    private Integer count;

}
