package com.hohe.util;

import java.util.List;
import java.util.Map;

import com.hohe.model.ImprotTemplate;

/**
 * 通用导入Excel返回值
 * @author lky
 * @date 2019.05.07
 */
public class ImportExcelResult {

	List<Map<String,Object>> dataList;//数据集合
	
	List<ImprotTemplate> tableTHList;//列集合
	
	int errorsize;//错误数量
	
	String errorFileName; // 错误文件名
	
	String key;//后台缓存KEY

	public List<ImprotTemplate> getTableTHList() {
		return tableTHList;
	}

	public void setTableTHList(List<ImprotTemplate> tableTHList) {
		this.tableTHList = tableTHList;
	}

	public List<Map<String, Object>> getDataList() {
		return dataList;
	}

	public void setDataList(List<Map<String, Object>> dataList) {
		this.dataList = dataList;
	}

	public int getErrorsize() {
		return errorsize;
	}

	public void setErrorsize(int errorsize) {
		this.errorsize = errorsize;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getErrorFileName() {
		return errorFileName;
	}

	public void setErrorFileName(String errorFileName) {
		this.errorFileName = errorFileName;
	}
	
	
}
