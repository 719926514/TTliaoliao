package com.work.mtmessenger.ui.myactivity;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.work.mtmessenger.MyApp;
import com.work.mtmessenger.R;
import com.work.mtmessenger.adapter.SortAdapter;
import com.work.mtmessenger.etil.Friend;
import com.work.mtmessenger.etil.NewAddFriend;
import com.work.mtmessenger.etil.NewName;
import com.work.mtmessenger.etil.Nodata;
import com.work.mtmessenger.ui.LiaisonManActivity;
import com.work.mtmessenger.ui.PaypassActivity;
import com.work.mtmessenger.ui.login.BaseActivity;
import com.work.mtmessenger.util.StatusBarUtil;
import com.work.mtmessenger.util.ToastUtil;
import com.zhangke.websocket.SimpleListener;
import com.zhangke.websocket.SocketListener;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.WebSocketManager;
import com.zhangke.websocket.response.ErrorResponse;

import java.nio.ByteBuffer;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.work.mtmessenger.ui.login.LoginActivity.st;


public class SetingnameActivity extends BaseActivity {


    @BindView(R.id.iv_go_back)
    ImageView ivGoBack;
    @BindView(R.id.tv_title_column)
    TextView tvTitleColumn;
    @BindView(R.id.tv_right_action)
    TextView tvRightAction;
    @BindView(R.id.nicname)
    EditText nicname;
    private Handler handler = new Handler();
    private NewName newName;
    private Nodata nodata;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setingname);
        ButterKnife.bind(this);


        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBarColor(this, R.color.main);
        ivGoBack.setVisibility(View.VISIBLE);
        tvTitleColumn.setText("昵称");
        tvRightAction.setText("保存");
        tvRightAction.setVisibility(View.VISIBLE);

        nicname.setHint(MyApp.nick_name);

        //        //通过此方法获取默认的 WebSocketManager 对象
        WebSocketManager manager = WebSocketHandler.getDefault();
        manager.addListener(socketListener);
    }


    @OnClick({R.id.iv_go_back, R.id.tv_right_action})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_go_back:
                finish();
                break;
            case R.id.tv_right_action:
                outname();
                break;
        }
    }


    private void outname() {
        String json1 = "{\"event_id\":10019";
        String json2 = ",\"nick_name\":\"";
//        String json22 = "\",\"pass\":\"";
        String json3 = "\"}";
        String json = json1 + json2 + nicname.getText().toString().trim() + json3;
        //发送 String 数据
        WebSocketHandler.getDefault().send(json);
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

                } else if (msg.contains("\"ret_code\":10000")) {
                    Gson gson = new Gson();
                    if (msg.contains("\"event_id\":10019")) {
                        newName = gson.fromJson(msg, NewName.class);
                        MyApp.nick_name = nicname.getText().toString().trim();
                        ToastUtil.show(SetingnameActivity.this, newName.getTips());
                        finish();
                    }


                } else {
                    Gson gson = new Gson();
                    nodata = gson.fromJson(msg, Nodata.class);
                    ToastUtil.show(SetingnameActivity.this, nodata.getTips());
                }
            }
        });
    }
}