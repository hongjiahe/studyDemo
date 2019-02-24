package com.hohe.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName PropertiesUtil 
 * @Description 读取配置文件信息
 * @author wanpeng
 * @date 2017年6月5日 下午2:57:33 
 *
 */
public class PropertiesUtil {
	
	private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
//	private final static String CONFIG_FILE = "/resources/aliyunoss.properties";
	private final static String CONFIG_FILE = "aliyunoss.properties";
	private static Properties pro;
	static {
		pro = new Properties();
		InputStream in = null;
		try {
//			String aa=Thread.currentThread().getContextClassLoader().getResource("/").getPath();
			String basepath=Thread.currentThread().getContextClassLoader().getResource("").toString();
			logger.info("basepath:-------------->"+basepath);
			//	System.out.println("获取总路径属性:******************"+basepath);
			String path =Thread.currentThread().getContextClassLoader().getResource(CONFIG_FILE).getPath();
			logger.info("path:-------------->"+path);
			//System.out.println("common.properties路径为："+path);
			//String path="F:/SCM_JABIL_BRANCH/20181223/v20181223/zlplatform.ess.web/src/test/java/zlplatform/ess/web/common.properties";
			File file = new File(path);
			in = new FileInputStream(file);
			pro.load(in);
			in.close();
		} catch (FileNotFoundException e) {
			logger.error("初始化配置文件失败！CONFIG_FILE="+CONFIG_FILE);
		} catch (Exception e) {
			logger.error("系统异常！");
			e.printStackTrace();
		}
	}
	
	public static String getString(String key) {
		return pro.getProperty(key);
	}
	
	
}
