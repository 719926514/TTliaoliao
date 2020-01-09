package com.work.mtmessenger.etil;

public class NewAddFriend {


    /**
     * ret_code : 10000
     * event_id : 10005
     * data : {"applicant_id":2,"head_url":"https://gss3.bdstatic9cd1e28ea88b87d65042ac193f334f05/jpg","nick_name":"啊啊啊啊啊","user_name":"test"}
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
         * applicant_id : 2
         * head_url : https://gss3.bdstatic9cd1e28ea88b87d65042ac193f334f05/jpg
         * nick_name : 啊啊啊啊啊
         * user_name : test
         */

        private int applicant_id;
        private String head_url;
        private String nick_name;
        private String user_name;

        public int getApplicant_id() {
            return applicant_id;
        }

        public void setApplicant_id(int applicant_id) {
            this.applicant_id = applicant_id;
        }

        public String getHead_url() {
            return head_url;
        }

        public void setHead_url(String head_url) {
            this.head_url = head_url;
        }

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }
    }
}
