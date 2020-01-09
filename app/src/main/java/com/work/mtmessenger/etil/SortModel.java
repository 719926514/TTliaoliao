package com.work.mtmessenger.etil;

public class SortModel {


    private int user_id;//好友id
    private int online;//是否在线
    private int unread_count;//未读消息数量
    private String nick_name;//昵称
    private String head_url;//头像

    public String letters;//显示拼音的首字母

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public void setUnread_count(int unread_count) {
        this.unread_count = unread_count;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getOnline() {
        return online;
    }

    public int getUnread_count() {
        return unread_count;
    }

    public String getNick_name() {
        return nick_name;
    }

    public String getHead_url() {
        return head_url;
    }

    public String getLetters() {
        return letters;
    }




    @Override
    public String toString() {
        return "SortModel{" +
                "user_id=" + user_id +
                ", online=" + online +
                ", unread_count=" + unread_count +
                ", nick_name='" + nick_name + '\'' +
                ", head_url='" + head_url + '\'' +
                ", letters='" + letters + '\'' +
                '}';
    }
}
