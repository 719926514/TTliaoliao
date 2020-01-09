package com.work.mtmessenger.etil;

import java.util.List;

public class Money {

    /**
     * ret_code : 10000
     * event_id : 10039
     * data : {"array":[{"create_time":0,"money":0,"new_money":0,"status":0,"type":0}],"current_index":0,"total_page":0,"total_size":0}
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
         * array : [{"create_time":0,"money":0,"new_money":0,"status":0,"type":0}]
         * current_index : 0
         * total_page : 0
         * total_size : 0
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
             * create_time : 0
             * money : 0
             * new_money : 0
             * status : 0
             * type : 0
             */

            private int create_time;
            private int money;
            private int new_money;
            private int status;
            private int type;
            private String target;

            public void setTarget(String target) {
                this.target = target;
            }

            public String getTarget() {
                return target;
            }

            public int getCreate_time() {
                return create_time;
            }

            public void setCreate_time(int create_time) {
                this.create_time = create_time;
            }

            public int getMoney() {
                return money;
            }

            public void setMoney(int money) {
                this.money = money;
            }

            public int getNew_money() {
                return new_money;
            }

            public void setNew_money(int new_money) {
                this.new_money = new_money;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }
}
