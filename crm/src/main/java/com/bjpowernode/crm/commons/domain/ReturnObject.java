package com.bjpowernode.crm.commons.domain;

/**
 * 2021/7/31 0031
 */
public class ReturnObject {

    private String code; //0失败 1成功
    private String message; //描述信息
    private Object retData; //任何一种类型

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getRetData() {
        return retData;
    }

    public void setRetData(Object retData) {
        this.retData = retData;
    }
}
