package com.work.mtmessenger.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.work.mtmessenger.MyApp;
import com.work.mtmessenger.R;
import com.work.mtmessenger.adapter.QunliaoAdapter;
import com.work.mtmessenger.adapter.SayListAdapter;
import com.work.mtmessenger.etil.Newclass;
import com.work.mtmessenger.etil.Nodata;
import com.work.mtmessenger.etil.Qunliao;
import com.work.mtmessenger.ui.login.BaseActivity;
import com.work.mtmessenger.util.StatusBarUtil;
import com.work.mtmessenger.util.ToastUtil;
import com.work.mtmessenger.widgets.xlistview.XListView;
import com.zhangke.websocket.SimpleListener;
import com.zhangke.websocket.SocketListener;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.WebSocketManager;
import com.zhangke.websocket.response.ErrorResponse;

import org.afinal.simplecache.ACache;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.work.mtmessenger.util.TimeUtil.getTime;


public class QunliaolistActivity extends BaseActivity {

    @BindView(R.id.iv_go_back)
    ImageView ivGoBack;
    @BindView(R.id.tv_title_column)
    TextView tvTitleColumn;
    @BindView(R.id.total_size)
    TextView total_size;
    @BindView(R.id.recyclerView)
    XListView list;
    private ACache acache;
    private Handler handler = new Handler();
    private int index = 1;
    private QunliaoAdapter listViewAdapter3;
    private Qunliao qunliao;
    private List<Qunliao.DataBean.ListBean> listBean = new ArrayList<Qunliao.DataBean.ListBean>();
    private Nodata nodata;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qunliao);
        ButterKnife.bind(this);

        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBarColor(this, R.color.main);

        //通过此方法获取默认的 WebSocketManager 对象
        WebSocketManager manager = WebSocketHandler.getDefault();
        manager.addListener(socketListener);

        ivGoBack.setVisibility(View.VISIBLE);
        tvTitleColumn.setText("群聊");


        list.setPullRefreshEnable(true);
        list.setPullLoadEnable(false);
        list.setAutoLoadEnable(false);
        list.setRefreshTime(getTime());
        list.setVerticalScrollBarEnabled(false);

        listViewAdapter3 = new QunliaoAdapter(QunliaolistActivity.this, R.layout.vw_list_item, listBean);
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
                        if (index < qunliao.getData().getTotal_index()) {
                            index = index + 1;
                            sed();
                            onLoad();
                        } else {
                            ToastUtil.show(QunliaolistActivity.this, "没有更多数据了");
                        }
                    }
                }, 1500);
            }
        });
        try {
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //带参数跳转

                    Intent i1 = new Intent(QunliaolistActivity.this, SayListActivity.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("name", listBean.get(i - 1).getNick_name());
                    bundle1.putInt("target_id", listBean.get(i - 1).getGroup_id());
                    bundle1.putInt("target_type", 2);
                    i1.putExtras(bundle1);
                    startActivity(i1);


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    if (msg.contains("\"event_id\":10030")) {
                        Gson gson = new Gson();
                        qunliao = gson.fromJson(msg, Qunliao.class);
                        if (msg.contains("array")) {
                            listBean.clear();
                            if (qunliao.getData().getList() != null) {
                                for (int i = 0; i < qunliao.getData().getList().size(); i++) {
                                    listBean.add(qunliao.getData().getList().get(i));
                                }

                                if (qunliao.getData().getTotal_index() > 1) {
                                    list.setPullLoadEnable(true);
                                    list.setAutoLoadEnable(true);
                                    list.setRefreshTime(getTime());
                                    list.setVerticalScrollBarEnabled(true);
                                }
                                total_size.setText(qunliao.getData().getTotal_size() + "个群组");
                                listViewAdapter3.notifyDataSetChanged();
                            }
                        }
                    }
                } else {
                    Gson gson = new Gson();
                    nodata = gson.fromJson(msg, Nodata.class);
                    ToastUtil.show(QunliaolistActivity.this, nodata.getTips());

                }
            }

        });
    }

    @OnClick(R.id.iv_go_back)
    public void onViewClicked() {
        finish();
    }


    private void sed() {
        String json1 = "{\"event_id\":" + 10030;
        String json2 = ",\"index\":" + index;
        String json3 = "}";
        String json = json1 + json2 + json3;

        //发送 String 数据
        WebSocketHandler.getDefault().send(json);

    }


    private void onLoad() {
        list.stopRefresh();
        list.stopLoadMore();
        list.setRefreshTime(getTime());
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        sed();
    }

}
