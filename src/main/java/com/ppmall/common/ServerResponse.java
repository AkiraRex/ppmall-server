package com.ppmall.common;

import java.io.IOException;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServerResponse<T> implements Serializable {
   
	private int status;
    private String msg;
    private T data;
    
    public ServerResponse() {
		// TODO Auto-generated constructor stub
    	// 用于GenericJacksonSerializable 反序列化
	}

    private ServerResponse(int status) {
        this.status = status;
    }

    private ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private ServerResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    private ServerResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public int getStatus() {
        return this.status;
    }

    public String getMsg() {
        return this.msg;
    }

    public T getData() {
        return this.data;
    }

    public static <T> ServerResponse<T> createSuccess() {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createSuccessMessage(String msg) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> ServerResponse<T> createSuccess(String msg, T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    public static <T> ServerResponse<T> createSuccess(T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> ServerResponse<T> createError(){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }

    public static <T> ServerResponse<T> createErrorMessage(String errorMsg){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),errorMsg);
    }

    public static <T> ServerResponse<T> createErrorStatus(int status,String errorMsg){
        return new ServerResponse<T>(status,errorMsg);
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String returnString =  mapper.writeValueAsString(this);
            return returnString;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.toString();
    }
}
