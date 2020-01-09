package com.work.mtmessenger.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.work.mtmessenger.MyApp;
import com.work.mtmessenger.R;
import com.work.mtmessenger.util.StatusBarUtil;
import com.zhangke.websocket.WebSocketHandler;
import org.afinal.simplecache.ACache;
import org.json.JSONObject;
import java.net.URLEncoder;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;
    private BaseResp resp = null;
//    private String WX_APP_ID = MyApp.wx_appId;
    private String WX_APP_ID = MyApp.wx_appId;
    // 获取第一步的code后，请求以下链接获取access_token
    private String GetCodeRequest = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    // 获取用户个人信息
    private String GetUserInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
    private String WX_APP_SECRET = MyApp.wx_app_secret;
//    private String WX_APP_SECRET = MyApp.wx_app_secret;
    private Handler handler = new Handler();
    private ACache acache;//缓存框架
    public static String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, WX_APP_ID, false);
        api.handleIntent(getIntent(), this);
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBarColor(this, R.color.main);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {

        finish();
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        String result = "";
        if (resp != null) {
            resp = resp;
        }
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "发送成功";
                String finalResult = result;
                Toast.makeText(WXEntryActivity.this, result, Toast.LENGTH_LONG).show();
                String code = ((SendAuth.Resp) resp).code;
                /*
                 * 将你前面得到的AppID、AppSecret、code，拼接成URL 获取access_token等等的信息(微信)
                 */
                String get_access_token = getCodeRequest(code);
                AsyncHttpClient client = new AsyncHttpClient();
                client.post(get_access_token, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        // TODO Auto-generated method stub
                        super.onSuccess(statusCode, response);
                        try {


                            if (!response.equals("")) {
                                String access_token = response.getString("access_token");
                                String openid = response.getString("openid");

                                id = openid;
//                                String get_user_info_url = getUserInfo(access_token, openid);
//                                getUserInfo(get_user_info_url);


                                //通过此方法获取默认的 WebSocketManager 对象
//                                WebSocketManager manager = WebSocketHandler.getDefault();
//                                manager.addListener(socketListener);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        String json1 = "{\"event_id\":" + 10036;
                                        String json2 = ",\"open_id\":\"";
                                        String json22 = "\",\"access_token\":\"";
                                        String json3 = "\"}";
                                        String json = json1 + json2 + openid + json22 + access_token + json3;
                                        //发送 String 数据;
                                        WebSocketHandler.getDefault().send(json);

                                    }
                                }, 3500);



                            }
                            finish();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });

                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:

                result = "发送取消";
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:

                result = "发送被拒绝";
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                finish();
                break;
            default:

                result = "发送返回";
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                finish();
                break;
        }
    }

    /**
     * 通过拼接的用户信息url获取用户信息
     *
     * @param user_info_url
     */
    private void getUserInfo(String user_info_url) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(user_info_url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, response);
                try {

                    System.out.println("获取用户信息:" + response);

                    if (!response.equals("")) {
                        String openid = response.getString("openid");
                        String nickname = response.getString("nickname");
                        String headimgurl = response.getString("headimgurl");


                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        finish();
    }

    /**
     * 获取access_token的URL（微信）
     *
     * @param code 授权时，微信回调给的
     * @return URL
     */
    private String getCodeRequest(String code) {
        String result = null;
        GetCodeRequest = GetCodeRequest.replace("APPID", urlEnodeUTF8(WX_APP_ID));
        GetCodeRequest = GetCodeRequest.replace("SECRET", urlEnodeUTF8(WX_APP_SECRET));
        GetCodeRequest = GetCodeRequest.replace("CODE", urlEnodeUTF8(code));
        result = GetCodeRequest;
        return result;
    }

    /**
     * 获取用户个人信息的URL（微信）
     *
     * @param access_token 获取access_token时给的
     * @param openid       获取access_token时给的
     * @return URL
     */
    private String getUserInfo(String access_token, String openid) {
        String result = null;
        GetUserInfo = GetUserInfo.replace("ACCESS_TOKEN", urlEnodeUTF8(access_token));
        GetUserInfo = GetUserInfo.replace("OPENID", urlEnodeUTF8(openid));
        result = GetUserInfo;
        return result;
    }

    private String urlEnodeUTF8(String str) {
        String result = str;
        try {
            result = URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


//    private SocketListener socketListener = new SimpleListener() {
//        @Override
//        public void onConnected() {
//            appendMsgDisplay("onConnected");
//        }
//
//        @Override
//        public void onConnectFailed(Throwable e) {
//            if (e != null) {
//                appendMsgDisplay("onConnectFailed:" + e.toString());
//            } else {
//                appendMsgDisplay("onConnectFailed:null");
//            }
//        }
//
//        @Override
//        public void onDisconnect() {
//            appendMsgDisplay("onDisconnect");
//        }
//
//        @Override
//        public void onSendDataError(ErrorResponse errorResponse) {
//            appendMsgDisplay("onSendDataError:" + errorResponse.toString());
//            errorResponse.release();
//        }
//
//        @Override
//        public <T> void onMessage(String message, T data) {
//            System.out.println("onMessage(String, T):" + message);
//            appendMsgDisplay(message);
//        }
//
//        @Override
//        public <T> void onMessage(ByteBuffer bytes, T data) {
//            appendMsgDisplay("onMessage(ByteBuffer, T):" + bytes);
//        }
//    };


//    private void appendMsgDisplay(String msg) {
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                if (msg.contains("\"ret_code\":10000")) {//如果json包含 "code":"1"
//                    if (msg.contains("\"event_id\":10001")) {
//                        Gson gson = new Gson();
//                        LoginActivity.st = gson.fromJson(msg, Lgin.class);
//
//                        //跳转
//                        //  我的昵称
//                        MyApp.nick_name = LoginActivity.st.getData().getNick_name();
//                        MyApp.token = LoginActivity.st.getData().getToken();
//                        //我的头像
//                        MyApp.head_url = LoginActivity.st.getData().getHead_url();
//                        //我的好友数量
//                        MyApp.friend_count = LoginActivity.st.getData().getFriend_count();
//                        //新好友数量
//                        MyApp.new_friend_count = LoginActivity.st.getData().getFriend_request_array().size();
//                        acache = ACache.get(WXEntryActivity.this);//创建ACache组件
//                        acache.put("newsaylist", msg);//将数据存入缓存中
//                        if (!TextUtils.isEmpty(LoginActivity.st.getData().getToken())) {
//                            acache.put("token", LoginActivity.st.getData().getToken());//将数据存入缓存中
//                        }
//                        startActivity(new Intent(WXEntryActivity.this, MainActivity.class));
//                        finish();
//
//                    }
//                } else if (msg.contains("\"ret_code\":10017")) {
//                    //带参数跳转
//                    Intent i1 = new Intent(WXEntryActivity.this, RegisterActivity.class);
//                    Bundle bundle1 = new Bundle();
//                    bundle1.putString("tepy", "绑定手机");
//                    bundle1.putString("open_id", id);
//                    i1.putExtras(bundle1);
//                    startActivity(i1);
//                    finish();
//                } else {
//                    MyApp.code(msg, WXEntryActivity.this);
//
//                }
//            }
//        });
//    }
}