package com.work.mtmessenger.ui;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.work.mtmessenger.MyApp;
import com.work.mtmessenger.R;
import com.work.mtmessenger.adapter.SortAdapter;
import com.work.mtmessenger.etil.Friend;
import com.work.mtmessenger.etil.Newclass;
import com.work.mtmessenger.etil.Nodata;
import com.work.mtmessenger.etil.SortModel;
import com.work.mtmessenger.ui.login.BaseActivity;
import com.work.mtmessenger.util.PinyinComparator;
import com.work.mtmessenger.util.PinyinUtils;
import com.work.mtmessenger.util.SideBar;
import com.work.mtmessenger.util.StatusBarUtil;
import com.work.mtmessenger.util.ToastUtil;
import com.work.mtmessenger.util.image.subString;
import com.zhangke.websocket.SimpleListener;
import com.zhangke.websocket.SocketListener;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.WebSocketManager;
import com.zhangke.websocket.response.ErrorResponse;

import org.afinal.simplecache.ACache;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class NewclassActivity extends BaseActivity implements SortAdapter.CallBack {
    @BindView(R.id.iv_go_back)
    ImageView ivGoBack;
    @BindView(R.id.tv_title_column)
    TextView tvTitleColumn;
    @BindView(R.id.et_context)
    EditText etContext;
    @BindView(R.id.tv_right_action)
    TextView tvRightAction;
    private Handler handler = new Handler();
    private Newclass newclass;
    private ACache acache;
    @BindView(R.id.total_size)
    TextView totalsize;
    private RecyclerView mRecyclerView;
    private SideBar sideBar;
    private SortAdapter adapter;
    LinearLayoutManager manager1;
    private List<SortModel> SourceDateList = new ArrayList<SortModel>();
    private int myposition = 0;
    private Friend friend;
    private int total_size = 0;//总条数
    private int current_index = 1;//当前页码
    private int target_id;
    private String title;
    private Nodata nodata;
    /**
     * 根据拼音来排列RecyclerView里面的数据类
     */
    private PinyinComparator pinyinComparator;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newclass);
        ButterKnife.bind(this);

        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBarColor(this, R.color.main);

        //通过此方法获取默认的 WebSocketManager 对象
        WebSocketManager manager = WebSocketHandler.getDefault();
        manager.addListener(socketListener);
        ivGoBack.setVisibility(View.VISIBLE);
        tvTitleColumn.setText("创建群聊");
        tvRightAction.setVisibility(View.VISIBLE);
        tvRightAction.setText("提交");


        pinyinComparator = new PinyinComparator();
        sideBar = (SideBar) findViewById(R.id.sideBar);


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        acache = ACache.get(this);//创建ACache组件
        String cacheData = acache.getAsString("friendList");//从缓存中取数据
        if (cacheData != null) {
            Gson gson = new Gson();
            friend = gson.fromJson(cacheData, Friend.class);
            if (MyApp.friend_count == friend.getData().getTotal_size()) {
                if (MyApp.friend_count > 0) {
                    totalsize.setText(friend.getData().getTotal_size() + "位联系人");
                    for (int i = 0; i < friend.getData().getArray().size(); i++) {
                        SourceDateList.add(friend.getData().getArray().get(i));
                    }
                    SourceDateList = filledData(SourceDateList);
//                    SourceDateList = filledData(Arrays.asList(getResources().getStringArray(R.array.date)));
                    // 根据a-z进行排序源数据
                    Collections.sort(SourceDateList, pinyinComparator);
                    //RecyclerView社置manager
                    manager1 = new LinearLayoutManager(NewclassActivity.this);
                    mRecyclerView.setLayoutManager(manager1);
                    SourceDateList = filledList(SourceDateList);
                    adapter = new SortAdapter(NewclassActivity.this, SourceDateList, NewclassActivity.this);
                    mRecyclerView.setAdapter(adapter);
                    adapter.setupRecyclerView(mRecyclerView);

                }


            }
        } else {
            getnewfriend();
        }


    }

    /**
     * 为RecyclerView填充数据
     *
     * @param date
     * @return
     */
    private List<SortModel> filledData(List<SortModel> date) {
        List<SortModel> mSortList = new ArrayList<>();
        for (int i = 0; i < date.size(); i++) {
            SortModel sortModel = new SortModel();
            sortModel.setHead_url(date.get(i).getHead_url());
            sortModel.setLetters(date.get(i).getLetters());
            sortModel.setNick_name(date.get(i).getNick_name());
            sortModel.setOnline(date.get(i).getOnline());
            sortModel.setUnread_count(date.get(i).getUnread_count());
            sortModel.setUser_id(date.get(i).getUser_id());
            //汉字转换成拼音
            String pinyin = PinyinUtils.getPingYin(date.get(i).getNick_name());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (!sortString.matches("[A-Z]")) {
//                sortModel.setLetters("常用共学圈");
            } else {
                sortModel.setLetters(sortString.toUpperCase());
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    private List<SortModel> filledList(List<SortModel> date) {
        List<SortModel> mSortList = new ArrayList<>();
        int z = 0;
        for (int i = 0; i < date.size(); i++) {
            SortModel sortModel = new SortModel();
            sortModel.setNick_name(date.get(i).getNick_name());
            sortModel.setUnread_count(date.get(i).getUnread_count());
            sortModel.setOnline(date.get(i).getOnline());
            sortModel.setLetters(date.get(i).getLetters());
            sortModel.setHead_url(date.get(i).getHead_url());
            sortModel.setUser_id(date.get(i).getUser_id());
            //汉字转换成拼音
            String pinyin = PinyinUtils.getPingYin(date.get(i).getNick_name());
            String sortString = pinyin.substring(0, 1).toUpperCase();
//             正则表达式，判断首字母是否是英文字母
//            if (Integer.parseInt(sortModel.getIs_stick()) == 1) {
//                sortModel.setLetters("常用共学圈");
//                mSortList.add(z, sortModel);
//                z++;
//            } else {
            sortModel.setLetters(sortString.toUpperCase());
            mSortList.add(sortModel);
//            }

        }
        return mSortList;

    }


    @OnClick({R.id.iv_go_back, R.id.tv_right_action})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_go_back:
                finish();
                break;
            case R.id.tv_right_action:
                String s = etContext.getText().toString().trim();
                if (TextUtils.isEmpty(s)) {
                    ToastUtil.show(NewclassActivity.this, "请输入群名称");
                    return;
                }
                List<String> users = new ArrayList<String>();
                if (adapter.getCbsResult().size() == 0) {
                    ToastUtil.show(NewclassActivity.this, "请添加群成员");
                } else {
                    for (int i = 0; i < adapter.getCbsResult().size(); i++) {
                        users.add(adapter.getCbsResult().get(i).getUser_id() + "");
                    }
                    String json1 = "{\"event_id\":" + 10013;
                    String json2 = ",\"name\":\"";
                    String json22 = "\",\"target_user\":";
                    String json3 = "}";
                    String json = json1 + json2 + s + json22 + users + json3;

                    //发送 String 数据
                    WebSocketHandler.getDefault().send(json);
                }

                break;
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
                }else if(msg.contains("onDisconnect")) {
                } else if (msg.contains("\"ret_code\":0")) {

                } else if (msg.contains("\"ret_code\":10000")) {//如果json包含 "code":"1"
                    if (msg.contains("\"event_id\":10013")) {
//                        String s = subString.subString(msg, "\"data\":", ",\"tips\"");
                        Gson gson = new Gson();
                        newclass = gson.fromJson(msg, Newclass.class);
                        ToastUtil.show(NewclassActivity.this, newclass.getTips());
                        finish();
                    }
                } else {
                    Gson gson = new Gson();
                    nodata = gson.fromJson(msg, Nodata.class);
                    ToastUtil.show(NewclassActivity.this, nodata.getTips());

                }
            }
        });
    }

    private void getnewfriend() {
        String json1 = "{\"event_id\":10012";
        String json2 = ",\"index\":" + current_index;
//        String json22 = "\",\"pass\":\"";
        String json3 = "}";
        String json = json1 + json2 + json3;
        //发送 String 数据
        WebSocketHandler.getDefault().send(json);
    }

    @Override
    public void deleteItem(int position) {

    }
}
