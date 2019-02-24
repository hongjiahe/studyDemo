package com.hohe.oss;

import java.io.Serializable;

/**
 * aliyun外网地址hlfs01.oss-cn-shenzhen.aliyuncs.com
 * hlfs01为bulket，oss服务分配给不同的服务使用
 * 内网地址    hlfs01.oss-cn-shenzhen-internal.aliyuncs.com 
 * 
 * 在保存文件时，阿里云会返回requestId，唯一，但不作为存、取、查找凭证，
 * 阿里云存、取、查找时直接使用文件地址
 * 
 * @author zaku
 *
 */
public class AliyunOssFile extends OssFile implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 58841188807247647L;

	private String requestId;
	
	//文件地址
	private String endPoint;
	
	//文件外网地址
	private String internalEndPoint;
	
	// 文件大小
	private long size;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public String getInternalEndPoint() {
		return internalEndPoint;
	}

	public void setInternalEndPoint(String internalEndPoint) {
		this.internalEndPoint = internalEndPoint;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "AliyunOssFile [requestId=" + requestId + ", endPoint=" + endPoint + ", internalEndPoint="
				+ internalEndPoint + "]";
	}
	
	
	
}
