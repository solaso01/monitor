package com.sjzy.data.monitor.utils;

/**
 * util工具类
 * 加载配置信息
 * 作者：wpp摘抄
 * 创建日期：2020年8月12日
 * 修改日期: 2020年8月12日
 */

import java.io.*;
import java.util.Properties;

public final class PropertiesUtil {
  private PropertiesUtil() {
  }

  public static Properties getProperties(String path) throws IOException{
    Properties props = null;
    InputStream in = null;

    try {
      in = new BufferedInputStream(new FileInputStream(new File(path)));
      props = new Properties();
      props.load(in);
      return props;
    } catch (IOException e) {
      throw e;
    } finally {
      if (in != null) {
        in.close();
      }
    }
  }
}
