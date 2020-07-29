package com.demo.controller;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.Date;

/**
 * @author mifei
 * @create 2020-05-12 16:41
 **/
@Slf4j
public class Scpclient {
    static private Scpclient instance;

    static synchronized public Scpclient getInstance(String IP, int port,
                                                     String username, String passward) {
        if (instance == null) {
            instance = new Scpclient(IP, port, username, passward);
        }
        return instance;
    }

    public Scpclient(String IP, int port, String username, String passward) {
        this.ip = IP;
        this.port = port;
        this.username = username;
        this.password = passward;
    }

    /**
     * 远程拷贝文件
     * @param remoteFile  远程源文件路径
     * @param localTargetDirectory 本地存放文件路径
     */
//    public Map<String,Object> getFile(String remoteFile, String localTargetDirectory) {
//        Connection conn = new Connection(ip,port);
//        try {
//            conn.connect();
//            boolean isAuthenticated = conn.authenticateWithPassword(username,
//                    password);
//            if (isAuthenticated == false) {
//                log.info(Level.ERROR_INT,"authentication failed");
//                return Common.pageMap(NetMsgUtil.INVOKING_SERVER_EXCEPTION,"authentication failed");
//            }
//            SCPClient client = new SCPClient(conn);
//            client.get(remoteFile, localTargetDirectory);
//            conn.close();
//        } catch (IOException e) {
//            logger.log(Level.ERROR_INT,e.getMessage());
//            return Common.pageMap(NetMsgUtil.INVOKING_SERVER_EXCEPTION,e.getMessage());
//        }
//        return Common.pageMap(NetMsgUtil.NET_NORMAL_MSG,"Document Success");
//    }

    public Connection getConnection() {
        Connection conn = new Connection(ip, port);
        try {
            conn.connect();
            boolean isAuthenticated = conn.authenticateWithPassword(username,
                    password);
            if (isAuthenticated == false) {
                log.info("authentication failed");
            }
        } catch (IOException e) {
            log.info(e.getMessage(),e);
        }
        return conn;
    }

    /**
     * 远程上传文件
     *
     * @param localFile             本地文件路径
     * @param remoteTargetDirectory 远程存放文件路径
     */
    public void putFile(String localFile, String remoteTargetDirectory) {
        try {
            Connection conn = getConnection();
            SCPClient client = new SCPClient(conn);
            client.put(localFile, remoteTargetDirectory);
            conn.close();
        } catch (IOException e) {
            log.info(e.getMessage(),e);
        }
    }

    /**
     * 远程上传文件并对上传文件重命名
     *
     * @param localFile             本地文件路径
     * @param remoteFileName远程文件名
     * @param remoteTargetDirectory 远程存放文件路径
     * @param mode                  默认"0600"，length=4
     */
    public void putFile(String localFile, String remoteFileName, String remoteTargetDirectory, String mode) {
        Connection conn = new Connection(ip, port);
        try {
            conn.connect();
            boolean isAuthenticated = conn.authenticateWithPassword(username,
                    password);
            if (isAuthenticated == false) {
                //logger.log(Level.ERROR_INT,"authentication failed");
            }
            SCPClient client = new SCPClient(conn);
            if ((mode == null) || (mode.length() == 0)) {
                mode = "0600";
            }
            client.put(localFile, remoteFileName, remoteTargetDirectory, mode);

            //重命名
            Session sess = conn.openSession();
            String tmpPathName = remoteTargetDirectory + File.separator + remoteFileName;
            String newPathName = tmpPathName.substring(0, tmpPathName.lastIndexOf("."));
            sess.execCommand("mv " + remoteFileName + " " + newPathName);

            conn.close();
        } catch (IOException e) {
            //logger.log(Level.ERROR_INT,e.getMessage());
        }
    }

    private static String DEFAULTCHART = "UTF-8";

    private String execmd(String cmd) {
        Connection connection = getConnection();;
        String result = "";
        try {
            if (connection != null) {
                Session session = connection.openSession();// 打开一个会话
                session.execCommand(cmd);// 执行命令
                result = processStdout(session.getStdout(), DEFAULTCHART);
                log.info(result);
                // 如果为得到标准输出为空，说明脚本执行出错了
                /*if (StringUtils.isBlank(result)) {
                 log.info("得到标准输出为空,链接conn:" + connection + ",执行的命令：" + cmd);
                 result = processStdout(session.getStderr(), DEFAULTCHART);
                } else {
                 log.info("执行命令成功,链接conn:" + connection + ",执行的命令：" + cmd);
                }*/
                connection.close();
                session.close();
            }
        } catch (IOException e) {
            log.info("执行命令失败,链接conn:" + connection + ",执行的命令：" + cmd + " " + e);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 解析脚本执行返回的结果集
     *
     * @param in      输入流对象
     * @param charset 编码
     * @return 以纯文本的格式返回
     */
    private static String processStdout(InputStream in, String charset) {
        InputStream stdout = new StreamGobbler(in);
        StringBuffer buffer = new StringBuffer();
        ;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout, charset));
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line + "\n");
                log.info(line);
            }
            br.close();
        } catch (UnsupportedEncodingException e) {
            log.info("解析脚本出错：" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            log.info("解析脚本出错：" + e.getMessage());
            e.printStackTrace();
        }
        return buffer.toString();
    }

    private String ip;
    private int port;
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private static Connection login(String ip, String username, String password) {
        boolean flag = false;
        Connection connection = null;
        try {
            connection = new Connection(ip);
            connection.connect();// 连接
            flag = connection.authenticateWithPassword(username, password);// 认证
            if (flag) {
                log.info("================登录成功==================");
                return connection;
            }
        } catch (IOException e) {
            log.info("=========登录失败=========" + e);
            connection.close();
        }
        return connection;
    }

    public static String getPid(String res) {
        res = res.substring(0, 15).replace("root", "");
        res = res.replace(" ", "");
        System.out.println(res);
        return res;
    }

    public static void main(String[] args) throws Exception {
        Scpclient scpclient = Scpclient.getInstance("47.98.190.69", 22, "root", "Mecome!1qaz");
        //上传jar
        scpclient.putFile("D:/temp/00/bg_5.jpg", "/home/board");


//        String res = scpclient.execmd(" ps -ef |grep jar ");
//        String[] lines = res.split("\\r?\\n");
//        for (String line : lines) {
//            System.out.println("" + line);
//            String pid = getPid(line);
//            if (!StringUtils.isEmpty(pid)) {
//                scpclient.execmd("kill -9 " + pid);//kill 掉旧的
//                break;
//            }
//        }
//        Thread.sleep(5000L);
//        String now = DateUtil.formatDate(new Date());
//        scpclient.execmd("sh /home/board_game/restart.sh");
    }
}
