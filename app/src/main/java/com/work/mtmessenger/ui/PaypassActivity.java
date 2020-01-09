package com.work.mtmessenger.ui;


import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.work.mtmessenger.MyApp;
import com.work.mtmessenger.R;
import com.work.mtmessenger.etil.Nodata;
import com.work.mtmessenger.ui.login.BaseActivity;
import com.work.mtmessenger.util.DesUtils;
import com.work.mtmessenger.util.StatusBarUtil;
import com.work.mtmessenger.util.ToastUtil;
import com.zhangke.websocket.SimpleListener;
import com.zhangke.websocket.SocketListener;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.WebSocketManager;
import com.zhangke.websocket.response.ErrorResponse;

import org.afinal.simplecache.ACache;

import java.nio.ByteBuffer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PaypassActivity extends BaseActivity {

    @BindView(R.id.iv_go_back)
    ImageView ivGoBack;
    @BindView(R.id.tv_title_column)
    TextView tvTitleColumn;
    @BindView(R.id.et_iod_pass)
    EditText etIodPass;
    @BindView(R.id.et_new_pass)
    EditText etNewPass;
    @BindView(R.id.tv_go)
    TextView tvGo;
    @BindView(R.id.ll_go_jieshao)
    LinearLayout llGoJieshao;
    private ACache acache;//缓存框架
    private boolean init = false;
    private Handler handler = new Handler();
    private Nodata nodata;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypass);
        ButterKnife.bind(this);
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBarColor(this, R.color.main);

        //通过此方法获取默认的 WebSocketManager 对象
        WebSocketManager manager = WebSocketHandler.getDefault();
        manager.addListener(socketListener);

        ivGoBack.setVisibility(View.VISIBLE);
        tvTitleColumn.setText("设置");
        acache = ACache.get(this);//创建ACache组件
        String cacheData = acache.getAsString("paypass");//从缓存中取数据

        if (cacheData == null) {
            init = true;
        }

        if (init) {
            etIodPass.setVisibility(View.GONE);
        }

    }

    @OnClick({R.id.iv_go_back, R.id.ll_go_jieshao})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_go_back:
                finish();
                break;
            case R.id.ll_go_jieshao:
                String old_pass = etIodPass.getText().toString().trim();
                String new_pass = etNewPass.getText().toString().trim();
                if (init) {
                    if (new_pass.length() < 6) {
                        ToastUtil.show(PaypassActivity.this, "请输入6位密码");
                        return;
                    }
                } else {
                    if (old_pass.length() < 6 || new_pass.length() < 6) {
                        ToastUtil.show(PaypassActivity.this, "请输入6位密码");
                        return;
                    }
                }

                old_pass = DesUtils.DesEncrypt(old_pass, MyApp.Deskey, MyApp.Desiv);
                new_pass = DesUtils.DesEncrypt(new_pass, MyApp.Deskey, MyApp.Desiv);

                String json1 = "{\"event_id\":10031";
                String json22 = ",\"new_pass\":\"" + new_pass;
                String json23 = "\",\"old_pass\":\"" + old_pass;
                String json222 = "\",\"init\":" + init;
                String json3 = "}";
                String json = null;
                if (init) {
                    json = json1 + json22 + json222 + json3;
                } else {
                    json = json1 + json22 + json23 + json222 + json3;
                }

                //发送 String 数据
                WebSocketHandler.getDefault().send(json);

                break;
        }
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


    private void appendMsgDisplay(String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (msg.contains("onConnected")) {
                }else if(msg.contains("onDisconnect")) {
                } else if (msg.contains("\"ret_code\":0")) {

                } else if (msg.contains("\"ret_code\":10000")) {//如果json包含 "code":"1"
                    if (msg.contains("\"event_id\":10031")) {
                        Gson gson = new Gson();
                        nodata = gson.fromJson(msg, Nodata.class);
                        acache.put("paypass", msg);//将数据存入缓存中
                        ToastUtil.show(PaypassActivity.this, nodata.getTips());
                        finish();

                    }
                } else {
                    Gson gson = new Gson();
                    nodata = gson.fromJson(msg, Nodata.class);
                    ToastUtil.show(PaypassActivity.this, nodata.getTips());
                }
            }
        });
    }

}