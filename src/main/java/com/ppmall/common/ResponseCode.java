package com.ppmall.common;

/**
 * response状态枚举
 * @author rex
 *
 */
public enum ResponseCode {
    SUCCESS(0,"SUCCESS"),
    ERROR(1,"ERROR"),
    NOT_LOGIN(10,"NOT_LOGIN"),
    TOKEN_EXPIRED(20,"TOKEN_EXPIRED"),
    TOKEN_ERROR(30,"TOKEN_ERROR"),
    TOKEN_INVALID(40,"TOKEN_INVALID"),
    ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT");

    private int code;
    private String desc;
    ResponseCode(int code,String desc){
        this.code = code;
        this.desc = desc;

    }

    public int getCode(){
        return this.code;
    }
    public String getDesc(){
        return this.desc;
    }
}
