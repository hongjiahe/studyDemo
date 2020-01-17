package com.hohe.importExcel;

/**
 * <p>
 * REST API 错误码接口
 * </p>
 *
 * @author hubin
 * @since 2018-06-05
 */
public interface IErrorCode {

    /**
     * 错误编码 -1、失败 0、成功
     */
    long getCode();

    /**
     * 错误描述
     */
    String getMsg();
}
