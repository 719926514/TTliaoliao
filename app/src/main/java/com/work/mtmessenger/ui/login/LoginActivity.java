package com.work.mtmessenger.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.work.mtmessenger.MyApp;
import com.work.mtmessenger.R;
import com.work.mtmessenger.etil.Lgin;
import com.work.mtmessenger.etil.Nodata;
import com.work.mtmessenger.ui.MainActivity;
import com.work.mtmessenger.ui.PaypassActivity;
import com.work.mtmessenger.util.DesUtils;
import com.work.mtmessenger.util.IsWxQQ;
import com.work.mtmessenger.util.StatusBarUtil;
import com.work.mtmessenger.util.ToastUtil;
//import com.work.mtmessenger.wxapi.WXEntryActivity;
import com.work.mtmessenger.wxapi.WXEntryActivity;
import com.zhangke.websocket.SimpleListener;
import com.zhangke.websocket.SocketListener;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.WebSocketManager;
import com.zhangke.websocket.response.ErrorResponse;

import org.afinal.simplecache.ACache;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.internal.Constants;
import cn.jpush.android.api.JPushInterface;
import me.leefeng.promptlibrary.PromptDialog;

public class LoginActivity extends BaseActivity {


    @BindView(R.id.tv_title_column)
    TextView tvTitleColumn;
    @BindView(R.id.tv_right_action)
    TextView tvRightAction;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.tv_go_find_password)
    TextView tvGoFindPassword;
    @BindView(R.id.tv_go_logining)
    TextView tvGoLogining;
    @BindView(R.id.tv_wx)
    TextView tv_wx;
    private Handler handler = new Handler();
    public static Lgin st;
    private ACache acache;//缓存框架
    private static IWXAPI WXapi;
    private String WX_APP_ID = MyApp.wx_appId;
    private Nodata nodata;
    private PromptDialog promptDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBarColor(this, R.color.main);
        tvTitleColumn.setText("登录");
        tvRightAction.setVisibility(View.VISIBLE);
        tvRightAction.setText("注册");

        //通过此方法获取默认的 WebSocketManager 对象
        WebSocketManager manager = WebSocketHandler.getDefault();
        manager.addListener(socketListener);

        acache = ACache.get(this);//创建ACache组件

    }


    @OnClick({R.id.tv_right_action, R.id.tv_go_find_password, R.id.tv_go_logining, R.id.tv_wx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_right_action:
                //带参数跳转
                Intent i1 = new Intent(LoginActivity.this, RegisterActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("tepy", "注册");
                i1.putExtras(bundle1);
                startActivity(i1);

                break;
            case R.id.tv_go_find_password:
                //带参数跳转
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("tepy", "忘记密码");
                i.putExtras(bundle);
                startActivity(i);
                break;
            case R.id.tv_go_logining:
                String Phone = etPhone.getText().toString().trim();
                String Password = edPassword.getText().toString().trim();

                if (TextUtils.isEmpty(Phone)) {
                    ToastUtil.show(LoginActivity.this, "账号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(Password)) {
                    ToastUtil.show(LoginActivity.this, "密码不能为空");
                    return;
                }
                MyApp.account = Phone;
                MyApp.pass = Password;
                getlogin(Phone, Password);


                break;
            case R.id.tv_wx:
                /**
                 * 登录微信
                 */
                if (IsWxQQ.isWeixinAvilible(LoginActivity.this)) {
                    //创建对象
                    promptDialog = new PromptDialog(this);
                    //设置自定义属性

                    promptDialog.showLoading("正在登录");

                    WXapi = WXAPIFactory.createWXAPI(this, WX_APP_ID, true);
                    WXapi.registerApp(WX_APP_ID);
                    SendAuth.Req req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";
                    req.state = "wechat_sdk_demo";
                    WXapi.sendReq(req);
                }else {
                    ToastUtil.show(LoginActivity.this,"您还没有安装微信，请先安装微信客户端");
                }
                break;
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebSocketHandler.getDefault().removeListener(socketListener);
    }

    private void appendMsgDisplay(String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (msg.contains("onConnected")) {
                    return;
                } else if (msg.contains("onDisconnect")) {
                } else if (msg.contains("\"ret_code\":0")) {
                    return;
                } else if (msg.contains("\"ret_code\":10000")) {
                    if (msg.contains("\"event_id\":10001")) {
                        Gson gson = new Gson();
                        st = gson.fromJson(msg, Lgin.class);

                        //跳转

                        MyApp.balance = st.getData().getBalance();
                        //  我的昵称
                        MyApp.nick_name = st.getData().getNick_name();
                        MyApp.token = st.getData().getToken();
                        //我的头像
                        MyApp.head_url = st.getData().getHead_url();
                        //我的好友数量
                        MyApp.friend_count = st.getData().getFriend_count();
                        //新好友数量
                        MyApp.new_friend_count = st.getData().getFriend_request_array().size();
                        acache.put("newsaylist", msg);//将数据存入缓存中
                        if (!TextUtils.isEmpty(st.getData().getToken())) {
                            acache.put("token", st.getData().getToken());//将数据存入缓存中
                        }
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();


                    }
                } else if (msg.contains("\"ret_code\":10017")) {
                    //带参数跳转
                    Intent i1 = new Intent(LoginActivity.this, RegisterActivity.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("tepy", "绑定手机");
                    bundle1.putString("open_id", WXEntryActivity.id);
                    i1.putExtras(bundle1);
                    startActivity(i1);
                    finish();
                } else {
                    Gson gson = new Gson();
                    nodata = gson.fromJson(msg, Nodata.class);
                    ToastUtil.show(LoginActivity.this, nodata.getTips());

                }
            }
        });
    }


    public void getlogin(String Phone, String Password) {
        String RegId = JPushInterface.getRegistrationID(getApplicationContext());
        Password = DesUtils.DesEncrypt(Password.trim(), MyApp.Deskey, MyApp.Desiv);
        String json1 = "{\"event_id\":" + 10001;
        String json2 = ",\"account\":\"";
        String json22 = "\",\"pass\":\"";
        String json222 = "\",\"reg_id\":\"";
        String json3 = "\"}";
        String json = json1 + json2 + Phone + json22 + Password.trim() + json222 + RegId + json3;


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
}
