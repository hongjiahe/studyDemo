package com.hohe.model;

public class ImprotTemplate {

    private Integer id;


    private String tableName;


    private String templateCode;


    private String templateNameCh;


    private String colsName;


    private String colsType;


    private Integer sort;


    private String colsLableName;


    private String format;


    private Integer isKey;


    private Integer isChangeValue;


    private Integer isDefaultCols;


    private Integer rowNum;


    private Integer colNum;


    private Integer isShow;

    private Integer isUpdate;

    private Integer notInsert;


    private Integer isNotnull;


    private Integer isSave;


    private String billtemplateId;


    private String defaultValue;


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public String getTableName() {
        return tableName;
    }


    public void setTableName(String tableName) {
        this.tableName = tableName == null ? null : tableName.trim();
    }


    public String getTemplateCode() {
        return templateCode;
    }


    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode == null ? null : templateCode.trim();
    }


    public String getTemplateNameCh() {
        return templateNameCh;
    }


    public void setTemplateNameCh(String templateNameCh) {
        this.templateNameCh = templateNameCh == null ? null : templateNameCh.trim();
    }


    public String getColsName() {
        return colsName;
    }


    public void setColsName(String colsName) {
        this.colsName = colsName == null ? null : colsName.trim();
    }


    public String getColsType() {
        return colsType;
    }


    public void setColsType(String colsType) {
        this.colsType = colsType == null ? null : colsType.trim();
    }


    public Integer getSort() {
        return sort;
    }


    public void setSort(Integer sort) {
        this.sort = sort;
    }


    public String getColsLableName() {
        return colsLableName;
    }


    public void setColsLableName(String colsLableName) {
        this.colsLableName = colsLableName == null ? null : colsLableName.trim();
    }


    public String getFormat() {
        return format;
    }


    public void setFormat(String format) {
        this.format = format == null ? null : format.trim();
    }


    public Integer getIsKey() {
        return isKey;
    }


    public void setIsKey(Integer isKey) {
        this.isKey = isKey;
    }


    public Integer getIsChangeValue() {
        return isChangeValue;
    }


    public void setIsChangeValue(Integer isChangeValue) {
        this.isChangeValue = isChangeValue;
    }


    public Integer getIsDefaultCols() {
        return isDefaultCols;
    }


    public void setIsDefaultCols(Integer isDefaultCols) {
        this.isDefaultCols = isDefaultCols;
    }


    public Integer getRowNum() {
        return rowNum;
    }


    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }


    public Integer getColNum() {
        return colNum;
    }


    public void setColNum(Integer colNum) {
        this.colNum = colNum;
    }


    public Integer getIsShow() {
        return isShow;
    }


    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }


    public Integer getNotInsert() {
        return notInsert;
    }


    public void setNotInsert(Integer notInsert) {
        this.notInsert = notInsert;
    }


    public Integer getIsNotnull() {
        return isNotnull;
    }


    public void setIsNotnull(Integer isNotnull) {
        this.isNotnull = isNotnull;
    }


    public Integer getIsSave() {
        return isSave;
    }


    public void setIsSave(Integer isSave) {
        this.isSave = isSave;
    }


    public String getBilltemplateId() {
        return billtemplateId;
    }


    public void setBilltemplateId(String billtemplateId) {
        this.billtemplateId = billtemplateId == null ? null : billtemplateId.trim();
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue == null ? null : defaultValue.trim();
    }

    public Integer getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(Integer isUpdate) {
        this.isUpdate = isUpdate;
    }
}