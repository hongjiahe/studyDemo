package com.hohe.importExcel;

/**
 * excel导入类型
 * @author wanpeng 2016-09-14
 */
public enum ExcelFormType {
	
 	LIST(1, "列表"),
 	FORM(2, "表单");
    
    private ExcelFormType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static String fromId(int id){
       for(ExcelFormType  e : ExcelFormType.values()){
           if(e.id == id){
               return e.getName();
           }
       }
        return null;
    }

    private final int id;
    private final String name;
    
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
}
