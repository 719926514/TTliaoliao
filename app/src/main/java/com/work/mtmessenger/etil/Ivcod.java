package com.work.mtmessenger.etil;

public class Ivcod {

    /**
     * code : 10000
     * data : {"id_key":"TV9EgMfNZfeNehZtuQre","img_data":"data:image/png;base64,iVB"}
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
         * id_key : TV9EgMfNZfeNehZtuQre
         * img_data : data:image/png;base64,iVB
         */

        private String id_key;
        private String img_data;

        public String getId_key() {
            return id_key;
        }

        public void setId_key(String id_key) {
            this.id_key = id_key;
        }

        public String getImg_data() {
            return img_data;
        }

        public void setImg_data(String img_data) {
            this.img_data = img_data;
        }
    }
}
