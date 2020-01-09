package com.work.mtmessenger.etil;

import java.util.List;

public class Seelistall {


    /**
     * ret_code : 10000
     * event_id : 10029
     * data : {"array":[{"nick_name":"捷胜 jiesheng","head_url":"http://47.244.96.179:5151/img/20002000009921322540.png","admin":true,"online":1,"user_id":13,"role":"[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\"]"},{"nick_name":"两炮情未了","head_url":"http://47.244.96.179:5151/img/5.jpg","user_id":15,"role":"[\"1\"]"},{"nick_name":"小白","head_url":"http://47.244.96.179:5151/img/20004000012548516637.png","user_id":11,"role":"[\"1\"]"},{"nick_name":"一炮泯恩仇","head_url":"http://47.244.96.179:5151/img/2.jpg","user_id":12,"role":"[\"1\"]"}],"current_index":1,"total_page":1,"total_size":4}
     * tips : ok
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
         * array : [{"nick_name":"捷胜 jiesheng","head_url":"http://47.244.96.179:5151/img/20002000009921322540.png","admin":true,"online":1,"user_id":13,"role":"[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\"]"},{"nick_name":"两炮情未了","head_url":"http://47.244.96.179:5151/img/5.jpg","user_id":15,"role":"[\"1\"]"},{"nick_name":"小白","head_url":"http://47.244.96.179:5151/img/20004000012548516637.png","user_id":11,"role":"[\"1\"]"},{"nick_name":"一炮泯恩仇","head_url":"http://47.244.96.179:5151/img/2.jpg","user_id":12,"role":"[\"1\"]"}]
         * current_index : 1
         * total_page : 1
         * total_size : 4
         */

        private int current_index;
        private int total_page;
        private int total_size;
        private List<ArrayBean> array;

        public int getCurrent_index() {
            return current_index;
        }

        public void setCurrent_index(int current_index) {
            this.current_index = current_index;
        }

        public int getTotal_page() {
            return total_page;
        }

        public void setTotal_page(int total_page) {
            this.total_page = total_page;
        }

        public int getTotal_size() {
            return total_size;
        }

        public void setTotal_size(int total_size) {
            this.total_size = total_size;
        }

        public List<ArrayBean> getArray() {
            return array;
        }

        public void setArray(List<ArrayBean> array) {
            this.array = array;
        }

        public static class ArrayBean {
            /**
             * nick_name : 捷胜 jiesheng
             * head_url : http://47.244.96.179:5151/img/20002000009921322540.png
             * admin : true
             * online : 1
             * user_id : 13
             * role : ["1","2","3","4","5","6"]
             */

            private String nick_name;
            private String head_url;
            private boolean admin;
            private int online;
            private int user_id;
            private String role;

            public String getNick_name() {
                return nick_name;
            }

            public void setNick_name(String nick_name) {
                this.nick_name = nick_name;
            }

            public String getHead_url() {
                return head_url;
            }

            public void setHead_url(String head_url) {
                this.head_url = head_url;
            }

            public boolean isAdmin() {
                return admin;
            }

            public void setAdmin(boolean admin) {
                this.admin = admin;
            }

            public int getOnline() {
                return online;
            }

            public void setOnline(int online) {
                this.online = online;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public String getRole() {
                return role;
            }

            public void setRole(String role) {
                this.role = role;
            }
        }
    }
}
