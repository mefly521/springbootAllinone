package com.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author mifei
 * @create 2020-09-10 17:09
 **/
@Data
public class Favorite implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * title
     */
    private String title;

    /**
     * url
     */
    private String url;

    /**
     * tag
     */
    private String tag;

    /**
     * create_time
     */
    private Date createTime;
}
