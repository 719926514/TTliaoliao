package com.work.mtmessenger.etil;

public class GroupUnreadArray {


    /**
     * ret_code : 10000
     * event_id : 10013
     * data : {"group_head_url":"https://encKqQx2aX9qqpjEt4FiVDVEs6HDHnV_Dj1DX4oGanr__a1q7","group_id":1,"group_name":"哈哈哈的群组"}
     * tips : 创建群组
     */

    private int ret_code;
    private int event_id;
    private DataBean data;
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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public static class DataBean {
        /**
         * group_head_url : https://encKqQx2aX9qqpjEt4FiVDVEs6HDHnV_Dj1DX4oGanr__a1q7
         * group_id : 1
         * group_name : 哈哈哈的群组
         */

        private String group_head_url;
        private int group_id;
        private String group_name;

        public String getGroup_head_url() {
            return group_head_url;
        }

        public void setGroup_head_url(String group_head_url) {
            this.group_head_url = group_head_url;
        }

        public int getGroup_id() {
            return group_id;
        }

        public void setGroup_id(int group_id) {
            this.group_id = group_id;
        }

        public String getGroup_name() {
            return group_name;
        }

        public void setGroup_name(String group_name) {
            this.group_name = group_name;
        }
    }
}
