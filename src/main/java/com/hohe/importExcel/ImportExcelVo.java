package com.hohe.importExcel;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ImportExcelVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8282963459808170579L;
	List<String> tList;
	List<Map<String, Object>> dataList;
	Map<String,String> insertUpdateSqlMap;
	String templateCode;
	String isUpdate;
	String templateUrl;
	String key;

	/**
	 * 导出错误按钮，1和null表示显示，0表示不显示
	 */
	Integer exportErrBtn;

	/**
	 * 导出错误按钮，1和null表示显示，0表示不显示
	 */
	Integer coverBtn;
	
	
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Map<String, String> getInsertUpdateSqlMap() {
		return insertUpdateSqlMap;
	}

	public String getIsUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
	}

	public void setInsertUpdateSqlMap(Map<String, String> insertUpdateSqlMap) {
		this.insertUpdateSqlMap = insertUpdateSqlMap;
	}

	public String getTemplateUrl() {
		return templateUrl;
	}

	public void setTemplateUrl(String templateUrl) {
		this.templateUrl = templateUrl;
	}

	public List<Map<String, Object>> getDataList() {
		return dataList;
	}

	public void setDataList(List<Map<String, Object>> dataList) {
		this.dataList = dataList;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public List<String> gettList() {
		return tList;
	}

	public void settList(List<String> tList) {
		this.tList = tList;
	}

	public Integer getExportErrBtn() {
		return exportErrBtn;
	}

	public void setExportErrBtn(Integer exportErrBtn) {
		this.exportErrBtn = exportErrBtn;
	}

	public Integer getCoverBtn() {
		return coverBtn;
	}

	public void setCoverBtn(Integer coverBtn) {
		this.coverBtn = coverBtn;
	}
}
