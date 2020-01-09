package com.work.mtmessenger.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.work.mtmessenger.MyApp;
import com.work.mtmessenger.R;
import com.work.mtmessenger.adapter.MoneyAdapter;
import com.work.mtmessenger.etil.Money;
import com.work.mtmessenger.etil.Nodata;
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


public class MymoneyActivity extends BaseActivity {


    @BindView(R.id.iv_go_back)
    ImageView ivGoBack;
    @BindView(R.id.tv_title_column)
    TextView tvTitleColumn;
    @BindView(R.id.go_showping)
    TextView go_showping;
    @BindView(R.id.list)
    XListView list;
    @BindView(R.id.tv_no_data)
    TextView tvNoData;
    @BindView(R.id.tvWalletBalance)
    TextView tvWalletBalance;

    private Handler handler = new Handler();
    private MoneyAdapter listViewAdapter3;
    private Money money;
    private List<Money.DataBean.ArrayBean> listBean = new ArrayList<Money.DataBean.ArrayBean>();
    private Nodata nodata;
    private int index = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymoney);
        ButterKnife.bind(this);
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBarColor(this, R.color.main);
        ivGoBack.setVisibility(View.VISIBLE);
        tvTitleColumn.setText("我的钱包");

        WebSocketManager manager = WebSocketHandler.getDefault();
        manager.addListener(socketListener);
        tvWalletBalance.setText(MyApp.balance + "");

        list.setPullRefreshEnable(false);
        list.setPullLoadEnable(false);
        list.setAutoLoadEnable(false);
        list.setRefreshTime(getTime());
        list.setVerticalScrollBarEnabled(false);


        listViewAdapter3 = new MoneyAdapter(MymoneyActivity.this, R.layout.vw_list_item, listBean);
        list.setAdapter(listViewAdapter3);


        //列表滑动监听
        list.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            // 下拉刷新
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        index = 1;
                        listBean.clear();
                        sed();
                        listViewAdapter3.notifyDataSetChanged();
                        onLoad();
                    }
                }, 2500);
            }

            @Override
            //上拉加载
            public void onLoadMore() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (index < money.getData().getTotal_page()) {
                            index = index + 1;
                            sed();
                            onLoad();
                        } else {
                            ToastUtil.show(MymoneyActivity.this, "没有更多数据了");
                        }
                    }
                }, 1500);
            }
        });
    }


    protected void onResume() {
        super.onResume();
        sed();
    }

    @OnClick(R.id.iv_go_back)
    public void onViewClicked() {
        finish();
    }


    private void onLoad() {
        list.stopRefresh();
        list.stopLoadMore();
        list.setRefreshTime(getTime());
    }

    private void sed() {
        String json1 = "{\"event_id\":10039";
        String json2 = ",\"index\":";
        String json3 = "}";
        String json = json1 + json2 + index + json3;
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

    //返回值监听
    private void appendMsgDisplay(String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (msg.contains("onConnected")) {
                } else if (msg.contains("onDisconnect")) {
                } else if (msg.contains("\"ret_code\":0")) {

                } else if (msg.contains("\"ret_code\":10000")) {
                    Gson gson = new Gson();
                    if (msg.contains("\"event_id\":10039")) {
                        money = gson.fromJson(msg, Money.class);
                        if (money.getData().getArray() != null) {
                            for (int i = 0; i < money.getData().getArray().size(); i++) {
                                listBean.add(money.getData().getArray().get(i));
                            }
                            if (money.getData().getTotal_page() > 0) {
                                list.setPullLoadEnable(true);
                                list.setAutoLoadEnable(true);
                                list.setRefreshTime(getTime());
                                list.setVerticalScrollBarEnabled(true);
                                list.setVisibility(View.VISIBLE);
                            } else {
                                list.setVisibility(View.GONE);
                                tvNoData.setVisibility(View.VISIBLE);
                            }
                            listViewAdapter3.notifyDataSetChanged();
                            tvWalletBalance.setText(listBean.get(0).getNew_money()+"");
                        }
                    }
                } else {
                    Gson gson = new Gson();
                    nodata = gson.fromJson(msg, Nodata.class);
                    ToastUtil.show(MymoneyActivity.this, nodata.getTips());
                }
            }
        });
    }
}
