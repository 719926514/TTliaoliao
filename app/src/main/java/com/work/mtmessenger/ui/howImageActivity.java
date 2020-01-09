package com.work.mtmessenger.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.work.mtmessenger.R;
import com.work.mtmessenger.util.BaseActivity;
import com.work.mtmessenger.util.StatusBarUtil;
import com.work.mtmessenger.util.image.MyImage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class howImageActivity extends Activity {


    @BindView(R.id.iv_big)
    MyImage ivBig;
    @BindView(R.id.iv_go_back)
    ImageView ivGoBack;
    private String iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_image);
        ButterKnife.bind(this);
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBarColor(this, R.color.main);

        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        iv = bundle.getString("iv");
        ivBig.setImageURL(iv);
    }


    @OnClick({R.id.iv_big, R.id.iv_go_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_big:
                finish();
                break;
            case R.id.iv_go_back:
                finish();
                break;
        }
    }
}