package com.work.mtmessenger.etil;

public class Img {


    /**
     * code : 10000
     * data : {"path":"传给我的地址","show_path":"显示地址"}
     * msg : 操作成功
     */

    private int code;
    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * path : 传给我的地址
         * show_path : 显示地址
         */

        private String path;
        private String show_path;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getShow_path() {
            return show_path;
        }

        public void setShow_path(String show_path) {
            this.show_path = show_path;
        }
    }
}
