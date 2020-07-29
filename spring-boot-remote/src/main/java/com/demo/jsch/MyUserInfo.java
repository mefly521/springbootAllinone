package com.demo.jsch;

import com.jcraft.jsch.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mifei
 * @create 2020-05-13 10:56
 **/
public class MyUserInfo implements UserInfo {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private String password;

    @Override
    public String getPassphrase() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean promptPassword(String message) {
        return true;
    }

    @Override
    public boolean promptPassphrase(String message) {
        return true;
    }

    @Override
    public boolean promptYesNo(String message) {
        return true;
    }

    @Override
    public void showMessage(String message) {
        logger.info(message);
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
