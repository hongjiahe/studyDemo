package com.hohe.importExcel;

/**
 * excel单元格数据类型枚举类
 * @author wanpeng 2016-09-14
 */
public enum CellTypeEnum {
	
 	NUMBER(0, "数字"),
 	STRING(1, "字符串"),
 	FORMULA(2,"公式"),
 	DATE(6, "日期");
    
    private CellTypeEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static String fromId(int id){
       for(CellTypeEnum  e : CellTypeEnum.values()){
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
