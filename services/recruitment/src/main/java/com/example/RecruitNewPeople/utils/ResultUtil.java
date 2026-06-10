package com.example.RecruitNewPeople.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResultUtil {
    private Integer code;
    private String message;
    private Object data;

    public ResultUtil(Integer code, String messagesg, Object data) {
        this.code = code;
        this.message = messagesg;
        this.data = data;
    }

    public  static ResultUtil success(Object data){
        return  new ResultUtil(200,"success",data);
    }
    public  static ResultUtil success(){
        return  new ResultUtil(200,"success",null);
    }
    public  static ResultUtil error(){
        return  new ResultUtil(0,"error",null);
    }
    public  static ResultUtil error(String message){
        return  new ResultUtil(0,message,null);
    }

    public  static ResultUtil error(int code,String message,Object data){
        return  new ResultUtil(code,message,data);
    }

    @Override
    public String toString() {
        return "ResultUtil{" +
                "code=" + code +
                ", msg='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
