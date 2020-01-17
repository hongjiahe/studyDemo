package com.hohe.importExcel;

public enum TemplateColsIsKeyEnum {
	
 	IS_KEY(1, "主键"),
 	IS_NOT_KEY(0, "非主键");
    
    private TemplateColsIsKeyEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static String fromId(int id){
       for(TemplateColsIsKeyEnum  e : TemplateColsIsKeyEnum.values()){
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
