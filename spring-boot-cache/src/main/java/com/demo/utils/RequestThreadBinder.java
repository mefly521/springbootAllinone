package com.demo.utils;

import com.demo.controller.UserInfo;

/**
 * @Description:线程绑定类
 * @Author: mifei
 * @Date: 2018-10-18
 **/
public class RequestThreadBinder {
    // 声明当前线程 指定泛型为request
    private static ThreadLocal<UserInfo> local = new ThreadLocal<>();

    // 将request绑定到当前线程的方法
    public static void bindUser(UserInfo request) {
        local.set(request);
    }

    // 从当前线程获取request的方法
    public static UserInfo getUser() {
        return local.get();
    }

    // 从当前线程移除request的方法
    public static void removeUser() {
        local.remove();
    }

    public static Long getComId() {
        UserInfo userInfo = getUserInfo();
        return userInfo.getComId();
    }

    public static UserInfo getUserInfo() {
        UserInfo user = RequestThreadBinder.getUser();
        if (user == null) {
            UserInfo userInfo = new UserInfo();
            //TODO 先写死一个用户用来测试
            userInfo.setUserId(3);
            userInfo.setDeptId(101L);
            userInfo.setIsLeader(false);
            return userInfo;
        } else {
            return user;
        }
    }
}
