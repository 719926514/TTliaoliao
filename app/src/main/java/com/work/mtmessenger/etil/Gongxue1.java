package com.work.mtmessenger.etil;

import java.util.List;

public class Gongxue1 {
    public String name;
    public String session;
    public String code;
    public String msg;
    public  List<SortModel> data;
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setSession(String session) {
        this.session = session;
    }
    public String getSession() {
        return session;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public List<SortModel> getSortModel() {
        return data;
    }

    public void setSortModel(List<SortModel> sortModel) {
        this.data = sortModel;
    }
}
