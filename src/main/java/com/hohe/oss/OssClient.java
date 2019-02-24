package com.hohe.oss;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class OssClient {
	protected static Logger logger = LoggerFactory.getLogger(OssClient.class);
	
	//OSS 
	protected String ossHost;
	protected String account;
	protected String pwd;

	/**
	 * to be implement
	 */
	public static String getToken(String account, String pwd){
		return null;
	}
	
	public abstract OssFile uploadFile(String dir, String filename, InputStream in, String token); 
	
	public abstract OssFile uploadFile(String dir, String filename, String localFilePath, String token); 
	
	public abstract void downloadFile(String filename, String tempFilePath);
	
	public abstract InputStream downloadFile(String filename);

	public abstract void downloadZipFile(List<String> filenameList, String zipName);
	
	public abstract InputStream downloadZipFile(List<String> filenameList);
	
	public abstract void initProperties(Map<String, Object> map);
	
}
