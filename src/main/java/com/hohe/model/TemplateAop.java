package com.hohe.model;

public class TemplateAop {

    private Integer id;

    private String templateCode;

    private String roleName;

    private Integer domainId;

    private String domainName;

    private String preAction;

    private String posAction;

    private String methodName;

    private String jsOrJava;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getDomainId() {
        return domainId;
    }

    public void setDomainId(Integer domainId) {
        this.domainId = domainId;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getPreAction() {
        return preAction;
    }

    public void setPreAction(String preAction) {
        this.preAction = preAction;
    }

    public String getPosAction() {
        return posAction;
    }

    public void setPosAction(String posAction) {
        this.posAction = posAction;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getJsOrJava() {
        return jsOrJava;
    }

    public void setJsOrJava(String jsOrJava) {
        this.jsOrJava = jsOrJava;
    }
}