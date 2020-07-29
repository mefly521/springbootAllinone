package com.demo.jsch;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SSHClientTest {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 直接执行命令
     */
    public void exec() {
        JSch jsch = new JSch();
        try {
            Session session = jsch.getSession("root", "47.98.190.69", 22);
            MyUserInfo ui = new MyUserInfo();
            ui.setPassword("Mecome!1qaz");
            session.setUserInfo(ui);
            session.connect();
            String cmd = "nohup java -jar /home/board_game/spring-boot-spider-1.0.jar -Dspring.config.location=/home/board_game/config/application.yml > /home/board_game/run.log 2>&1 &";
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(cmd);
            InputStream in = channel.getInputStream();
            OutputStream out = channel.getOutputStream();
            ((ChannelExec) channel).setErrStream(System.err);

            channel.connect();
            out.flush();

            logger.info(IOUtils.toString(in, Charset.forName("utf-8")));
            channel.disconnect();
            session.disconnect();
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    /**
     * sudo + cmd的形式
     */
    public void execWithSudo() {
        JSch jsch = new JSch();
        try {
            String password = "testuser1";
            Session session = jsch.getSession("omc", "192.168.101.35", 22);
            MyUserInfo ui = new MyUserInfo();
            ui.setPassword(password);
            session.setUserInfo(ui);
            session.connect();
            String cmd = "sudo -S -p '' systemctl start sasHandler";
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(cmd);
            InputStream in = channel.getInputStream();
            OutputStream out = channel.getOutputStream();
            ((ChannelExec) channel).setErrStream(System.err);

            channel.connect();

            out.write((password + "\n").getBytes());
            out.flush();

            logger.info(IOUtils.toString(in, Charset.forName("utf-8")));
            channel.disconnect();
            session.disconnect();
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public static void main(String[] args) {
        new SSHClientTest().exec();
    }
}