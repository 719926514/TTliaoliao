package com.work.mtmessenger.ui;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.nio.ByteBuffer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HongbaoActivity extends BaseActivity {


    @BindView(R.id.iv_go_back)
    ImageView ivGoBack;
    @BindView(R.id.tv_title_column)
    TextView tvTitleColumn;
    @BindView(R.id.etmoney)
    EditText etmoney;
    @BindView(R.id.text)
    EditText text;
    @BindView(R.id.tv_go)
    TextView tvGo;
    @BindView(R.id.pass)
    EditText edpass;

    private Nodata nodata;
    private Handler handler = new Handler();
    private int target_id, target_type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hongbao);
        ButterKnife.bind(this);

        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBarColor(this, R.color.main);

        tvTitleColumn.setText("发红包");
        ivGoBack.setVisibility(View.VISIBLE);

        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        target_id = bundle.getInt("target_id");// 聊天人  编号/群编号
        target_type = bundle.getInt("target_type");//聊天人类型   单人1/群2

        WebSocketManager manager = WebSocketHandler.getDefault();
        manager.addListener(socketListener);

        if (target_type == 1) {
            text.setText("1");
            text.setVisibility(View.GONE);
        } else {
            etmoney.setHint("红包总金额");
        }
        etmoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                //删除.后面超过两位的数字
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        etmoney.setText(s);
                        etmoney.setSelection(s.length());
                    }
                }

                //如果.在起始位置,则起始位置自动补0
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    etmoney.setText(s);
                    etmoney.setSelection(2);
                }

                //如果起始位置为0并且第二位跟的不是".",则无法后续输入
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        etmoney.setText(s.subSequence(0, 1));
                        etmoney.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

        });

    }


    @OnClick({R.id.iv_go_back, R.id.tv_go})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_go_back:
                finish();
                break;
            case R.id.tv_go:
                String money = etmoney.getText().toString().trim();
                String nab = text.getText().toString().trim();
                String pass = edpass.getText().toString().trim();
                if (TextUtils.isEmpty(money)) {
                    ToastUtil.show(HongbaoActivity.this, "请输入金额");
                    return;
                }
                if (TextUtils.isEmpty(nab)) {
                    ToastUtil.show(HongbaoActivity.this, "请输入支付密码");
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    ToastUtil.show(HongbaoActivity.this, "请输入支付密码");
                    return;
                }
                float f = Float.parseFloat(money);
                int i = Integer.parseInt(nab);
                pass = DesUtils.DesEncrypt(pass, MyApp.Deskey, MyApp.Desiv);
                String json1 = "{\"event_id\":" + 10032;
                String json2 = ",\"target_type\":" + target_type;
                String json22 = ",\"target_id\":" + target_id;
                String json222 = ",\"count\":" + i;
                String json2222 = ",\"money\":" + f;
                String json22222 = ",\"pass\":\"" + pass;
                String json3 = "\"}";
                String json = json1 + json2 + json22 + json222 + json2222 + json22222 + json3;
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
                Gson gson = new Gson();
                if (msg.contains("onConnected")) {
                } else if (msg.contains("onDisconnect")) {
                } else if (msg.contains("\"ret_code\":0")) {
                } else if (msg.contains("\"ret_code\":10000")) {//如果json包含 "code":"1"
                    if (msg.contains("\"event_id\":10032")) {
                        nodata = gson.fromJson(msg, Nodata.class);
                        ToastUtil.show(HongbaoActivity.this, nodata.getTips());
                        finish();

                    }
                } else {
                    nodata = gson.fromJson(msg, Nodata.class);
                    ToastUtil.show(HongbaoActivity.this, nodata.getTips());
                    finish();
                }
            }
        });
    }
}
