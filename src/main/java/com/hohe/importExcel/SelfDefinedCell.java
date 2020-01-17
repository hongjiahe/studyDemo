package com.hohe.importExcel;

public class SelfDefinedCell {
	String Name;// 名称
	Boolean isRequire;// 是否必填

	public SelfDefinedCell(String name, Boolean isRequire) {
		super();
		Name = name;
		this.isRequire = isRequire;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public Boolean getIsRequire() {
		return isRequire;
	}

	public void setIsRequire(Boolean isRequire) {
		this.isRequire = isRequire;
	}
}
