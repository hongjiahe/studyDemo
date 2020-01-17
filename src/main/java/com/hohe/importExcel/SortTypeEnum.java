package com.hohe.importExcel;

/**
 * 排序顺序类型枚举
 * @author zzp
 * @since 2019.10.16
 */
public enum SortTypeEnum {

    ASC("ASC", "升序"),
    DESC("DESC", "降序");

    private String code;
    private String name;

    private SortTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }
    public String getName() {
        return name;
    }

    public static String fromCode(String code){
        for(SortTypeEnum e : SortTypeEnum.values()){
            if(e.getCode().equals(code)){
                return e.getName();
            }
        }
        return null;
    }

}
