package com.offcn.user.exception;


import com.offcn.user.enums.UserExceptionEnum;

//自定义的用户异常类
public class UserException extends RuntimeException{
    public UserException(UserExceptionEnum exceptionEnum){
        super(exceptionEnum.getMsg());
    }
}
