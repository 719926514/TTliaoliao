package com.work.mtmessenger.etil;

import java.util.List;

public class Call {
    /**
     * ret_code : 10000
     * event_id : 10020
     * data : {"array":[{"send_user_id":13,"send_nick_name":"捷胜 jiesheng","send_head_url":"http://47.244.96.179:5151/img/20007000010644825196.png","value":"分之五老K","value_type":1,"create_time":1578478991,"is_me":true,"messages_id":420,"chat_windows_id":11},{"send_user_id":13,"send_nick_name":"捷胜 jiesheng","send_head_url":"http://47.244.96.179:5151/img/20007000010644825196.png","value":"敏敏哦","value_type":1,"create_time":1578478982,"is_me":true,"messages_id":419,"chat_windows_id":11},{"send_user_id":13,"send_nick_name":"捷胜 jiesheng","send_head_url":"http://47.244.96.179:5151/img/20007000010644825196.png","value":"来了","value_type":1,"create_time":1578478564,"is_me":true,"messages_id":410,"chat_windows_id":11},{"send_user_id":13,"send_nick_name":"捷胜 jiesheng","send_head_url":"http://47.244.96.179:5151/img/20007000010644825196.png","value":"急急急","value_type":1,"create_time":1578478522,"is_me":true,"messages_id":409,"chat_windows_id":11},{"send_user_id":14,"send_nick_name":"阿东","send_head_url":"http://47.244.96.179:5151/img/10.jpg","value":"我通过你的好友申请，快来找我聊天吧～～","value_type":1,"create_time":1578462030,"messages_id":406,"chat_windows_id":11}],"current_index":1,"total_page":1,"total_size":5}
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
         * array : [{"send_user_id":13,"send_nick_name":"捷胜 jiesheng","send_head_url":"http://47.244.96.179:5151/img/20007000010644825196.png","value":"分之五老K","value_type":1,"create_time":1578478991,"is_me":true,"messages_id":420,"chat_windows_id":11},{"send_user_id":13,"send_nick_name":"捷胜 jiesheng","send_head_url":"http://47.244.96.179:5151/img/20007000010644825196.png","value":"敏敏哦","value_type":1,"create_time":1578478982,"is_me":true,"messages_id":419,"chat_windows_id":11},{"send_user_id":13,"send_nick_name":"捷胜 jiesheng","send_head_url":"http://47.244.96.179:5151/img/20007000010644825196.png","value":"来了","value_type":1,"create_time":1578478564,"is_me":true,"messages_id":410,"chat_windows_id":11},{"send_user_id":13,"send_nick_name":"捷胜 jiesheng","send_head_url":"http://47.244.96.179:5151/img/20007000010644825196.png","value":"急急急","value_type":1,"create_time":1578478522,"is_me":true,"messages_id":409,"chat_windows_id":11},{"send_user_id":14,"send_nick_name":"阿东","send_head_url":"http://47.244.96.179:5151/img/10.jpg","value":"我通过你的好友申请，快来找我聊天吧～～","value_type":1,"create_time":1578462030,"messages_id":406,"chat_windows_id":11}]
         * current_index : 1
         * total_page : 1
         * total_size : 5
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
             * send_user_id : 13
             * send_nick_name : 捷胜 jiesheng
             * send_head_url : http://47.244.96.179:5151/img/20007000010644825196.png
             * value : 分之五老K
             * value_type : 1
             * create_time : 1578478991
             * is_me : true
             * messages_id : 420
             * chat_windows_id : 11
             */

            private int send_user_id;
            private String send_nick_name;
            private String send_head_url;
            private String value;
            private int value_type;
            private int create_time;
            private boolean is_me;
            private int messages_id;
            private int chat_windows_id;

            public int getSend_user_id() {
                return send_user_id;
            }

            public void setSend_user_id(int send_user_id) {
                this.send_user_id = send_user_id;
            }

            public String getSend_nick_name() {
                return send_nick_name;
            }

            public void setSend_nick_name(String send_nick_name) {
                this.send_nick_name = send_nick_name;
            }

            public String getSend_head_url() {
                return send_head_url;
            }

            public void setSend_head_url(String send_head_url) {
                this.send_head_url = send_head_url;
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

            public int getCreate_time() {
                return create_time;
            }

            public void setCreate_time(int create_time) {
                this.create_time = create_time;
            }

            public boolean isIs_me() {
                return is_me;
            }

            public void setIs_me(boolean is_me) {
                this.is_me = is_me;
            }

            public int getMessages_id() {
                return messages_id;
            }

            public void setMessages_id(int messages_id) {
                this.messages_id = messages_id;
            }

            public int getChat_windows_id() {
                return chat_windows_id;
            }

            public void setChat_windows_id(int chat_windows_id) {
                this.chat_windows_id = chat_windows_id;
            }
        }
    }

}
