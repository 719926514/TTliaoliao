package com.work.mtmessenger.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.work.mtmessenger.MyApp;
import com.work.mtmessenger.R;
import com.work.mtmessenger.ui.login.BaseActivity;
import com.work.mtmessenger.ui.myactivity.SetActivity;
import com.work.mtmessenger.ui.myactivity.SetingActivity;
import com.work.mtmessenger.util.StatusBarUtil;
import com.work.mtmessenger.util.image.MyImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyActivity extends BaseActivity {


    @BindView(R.id.tv_title_column)
    TextView tvTitleColumn;
    @BindView(R.id.tv_right_action)
    TextView tvRightAction;
    @BindView(R.id.iv_right_action)
    ImageView ivRightAction;
    @BindView(R.id.myiv_phot)
    MyImageView myivPhot;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_context)
    TextView tvContext;
    @BindView(R.id.ll_go_seting)
    LinearLayout llGoSeting;
    @BindView(R.id.ll_go_about)
    LinearLayout llGoAbout;
    @BindView(R.id.ll_go_Privacy)
    LinearLayout llGoPrivacy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        ButterKnife.bind(this);

        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBarColor(this, R.color.main);
        ivRightAction.setVisibility(View.VISIBLE);
        tvTitleColumn.setText("我的");
        tvContext.setVisibility(View.GONE);

    }

    @OnClick({R.id.iv_right_action, R.id.ll_go_seting, R.id.ll_go_about, R.id.ll_go_Privacy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_right_action:
                startActivity(new Intent(MyActivity.this, SearchActivity.class));
                break;
            case R.id.ll_go_seting:
                //设置
                startActivity(new Intent(MyActivity.this, SetingActivity.class));
                break;

            case R.id.ll_go_about:
                startActivity(new Intent(MyActivity.this, SetActivity.class));

                break;
            case R.id.ll_go_Privacy:
                startActivity(new Intent(MyActivity.this, MymoneyActivity.class));

               //钱包
                break;
        }
    }

    protected void onResume() {
        super.onResume();
        myivPhot.setImageURL(MyApp.head_url);
        tvName.setText(MyApp.nick_name);
    }


    @Override
    public void onBackPressed() {//重写的Activity返回

        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        startActivity(intent);

    }
}
