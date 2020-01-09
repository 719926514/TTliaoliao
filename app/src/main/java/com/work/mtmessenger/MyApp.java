package com.work.mtmessenger;

import android.app.Application;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.work.mtmessenger.etil.Init;
import com.work.mtmessenger.etil.Nodata;
import com.work.mtmessenger.etil.Trycod;
import com.work.mtmessenger.ui.SayListActivity;
import com.work.mtmessenger.util.DesUtils;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.WebSocketManager;
import com.zhangke.websocket.WebSocketSetting;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;


public class MyApp extends Application {
    private Nodata nodata;
    private android.os.Handler handler = new Handler();


    @Override
    public void onCreate() {
        JPushInterface.init(getApplicationContext());
        super.onCreate();

    }





    //token
    public static String token = "";
    //我的昵称
    public static String nick_name = "";
    //我的头像
    public static String head_url = "";
    //我的好友数量
    public static int friend_count = 0;
    //新好友数量
    public static int new_friend_count = 0;
    //我的账号
    public static String account = "";
    //我的密码
    public static String pass = "";
    ////发送人昵称
    public static String send_user_name = "";
    //发送人头像
    public static String send_head_url = "";
    //发送人id
    public static int send_user_id = 0;
    //未读数量
    public static int unread_array = 0;
    ////申请人昵称
    public static String new_nick_name = "";
    //申请人头像
    public static String new_head_url = "";
    public static int balance = 0;
    //微信app
    public static String wx_app_secret ;
    public static String wx_appId ;

    //图片地址
    public static String waiwang = "http://47.244.96.179:5151/";
    public static String nweiwang = "http://192.168.0.104:5151/";
    public static String imgrul = waiwang + "extension/upload_img";
    //视频地址
    public static String diveorul = waiwang + "extension/upload_voice";
    //隐私
    public static String yingsi = waiwang + "extension/privacy_agreement";

    //功能介绍
    public static String jieshao = waiwang + "/extension/get_introduction";

    //关于
    public static String wome = waiwang + "extension/get_about_us";
    //图形码
    public static String ivcod = waiwang + "extension/arithmetic_captcha";
    //验证码
    public static String cod = waiwang + "extension/phone_captcha";
    //默认头像
    public static String moren = waiwang + "extension/get_default_img_array";
    //des  加密
    public static String Deskey = "3WHNtzv6";
    public static String Desiv = "52w1NzPg";


}



