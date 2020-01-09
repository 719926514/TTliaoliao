package com.work.mtmessenger.etil;

public class Seelist {


    /**
     * ret_code : 10000
     * event_id : 10018
     * data : {"create_user_head_url":"","create_user_id":2,"create_user_nick_name":"test123355","group_id":72,"group_user_count":1,"head_url":"http://47.244.96.179:5151/img/default_group.png","me_role":"[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\"]","name":"jkb"}
     * tips : 查看群组信息
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
         * create_user_head_url :
         * create_user_id : 2
         * create_user_nick_name : test123355
         * group_id : 72
         * group_user_count : 1
         * head_url : http://47.244.96.179:5151/img/default_group.png
         * me_role : ["1","2","3","4","5","6"]
         * name : jkb
         */

        private String create_user_head_url;
        private int create_user_id;
        private String create_user_nick_name;
        private int group_id;
        private int group_user_count;
        private String head_url;
        private String me_role;
        private String name;

        public String getCreate_user_head_url() {
            return create_user_head_url;
        }

        public void setCreate_user_head_url(String create_user_head_url) {
            this.create_user_head_url = create_user_head_url;
        }

        public int getCreate_user_id() {
            return create_user_id;
        }

        public void setCreate_user_id(int create_user_id) {
            this.create_user_id = create_user_id;
        }

        public String getCreate_user_nick_name() {
            return create_user_nick_name;
        }

        public void setCreate_user_nick_name(String create_user_nick_name) {
            this.create_user_nick_name = create_user_nick_name;
        }

        public int getGroup_id() {
            return group_id;
        }

        public void setGroup_id(int group_id) {
            this.group_id = group_id;
        }

        public int getGroup_user_count() {
            return group_user_count;
        }

        public void setGroup_user_count(int group_user_count) {
            this.group_user_count = group_user_count;
        }

        public String getHead_url() {
            return head_url;
        }

        public void setHead_url(String head_url) {
            this.head_url = head_url;
        }

        public String getMe_role() {
            return me_role;
        }

        public void setMe_role(String me_role) {
            this.me_role = me_role;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
