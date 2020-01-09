package com.work.mtmessenger.ui;

import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.work.mtmessenger.MyApp;
import com.work.mtmessenger.R;
import com.work.mtmessenger.etil.Lgin;
import com.work.mtmessenger.jiguang.ExampleUtil;
import com.work.mtmessenger.jiguang.PushMessageReceiver;
import com.work.mtmessenger.ui.login.LoginActivity;
import com.work.mtmessenger.util.BadgeUtils;
import com.work.mtmessenger.util.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;


public class MainActivity extends TabActivity {

    @BindView(R.id.tv_unRead_message)
    TextView tvUnReadMessage;
    private TabHost tabHost;
    private Lgin st;
    public static boolean isForeground = false;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBarColor(this, R.color.main);


        // host 交易 我 3个选项卡
        tabHost = this.getTabHost();
        TabHost.TabSpec spec;
        Intent intent;


        if (MyApp.new_friend_count > 0) {
            tvUnReadMessage.setVisibility(View.INVISIBLE);
            tvUnReadMessage.setText(MyApp.new_friend_count+"");
        } else {
            tvUnReadMessage.setVisibility(View.GONE);
        }


        intent = new Intent().setClass(this, SayActivity.class);
        spec = tabHost.newTabSpec("聊天").setIndicator("聊天").setContent(intent);
        tabHost.addTab(spec);


        intent = new Intent().setClass(this, LiaisonManActivity.class);
        spec = tabHost.newTabSpec("联系人").setIndicator("联系人").setContent(intent);
        tabHost.addTab(spec);


        intent = new Intent().setClass(this, MyActivity.class);
        spec = tabHost.newTabSpec("我的").setIndicator("我的").setContent(intent);
        tabHost.addTab(spec);


        tabHost.setCurrentTab(0);

        RadioGroup radioGroup = (RadioGroup) this.findViewById(R.id.menu_rb_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    // TODO Auto-generated method stub

                    case R.id.rb1:
                        tabHost.setCurrentTabByTag("聊天");

                        break;

                    case R.id.rb2:

                        tabHost.setCurrentTabByTag("联系人");
                        MyApp.new_friend_count = 0;
                        break;


                    case R.id.rb3:
                        tabHost.setCurrentTabByTag("我的");

                        break;


                }
            }
        });

//接收广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.refreshFriend");
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);

    }

    // broadcast receiver   跟新界面
    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.refreshFriend")) {
                if (MyApp.new_friend_count > 0) {
                    tvUnReadMessage.setVisibility(View.INVISIBLE);
                    tvUnReadMessage.setText(MyApp.new_friend_count+"");
                } else {
                    tvUnReadMessage.setVisibility(View.GONE);
                }
            }
        }
    };

    //销毁注册广播
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mRefreshBroadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }


    @Override
    protected void onResume() {
        isForeground = true;
        if (0 != PushMessageReceiver.count) {
            //角标清空
            PushMessageReceiver.count = 0;
            BadgeUtils.setCount(PushMessageReceiver.count, MainActivity.this);
        }
        super.onResume();
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }



    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;


    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);


//        JPushInterface.init(getApplicationContext());
        String rid = JPushInterface.getRegistrationID(getApplicationContext());
        if (!rid.isEmpty()) {
            Toast.makeText(this, "RegId"+rid, Toast.LENGTH_SHORT).show();
            System.out.println("RegId="+rid);
        } else {
            Toast.makeText(this, "Get registration fail, JPush init failed!", Toast.LENGTH_SHORT).show();
        }
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
//                    setCostomMsg(showMsg.toString());
                }
            } catch (Exception e){
            }
        }
    }


    //后台运行而不退出程序



}
