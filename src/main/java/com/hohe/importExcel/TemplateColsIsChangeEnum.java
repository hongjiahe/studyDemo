package com.hohe.importExcel;

public enum TemplateColsIsChangeEnum {
	
 	IS_CHANGE(1, "需要转换"),
 	IS_NOT_CHANGE(0, "无需转换");
    
    private TemplateColsIsChangeEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static String fromId(int id){
       for(TemplateColsIsChangeEnum  e : TemplateColsIsChangeEnum.values()){
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
