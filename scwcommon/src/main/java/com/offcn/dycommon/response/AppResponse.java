package com.offcn.dycommon.response;

import com.offcn.dycommon.enums.ResponseCodeEnume;

public class AppResponse<T> {

    private Integer code;
    private String msg;
    private T data;

    //响应成功
    public  static<T> AppResponse<T> ok(T data){
        AppResponse<T> appResponse = new AppResponse<T>();
        appResponse.setCode(ResponseCodeEnume.SUCCESS.getCode());
        appResponse.setMsg(ResponseCodeEnume.SUCCESS.getMsg());
        appResponse.setData(data);
        return appResponse;
    }

    //响应失败
    public  static<T> AppResponse<T> fail(T data){
        AppResponse<T> appResponse = new AppResponse<T>();
        appResponse.setCode(ResponseCodeEnume.FAIL.getCode());
        appResponse.setMsg(ResponseCodeEnume.FAIL.getMsg());
        appResponse.setData(data);
        return appResponse;
    }

    public AppResponse() {
    }

    public AppResponse(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
