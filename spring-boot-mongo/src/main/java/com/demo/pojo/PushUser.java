package com.demo.pojo;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @Description 推送的用户
 * @Author DELL
 * @Create 2019-05-28
 * @Since 1.0.0
 */
@Data
@Document(collection = "EV_PUSH_USER")
@CompoundIndex(def = "{'userId' : 1, 'terminalId' : 1, 'userIds' : 1, 'appId' : 1,'client':1}", background = true)
public class PushUser implements Serializable {

    @Id
    private ObjectId id;

    private Set<ObjectId> userIds;

    private ObjectId terminalId;

    private ObjectId appId;

    private Long terminalPort;

    private Date updateAt;

    private Long action;

}
