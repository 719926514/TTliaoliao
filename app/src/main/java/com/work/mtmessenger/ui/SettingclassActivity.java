package com.work.mtmessenger.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.work.mtmessenger.MyApp;
import com.work.mtmessenger.R;
import com.work.mtmessenger.adapter.SettingClassAdapter;
import com.work.mtmessenger.etil.Nodata;
import com.work.mtmessenger.etil.Seelistall;
import com.work.mtmessenger.ui.login.BaseActivity;
import com.work.mtmessenger.util.StatusBarUtil;
import com.work.mtmessenger.util.ToastUtil;
import com.work.mtmessenger.widgets.xlistview.XListView;
import com.zhangke.websocket.SimpleListener;
import com.zhangke.websocket.SocketListener;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.WebSocketManager;
import com.zhangke.websocket.response.ErrorResponse;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.work.mtmessenger.util.TimeUtil.getTime;

public class SettingclassActivity extends BaseActivity {


    @BindView(R.id.iv_go_back)
    ImageView ivGoBack;
    @BindView(R.id.tv_title_column)
    TextView tvTitleColumn;
    @BindView(R.id.list)
    XListView list;
    private int target_id, index = 1, target_user;
    private int title;
    private Handler handler = new Handler();
    private SettingClassAdapter settingClassAdapter;
    private List<Seelistall.DataBean.ArrayBean> listBean = new ArrayList<Seelistall.DataBean.ArrayBean>();
    private Seelistall seelistall;
    private Nodata nodata;


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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingclass);
        ButterKnife.bind(this);

        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBarColor(this, R.color.main);


        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        title = bundle.getInt("title");
        target_id = bundle.getInt("target_id");
        ivGoBack.setVisibility(View.VISIBLE);
//        //通过此方法获取默认的 WebSocketManager 对象
        WebSocketManager manager = WebSocketHandler.getDefault();
        manager.addListener(socketListener);

        handler.post(new Runnable() {
            @Override
            public void run() {
                switch (title) {
                    case 3://踢出群成员
                        tvTitleColumn.setText("踢出群成员");
                        break;
                    case 4://禁言他人
                        tvTitleColumn.setText("禁言他人");
                        break;
                    case 5://添加管理员
                        tvTitleColumn.setText("添加管理员");
                        break;
                    case 6://移除管理员权限
                        tvTitleColumn.setText("移除管理员权限");
                        break;
                }
            }
        });
        list.setPullRefreshEnable(false);
        list.setPullLoadEnable(false);
        list.setAutoLoadEnable(true);
        list.setRefreshTime(getTime());

        settingClassAdapter = new SettingClassAdapter(SettingclassActivity.this, R.layout.vw_list_item, listBean, title);
        list.setAdapter(settingClassAdapter);

        sed();


        //列表滑动监听
        list.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            // 下拉刷新
            public void onRefresh() {
            }

            @Override
            //上拉加载
            public void onLoadMore() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (index < seelistall.getData().getTotal_page()) {
                            index = index + 1;
                            sed();
                            onLoad();
                        } else {
                            ToastUtil.show(SettingclassActivity.this, "没有更多数据了");
                        }
                    }
                }, 2500);
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                target_user = listBean.get(i - 1).getUser_id();
                String tepy = listBean.get(i - 1).getRole();
                switch (title) {
                    case 3://踢出群成员
                        String json1 = "{\"event_id\":10026";
                        String json2 = ",\"target_id\":" + target_user;
                        String json3 = ",\"group_id\":" + target_id;
                        String json4 = "}";
                        String json = json1 + json2 + json3 + json4;
                        WebSocketHandler.getDefault().send(json);
                        break;
                    case 4://禁言他人
                        int speak = 0;
                        if (tepy.contains("1")) {
                            speak = 0;
                        } else {
                            speak = 1;
                        }
                        String json11 = "{\"event_id\":10025";
                        String json22 = ",\"target_id\":" + target_user;
                        String json33 = ",\"group_id\":" + target_id;
                        String json55 = ",\"speak\":" + speak;
                        String json44 = "}";
                        String json0 = json11 + json22 + json33 + json55 + json44;
                        WebSocketHandler.getDefault().send(json0);
                        break;
                    case 5://添加管理员
                        String json111 = "{\"event_id\":10027";
                        String json222 = ",\"target_id\":" + target_user;
                        String json333 = ",\"group_id\":" + target_id;
                        String json444 = "}";
                        String json00 = json111 + json222 + json333 + json444;
                        WebSocketHandler.getDefault().send(json00);
                        break;
                    case 6://移除管理员权限
                        String json1111 = "{\"event_id\":10028";
                        String json2222 = ",\"target_id\":" + target_user;
                        String json3333 = ",\"group_id\":" + target_id;
                        String json4444 = "}";
                        String json000 = json1111 + json2222 + json3333 + json4444;
                        WebSocketHandler.getDefault().send(json000);
                        break;
                }

            }
        });


    }

    @OnClick(R.id.iv_go_back)
    public void onViewClicked() {
        finish();
    }


    public void onResume() {
        super.onResume();

    }


    private void appendMsgDisplay(String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (msg.contains("onConnected")) {
                }else if(msg.contains("onDisconnect")) {
                }else if (msg.contains("\"ret_code\":0")) {

                } else if (msg.contains("\"ret_code\":10000")) {
                    Gson gson = new Gson();
                    if (msg.contains("\"event_id\":10029")) {
                        seelistall = gson.fromJson(msg, Seelistall.class);
                        for (int i = 0; i < seelistall.getData().getArray().size(); i++) {
                            listBean.add(seelistall.getData().getArray().get(i));
                        }
                        settingClassAdapter.notifyDataSetChanged();
                    } else if (msg.contains("\"event_id\":10026")) {
                        nodata = gson.fromJson(msg, Nodata.class);
                        ToastUtil.show(SettingclassActivity.this, nodata.getTips());
                        finish();
                    } else if (msg.contains("\"event_id\":10025")) {
                        nodata = gson.fromJson(msg, Nodata.class);
                        ToastUtil.show(SettingclassActivity.this, nodata.getTips());
                        finish();
                    } else if (msg.contains("\"event_id\":10027")) {
                        nodata = gson.fromJson(msg, Nodata.class);
                        ToastUtil.show(SettingclassActivity.this, nodata.getTips());
                        finish();
                    } else if (msg.contains("\"event_id\":10028")) {
                        nodata = gson.fromJson(msg, Nodata.class);
                        ToastUtil.show(SettingclassActivity.this, nodata.getTips());
                        finish();
                    }
                } else {
                    Gson gson = new Gson();
                    nodata = gson.fromJson(msg, Nodata.class);
                    ToastUtil.show(SettingclassActivity.this, nodata.getTips());

                }
            }

        });
    }


    private void sed() {
        String json1 = "{\"event_id\":" + 10029;
        String json11 = ",\"index\":" + index;
        String json2 = ",\"group_id\":" + target_id;
        String json3 = "}";
        String json = json1 + json11 + json2 + json3;

        //发送 String 数据
        WebSocketHandler.getDefault().send(json);

    }

    private void onLoad() {
        list.stopLoadMore();

    }
}
