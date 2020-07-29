package com.demo.controller;

import lombok.Data;

import java.util.List;

/**
 * @author mifei
 * @create 2019-09-30 14:44
 **/
@Data
public class UserInfo {

    private Integer userId;
    private String uname;//用户名
    private Long deptId;
    private Boolean isLeader;
    private List<Integer> roleIds;
    private String token;
    private String realname;
    private String post;
    private Long comId;
    private Integer terminalId;//接入端

    /**
     * 当前角色id
     */
    private Integer currentRoleId;

    public Boolean getIsLeader() {
        return isLeader;
    }

    public void setIsLeader(Boolean leader) {
        isLeader = leader;
    }
}
