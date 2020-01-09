package com.work.mtmessenger.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;

import com.google.gson.Gson;
import com.work.mtmessenger.MyApp;
import com.work.mtmessenger.R;
import com.work.mtmessenger.adapter.GridAdapter;
import com.work.mtmessenger.etil.Nodata;
import com.work.mtmessenger.etil.Seelist;
import com.work.mtmessenger.etil.Seelistall;
import com.work.mtmessenger.ui.login.BaseActivity;
import com.work.mtmessenger.util.StatusBarUtil;
import com.work.mtmessenger.util.ToastUtil;
import com.work.mtmessenger.widgets.xlistview.MyGridView;
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


public class SeeListAllActivity extends BaseActivity {
    @BindView(R.id.iv_go_back)
    ImageView ivGoBack;
    @BindView(R.id.tv_title_column)
    TextView tvTitleColumn;
    @BindView(R.id.scroll_view_my_study3)
    NestedScrollView nestedScrollView;
    @BindView(R.id.gv_my_wallet_category)
    MyGridView gvMyWalletCategory;
    private Handler handler = new Handler();
    private int target_id;
    private int index = 1;
    private GridAdapter gridAdapter;
    private List<Seelistall.DataBean.ArrayBean> listBean = new ArrayList<Seelistall.DataBean.ArrayBean>();
    private Seelistall seelistall;
    private Nodata nodata;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seelistall);
        ButterKnife.bind(this);

        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBarColor(this, R.color.main);

        //通过此方法获取默认的 WebSocketManager 对象
        WebSocketManager manager = WebSocketHandler.getDefault();
        manager.addListener(socketListener);
        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        target_id = bundle.getInt("target_id");// 聊天人  编号/群编号

        ivGoBack.setVisibility(View.VISIBLE);
        tvTitleColumn.setText("查看群聊成员");
        sed();

        gridAdapter = new GridAdapter(SeeListAllActivity.this, R.layout.vw_list_item, listBean);
        gvMyWalletCategory.setAdapter(gridAdapter);


        //加载更多
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int
                    oldScrollX, int oldScrollY) {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (index < seelistall.getData().getTotal_page()) {
                                index = index + 1;
                                sed();
                            } else {
                                ToastUtil.show(SeeListAllActivity.this, "没有更多数据了");
                            }

                            gridAdapter.notifyDataSetChanged();
                        }
                    }, 1500);
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
                }else if(msg.contains("onDisconnect")) {
                }else if (msg.contains("\"ret_code\":0")) {

                } else if (msg.contains("\"ret_code\":10000")) {//如果json包含 "code":"1"
                    if (msg.contains("\"event_id\":10029")) {
                        Gson gson = new Gson();
                        seelistall = gson.fromJson(msg, Seelistall.class);
                        for (int i = 0; i < seelistall.getData().getArray().size(); i++) {
                            listBean.add(seelistall.getData().getArray().get(i));
                        }
                        gridAdapter.notifyDataSetChanged();
                    }
                } else {
                    Gson gson = new Gson();
                    nodata = gson.fromJson(msg, Nodata.class);
                    ToastUtil.show(SeeListAllActivity.this, nodata.getTips());

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


    @OnClick(R.id.iv_go_back)
    public void onViewClicked() {
        finish();
    }


}
