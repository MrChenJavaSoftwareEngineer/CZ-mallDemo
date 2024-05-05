package com.imooc.mall.common;

import com.imooc.mall.exception.ImoocMallExceptionEnum;

public class ApiRestResponse<T> {
  private Integer code;
   private String msg;

   private T data;

   private static final Integer OK_CODE=10000;
    private static final String OK_MSG="SUCCESS";
    public ApiRestResponse(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public ApiRestResponse(Integer code,String msg){
        this.code=code;
        this.msg=msg;
    }
    public ApiRestResponse(){
        this(OK_CODE,OK_MSG);
    }
    public static  <T>ApiRestResponse<T> success(){
        return new ApiRestResponse<>();
    }


    public static  <T>ApiRestResponse<T> success(T data){
        ApiRestResponse<T> response = new ApiRestResponse<>();
        response.setData(data);
        return response ;
    }

    public static  <T>ApiRestResponse<T> error(Integer code,String msg){
        return new ApiRestResponse<>(code,msg);
    }

    public static <T>ApiRestResponse<T> error(ImoocMallExceptionEnum ex){
        return new ApiRestResponse<>(ex.getCode(), ex.getMsg());
    }

    @Override
    public String toString() {
        return "ApiRestResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
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
