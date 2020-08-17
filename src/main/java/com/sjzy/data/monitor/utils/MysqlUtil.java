package com.sjzy.data.monitor.utils;

import com.google.common.collect.Lists;
import com.sjzy.data.monitor.common.Constants;
import com.sjzy.data.monitor.pojo.JobInfoDemo;
import com.sjzy.data.monitor.pojo.SHDemo;
import com.sjzy.data.monitor.rs.ListResultSetExtractor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class MysqlUtil {

    /**
     * 查出记录的job信息
     * @param jdbcTemplate  jdbcTemplate
     * @return              结果集
     */
    public static List<JobInfoDemo> getJobInfo(JdbcTemplate jdbcTemplate) {
        List<JobInfoDemo> result = new ArrayList<JobInfoDemo>();
        Optional<List> list = Optional.ofNullable(jdbcTemplate.query(Constants.GET_JOB_MONITOR_SQL
                                                            ,new ListResultSetExtractor(Lists.newArrayList("pid","job_name"))));
        for (int i = 0;i < list.get().size(); i++ ){
            result.add(new JobInfoDemo(list.get().get(i).toString(), list.get().get(++i).toString()));
        }
        return result;
    }


    /**
     * 查出记录的内存信息
     * @param jdbcTemplate  jdbcTemplate
     * @return              结果集
     */
    public static List<SHDemo> getSoftInfo(JdbcTemplate jdbcTemplate) {
        List<SHDemo> result = new ArrayList<SHDemo>();
        Optional<List> list = Optional.ofNullable(jdbcTemplate.query(Constants.GET_SOFT_MONITOR_SQL
                                                            ,new ListResultSetExtractor(Lists.newArrayList("roboot","threshold"))));
        for (int i = 0;i < list.get().size(); i++ ){
            result.add(new SHDemo(Integer.parseInt(list.get().get(i).toString()), Integer.parseInt(list.get().get(++i).toString())));
        }
        return result;
    }


    /**
     * 查出记录的磁盘信息
     * @param jdbcTemplate  jdbcTemplate
     * @return              结果集
     */
    public static List<SHDemo> getHardInfo(JdbcTemplate jdbcTemplate) {
        List<SHDemo> result = new ArrayList<SHDemo>();
        Optional<List> list = Optional.ofNullable(jdbcTemplate.query(Constants.GET_HARD_MONITOR_SQL
                                                            ,new ListResultSetExtractor(Lists.newArrayList("roboot","threshold"))));
        for (int i = 0;i < list.get().size(); i++ ){
            result.add(new SHDemo(Integer.parseInt(list.get().get(i).toString()), Integer.parseInt(list.get().get(++i).toString())));
        }
        return result;
    }


    /**
     * 初始化记录的job信息（先清空再插入）
     * @param iPJobInfo         要记录的信息
     * @param jdbcTemplate      jdbcTemplate
     */
    public static void changeJobInfo(List<JobInfoDemo> iPJobInfo, JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute(Constants.TRUN_JOB_MONITOR_SQL);
        jdbcTemplate.batchUpdate(Constants.INSERT_JOB_MONITOR_SQL, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i)throws SQLException {
                String name=iPJobInfo.get(i).getName();
                String pid=iPJobInfo.get(i).getPid();
                ps.setString(1, pid);
                ps.setString(2, name);
                ps.setInt(3, 1);
            }
            public int getBatchSize() {
                return iPJobInfo.size();
            }
        });

    }

    /**
     * 初始化记录的内存磁盘信息（先清空再插入）
     * @param ipSoftInfo         要记录的信息
     * @param jdbcTemplate      jdbcTemplate
     */
    public static void changeSHInfo(List<SHDemo> mysqlSoftInfo, List<SHDemo> ipSoftInfo
                                            , List<SHDemo> mysqlHardInfo, List<SHDemo> ipHardInfo, JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute(Constants.TRUN_SH_MONITOR_SQL);
        jdbcTemplate.batchUpdate(Constants.INSERT_SH_MONITOR_SQL, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i)throws SQLException {
                Integer ip=ipSoftInfo.get(i).getRoboot();
                Integer thresholdMax = ipSoftInfo.get(i).getThreshold();

                //找到最大的值
                for ( SHDemo single : mysqlSoftInfo ){
                    if ( single.getRoboot() == ip ){
                        thresholdMax = Math.max(thresholdMax,single.getThreshold());
                    }
                }
                ps.setInt(1, ip);
                ps.setString(2, "soft");
                ps.setInt(3, thresholdMax);
                ps.setInt(4, 1);
            }
            public int getBatchSize() {
                return ipSoftInfo.size();
            }
        });
        jdbcTemplate.batchUpdate(Constants.INSERT_SH_MONITOR_SQL, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i)throws SQLException {
                Integer ip = ipHardInfo.get(i).getRoboot();
                Integer thresholdMax = ipHardInfo.get(i).getThreshold();

                //找到最大的值
                for ( SHDemo single : mysqlHardInfo ){
                    if ( single.getRoboot() == ip ){
                        thresholdMax = Math.max(thresholdMax,single.getThreshold());
                    }
                }
                ps.setInt(1, ip);
                ps.setString(2, "hard");
                ps.setInt(3, thresholdMax);
                ps.setInt(4, 1);
            }
            public int getBatchSize() {
                return ipHardInfo.size();
            }
        });
    }
}
