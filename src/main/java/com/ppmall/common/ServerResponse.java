package com.ppmall.common;

import java.io.IOException;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 服务response封装工具
 * @author rex
 *
 * @param <T>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServerResponse<T> implements Serializable {
   /**
    * 状态
    */
	private int status;
	
	/**
	 * 返回消息
	 */
    private String msg;
    
    /**
     * 返回数据(泛型)
     */
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

    /**
     * 创建一个只有成功状态的response
     * @return
     */
    public static <T> ServerResponse<T> createSuccess() {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    /**
     * 创建一个带成功信息的response
     * @return
     */
    public static <T> ServerResponse<T> createSuccessMessage(String msg) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg);
    }

    /**
     * 创建一个带消息以及数据的response
     * @return
     */
    public static <T> ServerResponse<T> createSuccess(String msg, T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg, data);
    }
    
    /**
     * 创建一个只有数据的response
     * @return
     */
    public static <T> ServerResponse<T> createSuccess(T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), data);
    }

    /**
     * 创建一个错误状态的response
     * @return
     */
    public static <T> ServerResponse<T> createError(){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }

    /**
     * 创建一个带错误消息的response
     * @return
     */
    public static <T> ServerResponse<T> createErrorMessage(String errorMsg){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),errorMsg);
    }

    /**
     * 创建一个自定义错误状态与消息的response
     * @param status
     * @param errorMsg
     * @return
     */
    public static <T> ServerResponse<T> createErrorStatus(int status,String errorMsg){
        return new ServerResponse<T>(status,errorMsg);
    }
    
    public static <T> ServerResponse<T> createErrorStatus(ResponseCode code){
        return new ServerResponse<T>(code.getCode(),code.getDesc());
    }

    
    @Override
    public String toString() {
    	// jackson序列化
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
