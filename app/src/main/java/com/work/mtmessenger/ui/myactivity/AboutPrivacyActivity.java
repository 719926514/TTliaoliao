package com.work.mtmessenger.ui.myactivity;


import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.work.mtmessenger.MyApp;
import com.work.mtmessenger.R;
import com.work.mtmessenger.etil.Abou;
import com.work.mtmessenger.etil.Img;
import com.work.mtmessenger.ui.login.BaseActivity;
import com.work.mtmessenger.util.StatusBarUtil;
import com.work.mtmessenger.widgets.xlistview.manage.AudioRecorderButton;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;


public class AboutPrivacyActivity extends BaseActivity {


    @BindView(R.id.iv_go_back)
    ImageView ivGoBack;
    @BindView(R.id.tv_title_column)
    TextView tvTitleColumn;
    @BindView(R.id.tv_context)
    TextView tvContext;
    String tepy;
    private Handler handler = new Handler();
    private Abou abou;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBarColor(this, R.color.main);
        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        tepy = bundle.getString("tepy");

        ivGoBack.setVisibility(View.VISIBLE);
        tvTitleColumn.setText(tepy);


        if (tepy.equals("关于我们")) {
            getdata( MyApp.wome);
        } else if (tepy.equals("隐私政策")) {
            getdata( MyApp.yingsi);
        }else if (tepy.equals("功能介绍")){
            getdata( MyApp.jieshao);
        }


    }

    @OnClick(R.id.iv_go_back)
    public void onViewClicked() {
        finish();
    }


    private void getdata(String url) {
        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=--------------------------142890637432470731177857");
        OkHttpClient okHttpClient = new OkHttpClient();
        //申明给服务端传递一个json串
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
//        RequestBody requestBody = RequestBody.create(JSON, json);
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("", "")
                .build();

        //创建一个请求对象
        String urlapi = url;
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
                        Toast.makeText(AboutPrivacyActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
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
                            if (jsonData.contains("\"code\":10000")) {
                                abou = gson.fromJson(jsonData, Abou.class);

                                String html = abou.getData().getValue();

                                CharSequence charSequence = Html.fromHtml(html, new Html.ImageGetter() {

                                    @Override
                                    public Drawable getDrawable(String arg0) {
                                        Drawable drawable = AboutPrivacyActivity.this.getResources().getDrawable(R.drawable.ic_launcher);
                                        //下面这句话不可缺少
                                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                                        return drawable;
                                    }
                                }, null);

                                tvContext.setText(charSequence);
                            } else {
                                Toast.makeText(AboutPrivacyActivity.this, abou.getMsg(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }

            }

        });

    }
}