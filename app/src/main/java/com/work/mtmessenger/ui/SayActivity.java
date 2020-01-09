package com.work.mtmessenger.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;

import com.google.gson.Gson;
import com.work.mtmessenger.MyApp;
import com.work.mtmessenger.R;
import com.work.mtmessenger.adapter.MessgListViewAdapter;
import com.work.mtmessenger.adapter.MessgListViewAdapter2;
import com.work.mtmessenger.etil.GroupUnreadArray;
import com.work.mtmessenger.etil.Lgin;
import com.work.mtmessenger.etil.Newclass;
import com.work.mtmessenger.etil.Nodata;
import com.work.mtmessenger.etil.Trycod;
import com.work.mtmessenger.etil.Tuisong;
import com.work.mtmessenger.ui.login.BaseActivity;
import com.work.mtmessenger.ui.myactivity.SetActivity;
import com.work.mtmessenger.util.StatusBarUtil;
import com.work.mtmessenger.util.ToastUtil;
import com.work.mtmessenger.widgets.xlistview.ListViewForScrollView;
import com.zhangke.websocket.SimpleListener;
import com.zhangke.websocket.SocketListener;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.WebSocketManager;
import com.zhangke.websocket.response.ErrorResponse;

import org.afinal.simplecache.ACache;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.work.mtmessenger.util.TimeUtil.getTime;


public class SayActivity extends BaseActivity {
    private static final String TAG = "新消息提示";
    @BindView(R.id.tv_title_column)
    TextView tvTitleColumn;
    @BindView(R.id.d123)
    TextView d123;
    @BindView(R.id.tv_jiaqun)
    TextView tv_jiaqun;
    @BindView(R.id.tv_jiaren)
    TextView tv_jiaren;
    @BindView(R.id.ll_123)
    LinearLayout ll_123;
    @BindView(R.id.rl_12)
    RelativeLayout rl_12;
    @BindView(R.id.ll_12)
    LinearLayout ll_12;
    @BindView(R.id.iv_right_action)
    ImageView ivRightAction;
    @BindView(R.id.list)
    ListViewForScrollView list;
    @BindView(R.id.list2)
    ListViewForScrollView list2;
    @BindView(R.id.tv_no_data)
    TextView tvNoData;
    @BindView(R.id.iv_go_back)
    ImageView ivGoBack;
    private MessgListViewAdapter listViewAdapter3;
    private List<Lgin.DataBean.FriendUnreadArrayBean> listBean = new ArrayList<Lgin.DataBean.FriendUnreadArrayBean>();
    private Handler handler = new Handler();
    private ACache acache;
    private Newclass newclass;
    private Lgin st;
    private GroupUnreadArray groupUnreadArray;
    private MessgListViewAdapter2 listViewAdapter4;
    private List<Lgin.DataBean.GroupUnreadArray> listBean2 = new ArrayList<Lgin.DataBean.GroupUnreadArray>();
    private Tuisong tuisong;
    private MediaPlayer mediaPlayer = new MediaPlayer();//这个我定义了一个成员函数
    private boolean shouldPlayBeep;
    private String isshenyin = "";
    private String iszhendong = "";
    private Nodata nodata;
    private Trycod trycod;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_say);
        ButterKnife.bind(this);

        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBarColor(this, R.color.main);

        //通过此方法获取默认的 WebSocketManager 对象
        WebSocketManager manager = WebSocketHandler.getDefault();
        manager.addListener(socketListener);

        tvTitleColumn.setText("消息");
        ivRightAction.setVisibility(View.VISIBLE);


