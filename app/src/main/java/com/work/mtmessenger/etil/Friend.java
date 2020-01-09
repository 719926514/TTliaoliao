package com.work.mtmessenger.etil;

import java.util.List;

public class Friend {


    /**
     * ret_code : 10000
     * event_id : 10012
     * data : {"array":[{"user_id":2,"nick_name":"","head_url":"https://gss3.bdstatic.cc547598.jpg","unread_count":0,"online":1}],"current_index":1,"total_index":1,"total_size":1}
     * tips : 成功
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
         * array : [{"user_id":2,"nick_name":"","head_url":"https://gss3.bdstatic.cc547598.jpg","unread_count":0,"online":1}]
         * current_index : 1
         * total_index : 1
         * total_size : 1
         */

        private int current_index;
        private int total_page;
        private int total_size;
        private List<SortModel> array;

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

        public List<SortModel> getArray() {
            return array;
        }

        public void setArray(List<SortModel> array) {
            this.array = array;
        }


    }
}
