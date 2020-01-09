package com.work.mtmessenger.etil;

public class Newclass {

    /**
     * ret_code : 10000
     * event_id : 10013
     * data : {"group_head_url":"https://encKqQx2aX9qqpjEt4FiVDVEs6HDHnV_Dj1DX4oGanr__a1q7","group_id":1,"group_name":"哈哈哈的群组"}
     * tips : 创建群组
     */

    private int ret_code;
    private int event_id;
    private GroupUnreadArray data;
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

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public void setData(GroupUnreadArray data) {
        this.data = data;
    }

    public GroupUnreadArray getData() {
        return data;
    }
}