//铃声提醒
        SayActivity.this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        AudioManager audioService = (AudioManager) SayActivity.this.getSystemService(Context.AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            shouldPlayBeep = false;
        }
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer player) {
                player.seekTo(0);
            }
        });
        AssetFileDescriptor file = SayActivity.this.getResources().openRawResourceFd(R.raw.sy1);
        try {
            mediaPlayer.setDataSource(file.getFileDescriptor(),
                    file.getStartOffset(), file.getLength());
            file.close();
            mediaPlayer.setVolume(0.5f, 0.5f);
            mediaPlayer.prepare();
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            mediaPlayer = null;
        }


        acache = ACache.get(this);//创建ACache组件
        isshenyin = acache.getAsString("shenyin");//从缓存中取数据
        if (isshenyin == null) {
            isshenyin = "1";
        }
        iszhendong = acache.getAsString("zhendong");//从缓存中取数据
        if (iszhendong == null) {
            iszhendong = "1";
        }
        Gson gson = new Gson();

        listViewAdapter3 = new MessgListViewAdapter(SayActivity.this, R.layout.vw_list_item, listBean);
        list.setAdapter(listViewAdapter3);
        setListViewHeightBasedOnChildren(list);
        listViewAdapter4 = new MessgListViewAdapter2(SayActivity.this, R.layout.vw_list_item, listBean2);
        list2.setAdapter(listViewAdapter4);
        setListViewHeightBasedOnChildren(list2);

        String cacheData = acache.getAsString("newsaylist");//从缓存中取数据
        if (cacheData != null) {
            st = gson.fromJson(cacheData, Lgin.class);
            if (st.getData().getFriend_unread_array().size() > 0) {
                for (int i = 0; i < st.getData().getFriend_unread_array().size(); i++) {
                    listBean.add(st.getData().getFriend_unread_array().get(i));
                }

                listViewAdapter3.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(list);
            }

            if (st.getData().getGroup_unread_array().size() > 0) {
                for (int i = 0; i < st.getData().getGroup_unread_array().size(); i++) {
                    listBean2.add(st.getData().getGroup_unread_array().get(i));
                }
                listViewAdapter4.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(list2);
            }
        }


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = listBean.get(position).getSend_nick_name();
                int target_id = listBean.get(position).getSend_user_id();
                int target_type = 1;
                Intent i1 = new Intent(SayActivity.this, SayListActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("name", name);
                bundle1.putInt("target_id", target_id);
                bundle1.putInt("target_type", target_type);
                i1.putExtras(bundle1);
                startActivity(i1);
                listBean.get(position).setUnread_array(0);
                //消息数量
                deletemg(target_id, target_type);

                listViewAdapter3.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(list);
            }
        });

        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = listBean2.get(position).getSend_nick_name();
                int target_id = listBean2.get(position).getSend_user_id();
                int target_type = 2;
                Intent i1 = new Intent(SayActivity.this, SayListActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("name", name);
                bundle1.putInt("target_id", target_id);
                bundle1.putInt("target_type", target_type);
                i1.putExtras(bundle1);
                startActivity(i1);
                //消息数量
                deletemg(target_id, target_type);
                listBean2.get(position).setUnread_array(0);
                listViewAdapter4.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(list2);
            }
        });

    }


    public void onResume() {
        super.onResume();


    }


    private void appendMsgDisplay(String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                if (msg.contains("onConnected")) {
                } else if (msg.contains("onDisconnect")) {
                } else if (msg.contains("\"ret_code\":0")) {

                } else if (msg.contains("\"ret_code\":10000")) {//如果json包含 "code":"1"
                    if (msg.contains("\"event_id\":10013")) {
                        groupUnreadArray = gson.fromJson(msg, GroupUnreadArray.class);
//                        acache.put("newgrouplist", msg);//将数据存入缓存中
                        Lgin.DataBean.GroupUnreadArray groupUnreadArray1 = new Lgin.DataBean.GroupUnreadArray();
                        groupUnreadArray1.setSend_user_id(groupUnreadArray.getData().getGroup_id());
                        groupUnreadArray1.setSend_nick_name(groupUnreadArray.getData().getGroup_name());
                        groupUnreadArray1.setSend_head_url(groupUnreadArray.getData().getGroup_head_url());
                        listBean2.add(groupUnreadArray1);
                        listViewAdapter4.notifyDataSetChanged();
                        setListViewHeightBasedOnChildren(list2);
                    } else if (msg.contains("\"event_id\":10007")) {
                        tuisong = gson.fromJson(msg, Tuisong.class);
                        if (tuisong.getData().getTarget_type() == 1) {
                            //判断是否是 新窗口
                            boolean bool = true;
                            for (int b = 0; b < listBean.size(); b++) {
                                if (listBean.get(b).getChat_windows_id() == tuisong.getData().getChat_windows_id()) {
                                    listBean.get(b).setUnread_array(listBean.get(b).getUnread_array() + 1);
                                    listBean.get(b).setNews_create_time(tuisong.getData().getCreate_time());
                                    if (tuisong.getData().getValue() != null) {
                                        listBean.get(b).setNews_messages(tuisong.getData().getValue());
                                    }
                                    bool = false;
                                    break;
                                }
                            }
                            if (bool) {
                                Lgin.DataBean.FriendUnreadArrayBean friendUnreadArrayBean = new Lgin.DataBean.FriendUnreadArrayBean();
                                friendUnreadArrayBean.setChat_windows_id(tuisong.getData().getChat_windows_id());
                                friendUnreadArrayBean.setSend_nick_name(tuisong.getData().getSend_user_name());
                                friendUnreadArrayBean.setUnread_array(1);
                                friendUnreadArrayBean.setSend_head_url(tuisong.getData().getSend_head_url());
                                friendUnreadArrayBean.setSend_user_id(tuisong.getData().getSend_user_id());
                                friendUnreadArrayBean.setNews_create_time(tuisong.getData().getCreate_time());
                                if (tuisong.getData().getValue() != null) {
                                    friendUnreadArrayBean.setNews_messages(tuisong.getData().getValue());
                                }
                                listBean.add(friendUnreadArrayBean);
                            }
                            listViewAdapter3.notifyDataSetChanged();
                            setListViewHeightBasedOnChildren(list);

                            if (!(isshenyin.equals("2"))) {
                                //语音提示
                                mediaPlayer.start();
                            }
                            if (!(iszhendong.equals("2"))) {
                                Vibrator vibrator = (Vibrator) SayActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                                //震动一次
                                vibrator.vibrate(800);
                            }

                        } else {
                            boolean bool = true;
                            for (int b = 0; b < listBean2.size(); b++) {
                                if (listBean2.get(b).getChat_windows_id() == tuisong.getData().getChat_windows_id()) {
                                    listBean2.get(b).setSend_nick_name(tuisong.getData().getChat_windows_name());
                                    listBean2.get(b).setUnread_array(listBean2.get(b).getUnread_array() + 1);
                                    listBean2.get(b).setNews_create_time(tuisong.getData().getCreate_time());
                                    if (tuisong.getData().getValue() != null) {
                                        listBean2.get(b).setNews_messages(tuisong.getData().getValue());
                                    }
                                    bool = false;
                                    break;
                                }
                            }
                            if (bool) {
                                Lgin.DataBean.GroupUnreadArray groupUnreadArray = new Lgin.DataBean.GroupUnreadArray();
                                groupUnreadArray.setChat_windows_id(tuisong.getData().getChat_windows_id());
                                groupUnreadArray.setSend_nick_name(tuisong.getData().getChat_windows_name());
                                groupUnreadArray.setUnread_array(1);
                                groupUnreadArray.setSend_head_url(tuisong.getData().getChat_windows_head_url());
                                groupUnreadArray.setSend_user_id(tuisong.getData().getChat_windows_id());
                                groupUnreadArray.setNews_create_time(tuisong.getData().getCreate_time());
                                if (tuisong.getData().getValue() != null) {
                                    groupUnreadArray.setNews_messages(tuisong.getData().getValue());
                                }
                                listBean2.add(groupUnreadArray);
                            }
                            setListViewHeightBasedOnChildren(list2);
                            listViewAdapter4.notifyDataSetChanged();
                        }


                    }
                } else if (msg.contains("\"event_id\":10013")) {
                    groupUnreadArray = gson.fromJson(msg, GroupUnreadArray.class);
                    for (int i = 0; i < listBean2.size(); i++) {
                        if (listBean2.get(i).getSend_user_id() == groupUnreadArray.getData().getGroup_id()) {
                            listBean2.remove(i);
                        }
                    }

                    listViewAdapter4.notifyDataSetChanged();
                } else if (msg.contains("\"event_id\":10016")) {
                    for (int i = 0; i < listBean2.size(); i++) {
                        if (listBean2.get(i).getSend_user_id() == SeeListActivity.groupid) {
                            listBean2.remove(i);
                        }
                    }

                    listViewAdapter4.notifyDataSetChanged();
                } else if (msg.contains("\"event_id\":10034")) {
                    for (int i = 0; i < listBean2.size(); i++) {
                        if (listBean2.get(i).getSend_user_id() == SeeListActivity.groupid) {
                            listBean2.remove(i);
                        }
                    }

                    listViewAdapter4.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(list2);
                } else {
                    nodata = gson.fromJson(msg, Nodata.class);
                    ToastUtil.show(SayActivity.this, nodata.getTips());

                }
            }
        });
    }


    @OnClick({R.id.iv_right_action, R.id.tv_jiaqun, R.id.tv_jiaren, R.id.rl_12, R.id.ll_12})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_right_action:
                //使用Handler更新了界面
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        d123.setVisibility(View.VISIBLE);
                        ll_123.setVisibility(View.VISIBLE);
                    }
                });
                break;
            case R.id.tv_jiaqun:
                //添加群组
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        d123.setVisibility(View.GONE);
                        ll_123.setVisibility(View.GONE);
                        //请求添加群组
                        //带参数跳转
                        Intent i1 = new Intent(SayActivity.this, NewclassActivity.class);
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("name", "创建群组");
                        i1.putExtras(bundle1);
                        startActivity(i1);

                    }
                });
                break;
            case R.id.tv_jiaren:
                //添加好友
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        d123.setVisibility(View.GONE);
                        ll_123.setVisibility(View.GONE);
                        startActivity(new Intent(SayActivity.this, SearchActivity.class));
                    }
                });
                break;
            case R.id.rl_12:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        d123.setVisibility(View.GONE);
                        ll_123.setVisibility(View.GONE);
                    }
                });

                break;
            case R.id.ll_12:
                //使用Handler更新了界面
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        d123.setVisibility(View.GONE);
                        ll_123.setVisibility(View.GONE);
                    }
                });
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


    public void deletemg(int send_user_id, int chat_type) {
        String json1 = "{\"event_id\":" + 10009;
        String json2 = ",\"send_user_id\":";
        String json22 = ",\"chat_type\":";
        String json3 = "}";
        String json = json1 + json2 + send_user_id + json22 + chat_type + json3;
        //发送 String 数据
        WebSocketHandler.getDefault().send(json);
    }


    @Override
    public void onBackPressed() {//重写的Activity返回

        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        startActivity(intent);

    }


    /**
     * scrollview嵌套listview显示不全解决
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


}
