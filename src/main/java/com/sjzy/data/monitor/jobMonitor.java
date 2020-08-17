package com.sjzy.data.monitor;


import com.alibaba.druid.pool.DruidDataSource;
import com.sjzy.data.monitor.common.Constants;
import com.sjzy.data.monitor.db.DBMailHelper;
import com.sjzy.data.monitor.mail.SendMailSmtp;
import com.sjzy.data.monitor.pojo.JobInfoDemo;
import com.sjzy.data.monitor.pojo.SHDemo;
import com.sjzy.data.monitor.pojo.TwoTuple;
import com.sjzy.data.monitor.utils.ConnectLinuxUtil;
import com.sjzy.data.monitor.utils.MysqlUtil;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;


/**
 * Created by wpp on 2020/8/14.
 */
public class jobMonitor {

    public static void main(String[] args) {

        Calendar cal = Calendar.getInstance();
        Date firstTime = cal.getTime();

        int period = Constants.SECOND * 60 * 5;

        DruidDataSource dataSource = new DBMailHelper().getDataSource(args[0]);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        Timer timer = new Timer();

        timer.schedule(new wppMailTimerTask(args[0],jdbcTemplate), firstTime, period);

        dataSource.close();

    }


    /**
     * 关键类
     */
    public static class wppMailTimerTask extends TimerTask {

        private String path = null;

        private JdbcTemplate jdbcTemplate = null;

        private wppMailTimerTask() {
        }

        private wppMailTimerTask(String path,JdbcTemplate jdbcTemplate) {
            this.path = path;
            this.jdbcTemplate = jdbcTemplate;
        }

        @Override
        public void run() {

            //获取job信息，并处理
            List<JobInfoDemo> iPJobInfo = ConnectLinuxUtil.getJobInfo(Constants.MONITOR_IPS, Constants.EXEC_JPS_CMD);
            List<JobInfoDemo> mysqlJobInfo = MysqlUtil.getJobInfo(jdbcTemplate);

            //获取内存信息，并处理
            List<SHDemo> ipSoftInfo = ConnectLinuxUtil.getSoftInfo(Constants.MONITOR_IPS, Constants.EXEC_SOFT_CMD);
            List<SHDemo> mysqlSoftInfo = MysqlUtil.getSoftInfo(jdbcTemplate);

            //获取磁盘信息，并处理
            List<SHDemo> ipHardInfo = ConnectLinuxUtil.getHardInfo(Constants.MONITOR_IPS, Constants.EXEC_HARD_CMD);
            List<SHDemo> mysqlHardInfo = MysqlUtil.getHardInfo(jdbcTemplate);

            //是否发送邮件
            TwoTuple<Boolean,String> tmp = isSend(mysqlJobInfo,iPJobInfo,mysqlSoftInfo,ipSoftInfo,mysqlHardInfo,ipHardInfo);

            //如果发送邮件，则。。。。
            if ( tmp.first ){
                SendMailSmtp.SendMail(tmp.second,path);

                //更改记录
                MysqlUtil.changeJobInfo(iPJobInfo,jdbcTemplate);
                MysqlUtil.changeSHInfo(mysqlSoftInfo,ipSoftInfo,mysqlHardInfo,ipHardInfo,jdbcTemplate);
            }

        }
    }

    public static TwoTuple<Boolean,String> isSend(List<JobInfoDemo> mysqlJobInfo, List<JobInfoDemo> iPJobInfo,
                                                  List<SHDemo> mysqlSoftInfo, List<SHDemo> ipSoftInfo, List<SHDemo> mysqlHardInfo, List<SHDemo> ipHardInfo) {
        return ConnectLinuxUtil.getMailMessage(mysqlJobInfo,iPJobInfo,mysqlSoftInfo,ipSoftInfo,mysqlHardInfo,ipHardInfo);
    }


}
