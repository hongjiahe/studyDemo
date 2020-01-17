package com.hohe.importExcel;

import java.util.Map;

public interface JavaAction {

    public static String ERRORINFO="errorInfo";
    public static String DATAEXISTSNO="数据已存在";
    public static String DATAEXISTS=DATAEXISTSNO+";";
    public static String VALEX="X";
    public static Integer maxNumber=1000;//最大处理数


    /**
     * 执行程序
     * @param params
     */
    public void excute(Map params);
}
