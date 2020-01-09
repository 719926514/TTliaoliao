package com.work.mtmessenger.ui.login;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.work.mtmessenger.MyApp;
import com.work.mtmessenger.R;
import com.work.mtmessenger.etil.Img;
import com.work.mtmessenger.etil.Ivcod;
import com.work.mtmessenger.etil.Nodata;
import com.work.mtmessenger.etil.Regist;
import com.work.mtmessenger.etil.Shoujiyanzma;
import com.work.mtmessenger.ui.myactivity.SetingActivity;
import com.work.mtmessenger.util.DesUtils;
import com.work.mtmessenger.util.MapToString;
import com.work.mtmessenger.util.StatusBarUtil;
import com.work.mtmessenger.util.ToastUtil;
import com.zhangke.websocket.SimpleListener;
import com.zhangke.websocket.SocketListener;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.WebSocketManager;
import com.zhangke.websocket.response.ErrorResponse;

import java.io.IOException;
import java.nio.ByteBuffer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.iv_go_back)
    ImageView ivGoBack;
    @BindView(R.id.tv_title_column)
    TextView tvTitleColumn;

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_yanzma)
    TextView tvYanzma;
    @BindView(R.id.ed_yanzm)
    EditText edYanzm;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.ed_password2)
    EditText edPassword2;
    @BindView(R.id.tv_go_net)
    TextView tvGoNet;
    @BindView(R.id.activity_main)
    LinearLayout activityMain;
    @BindView(R.id.ed_ivyanzm)
    EditText edIvyanzm;
    @BindView(R.id.tv_ivyanzma)
    ImageView tvIvyanzma;
    @BindView(R.id.rl_ivyzm)
    LinearLayout rlIvyzm;
    @BindView(R.id.ll_cod)
    LinearLayout ll_cod;
    @BindView(R.id.tv_go_cod)
    TextView tvgocod;
    private Handler handler = new Handler();
    private Boolean showPassword = false;
    private Boolean showPassword1 = false;
    private MyCountDownTimer myCountDownTimer;
    private String tepy;
    private Regist regist;
    private Ivcod ivcod;
    private String id_key;
    private String open_id = "";
    private Nodata nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpassword);
        ButterKnife.bind(this);
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBarColor(this, R.color.main);
        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        tepy = bundle.getString("tepy");
        open_id = bundle.getString("open_id", "");
        tvTitleColumn.setText(tepy);
        ivGoBack.setVisibility(View.VISIBLE);


        if (tepy.equals("注册")) {
            tvGoNet.setText("确认");
        } else {
            tvGoNet.setText("提交");
        }
        if (tepy.equals("绑定手机")) {
            edPassword.setVisibility(View.GONE);
            edPassword2.setVisibility(View.GONE);
        }
        //        //通过此方法获取默认的 WebSocketManager 对象
        WebSocketManager manager = WebSocketHandler.getDefault();
        manager.addListener(socketListener);


        getivcod();
    }

    @OnClick({R.id.iv_go_back, R.id.tv_yanzma, R.id.tv_go_net, R.id.tv_go_cod, R.id.tv_ivyanzma})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_go_back:
                finish();
                break;
            case R.id.tv_ivyanzma:
                getivcod();
                break;
            case R.id.tv_go_cod:
                if (TextUtils.isEmpty(etPhone.getText())) {
                    ToastUtil.show(RegisterActivity.this, "请输入手机号");
                    return;
                }

                break;


            case R.id.tv_yanzma:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (TextUtils.isEmpty(etPhone.getText())) {
                            ToastUtil.show(RegisterActivity.this, "请输入手机号");
                            return;
                        } else if (TextUtils.isEmpty(edIvyanzm.getText())) {
                            ToastUtil.show(RegisterActivity.this, "请输入图形码");
                            return;
                        }
                        getcod();

                    }
                });


                break;
            case R.id.tv_go_net:
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (TextUtils.isEmpty(etPhone.getText())) {
                            ToastUtil.show(RegisterActivity.this, "请输入手机号");
                            return;
                        }
                        if (TextUtils.isEmpty(edIvyanzm.getText())) {
                            ToastUtil.show(RegisterActivity.this, "请输入图形码");
                            return;
                        }
                        if (TextUtils.isEmpty(edYanzm.getText())) {
                            ToastUtil.show(RegisterActivity.this, "请输验证码");
                            return;
                        }

                        if (tepy.equals("绑定手机")) {
                            String RegId = JPushInterface.getRegistrationID(getApplicationContext());
                            String json1 = "{\"event_id\":10037";
                            String json01 = ",\"phone_code\":\"";
                            String json2 = "\",\"phone\":\"";
                            String json22 = "\",\"open_id\":\"";
                            String json222 = "\",\"reg_id\":\"";
                            String json3 = "\"}";
                            String json = json1 + json01 + edYanzm.getText().toString() + json2 + etPhone.getText() + json22 + open_id + json222 + RegId + json3;
                            //发送 String 数据
                            WebSocketHandler.getDefault().send(json);
                        } else if (tepy.equals("注册")) {
                            if (TextUtils.isEmpty(edPassword.getText())) {
                                ToastUtil.show(RegisterActivity.this, "请输入密码");
                                return;
                            }
                            if (edPassword.getText().length() > 6) {
                                ToastUtil.show(RegisterActivity.this, "密码不能小于6位数");
                                return;
                            }
                            if (edPassword2.getText().equals(edPassword.getText())) {
                                ToastUtil.show(RegisterActivity.this, "两次密码不一致");
                                edPassword2.setText("");
                                return;
                            }
                            String pass = DesUtils.DesEncrypt(edPassword.getText().toString().trim(), MyApp.Deskey, MyApp.Desiv);
                            //注册请求
                            String json1 = "{\"event_id\":10002";
                            String json2 = ",\"account\":\"";
                            String json22 = "\",\"pass\":\"";
                            String json3 = "\"}";
                            String json = json1 + json2 + etPhone.getText() + json22 + pass + json3;
                            //发送 String 数据
                            WebSocketHandler.getDefault().send(json);
                        }
                    }
                });

                break;
        }
    }


    //复写倒计时
    class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
