package com.hohe.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
public class OperationLog {

    private Integer id;
    private String ip;
    private String url;
    private String operator;
    private String functionName;
    private Date operateDate;
    private String currentVersion;
    private String platformFlag;
    private String platformLoginId;
    private String companyCode;
    private String parameters;

}