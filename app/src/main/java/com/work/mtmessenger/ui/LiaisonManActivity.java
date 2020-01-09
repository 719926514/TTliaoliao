package com.work.mtmessenger.ui;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.work.mtmessenger.MyApp;
import com.work.mtmessenger.R;
import com.work.mtmessenger.adapter.NewLiaisonAdapter;
import com.work.mtmessenger.adapter.Sort0Adapter;
import com.work.mtmessenger.etil.Friend;
import com.work.mtmessenger.etil.Lgin;
import com.work.mtmessenger.etil.NewAddFriend;
import com.work.mtmessenger.etil.Nodata;
import com.work.mtmessenger.etil.SortModel;
import com.work.mtmessenger.ui.login.BaseActivity;
import com.work.mtmessenger.util.PinyinComparator;
import com.work.mtmessenger.util.PinyinUtils;
import com.work.mtmessenger.util.SideBar;
import com.work.mtmessenger.util.StatusBarUtil;
import com.work.mtmessenger.util.ToastUtil;
import com.zhangke.websocket.SimpleListener;
import com.zhangke.websocket.SocketListener;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.WebSocketManager;
import com.zhangke.websocket.response.ErrorResponse;

import org.afinal.simplecache.ACache;
import org.json.JSONArray;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.work.mtmessenger.ui.login.LoginActivity.st;
import static com.work.mtmessenger.util.TimeUtil.getTime;

public class LiaisonManActivity extends BaseActivity implements Sort0Adapter.CallBack {


    @BindView(R.id.tv_title_column)
    TextView tvTitleColumn;
    @BindView(R.id.iv_right_action)
    ImageView ivRightAction;
    @BindView(R.id.total_size)
    TextView totalsize;
    @BindView(R.id.qunliao)
    TextView qunliao;
    private Nodata nodata;
    private RecyclerView mRecyclerView;
    private SideBar sideBar;
    private Sort0Adapter adapter;
    LinearLayoutManager manager1;
    private Handler handler = new Handler();
    private List<SortModel> SourceDateList = new ArrayList<SortModel>();
    private RelativeLayout rl_ic;
    private RelativeLayout mrlay;
    private ListView listview;
    private NewLiaisonAdapter newLiaisonAdapter;
    private NewAddFriend newAddFriend;
    private List<Lgin.DataBean.FriendRequestArrayBean> Newliaisonlist = new ArrayList<Lgin.DataBean.FriendRequestArrayBean>();
    private int myposition = 0;
    private ACache acache;//缓存框架
    private Friend friend;
    private int total_size = 0;//总条数
    private int current_index = 1;//当前页码

    /**
     * 根据拼音来排列RecyclerView里面的数据类
     */
    private PinyinComparator pinyinComparator;
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
        setContentView(R.layout.activity_liausonman);
        ButterKnife.bind(this);

        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBarColor(this, R.color.main);

        tvTitleColumn.setText("联系人");
        ivRightAction.setVisibility(View.VISIBLE);


//        //通过此方法获取默认的 WebSocketManager 对象
        WebSocketManager manager = WebSocketHandler.getDefault();
        manager.addListener(socketListener);


