package com.sjzy.data.monitor.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.sjzy.data.monitor.common.Constants;
import com.sjzy.data.monitor.utils.PropertiesUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Created by w on 2019/12/23.
 */
public class DBMailHelper {

    /**
     * 得到DruidDataSource
     * @param path  配置文件地址
     * @return      DruidDataSource
     */
    public DruidDataSource getDataSource(String path) {
        Properties serverProp = init(path);
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://" + serverProp.getProperty("mysqlIp") + ":" +
                        serverProp.getProperty("mysqlPort") + "/" + Constants.MYSQL_DATABASE + Constants.MYSQL_URL);
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUsername(serverProp.getProperty("mysqlUserName"));
        dataSource.setValidationQuery("select 1");
        dataSource.setInitialSize(3);
        dataSource.setMaxActive(50);
        dataSource.setMinIdle(5);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestWhileIdle(true);
        dataSource.setValidationQueryTimeout(3);
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setPoolPreparedStatements(false);
        dataSource.setPassword(serverProp.getProperty("mysqlPasswd"));
        return dataSource;
    }

    /**
     * 配置获取
     * @param path  配置文件地址
     * @return      返回配置Properties
     */
    public static Properties init(String path) {
        Properties serverProp = null;
        try {
            serverProp = PropertiesUtil.getProperties(path);
//            serverProp = PropertiesUtil.getProperties("C:\\Users\\Administrator\\Desktop\\maxwellmonitor\\src\\main\\resources\\wppProperty.properties");
        } catch (IOException e) {
            System.out.println("未找到配置文件");
        }
        return serverProp;
    }

    /**
     * 获取要发送的邮件地址
     * @param path  配置文件地址
     * @return      邮件地址
     */
    public List<String> getToMailAddrs(String path) {
        Properties serverProp = init(path);
        List<String> toMailAddrs = new ArrayList<String>();
        Collections.addAll(toMailAddrs, serverProp.getProperty("toMailAddrs").split(","));
        return toMailAddrs;
    }
}
