package com.hohe.importExcel;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义模板
 * 
 * @author yfzx
 *
 */
public class SelfDefinedTemplate {

	private String sheetName;
	private List<SelfDefinedCell> titleList = new ArrayList<>();
	private List<List<Object>> dataList = new ArrayList<>();

	public SelfDefinedTemplate() {
		super();
	}

	public SelfDefinedTemplate(String sheetName) {
		super();
		this.sheetName = sheetName;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public List<SelfDefinedCell> getTitleList() {
		return titleList;
	}

	public void setTitleList(List<SelfDefinedCell> titleList) {
		this.titleList = titleList;
	}

	public void addCell(String name, Boolean isRequire) {
		if (titleList == null) {
			titleList = new ArrayList<>();
		}
		titleList.add(new SelfDefinedCell(name, isRequire));
	}

	public List<List<Object>> getDataList() {
		return dataList;
	}

	public void setDataList(List<List<Object>> dataList) {
		this.dataList = dataList;
	}


}
