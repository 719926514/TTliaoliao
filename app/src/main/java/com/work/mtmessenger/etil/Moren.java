package com.work.mtmessenger.etil;

import java.util.List;

public class Moren {


    /**
     * code : 10000
     * data : ["http://47.244.96.179:5151/img/1.jpg","http://47.244.96.179:5151/img/2.jpg","http://47.244.96.179:5151/img/3.jpg","http://47.244.96.179:5151/img/4.jpg","http://47.244.96.179:5151/img/5.jpg","http://47.244.96.179:5151/img/6.jpg","http://47.244.96.179:5151/img/7.jpg","http://47.244.96.179:5151/img/8.jpg","http://47.244.96.179:5151/img/9.jpg","http://47.244.96.179:5151/img/10.jpg"]
     * msg : 操作成功
     */

    private int code;
    private String msg;
    private List<String> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
