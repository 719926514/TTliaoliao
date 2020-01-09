package com.work.mtmessenger.ui.myactivity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suke.widget.SwitchButton;
import com.work.mtmessenger.R;
import com.work.mtmessenger.etil.Abou;
import com.work.mtmessenger.ui.PaypassActivity;
import com.work.mtmessenger.ui.login.BaseActivity;
import com.work.mtmessenger.util.StatusBarUtil;

import org.afinal.simplecache.ACache;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SetActivity extends BaseActivity {
    private static final String TAG = "";
    @BindView(R.id.iv_go_back)
    ImageView ivGoBack;
    @BindView(R.id.tv_title_column)
    TextView tvTitleColumn;
    @BindView(R.id.ll_go_jieshao)
    LinearLayout llGoJieshao;
    @BindView(R.id.ll_go_toushu)
    LinearLayout llGoToushu;
    @BindView(R.id.ll_go_banben)
    LinearLayout llGoBanben;
    @BindView(R.id.ll_go_yinsi)
    LinearLayout llGoYinsi;
    @BindView(R.id.shenyin)
    SwitchButton shenyin;
    @BindView(R.id.zhendong)
    SwitchButton zhendong;
    @BindView(R.id.ll_go_paypass)
    LinearLayout ll_go_paypass;
    private Handler handler = new Handler();
    private Abou abou;
    private ACache acache;//缓存框架

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        ButterKnife.bind(this);
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBarColor(this, R.color.main);


        ivGoBack.setVisibility(View.VISIBLE);
        tvTitleColumn.setText("设置");
        acache = ACache.get(this);//创建ACache组件


        String cacheData = acache.getAsString("shenyin");//从缓存中取数据
        if (cacheData != null) {
            if (cacheData.equals(1)) {
                shenyin.setChecked(true);
            } else {
                shenyin.setChecked(false);
            }
        }
        String cacheData2 = acache.getAsString("zhendong");//从缓存中取数据
        if (cacheData2 != null) {
            if (cacheData2.equals("1")) {
                zhendong.setChecked(true);
            } else {
                zhendong.setChecked(false);
            }

        }
        shenyin.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton buttonView, boolean isChecked) {
                if (isChecked) {
                    acache.put("shenyin", "1");//将数据存入缓存中
                } else {
                    acache.put("shenyin", "2");//将数据存入缓存中
                }

            }
        });

        zhendong.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton buttonView, boolean isChecked) {
                if (isChecked) {
                    acache.put("zhendong", "1");//将数据存入缓存中
                } else {
                    acache.put("zhendong", "2");//将数据存入缓存中
                }
            }
        });
    }


    @OnClick({R.id.iv_go_back, R.id.ll_go_jieshao, R.id.ll_go_toushu, R.id.ll_go_banben, R.id.ll_go_yinsi, R.id.shenyin, R.id.zhendong,R.id.ll_go_paypass})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_go_back:
                finish();
                break;
            case R.id.ll_go_jieshao:
                Intent i = new Intent(SetActivity.this, AboutPrivacyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("tepy", "功能介绍");
                i.putExtras(bundle);
                startActivity(i);
                break;
            case R.id.ll_go_toushu:
                startActivity(new Intent(SetActivity.this, TouSuActivity.class));
//                Intent i1 = new Intent(SetActivity.this, TouSuActivity.class);
//                Bundle bundle1 = new Bundle();
//                bundle1.putString("tepy", "投诉反馈");
//                i1.putExtras(bundle1);
//                startActivity(i1);
                break;
            case R.id.ll_go_banben:
                Intent i2 = new Intent(SetActivity.this, AboutPrivacyActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putString("tepy", "版本更新");
                i2.putExtras(bundle2);
                startActivity(i2);
                break;

            case R.id.ll_go_yinsi:
                Intent i3 = new Intent(SetActivity.this, AboutPrivacyActivity.class);
                Bundle bundle3 = new Bundle();
                bundle3.putString("tepy", "隐私政策");
                i3.putExtras(bundle3);
                startActivity(i3);
                break;


            case R.id.ll_go_paypass:
                startActivity(new Intent(SetActivity.this, PaypassActivity.class));
                break;

        }
    }

}