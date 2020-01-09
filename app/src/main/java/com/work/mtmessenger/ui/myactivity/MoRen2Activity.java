package com.work.mtmessenger.ui.myactivity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.work.mtmessenger.MyApp;
import com.work.mtmessenger.R;
import com.work.mtmessenger.adapter.GridMorenAdapter;
import com.work.mtmessenger.etil.Moren;
import com.work.mtmessenger.etil.Nodata;
import com.work.mtmessenger.etil.Seelist;
import com.work.mtmessenger.ui.MainActivity;
import com.work.mtmessenger.ui.SeeListActivity;
import com.work.mtmessenger.ui.login.BaseActivity;
import com.work.mtmessenger.util.StatusBarUtil;
import com.work.mtmessenger.util.ToastUtil;
import com.zhangke.websocket.SimpleListener;
import com.zhangke.websocket.SocketListener;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.WebSocketManager;
import com.zhangke.websocket.response.ErrorResponse;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;


public class MoRen2Activity extends BaseActivity {


    @BindView(R.id.iv_go_back)
    ImageView ivGoBack;
    @BindView(R.id.tv_title_column)
    TextView tvTitleColumn;
    @BindView(R.id.gv_my_wallet_category)
    GridView gvMyWalletCategory;
    private Moren moren;
    private GridMorenAdapter gridAdapter;
    private List<Moren> listBean = new ArrayList<Moren>();
    private Handler handler = new Handler();
    private String tururl;
    private int target_id;
    private Nodata nodata;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moren);
        ButterKnife.bind(this);
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBarColor(this, R.color.main);
        ivGoBack.setVisibility(View.VISIBLE);
        tvTitleColumn.setText("默认头像");
        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值

        target_id = bundle.getInt("target_id");

        outmoren();
        gridAdapter = new GridMorenAdapter(MoRen2Activity.this, R.layout.vw_list_item, listBean);
        gvMyWalletCategory.setAdapter(gridAdapter);
        //通过此方法获取默认的 WebSocketManager 对象
        WebSocketManager manager = WebSocketHandler.getDefault();
        manager.addListener(socketListener);

        gvMyWalletCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tururl = listBean.get(i).getData().get(i);

                String json1 = "{\"event_id\":10041";
                String json2 = ",\"group_id\":";
                String json22 = ",\"url\":\"";
                String json3 = "\"}";
                String json = json1 + json2 + target_id + json22 + tururl + json3;
                //发送 String 数据
                WebSocketHandler.getDefault().send(json);


            }
        });


    }


    @OnClick(R.id.iv_go_back)
    public void onViewClicked() {
        finish();
    }


    private void outmoren() {

        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=--------------------------142890637432470731177857");
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("", "")
                .build();

        //创建一个请求对象
        String urlapi = MyApp.moren;
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

                runOnUiThread(new Runnable() {//子线程中不能直接吐司
                    @Override
                    public void run() {

                        Toast.makeText(MoRen2Activity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e(TAG, "onFailure: " + e);
            }

            //请求成功时调用
            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    System.out.println("/njsonData===" + jsonData);

                    //使用Handler更新了界面
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            moren = gson.fromJson(jsonData, Moren.class);
                            for (int i = 0; i < moren.getData().size(); i++) {
                                listBean.add(moren);
                            }

                            gridAdapter.notifyDataSetChanged();
                        }
                    });
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
                if (msg.contains("onConnected")) {
                } else if (msg.contains("onDisconnect")) {
                } else if (msg.contains("\"ret_code\":0")) {

                } else if (msg.contains("\"ret_code\":10000")) {//如果json包含 "code":"1"
                    Gson gson = new Gson();
                    if (msg.contains("\"event_id\":10041")) {
                        nodata = gson.fromJson(msg, Nodata.class);
                        ToastUtil.show(MoRen2Activity.this, nodata.getTips());
                        finish();
                    }
                } else {
                    Gson gson = new Gson();
                    nodata = gson.fromJson(msg, Nodata.class);
                    ToastUtil.show(MoRen2Activity.this, nodata.getTips());
                }
            }
        });
    }
}