package com.sjzy.data.monitor.common;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wpp on 2020/8/12.
 *
 * 相关的静态变量
 */
public class Constants {

    public static final String MYSQL_URL = "?serverTimezone=UTC&setUnicode=true&characterEncoding=utf8";

    public static final String MYSQL_DATABASE = "monitor";

    public static final String EMAIL_PERSONAL = "三井核保邮件监控通知";

    public static final String EMAIL_TOPIC = "监控报警";

    public static final List<String> MONITOR_IPS = new ArrayList<String>(Arrays.asList("192.1.6.73","192.1.6.74","192.1.6.75"));

    public static final String EXEC_JPS_CMD = " '/usr/local/java/jdk1.8.0_221/bin/jps'";

    public static final String EXEC_SOFT_CMD = " 'free -m | grep Mem'";

    public static final String EXEC_HARD_CMD = " 'df -h | grep /dev/mapper/centos-root'";

    public static final String GET_SOFT_MONITOR_SQL = "select * from monitor.tb_thre_monitor where name='soft'";

    public static final String GET_HARD_MONITOR_SQL = "select * from monitor.tb_thre_monitor where name='hard'";

    public static final String GET_JOB_MONITOR_SQL = "select * from monitor.tb_job_monitor";

    public static final String TRUN_JOB_MONITOR_SQL = "delete from monitor.tb_job_monitor";

    public static final String TRUN_SH_MONITOR_SQL = "delete from monitor.tb_thre_monitor";

    public static final String INSERT_JOB_MONITOR_SQL = "INSERT INTO monitor.tb_job_monitor (pid,job_name,state) VALUES (?,?,?)";

    public static final String INSERT_SH_MONITOR_SQL = "INSERT INTO monitor.tb_thre_monitor (roboot,name,threshold,state) VALUES (?,?,?,?)";

    public static final Integer SECOND = 1000;

}

