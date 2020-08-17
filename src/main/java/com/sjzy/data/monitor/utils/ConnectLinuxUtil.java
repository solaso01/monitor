package com.sjzy.data.monitor.utils;

/**
 * Created by wpp on 2020/8/12.
 */

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.sjzy.data.monitor.pojo.JobInfoDemo;
import com.sjzy.data.monitor.pojo.RemoteConnect;
import com.sjzy.data.monitor.pojo.SHDemo;
import com.sjzy.data.monitor.pojo.TwoTuple;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.*;

/**
 *
 * 描述：执行shell操作
 * 作者：wpp摘抄
 * 创建日期：2020年8月12日
 * 修改日期: 2020年8月12日
 */
public class ConnectLinuxUtil {

    private static String DEFAULTCHARTSET = "UTF-8";
    private static Connection conn;

    /**
     * @throws
     * @Title: login
     * @Description: 用户名密码方式  远程登录linux服务器
     * @return: Boolean
     */
    public static Boolean login(RemoteConnect remoteConnect) {
        boolean flag = false;
        try {
            conn = new Connection(remoteConnect.getIp());
            conn.connect();// 连接
            flag = conn.authenticateWithPassword(remoteConnect.getUserName(), remoteConnect.getPassword());// 认证
            if (flag) {
                System.out.println("认证成功！");
            } else {
                System.out.println("认证失败！");
                conn.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * @param remoteConnect
     * @param keyFile       一个文件对象指向一个文件，该文件包含OpenSSH密钥格式的用户的DSA或RSA私钥(PEM，不能丢失"-----BEGIN DSA PRIVATE KEY-----" or "-----BEGIN RSA PRIVATE KEY-----"标签
     * @param keyfilePass   如果秘钥文件加密 需要用该参数解密，如果没有加密可以为null
     * @return Boolean
     * @throws
     * @Title: loginByKey
     * @Description: 秘钥方式  远程登录linux服务器
     */
    public static Boolean loginByFileKey(RemoteConnect remoteConnect, File keyFile, String keyfilePass) {
        boolean flag = false;
        // 输入密钥所在路径
        // File keyfile = new File("C:\\temp\\private");
        try {
            conn = new Connection(remoteConnect.getIp());
            conn.connect();
            // 登录认证
            flag = conn.authenticateWithPublicKey(remoteConnect.getUserName(), keyFile, keyfilePass);
            if (flag) {
                System.out.println("认证成功！");
            } else {
                System.out.println("认证失败！");
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * @param remoteConnect
     * @param keys          一个字符[]，其中包含用户的DSA或RSA私钥(OpenSSH密匙格式，您不能丢失“----- begin DSA私钥-----”或“-----BEGIN RSA PRIVATE KEY-----“标签。char数组可以包含换行符/换行符。
     * @param keyPass       如果秘钥字符数组加密  需要用该字段解密  否则不需要可以为null
     * @return Boolean
     * @throws
     * @Title: loginByCharsKey
     * @Description: 秘钥方式  远程登录linux服务器
     */
    public static Boolean loginByCharsKey(RemoteConnect remoteConnect, char[] keys, String keyPass) {

        boolean flag = false;
        // 输入密钥所在路径
        // File keyfile = new File("C:\\temp\\private");
        try {
            conn = new Connection(remoteConnect.getIp());
            conn.connect();
            // 登录认证
            flag = conn.authenticateWithPublicKey(remoteConnect.getUserName(), keys, keyPass);
            if (flag) {
                System.out.println("认证成功！");
            } else {
                System.out.println("认证失败！");
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * @param cmd 脚本命令
     * @throws
     * @Title: execute
     * @Description: 远程执行shll脚本或者命令
     * @return: result 命令执行完毕返回结果
     */
    public static String execute(String cmd) {
        String result = "";
        try {
            Session session = conn.openSession();// 打开一个会话
            session.execCommand(cmd);// 执行命令
            result = processStdout(session.getStdout(), DEFAULTCHARTSET);
            // 如果为得到标准输出为空，说明脚本执行出错了
            if (StringUtils.isBlank(result)) {
                result = processStdout(session.getStderr(), DEFAULTCHARTSET);
            }
            conn.close();
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param cmd
     * @return String 命令执行成功后返回的结果值，如果命令执行失败，返回空字符串，不是null
     * @throws
     * @Title: executeSuccess
     * @Description: 远程执行shell脚本或者命令
     */
    public static String executeSuccess(String cmd) {
        String result = "";
        try {
            Session session = conn.openSession();// 打开一个会话
            session.execCommand(cmd);// 执行命令
            result = processStdout(session.getStdout(), DEFAULTCHARTSET);
            conn.close();
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param in      输入流对象
     * @param charset 编码
     * @return String 以纯文本的格式返回
     * @throws
     * @Title: processStdout
     * @Description: 解析脚本执行的返回结果
     */
    public static String processStdout(InputStream in, String charset) {
        InputStream stdout = new StreamGobbler(in);
        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout, charset));
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line + "\n");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * @return String
     * @throws
     * @Title: ConnectLinux
     * @Description: 通过用户名和密码关联linux服务器
     */
    public static String connectLinux(String ip, String userName, String password, String commandStr) {

        System.out.println("ConnectLinuxCommand  scpGet===" + "ip:" + ip + "  userName:" + userName + "  commandStr:"
                + commandStr);

        String returnStr = "";
        boolean result = true;

        RemoteConnect remoteConnect = new RemoteConnect();
        remoteConnect.setIp(ip);
        remoteConnect.setUserName(userName);
        remoteConnect.setPassword(password);
        try {
            if (login(remoteConnect)) {
                returnStr = execute(commandStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (StringUtils.isBlank(returnStr)) {
            result = false;
        }
        conn.close();
        return returnStr;
    }

    /**
     * @param ip       ip(其他服务器)
     * @param userName   用户名(其他服务器)
     * @param password   密码(其他服务器)
     * @param remoteFile 文件位置(其他服务器)
     * @param localDir   本服务器目录
     * @return void
     * @throws IOException
     * @throws
     * @Title: scpGet
     * @Description: 从其他服务器获取文件到本服务器指定目录
     */
    public static void scpGet(String ip, String userName, String password, String remoteFile, String localDir)
            throws IOException {

        System.out.println("ConnectLinuxCommand  scpGet===" + "ip:" + ip + "  userName:" + userName + "  remoteFile:"
                + remoteFile + "  localDir:" + localDir);
        RemoteConnect remoteConnect = new RemoteConnect();
        remoteConnect.setIp(ip);
        remoteConnect.setUserName(userName);
        remoteConnect.setPassword(password);

        if (login(remoteConnect)) {
            SCPClient client = new SCPClient(conn);
            client.get(remoteFile, localDir);
            conn.close();
        }
    }


    /**
     * job信息获取
     * @param ipAddess      IP地址
     * @param commandStr    命令
     * @return              job信息
     */
    public static List<JobInfoDemo> getJobInfo(List<String> ipAddess, String commandStr) {

        List<JobInfoDemo> result = new ArrayList<JobInfoDemo>();

        for ( String ip : ipAddess ){
            String info = exeCmd("ssh "+ ip + commandStr);
            String[] EveryInfoString = info.split("\n");
            for ( int i = 0;i < EveryInfoString.length; i++ ){
                //只要kafka和spark
                if ( EveryInfoString[i].contains("Kafka") || EveryInfoString[i].contains("SparkSubmit") ){
                    JobInfoDemo single = new JobInfoDemo(EveryInfoString[i].split(" ")[0],ip+": "+EveryInfoString[i].split(" ")[1]);
                    result.add(single);
                }
            }
        }

        return result;

    }


    /**
     * 内存判断的逻辑
     * @param ipAddess
     * @param commandStr
     * @return
     */
    public static List<SHDemo> getSoftInfo(List<String> ipAddess, String commandStr) {

        List<SHDemo> result = new ArrayList<SHDemo>();

        for ( String ip : ipAddess ){
            String info = exeCmd("ssh "+ ip + commandStr);
            String[] EveryInfoString = info.split("\\s+");
            Integer softTotal = Integer.parseInt(EveryInfoString[1]);
            Integer softAva = Integer.parseInt(EveryInfoString[3]) + Integer.parseInt(EveryInfoString[5]);
            Integer ipInt = Integer.parseInt(ip.split("\\.")[3]);
            result.add(new SHDemo(ipInt,(softTotal-softAva)*100/softTotal));
        }

        return result;

    }


    /**
     * 磁盘判断的逻辑
     * @param ipAddess
     * @param commandStr
     * @return
     */
    public static List<SHDemo> getHardInfo(List<String> ipAddess, String commandStr) {

        List<SHDemo> result = new ArrayList<SHDemo>();

        for ( String ip : ipAddess ){
            String info = exeCmd("ssh "+ ip + commandStr);
            String[] EveryInfoString = info.split("\\s+");
            Integer hard = Integer.parseInt(EveryInfoString[4].replace("%",""));
            Integer ipInt = Integer.parseInt(ip.split("\\.")[3]);
            result.add(new SHDemo(ipInt,hard));
        }

        return result;
    }


    /**
     * 比较两个的信息
     * @param mysqlJobInfo  mysql中job记录的信息
     * @param ipJobInfo     得到的信息
     * @param mysqlSoftInfo mysql中内存记录的信息
     * @param ipSoftInfo    得到内存的信息
     * @param mysqlHardInfo mysql中磁盘记录的信息
     * @param ipHardInfo    得到自盘的信息
     * @return              消息
     */
    public static TwoTuple<Boolean,String> getMailMessage(List<JobInfoDemo> mysqlJobInfo, List<JobInfoDemo> ipJobInfo, List<SHDemo> mysqlSoftInfo, List<SHDemo> ipSoftInfo, List<SHDemo> mysqlHardInfo, List<SHDemo> ipHardInfo) {

        String message = "";
        Boolean flag = false;

        //job验证
        Map<String,String> mysqlJobInfoMap = new HashMap<String,String>();
        Map<String,String> ipJobInfoMap = new HashMap<String,String>();

        for ( JobInfoDemo single : mysqlJobInfo ){
            mysqlJobInfoMap.put(single.getPid(),single.getName());
        }
        for ( JobInfoDemo single : ipJobInfo ){
            ipJobInfoMap.put(single.getPid(),single.getName());
        }

        for (Map.Entry<String,String> single : mysqlJobInfoMap.entrySet() ){
            if ( !ipJobInfoMap.containsKey(single.getKey()) ){
                message = message+"有job挂掉（或kill掉）了，具体为：["+single.getKey()+"] 内容为： ["+single.getValue()+"]!\n";
                flag = true;
            }
        }

        for (Map.Entry<String,String> single : ipJobInfoMap.entrySet() ){
            if ( !mysqlJobInfoMap.containsKey(single.getKey()) ){
                message = message+"又有新的job加入，具体为：["+single.getKey()+"] 内容为： ["+single.getValue()+"]!\n";
                flag = true;
            }
        }


        //hard验证
        for ( SHDemo ipSingle : ipHardInfo ){
            for ( SHDemo mysqlSingle : mysqlHardInfo ) {
                if ( ipSingle.getRoboot() == mysqlSingle.getRoboot() && ipSingle.getThreshold() > mysqlSingle.getThreshold() ) {
                    message = message+"["+ipSingle.getRoboot()+"] 的磁盘空间已经到达："+ipSingle.getThreshold()+"% 请注意！\n";
                    flag = true;
                }
            }
        }

        //soft验证
        for ( SHDemo ipSingle : ipSoftInfo ){
            for ( SHDemo mysqlSingle : mysqlSoftInfo ) {
                if (  ipSingle.getRoboot() == mysqlSingle.getRoboot() && ipSingle.getThreshold() > mysqlSingle.getThreshold() ) {
                    message = message+"["+ipSingle.getRoboot()+"] 的内存空间已经到达："+ipSingle.getThreshold()+"% 请注意！\n";
                    flag = true;
                }
            }
        }

        return new TwoTuple<Boolean,String>(flag, message);

    }


    /**
     * @return void
     * @throws IOException
     * @throws
     * @Title: scpPut
     * @Description: 将文件复制到其他计算机中
     */
    public static void scpPut(String ip, String userName, String password, String localFile, String remoteDir)
            throws IOException {
        System.out.println("ConnectLinuxCommand  scpPut===" + "ip:" + ip + "  userName:" + userName + "  localFile:"
                + localFile + "  remoteDir:" + remoteDir);

        RemoteConnect remoteConnect = new RemoteConnect();
        remoteConnect.setIp(ip);
        remoteConnect.setUserName(userName);
        remoteConnect.setPassword(password);

        if (login(remoteConnect)) {
            SCPClient client = new SCPClient(conn);
            client.put(localFile, remoteDir);
            conn.close();
        }
    }


    /**
     * 获取命令返回值
     * @param commandStr    shell命令
     * @return
     */
    public static String exeCmd(String commandStr) {

        String result = null;
        try {
            String[] cmd = new String[]{"/bin/sh", "-c",commandStr};
            Process ps = Runtime.getRuntime().exec(cmd);

            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                //执行结果加上回车
                sb.append(line).append("\n");
            }
            result = sb.toString();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }

}
