package com.hohe.util;

import java.io.Serializable;

public class Result <T> implements Serializable {

    private static final long serialVersionUID = -731465216371397304L;

    private Integer statusCode;
    public static int SUCCESS = 1;//成功时标志为1，其它情况都为失败
    public static int ERROR = 0;// 默认失败标志为0
    /**
     * 状态：1为成功，0为失败
     */
    private int status;//状态：true为成功，false为失败
    /**
     * 返回数据
     */
    private T data;//封装的结果数据
    private String msg;//提示信息
    private String tip;//提示信息

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
    public Integer getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Result() {
        this.status=SUCCESS;
    }
    public Result(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.tip = msg;
        this.data = data;
    }
    public Result(Integer statusCode, int status, String msg, T data) {
        this.statusCode = statusCode;
        this.status = status;
        this.msg = msg;
        this.tip = msg;
        this.data = data;
    }
    public Result(Integer statusCode, String msg, T data) {
        this.statusCode = statusCode;
        this.msg = msg;
        this.tip = msg;
        this.data = data;
    }
    public static <E> Result<E> success(String msg, E data) {
        return new Result<E>(SUCCESS, msg, data);
    }
    public static <E> Result<E> error(String msg, E data) {
        return new Result<E>(ERROR, msg, data);
    }
    public static <E> Result<E> error(Integer statusCode, String msg, E data) {
        return new Result<E>(statusCode, ERROR, msg, data);
    }
    public static <E> Result<E> error(Integer statusCode, int status, String msg, E data) {
        return new Result<E>(statusCode, status, msg, data);
    }

    public String getTip() {
        return tip;
    }
    public void setTip(String tip) {
        this.tip = tip;
    }

    public boolean isSuccess(){
        return SUCCESS == this.status;
    }

}
