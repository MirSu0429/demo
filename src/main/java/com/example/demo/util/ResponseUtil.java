package com.example.demo.util;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @Description //TODO ajax 回执
 * @Param 
 * @return 
 **/
@Data
public class ResponseUtil {

    //默认成功
    private boolean flag = true;
    private String msg;
    private JSONObject josnObj;
    private Integer status;
    private Object data;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public ResponseUtil() {
    }

    public ResponseUtil(boolean flag, String msg) {
        this.flag = flag;
        this.msg = msg;
    }

    public ResponseUtil(boolean flag, String msg, Integer status) {
        this.flag = flag;
        this.msg = msg;
        this.status = status;
    }

    /**
     * restful 返回
     */
    public static ResponseUtil error(String msg) {
        return new ResponseUtil(false, msg);
    }

    public static ResponseUtil sucess(String msg) {
        return new ResponseUtil(true, msg);
    }
}
