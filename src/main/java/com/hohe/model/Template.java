package com.hohe.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 模版
 */
@Setter
@Getter
public class Template {

    private Integer id;

    /**
     * 模版code
     */
    private String templateCode;

    /**
     * 模版名称
     */
    private String templateName;

    /**
     * 表名
     */
    private String tableName;

    /**
     * Sheet名称
     */
    private String sheetName;

    /**
     * 表格类型，1：列表，2：表单
     */
    private int excelFormType;

    /**
     * 列表标题行号
     */
    private int titleRownum;
}
