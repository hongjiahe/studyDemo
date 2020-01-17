package com.hohe.importExcel;

import java.io.Serializable;

/**
 * @Description 导出自定义列Dto
 * @Author zzp
 * @since 2019.06.20
 **/
public class BaseExportDto implements Serializable{

    private static final long serialVersionUID = -1L;

    /**
     * 列的字段名
     */
    private String key;

    /**
     * 列的名称
     */
    private String name;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
