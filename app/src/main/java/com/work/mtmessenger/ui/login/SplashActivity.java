package com.work.mtmessenger.ui.login;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import com.google.gson.Gson;
import com.work.mtmessenger.MyApp;
import com.work.mtmessenger.R;
import com.work.mtmessenger.etil.GroupUnreadArray;
import com.work.mtmessenger.etil.Init;
import com.work.mtmessenger.etil.Lgin;
import com.work.mtmessenger.etil.Nodata;
import com.work.mtmessenger.etil.Trycod;
import com.work.mtmessenger.ui.HongbaoActivity;
import com.work.mtmessenger.ui.MainActivity;
import com.work.mtmessenger.ui.NewclassActivity;
import com.work.mtmessenger.ui.PaypassActivity;
import com.work.mtmessenger.ui.SayActivity;
import com.work.mtmessenger.ui.SearchActivity;
import com.work.mtmessenger.ui.SettingclassActivity;
import com.work.mtmessenger.util.DesUtils;
import com.work.mtmessenger.util.StatusBarUtil;
import com.work.mtmessenger.util.ToastUtil;
import com.zhangke.websocket.SimpleListener;
import com.zhangke.websocket.SocketListener;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.WebSocketManager;
import com.zhangke.websocket.WebSocketSetting;
import com.zhangke.websocket.response.ErrorResponse;

import org.afinal.simplecache.ACache;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;
import static com.work.mtmessenger.MyApp.waiwang;

/**
 * 启动页，app刚打开时的activity
 * create by linbin
 * /**
 * 权限的请求操作封装在BaseActivity基类中，当前类继承BaseActivity基类
 * 首先声明（废话）：动态权限是Android 6.0版本以后（SDK:23），谷歌更新的对于一些危险/隐秘权限的动态提醒和动态授权的“规定”
 * 可以用判断方法：Build.VERSION.SDK_INT >= 23，来判断当前版本是不是大于SDK 23。——废话结束。
 * 使用权限方法，直接调用requestRuntimePermission（要请求的权限数组，PermissionListener监听方法）的方法，
 * 注：首先所有的动态请求，都是要在AndroidManifest.xml，中去添加的，例如：
 * <uses-permission android:name="android.permission.CALL_PHONE" />//打电话权限
 * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />//精准定位权限
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />//写数据权限
 * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />//读数据权限
 * <uses-permission android:name="android.permission.CAMERA" />//相机权限
 * <uses-permission android:name="android.permission.RECORD_AUDIO" />//录音权限
 * 等等...
 */

