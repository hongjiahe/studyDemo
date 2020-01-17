package com.hohe.importExcel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FilePropertyUtil {
	private static Properties filePath = new Properties();
	static{
		//但类装载只会装载一次，以至不能读取更新的数据。
		try (InputStream in = FilePropertyUtil.class.getClassLoader().getResourceAsStream("resources/file.properties")){
			filePath.load(in);
		} catch (IOException e) {
			throw new ExceptionInInitializerError("加载file.properties资源文件时出错!");
		}
	}
	public static Properties getFilePathProperty(){
		return filePath;
	}
	public static String getWebTempPath(){
		return filePath.getProperty("web.temp.path");
	}
	public static String getWebTempUrl(){
		return filePath.getProperty("web.temp.url");
	}
}