//                getyzm();//请求验证码
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            tvYanzma.setClickable(false);
            tvYanzma.setText(l / 1000 + "s");

        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            tvYanzma.setText("发送验证码");
            //设置可点击
            tvYanzma.setClickable(true);
        }
    }


    protected void onDestroy() {
        System.out.println("-----------onDestroy------");
        if (myCountDownTimer != null) {
            myCountDownTimer.cancel();
        }
        super.onDestroy();
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
            appendMsgDisplay("onSendDataError:" + errorResponse.toString());
            errorResponse.release();
        }

        @Override
        public <T> void onMessage(String message, T data) {
            System.out.println("onMessage(String, T):" + message);
            appendMsgDisplay(message);
        }

        @Override
        public <T> void onMessage(ByteBuffer bytes, T data) {
            appendMsgDisplay("onMessage(ByteBuffer, T):" + bytes);
        }
    };

    //返回值监听
    private void appendMsgDisplay(String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (msg.contains("onConnected")) {
                } else if (msg.contains("onDisconnect")) {
                } else if (msg.contains("\"ret_code\":0")) {

                } else if (msg.contains("\"ret_code\":10000")) {
                    Gson gson = new Gson();
                    if (msg.contains("\"event_id\":10002")) {
                        regist = gson.fromJson(msg, Regist.class);
                        if (regist.getTips().equals("注册成功")) {
                            ToastUtil.show(RegisterActivity.this, regist.getTips());
                            finish();
                        } else {
                            ToastUtil.show(RegisterActivity.this, regist.getTips());
                        }
                    } else if (msg.contains("\"event_id\":10037")) {
                        nodata = gson.fromJson(msg, Nodata.class);
                        ToastUtil.show(RegisterActivity.this, nodata.getTips());
                        finish();
                    }

                } else {
                    Gson gson = new Gson();
                    nodata = gson.fromJson(msg, Nodata.class);
                    ToastUtil.show(RegisterActivity.this, nodata.getTips());
                }
            }
        });
    }

    //图形验证码
    private void getivcod() {
        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=--------------------------142890637432470731177857");
        OkHttpClient okHttpClient = new OkHttpClient();
        //申明给服务端传递一个json串
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
//        RequestBody requestBody = RequestBody.create(JSON, json);
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("", "")
                .build();

        //创建一个请求对象
        String urlapi = MyApp.ivcod;
        System.out.println("/n需要发送的地址" + urlapi);
        Request request = new Request.Builder()
                .url(urlapi)
                .method("POST", requestBody)
                .addHeader("Content-Type", "multipart/form-data; boundary=--------------------------142890637432470731177857")
                .build();


        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            //请求失败时调用
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {//子线程中不能直接吐司
                    @Override
                    public void run() {
                        System.out.println("网络异常");
                        Toast.makeText(RegisterActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e(TAG, "onFailure: " + e);
            }

            //请求成功时调用
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    System.out.println("/njsonData===" + jsonData);

                    //使用Handler更新了界面
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            ivcod = gson.fromJson(jsonData, Ivcod.class);
                            if (jsonData.contains("\"code\":10000")) {
                                String s = ivcod.getData().getImg_data();
                                String s2 = "data:image/png;base64,";
                                String sub = "";
                                sub = s.replaceAll(s2, "");
                                System.out.println("编译后：" + sub);
                                byte[] decodedString = Base64.decode(sub, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                tvIvyanzma.setImageBitmap(decodedByte);
                                id_key = ivcod.getData().getId_key();
                            } else {
                                ToastUtil.show(RegisterActivity.this, ivcod.getMsg());
                            }

                        }
                    });
                }

            }

        });
    }


    //手机验证码
    private void getcod() {
        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=--------------------------142890637432470731177857");
        OkHttpClient okHttpClient = new OkHttpClient();
        //申明给服务端传递一个json串
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
//        RequestBody requestBody = RequestBody.create(JSON, json);
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("id_key", id_key)
                .addFormDataPart("captcha", edIvyanzm.getText().toString())
                .addFormDataPart("phone", etPhone.getText().toString())
                .build();

        //创建一个请求对象
        String urlapi = MyApp.cod;
        System.out.println("/n需要发送的地址" + urlapi);
        Request request = new Request.Builder()
                .url(urlapi)
                .method("POST", requestBody)
                .addHeader("Content-Type", "multipart/form-data; boundary=--------------------------142890637432470731177857")
                .build();


        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            //请求失败时调用
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {//子线程中不能直接吐司
                    @Override
                    public void run() {
                        System.out.println("网络异常");
                        Toast.makeText(RegisterActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e(TAG, "onFailure: " + e);
            }

            //请求成功时调用
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    //使用Handler更新了界面
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            Shoujiyanzma shoujiyanzma = gson.fromJson(jsonData, Shoujiyanzma.class);
                            if (jsonData.contains("\"code\":10000")) {
                                ToastUtil.show(RegisterActivity.this, shoujiyanzma.getData());
                                //new倒计时对象,总共的时间,每隔多少秒更新一次时间
                                myCountDownTimer = new MyCountDownTimer(60000, 1000);
                                myCountDownTimer.start();

                            } else {
                                ToastUtil.show(RegisterActivity.this, shoujiyanzma.getMsg());
                            }
                        }
                    });
                }

            }

        });
    }


    public String deleteCharString0(String sourceString, char chElemData) {
        String deleteString = "";
        for (int i = 0; i < sourceString.length(); i++) {
            if (sourceString.charAt(i) != chElemData) {
                deleteString += sourceString.charAt(i);
            }
        }
        return deleteString;
    }

}