public class SplashActivity extends BaseActivity {
    private ACache acache;
    private Handler handler = new Handler();
    private Nodata nodata;
    private Trycod trycod;
    private String string = null;
    private Init init;
    private String wsUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBarColor(this, R.color.main);
        /**
         * （这里引用布局的onClick点击方法）
         *
         * @param view 统一请求很多权限：
         */
        requestRuntimePermission(new String[]{
//                Manifest.permission.CALL_PHONE,//打电话权限
//                    Manifest.permission.ACCESS_FINE_LOCATION,//精准定位权限
                Manifest.permission.CAMERA,//相机权限
                Manifest.permission.WRITE_EXTERNAL_STORAGE,//写数据权限
                Manifest.permission.READ_EXTERNAL_STORAGE,//读数据权限
                Manifest.permission.RECORD_AUDIO//录音权限
        }, new PermissionListener() {
            //授权后的回调方法
            @Override
            public void onGranted() {
                getinit();
            }

            //权限被拒绝的回调方法
            @Override
            public void onDenied(List<String> deniedPermission) {
                for (String permission : deniedPermission) {
                    Toast.makeText(SplashActivity.this, "被拒绝权限：" + permission, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private SocketListener socketListener = new SimpleListener() {
        @Override
        public void onConnected() {
            appendMsgDisplay("onConnected");
        }

        @Override
        public void onConnectFailed(Throwable e) {
            if (e != null) {
                appendMsgDisplay("onConnectFailed:" + e.toString());
            } else {
                appendMsgDisplay("onConnectFailed:null");
            }
        }

        @Override
        public void onDisconnect() {
            appendMsgDisplay("onDisconnect");
        }

        @Override
        public void onSendDataError(ErrorResponse errorResponse) {
            appendMsgDisplay("" + errorResponse.toString());
            errorResponse.release();
        }

        @Override
        public <T> void onMessage(String message, T data) {
            System.out.println("onMessage(String, T):" + message);
            appendMsgDisplay(message);
        }

        @Override
        public <T> void onMessage(ByteBuffer bytes, T data) {
            appendMsgDisplay("" + bytes);
        }
    };

    private void appendMsgDisplay(String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                System.out.println("aaaaa=" + msg);
                if (msg.contains("onConnected")) {
                    tokenlogin();
                } else if (msg.contains("onDisconnect")) {
                } else if (msg.contains("\"ret_code\":0")) {
                } else if (msg.contains("\"ret_code\":10000")) {
                    if (msg.contains("\"event_id\":10001")) {
                        Gson gson = new Gson();
                        LoginActivity.st = gson.fromJson(msg, Lgin.class);
                        MyApp.balance = LoginActivity.st.getData().getBalance();
                        MyApp.nick_name = LoginActivity.st.getData().getNick_name();
                        MyApp.token = LoginActivity.st.getData().getToken();
                        //我的头像
                        MyApp.head_url = LoginActivity.st.getData().getHead_url();
                        //我的好友数量
                        MyApp.friend_count = LoginActivity.st.getData().getFriend_count();
                        //新好友数量
                        MyApp.new_friend_count = LoginActivity.st.getData().getFriend_request_array().size();
                        acache.put("newsaylist", msg);//将数据存入缓存中
                        System.out.println("11111116");
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                } else {
                    Gson gson = new Gson();
                    nodata = gson.fromJson(msg, Nodata.class);
                    ToastUtil.show(SplashActivity.this, nodata.getTips());
                    if (nodata.getTips().equals("重复登录")) {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                    if (nodata.getTips().equals("登录失效")) {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                }

            }
        });
    }

    private void tokenlogin() {
        acache = ACache.get(SplashActivity.this);//创建ACache组件
        String cacheData = acache.getAsString("token");//从缓存中取数据
        if (!TextUtils.isEmpty(cacheData)) {
            String json1 = "{\"event_id\":" + 10021;
            String json2 = ",\"token\":\"";
            String json3 = "\"}";
            String json = json1 + json2 + cacheData + json3;
            WebSocketHandler.getDefault().send(json);
            //必须延迟发送  否则后台异常
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    WebSocketHandler.getDefault().send(json);
//                    System.out.println("11111112" + json);
//                }
//            }, 2000);

        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));

        }
    }


    private void getinit() {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("", "")
                .build();

        //创建一个请求对象
        String urlapi = MyApp.waiwang + "extension/init";
        System.out.println("/n需要发送的地址" + urlapi);
        Request request = new Request.Builder()
                .url(urlapi)
                .method("POST", requestBody)
                .addHeader("Content-Type", "multipart/form-data; boundary=--------------------------142890637432470731177857")
                .build();

        okhttp3.Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            //请求失败时调用
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.e(TAG, "onFailure: " + e);
            }

            //请求成功时调用
            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    System.out.println("/njsonData===" + jsonData);
                    Gson gson = new Gson();
                    if (jsonData.contains("\"code\":10000")) {
                        trycod = gson.fromJson(jsonData, Trycod.class);
                        string = trycod.getData();
                        string = DesUtils.DesDecrypt(string, MyApp.Deskey, MyApp.Desiv);
                        System.out.println("初始化内容" + string);
                        init = gson.fromJson(string, Init.class);
                        wsUrl = init.getWs_url();
                        MyApp.wx_app_secret = init.getWx_app_secret();
                        MyApp.wx_appId = init.getWx_appId();
                        getws();//websock长连

                    } else {
                        nodata = gson.fromJson(jsonData, Nodata.class);
                        ToastUtil.show(SplashActivity.this, nodata.getTips());

                    }

                }

            }

        });
    }

    //websock长连
    private void getws() {
        WebSocketSetting setting = new WebSocketSetting();
        //设置请求头
        Map<String, String> map = new HashMap<>();
//        map.put("Origin", "ws://192.168.0.104:4545/");
        map.put("Origin", wsUrl + "/");
        String s = System.getProperty("http.agent");
        map.put("User-Agent", s);
        //添加请求头
        setting.setHttpHeaders(map);
        setting.setConnectUrl(wsUrl + "/");//必填

        //设置连接超时时间
        setting.setConnectTimeout(15 * 1000);

        //设置心跳间隔时间
        setting.setConnectionLostTimeout(40);

        //设置断开后的重连次数，可以设置的很大，不会有什么性能上的影响
        setting.setReconnectFrequency(60);

        //网络状态发生变化后是否重连，
        //需要调用 WebSocketHandler.registerNetworkChangedReceiver(context) 方法注册网络监听广播
        setting.setReconnectWithNetworkChanged(true);

        //通过 init 方法初始化默认的 WebSocketManager 对象
        WebSocketManager manager = WebSocketHandler.init(setting);
        //启动连接
        manager.start();

        //注意，需要在 AndroidManifest 中配置网络状态获取权限
        //注册网路连接状态变化广播
//        WebSocketHandler.registerNetworkChangedReceiver(this);

        //连接成功 收到框架自带返回值 onConnected后开始请求后端
        WebSocketManager manager1 = WebSocketHandler.getDefault();
        manager1.addListener(socketListener);

//        tokenlogin();
    }
}
