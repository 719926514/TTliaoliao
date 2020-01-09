package com.work.mtmessenger.etil;

public class Regist {

    /**
     * ret_code : 10000
     * event_id : 10002
     * data : null
     * tips : 客户端注册
     */

    private int ret_code;
    private int event_id;
    private Object data;
    private String tips;

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }
}
