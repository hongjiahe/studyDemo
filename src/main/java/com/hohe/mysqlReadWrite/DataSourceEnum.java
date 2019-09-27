package com.hohe.mysqlReadWrite;

public enum DataSourceEnum {

    MASTER("master", "主库"),
    SLAVE1("slave1", "从库1");

    DataSourceEnum(String code, String name){
        this.code = code;
        this.name = name;
    }

    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
