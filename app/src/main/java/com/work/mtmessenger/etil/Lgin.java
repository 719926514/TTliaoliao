package com.work.mtmessenger.etil;

import java.util.List;

public class Lgin {


    /**
     * ret_code : 10000
     * event_id : 10001
     * data : {"nick_name":"小白","head_url":"https://8.jpg","friend_count":0,"friend_unread_array":[{"send_user_name":"asd","send_head_url":"asd","send_user_id":111,"unread_array":1}],"friend_request_array":[{"nick_name":"啊啊啊啊啊","head_url":"https://gss37598.jpg","user_name":"test","applicant_id":2}]}
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
         * nick_name : 小白
         * head_url : https://8.jpg
         * friend_count : 0
         * friend_unread_array : [{"send_user_name":"asd","send_head_url":"asd","send_user_id":111,"unread_array":1}]
         * friend_request_array : [{"nick_name":"啊啊啊啊啊","head_url":"https://gss37598.jpg","user_name":"test","applicant_id":2}]
         */

        private String nick_name;
        private String head_url;
        private int friend_count;
        private String token;
        private int balance;
        private List<FriendUnreadArrayBean> friend_unread_array;
        private List<FriendRequestArrayBean> friend_request_array;
        private List<GroupUnreadArray> group_unread_array;

        public void setBalance(int balance) {
            this.balance = balance;
        }

        public int getBalance() {
            return balance;
        }

        public void setGroup_unread_array(List<GroupUnreadArray> group_unread_array) {
            this.group_unread_array = group_unread_array;
        }

        public List<GroupUnreadArray> getGroup_unread_array() {
            return group_unread_array;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

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

        public int getFriend_count() {
            return friend_count;
        }

        public void setFriend_count(int friend_count) {
            this.friend_count = friend_count;
        }

        public List<FriendUnreadArrayBean> getFriend_unread_array() {
            return friend_unread_array;
        }

        public void setFriend_unread_array(List<FriendUnreadArrayBean> friend_unread_array) {
            this.friend_unread_array = friend_unread_array;
        }

        public List<FriendRequestArrayBean> getFriend_request_array() {
            return friend_request_array;
        }

        public void setFriend_request_array(List<FriendRequestArrayBean> friend_request_array) {
            this.friend_request_array = friend_request_array;
        }

        public static class FriendRequestArrayBean {
            /**
             * nick_name : 啊啊啊啊啊
             * head_url : https://gss37598.jpg
             * user_name : test
             * applicant_id : 2
             */

            private String nick_name;
            private String head_url;
            private String user_name;
            private int applicant_id;

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

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public int getApplicant_id() {
                return applicant_id;
            }

            public void setApplicant_id(int applicant_id) {
                this.applicant_id = applicant_id;
            }
        }


        public static class FriendUnreadArrayBean {
            /**
             * send_user_name : as
             * send_head_url : as
             * send_user_id : 111
             * unread_array : 1
             */

            private String send_nick_name;
            private String send_head_url;
            private int send_user_id;
            private int unread_array;
            private int news_create_time;
            private String news_messages;
            private int chat_windows_id;

            public void setChat_windows_id(int chat_windows_id) {
                this.chat_windows_id = chat_windows_id;
            }

            public int getChat_windows_id() {
                return chat_windows_id;
            }

            private String group_head_url;
            private int group_id;
            private String group_name;

            public void setGroup_head_url(String group_head_url) {
                this.group_head_url = group_head_url;
            }

            public void setGroup_id(int group_id) {
                this.group_id = group_id;
            }

            public void setGroup_name(String group_name) {
                this.group_name = group_name;
            }

            public String getGroup_head_url() {
                return group_head_url;
            }

            public int getGroup_id() {
                return group_id;
            }

            public String getGroup_name() {
                return group_name;
            }

            public void setNews_create_time(int news_create_time) {
                this.news_create_time = news_create_time;
            }

            public void setNews_messages(String news_messages) {
                this.news_messages = news_messages;
            }

            public int getNews_create_time() {
                return news_create_time;
            }

            public String getNews_messages() {
                return news_messages;
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

            public int getSend_user_id() {
                return send_user_id;
            }

            public void setSend_user_id(int send_user_id) {
                this.send_user_id = send_user_id;
            }

            public int getUnread_array() {
                return unread_array;
            }

            public void setUnread_array(int unread_array) {
                this.unread_array = unread_array;
            }
        }


        public static class GroupUnreadArray {

            /**
             * group_head_url : http://47.244.96.179:5151/img/default_group.png
             * group_id : 9
             * group_name : yitianyiri
             */


            private int news_create_time;
            private String news_messages;
            private String send_head_url;
            private String send_nick_name;
            private int send_user_id;
            private int unread_array;
            private int chat_windows_id;
            private String chat_windows_name;

            public void setChat_windows_name(String chat_windows_name) {
                this.chat_windows_name = chat_windows_name;
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

            public void setSend_nick_name(String send_nick_name) {
                this.send_nick_name = send_nick_name;
            }

            public void setSend_head_url(String send_head_url) {
                this.send_head_url = send_head_url;
            }

            public void setSend_user_id(int send_user_id) {
                this.send_user_id = send_user_id;
            }

            public void setUnread_array(int unread_array) {
                this.unread_array = unread_array;
            }

            public void setNews_create_time(int news_create_time) {
                this.news_create_time = news_create_time;
            }

            public void setNews_messages(String news_messages) {
                this.news_messages = news_messages;
            }

            public String getSend_nick_name() {
                return send_nick_name;
            }

            public String getSend_head_url() {
                return send_head_url;
            }

            public int getSend_user_id() {
                return send_user_id;
            }

            public int getUnread_array() {
                return unread_array;
            }

            public int getNews_create_time() {
                return news_create_time;
            }

            public String getNews_messages() {
                return news_messages;
            }
        }

    }


}
