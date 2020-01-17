package com.hohe.vo;

import java.io.InputStream;
import java.io.Serializable;

/**
 * @ClassName FileVo
 * @Description 文件vo
 */
public class FileVo implements Serializable {

    /**
     * @Fields serialVersionUID  用一句话描述这个变量表示什么
     */
    private static final long serialVersionUID = 2637313214186048902L;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件流
     */
    private InputStream inputStream;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件大小
     */
    private long fileSize;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

}