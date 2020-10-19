package com.demo.entity;

import lombok.Data;

import java.util.List;

/**
 * @author mifei
 * @create 2020-07-30 13:59
 **/
@Data
public class Authorize {
    private String realm;
    private String randomKey;
    private String encryptType;
    private String publickey;
}
