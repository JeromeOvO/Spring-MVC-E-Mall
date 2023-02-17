package com.mmall.common;

import com.sun.org.apache.regexp.internal.RE;
import org.aspectj.weaver.reflect.ReflectionWorld;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * Created by 70457 on 1/12/2023.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
//make sure when serialize json, if object is null, key will vanish
public class ServerResponse <T> implements Serializable{
    private int status;
    private String msg;
    private T data;

    private ServerResponse(int status){
        this.status = status;
    }

    private  ServerResponse(int status, T data){
        this.status = status;
        this.data = data;
    }

    private ServerResponse(int status, String msg, T data){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    private ServerResponse(int status, String msg){
        this.status = status;
        this.msg = msg;
    }

    @JsonIgnore
    //make it is not be in json serialized output
    public boolean isSuccess(){
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public int getStatus(){
        return status;
    }

    public T getData(){
        return data;
    }

    public String getMsg(){
        return msg;
    }

    public static <T> ServerResponse createBySuccess(){
        return new ServerResponse(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse createBySuccessMessage(String msg){
        return new ServerResponse(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> ServerResponse createBySuccess(T data){
        return new ServerResponse(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> ServerResponse createBySuccess(String msg, T data){
        return new ServerResponse(ResponseCode.SUCCESS.getCode(), msg, data);
    }


    public static <T> ServerResponse createByError(){
        return new ServerResponse(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
    }

    public static <T> ServerResponse createByErrorMessage(String errorMessage){
        return new ServerResponse(ResponseCode.ERROR.getCode(), errorMessage);
    }

    public static <T> ServerResponse createByErrorCodeMessage(int errorCode, String errorMessage){
        return new ServerResponse(errorCode, errorMessage);
    }


}
