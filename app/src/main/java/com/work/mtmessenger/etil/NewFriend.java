package com.work.mtmessenger.etil;

public class NewFriend {

    /**
     * ret_code : 10000
     * event_id : 10006
     * data : 操作成功
     * tips : 反馈好友申请
     */

    private int ret_code;
    private int event_id;
    private String data;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }
}
