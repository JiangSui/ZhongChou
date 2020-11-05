package com.offcn.dycommon.enums;



public enum  ResponseCodeEnume {
    SUCCESS(0,"操作成功"),
    FAIL(1,"服务器异常"),
    NOT_FOUND(404,"资源未找到"),
    NOT_AUTHED(403,"无权限，访问拒绝"),
    PARAM_INVAILD(400,"提交参数非法");

    private Integer code;
    private String msg;

    ResponseCodeEnume(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    ResponseCodeEnume() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
