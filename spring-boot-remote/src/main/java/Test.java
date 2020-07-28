import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @author mifei
 * @create 2020-05-12 15:11
 **/
public class Test {
    public static String getPid(String res) {
        res = res.substring(0, 15).replace("root", "");
        res = res.replace(" ", "");
        System.out.println(res);
        return res;
    }

    public static String run(String[] command,String key) throws IOException {
        Scanner input = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            try {
                //等待命令执行完成
                process.waitFor(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            InputStream is = process.getInputStream();
            input = new Scanner(is);
            while (input.hasNextLine()) {
                String res = input.nextLine();
                System.out.println("jar=="+res);
                if (res.indexOf(key) > 0) {
                    String pid = Test.getPid(res);
                    String c = "kill -9 " + pid;
                    System.out.println("kill==" + c);
                    exec(c);
                    break;
                }
            }
        } finally {
            if (input != null) {
                input.close();
            }
            if (process != null) {
                process.destroy();
            }
        }
        return "";
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        String cmd = "";
        if (args == null || args.length == 0) {
            System.out.println("请输入命令行参数");
        } else {
            for (int i = 0; i < args.length; i++) {//获得输入的命令
                cmd += args[i] + "";
            }
        }

        exec("pwd");
        //多条执行
//        List<String> command = new ArrayList<>();
//        command.add("/bin/sh");
//        command.add("-c");
//        command.add("ps -ef |grep jar");
//        String res = run(command.toArray(new String[command.size()]),cmd);
//        System.out.println(res);
    }

    private static void psef(String cmd, String key) {
        System.out.println("="+cmd);
        System.out.println("="+key);
        try {
            Process process = Runtime.getRuntime().exec(cmd);//执行命令
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line;
            while ((line = input.readLine()) != null) {//输出结果
                System.out.println("1==" + line);
                if (line.indexOf(key) > 0) {
                    String pid = Test.getPid(line);
                    String c = " kill -9 " + pid;
                    System.out.println("2==" + c);
                    exec(c);
                }
            }
        } catch (IOException e) {
            System.err.println("IOException " + e.getMessage());//捕捉异常
        }
    }

    private static void exec(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec(cmd);//执行命令
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line;
            while ((line = input.readLine()) != null) {//输出结果
                System.out.println("==:" + line);
            }
        } catch (IOException e) {
            System.err.println("IOException " + e.getMessage());//捕捉异常
        }
    }
}
