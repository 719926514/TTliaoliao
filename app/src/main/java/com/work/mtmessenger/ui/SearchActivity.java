package com.work.mtmessenger.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.work.mtmessenger.MyApp;
import com.work.mtmessenger.R;
import com.work.mtmessenger.adapter.SearchListViewAdapter;
import com.work.mtmessenger.etil.NewName;
import com.work.mtmessenger.etil.Newfrend10004;
import com.work.mtmessenger.etil.Nodata;
import com.work.mtmessenger.etil.asd;
import com.work.mtmessenger.ui.login.BaseActivity;
import com.work.mtmessenger.ui.myactivity.SetingnameActivity;
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

public class SearchActivity extends BaseActivity {


    @BindView(R.id.iv_go_back)
    ImageView ivGoBack;
    @BindView(R.id.ed_title_column)
    EditText edTitleColumn;
    @BindView(R.id.tv_title_column)
    TextView tvTitleColumn;
    @BindView(R.id.tv_right_action)
    TextView tvRightAction;
    @BindView(R.id.tv_no_data)
    TextView tvNoData;
    @BindView(R.id.list)
    ListView list;
    private Handler handler = new Handler();
    private asd st;
    private SearchListViewAdapter listViewAdapter3;
    private List<asd> listBean = new ArrayList<asd>();
    private Newfrend10004 newfrend10004;
    private Nodata nodata;

    @Override  //搜索
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBarColor(this, R.color.main);


        //        //通过此方法获取默认的 WebSocketManager 对象
        WebSocketManager manager = WebSocketHandler.getDefault();
        manager.addListener(socketListener);

        listViewAdapter3 = new SearchListViewAdapter(SearchActivity.this, R.layout.vw_list_item, listBean);
        list.setAdapter(listViewAdapter3);

        listViewAdapter3.setOnAddListener(new SearchListViewAdapter.onAddListener() {
            @Override
            public void onAddListener(String CommentId, String user_name) {
                //添加好友
                addfriend(user_name);
            }
        });


        edTitleColumn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(edTitleColumn.getText().toString())) {
                            tvTitleColumn.setVisibility(View.GONE);
                        } else {
                            tvTitleColumn.setVisibility(View.VISIBLE);
                        }
                    }
                });


            }
        });


    }


    @OnClick({R.id.iv_go_back, R.id.tv_right_action})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_go_back:
                finish();
                break;

            case R.id.tv_right_action:
                //搜索按钮
                getnewfriend();
                break;
        }
    }


    private void addfriend(String user_name) {
        String json1 = "{\"event_id\":10004";
//        String json2 = ",\"index\":" + current_index;
        String json22 = ",\"user_name\":\"";
        String json3 = "\"}";
        String json = json1 + json22 + user_name + json3;
        //发送 String 数据
        WebSocketHandler.getDefault().send(json);
    }


    private void getnewfriend() {
        handler.post(new Runnable() {
            @Override
            public void run() {

                String username = edTitleColumn.getText().toString().trim();
                if (!TextUtils.isEmpty(username)) {
                    String json1 = "{\"event_id\":10003";
                    String json22 = ",\"user_name\":\"";
                    String json3 = "\"}";
                    String json = json1 + json22 + username + json3;
                    //发送 String 数据
                    WebSocketHandler.getDefault().send(json);
                } else {
                    ToastUtil.show(SearchActivity.this, "请输入用户名");

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

    private void appendMsgDisplay(String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (msg.contains("onConnected")) {
                }else if(msg.contains("onDisconnect")) {
                }else if (msg.contains("\"ret_code\":0")) {

                } else if (msg.contains("\"ret_code\":10000")) {
                    Gson gson = new Gson();
                    if (msg.contains("\"event_id\":10003")) {
                        st = gson.fromJson(msg, asd.class);
                        listBean.clear();
                        listBean.add(st);
                        if (st.getData() != null) {
                            tvNoData.setVisibility(View.GONE);
                            list.setVisibility(View.VISIBLE);
                            listViewAdapter3.notifyDataSetChanged();
                        } else {
                            list.setVisibility(View.GONE);
                            tvNoData.setVisibility(View.VISIBLE);
                            tvNoData.setText(st.getTips());
                        }

                    } else if (msg.contains("\"event_id\":10004")) {
                        newfrend10004 = gson.fromJson(msg, Newfrend10004.class);
                        ToastUtil.show(SearchActivity.this, newfrend10004.getTips());

                    }


                } else {
                    Gson gson = new Gson();
                    nodata = gson.fromJson(msg, Nodata.class);
                    ToastUtil.show(SearchActivity.this, nodata.getTips());
                }
            }
        });
    }
}




