package com.work.mtmessenger.etil;

import java.util.List;

public class Qunliao {


    /**
     * ret_code : 10000
     * event_id : 10030
     * data : {"current_index":1,"list":[{"nick_name":"jkb","head_url":"img/default_group.png","group_id":72},{"nick_name":"poi","head_url":"img/default_group.png","group_id":71},{"nick_name":"yhb","head_url":"img/default_group.png","group_id":70},{"nick_name":"yyyyyyyy","head_url":"img/default_group.png","group_id":69},{"nick_name":"yyy","head_url":"img/default_group.png","group_id":68},{"nick_name":"iii","head_url":"img/default_group.png","group_id":67},{"nick_name":"ui","head_url":"img/default_group.png","group_id":64},{"nick_name":"ui","head_url":"img/default_group.png","group_id":65},{"nick_name":"ui","head_url":"img/default_group.png","group_id":66},{"nick_name":"ui","head_url":"img/default_group.png","group_id":59}],"total_index":7,"total_size":67}
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
         * current_index : 1
         * list : [{"nick_name":"jkb","head_url":"img/default_group.png","group_id":72},{"nick_name":"poi","head_url":"img/default_group.png","group_id":71},{"nick_name":"yhb","head_url":"img/default_group.png","group_id":70},{"nick_name":"yyyyyyyy","head_url":"img/default_group.png","group_id":69},{"nick_name":"yyy","head_url":"img/default_group.png","group_id":68},{"nick_name":"iii","head_url":"img/default_group.png","group_id":67},{"nick_name":"ui","head_url":"img/default_group.png","group_id":64},{"nick_name":"ui","head_url":"img/default_group.png","group_id":65},{"nick_name":"ui","head_url":"img/default_group.png","group_id":66},{"nick_name":"ui","head_url":"img/default_group.png","group_id":59}]
         * total_index : 7
         * total_size : 67
         */

        private int current_index;
        private int total_page;
        private int total_size;
        private List<ListBean> array;

        public int getCurrent_index() {
            return current_index;
        }

        public void setCurrent_index(int current_index) {
            this.current_index = current_index;
        }

        public int getTotal_index() {
            return total_page;
        }

        public void setTotal_index(int total_index) {
            this.total_page = total_index;
        }

        public int getTotal_size() {
            return total_size;
        }

        public void setTotal_size(int total_size) {
            this.total_size = total_size;
        }

        public List<ListBean> getList() {
            return array;
        }

        public void setList(List<ListBean> list) {
            this.array = list;
        }

        public static class ListBean {
            /**
             * nick_name : jkb
             * head_url : img/default_group.png
             * group_id : 72
             */

            private String nick_name;
            private String head_url;
            private int group_id;

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

            public int getGroup_id() {
                return group_id;
            }

            public void setGroup_id(int group_id) {
                this.group_id = group_id;
            }
        }
    }
}