        listview = (ListView) findViewById(R.id.listview);
        mrlay = (RelativeLayout) findViewById(R.id.rlay_1);
        if (st.getData().getFriend_request_array() != null) {
            for (int i = 0; i < st.getData().getFriend_request_array().size(); i++) {
                Newliaisonlist.add(st.getData().getFriend_request_array().get(i));
            }
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (Newliaisonlist.size() == 0) {
                    android.view.ViewGroup.LayoutParams pp = mrlay.getLayoutParams();
                    pp.height = 0;
                    mrlay.setLayoutParams(pp);
                } else if (Newliaisonlist.size() == 1) {
                    android.view.ViewGroup.LayoutParams pp = mrlay.getLayoutParams();
                    pp.height = 200;
                    mrlay.setLayoutParams(pp);
                } else if (Newliaisonlist.size() == 2) {
                    android.view.ViewGroup.LayoutParams pp = mrlay.getLayoutParams();
                    pp.height = 400;
                    mrlay.setLayoutParams(pp);
                } else if (Newliaisonlist.size() > 2) {
                    android.view.ViewGroup.LayoutParams pp = mrlay.getLayoutParams();
                    pp.height = 600;
                    mrlay.setLayoutParams(pp);
                }
            }
        });


        newLiaisonAdapter = new NewLiaisonAdapter(LiaisonManActivity.this, R.layout.vw_list_item, Newliaisonlist);
        listview.setAdapter(newLiaisonAdapter);


        pinyinComparator = new PinyinComparator();
        sideBar = (SideBar) findViewById(R.id.sideBar);


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        newLiaisonAdapter.setOnAddListener(new NewLiaisonAdapter.onAddListener() {
            @Override
            public void onAddListener(int applicant_id, int position) {
                String json1 = "{\"event_id\":10006";
                String json2 = ",\"applicant_id\":" + applicant_id;
                String json22 = ",\"operating\":1";
                String json3 = "}";
                String json = json1 + json2 + json22 + json3;
                //发送 String 数据
                myposition = position;
                WebSocketHandler.getDefault().send(json);
            }
        });

        newLiaisonAdapter.setOnnotListener(new NewLiaisonAdapter.onnotListener() {
            @Override
            public void onnotListener(int applicant_id, int position) {
                String json1 = "{\"event_id\":10006";
                String json2 = ",\"applicant_id\":" + applicant_id;
                String json22 = ",\"operating\":2";
                String json3 = "}";
                String json = json1 + json2 + json22 + json3;
                //发送 String 数据
                myposition = position;
                WebSocketHandler.getDefault().send(json);
            }
        });

        acache = ACache.get(this);//创建ACache组件
        String cacheData = acache.getAsString("friendList");//从缓存中取数据
        if (cacheData != null) {
            Gson gson = new Gson();
            friend = gson.fromJson(cacheData, Friend.class);
            try {
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
                        manager1 = new LinearLayoutManager(LiaisonManActivity.this);
                        mRecyclerView.setLayoutManager(manager1);
                        SourceDateList = filledList(SourceDateList);
                        adapter = new Sort0Adapter(LiaisonManActivity.this, SourceDateList, LiaisonManActivity.this);
                        mRecyclerView.setAdapter(adapter);
                        adapter.setupRecyclerView(mRecyclerView);
                        adapter.setOnItemClickListener(new Sort0Adapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                //带参数跳转
                                String name = ((SortModel) adapter.getItem(position)).getNick_name();
                                int target_id = ((SortModel) adapter.getItem(position)).getUser_id();
                                int target_type = 1;
                                Intent i1 = new Intent(LiaisonManActivity.this, SayListActivity.class);
                                Bundle bundle1 = new Bundle();
                                bundle1.putString("name", name);
                                bundle1.putInt("target_id", target_id);
                                bundle1.putInt("target_type", target_type);
                                i1.putExtras(bundle1);
                                startActivity(i1);
                            }
                        });
                    }
                } else {
                    getnewfriend();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            getnewfriend();
        }
        qunliao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LiaisonManActivity.this, QunliaolistActivity.class));
            }
        });
    }

    @OnClick(R.id.iv_right_action)
    public void onViewClicked() {
        startActivity(new Intent(LiaisonManActivity.this, SearchActivity.class));
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

    @Override
    public void deleteItem(int position) {

    }

    public void onResume() {
        super.onResume();

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


    private void appendMsgDisplay(String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (msg.contains("onConnected")) {
                } else if (msg.contains("onDisconnect")) {
                } else if (msg.contains("\"ret_code\":0")) {

                } else if (msg.contains("\"ret_code\":10000")) {
                    Gson gson = new Gson();
                    if (msg.contains("\"event_id\":10005")) {
                        newAddFriend = gson.fromJson(msg, NewAddFriend.class);

                        st.getData().getFriend_request_array().get(0).setApplicant_id(newAddFriend.getData().getApplicant_id());
                        st.getData().getFriend_request_array().get(0).setHead_url(newAddFriend.getData().getHead_url());
                        st.getData().getFriend_request_array().get(0).setNick_name(newAddFriend.getData().getNick_name());
                        st.getData().getFriend_request_array().get(0).setUser_name(newAddFriend.getData().getUser_name());
                        Newliaisonlist.add(st.getData().getFriend_request_array().get(0));
                        if (Newliaisonlist.size() == 0) {
                            android.view.ViewGroup.LayoutParams pp = mrlay.getLayoutParams();
                            pp.height = 0;
                            mrlay.setLayoutParams(pp);
                        } else if (Newliaisonlist.size() == 1) {
                            android.view.ViewGroup.LayoutParams pp = mrlay.getLayoutParams();
                            pp.height = 200;
                            mrlay.setLayoutParams(pp);
                        } else if (Newliaisonlist.size() == 2) {
                            android.view.ViewGroup.LayoutParams pp = mrlay.getLayoutParams();
                            pp.height = 400;
                            mrlay.setLayoutParams(pp);
                        } else if (Newliaisonlist.size() > 2) {
                            android.view.ViewGroup.LayoutParams pp = mrlay.getLayoutParams();
                            pp.height = 600;
                            mrlay.setLayoutParams(pp);
                        }
                        newLiaisonAdapter.notifyDataSetChanged();
                    } else if (msg.contains("\"event_id\":10006")) {
                        Newliaisonlist.remove(myposition + 1);
                        if (Newliaisonlist.size() == 0) {
                            android.view.ViewGroup.LayoutParams pp = mrlay.getLayoutParams();
                            pp.height = 0;
                            mrlay.setLayoutParams(pp);
                        } else if (Newliaisonlist.size() == 1) {
                            android.view.ViewGroup.LayoutParams pp = mrlay.getLayoutParams();
                            pp.height = 200;
                            mrlay.setLayoutParams(pp);
                        } else if (Newliaisonlist.size() == 2) {
                            android.view.ViewGroup.LayoutParams pp = mrlay.getLayoutParams();
                            pp.height = 400;
                            mrlay.setLayoutParams(pp);
                        } else if (Newliaisonlist.size() > 2) {
                            android.view.ViewGroup.LayoutParams pp = mrlay.getLayoutParams();
                            pp.height = 600;
                            mrlay.setLayoutParams(pp);
                        }
                        newLiaisonAdapter.notifyDataSetChanged();
                        getnewfriend();
                    } else if (msg.contains("\"event_id\":10012")) {
                        //取出缓存获取总数    解析数据获取总数   判断总数
                        //总数不一样 清空数据 重新请求
                        //判断是否有下一页 请求所有数据
                        //排序数据
                        friend = gson.fromJson(msg, Friend.class);
                        if (friend.getData().getArray() != null) {
                            SourceDateList.clear();
                            acache.remove("friendList");
                            if (current_index < friend.getData().getTotal_index()) {
                                for (int i = 1; i < friend.getData().getTotal_index() + 1; i++) {
                                    current_index = i;
                                    getnewfriend();
                                }
                            }
                            for (int i = 0; i < friend.getData().getArray().size(); i++) {
                                SourceDateList.add(friend.getData().getArray().get(i));
                            }
                            totalsize.setText(friend.getData().getTotal_size() + "位联系人");
                            acache.put("friendList", msg);//将数据存入缓存中
                            SourceDateList = filledData(SourceDateList);
//                    SourceDateList = filledData(Arrays.asList(getResources().getStringArray(R.array.date)));
                            // 根据a-z进行排序源数据
                            Collections.sort(SourceDateList, pinyinComparator);
                            //RecyclerView社置manager
                            manager1 = new LinearLayoutManager(LiaisonManActivity.this);
                            mRecyclerView.setLayoutManager(manager1);
                            SourceDateList = filledList(SourceDateList);
                            adapter = new Sort0Adapter(LiaisonManActivity.this, SourceDateList, LiaisonManActivity.this);
                            mRecyclerView.setAdapter(adapter);
                            adapter.setupRecyclerView(mRecyclerView);
                        }
                    }


                } else {
                    Gson gson = new Gson();
                    nodata = gson.fromJson(msg, Nodata.class);
                    ToastUtil.show(LiaisonManActivity.this, nodata.getTips());

                }
            }
        });
    }


    @Override
    public void onBackPressed() {//重写的Activity返回

        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        startActivity(intent);

    }
}
