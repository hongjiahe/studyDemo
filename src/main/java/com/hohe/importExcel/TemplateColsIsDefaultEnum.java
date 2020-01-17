package com.hohe.importExcel;

public enum TemplateColsIsDefaultEnum {
	
 	IS_DEFAULT(1, "默认值"),
 	IS_NOT_DEFAULT(0, "非默认");
    
    private TemplateColsIsDefaultEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static String fromId(int id){
       for(TemplateColsIsDefaultEnum  e : TemplateColsIsDefaultEnum.values()){
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
