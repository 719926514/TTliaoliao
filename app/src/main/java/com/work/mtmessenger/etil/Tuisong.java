package com.work.mtmessenger.etil;

public class Tuisong {

    /**
     * ret_code : 10000
     * event_id : 10007
     * data : {"send_user_name":"小白","send_head_url":"https://gss3.b0923dc547598.jpg","send_user_id":3,"value":"我通过你的好友申请，快来找我聊天吧～～","value_type":1,"target_type":1,"create_time":1576642356}
     * tips :
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
         * send_user_name : 小白
         * send_head_url : https://gss3.b0923dc547598.jpg
         * send_user_id : 3
         * value : 我通过你的好友申请，快来找我聊天吧～～
         * value_type : 1
         * target_type : 1
         * create_time : 1576642356
         */

        private String send_user_name;
        private String send_head_url;
        private int send_user_id;
        private String value;
        private int value_type;
        private int target_type;
        private int create_time;
        private int chat_windows_id;
        private String chat_windows_head_url;
        private String chat_windows_name;

        public void setChat_windows_head_url(String chat_windows_head_url) {
            this.chat_windows_head_url = chat_windows_head_url;
        }

        public void setChat_windows_name(String chat_windows_name) {
            this.chat_windows_name = chat_windows_name;
        }

        public String getChat_windows_head_url() {
            return chat_windows_head_url;
        }

        public String getChat_windows_name() {
            return chat_windows_name;
        }

        public void setChat_windows_id(int chat_windows_id) {
            this.chat_windows_id = chat_windows_id;
        }

        public int getChat_windows_id() {
            return chat_windows_id;
        }

        public String getSend_user_name() {
            return send_user_name;
        }

        public void setSend_user_name(String send_user_name) {
            this.send_user_name = send_user_name;
        }

        public String getSend_head_url() {
            return send_head_url;
        }

        public void setSend_head_url(String send_head_url) {
            this.send_head_url = send_head_url;
        }

        public int getSend_user_id() {
            return send_user_id;
        }

        public void setSend_user_id(int send_user_id) {
            this.send_user_id = send_user_id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getValue_type() {
            return value_type;
        }

        public void setValue_type(int value_type) {
            this.value_type = value_type;
        }

        public int getTarget_type() {
            return target_type;
        }

        public void setTarget_type(int target_type) {
            this.target_type = target_type;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }
    }
}
