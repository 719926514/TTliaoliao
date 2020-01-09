package com.work.mtmessenger.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.work.mtmessenger.MyApp;
import com.work.mtmessenger.R;
import com.work.mtmessenger.adapter.SayListAdapter;
import com.work.mtmessenger.etil.Call;
import com.work.mtmessenger.etil.Img;
import com.work.mtmessenger.etil.Nodata;
import com.work.mtmessenger.etil.Open10033;
import com.work.mtmessenger.etil.Send10008;
import com.work.mtmessenger.etil.Trycod;
import com.work.mtmessenger.ui.login.BaseActivity;
import com.work.mtmessenger.ui.login.PermissionListener;
import com.work.mtmessenger.util.Base64Encoder;
import com.work.mtmessenger.util.CommonSelectorDialog;
import com.work.mtmessenger.util.HongbaoDialog;
import com.work.mtmessenger.util.LQRPhotoSelectUtils;
import com.work.mtmessenger.util.MapToString;
import com.work.mtmessenger.util.Shensu2Dialog;
import com.work.mtmessenger.util.StatusBarUtil;
import com.work.mtmessenger.util.ToastUtil;
import com.work.mtmessenger.util.image.ImageInfoObj;
import com.work.mtmessenger.util.image.ImageWidgetInfoObj;
import com.work.mtmessenger.widgets.xlistview.XListView;
import com.work.mtmessenger.widgets.xlistview.manage.AudioRecorderButton;
import com.zhangke.websocket.SimpleListener;
import com.zhangke.websocket.SocketListener;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.WebSocketManager;
import com.zhangke.websocket.response.ErrorResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;
import static com.work.mtmessenger.util.TimeUtil.getTime;


public class SayListActivity extends BaseActivity {
    @BindView(R.id.iv_go_back)
    ImageView ivGoBack;
    @BindView(R.id.tv_title_column)
    TextView tvTitleColumn;
    @BindView(R.id.list)
    XListView list;
    @BindView(R.id.ic_go_yuyin)
    TextView ic_go_yuyin;
    @BindView(R.id.et_context)
    EditText et_context;
    @BindView(R.id.ic_go_wenzi)
    TextView ic_go_wenzi;
    @BindView(R.id.id_recorder_button)
    AudioRecorderButton mAudioRecorderButton;
    @BindView(R.id.addmor)
    TextView addmor;
    @BindView(R.id.send)
    TextView send;
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;
    @BindView(R.id.iv_right_action)
    ImageView ivRightAction;

    private Nodata nodata;
    private HongbaoDialog hongbaoDialog;
    private Img img;
    private LQRPhotoSelectUtils mLqrPhotoSelectUtils;
    private String value;
    private Handler handler = new Handler();
    private SayListAdapter listViewAdapter3;
    private List<Call.DataBean.ArrayBean> listBean = new ArrayList<Call.DataBean.ArrayBean>();
    private List<Call.DataBean.ArrayBean> listBean2 = new ArrayList<Call.DataBean.ArrayBean>();
    private String name, map1 = "";
    private int target_id, target_type, value_type;//消息内容 1:文本，２：图片，３：语音
    private Send10008 send10008;
    private int index = 1;
    private Call call;
    private Shensu2Dialog shensu2Dialog;
    private StringBuilder map111;
    private static Uri is_outputUri;
    private View mAnimView;
    MediaPlayer mediaPlayer = new MediaPlayer();
    private CommonSelectorDialog commonSelectorDialog;
    private boolean shuaxin = true;
    public static boolean mIsListViewIdle= true;
    private ImageInfoObj imageInfoObj;
    private ImageWidgetInfoObj imageWidgetInfoObj;
    private Trycod trycod;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saylist);
        ButterKnife.bind(this);

        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBarColor(this, R.color.main);

        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        name = bundle.getString("name");//聊天人 名称/群名称
        target_id = bundle.getInt("target_id");// 聊天人  编号/群编号
        target_type = bundle.getInt("target_type");//聊天人类型   单人1/群2
        tvTitleColumn.setText(name);
        ivGoBack.setVisibility(View.VISIBLE);
        if (target_type == 2) {
            ivRightAction.setVisibility(View.VISIBLE);
            ivRightAction.setImageResource(R.drawable.mor);
        }
        list.setPullRefreshEnable(true);
        list.setPullLoadEnable(false);
        list.setAutoLoadEnable(true);
        list.setRefreshTime(getTime());
        list.setVerticalScrollBarEnabled(true);

        listViewAdapter3 = new SayListAdapter(SayListActivity.this, R.layout.vw_list_item, listBean);
        list.setAdapter(listViewAdapter3);
        //列表滑动监听
        list.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            // 下拉刷新
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (index < call.getData().getTotal_page()) {
                            index = index + 1;
//                            Collections.reverse(listBean);
//                            listBean.clear();
                            call(false);
                            onLoad();
                        } else {

                        }

                    }
                }, 1000);
            }

            @Override
            //上拉加载
            public void onLoadMore() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onLoad();
                    }
                }, 500);
            }
        });
//滑动优化
        list.setOnScrollListener(new XListView.OnXScrollListener() {
            @Override
            public void onXScrolling(View view) {

            }

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    mIsListViewIdle = true;
                    listViewAdapter3.notifyDataSetChanged();
                } else {
                    mIsListViewIdle = false;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });


        View decorView = getWindow().getDecorView();
        View contentView = findViewById(Window.ID_ANDROID_CONTENT);
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(getGlobalLayoutListener(decorView, contentView));


        et_context.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(et_context.getText())) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            addmor.setVisibility(View.VISIBLE);
                            send.setVisibility(View.GONE);
                        }
                    });
                } else {
                    addmor.setVisibility(View.GONE);
                    send.setVisibility(View.VISIBLE);
                    value_type = 1;
                }


            }
        });


        //        //通过此方法获取默认的 WebSocketManager 对象
        WebSocketManager manager = WebSocketHandler.getDefault();
        manager.addListener(socketListener);


        mAudioRecorderButton.setAudioFinishRecorderListener(new AudioRecorderButton.AudioFinishRecorderListener() {
            @Override
            public void onFinish(float seconds, String filePath) throws Exception {
                //每完成一次录音
                String s = "data:audio/mp3;base64," + encodeBase64File(filePath);
                System.out.println("64音频=" + s);
                outvideo(s);
            }
        });

//播放音频
        listViewAdapter3.setOnAddListener(new SayListAdapter.onAddListener() {
            @Override
            public void onAddListener(String value, View view, boolean isme) {
                //如果第一个动画正在运行， 停止第一个播放其他的
                if (mAnimView != null) {
                    if (isme) {
                        mAnimView.setBackgroundResource(R.drawable.adj);
                    } else {
                        mAnimView.setBackgroundResource(R.drawable.adj1);
                    }
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mAnimView = null;
                }
                //播放动画
                if (isme) {
                    mAnimView = view.findViewById(R.id.id_recorder_anim1);
                    mAnimView.setBackgroundResource(R.drawable.play_anim);
                } else {
                    mAnimView = view.findViewById(R.id.id_recorder_anim);
                    mAnimView.setBackgroundResource(R.drawable.play_anim1);
                }

                try {
                    mediaPlayer.setDataSource(value);
                    //3 准备播放
                    mediaPlayer.prepareAsync();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            mediaPlayer.start();
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        Log.d("tag", "播放完毕");
                        if (isme) {
                            mAnimView.setBackgroundResource(R.drawable.adj);
                        } else {
                            mAnimView.setBackgroundResource(R.drawable.adj1);
                        }
                        mediaPlayer.stop();
                        mediaPlayer.reset();

                    }
                });

            }
        });

//长按撤回
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int messages_id, long l) {
                commonSelectorDialog = new CommonSelectorDialog(SayListActivity.this);
                commonSelectorDialog.show();
                commonSelectorDialog.setOnPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String json1 = "{\"event_id\":10023";
                        String json2 = ",\"messages_id\":" + listBean.get(messages_id - 1).getMessages_id();
                        String json3 = "}";
                        String json = json1 + json2 + json3;
                        //发送 String 数据
                        WebSocketHandler.getDefault().send(json);
                        commonSelectorDialog.dismiss();
                    }
                });
                commonSelectorDialog.setOnNegativeListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        commonSelectorDialog.dismiss();
                    }
                });
                return false;
            }
        });

        //红包
        listViewAdapter3.setOnopenListener(new SayListAdapter.onopenListener() {
            @Override
            public void onopenListener(int Value_type, int messages_id, boolean isme) {
                if (Value_type == 4) {
                    if (isme) {
                        return;
                    } else {
                        String json1 = "{\"event_id\":10033";
                        String json2 = ",\"messages_id\":" + messages_id;
                        String json3 = "}";
                        String json = json1 + json2 + json3;
                        //发送 String 数据
                        WebSocketHandler.getDefault().send(json);
                    }
                }
            }
        });
//大图
        listViewAdapter3.setOnbigListener(new SayListAdapter.onbigListener() {
            @Override
            public void onbigListener(String Value) {
                Intent i1 = new Intent(SayListActivity.this, howImageActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("iv", Value);
                i1.putExtras(bundle1);
                startActivity(i1);
            }
        });

    }

    private ViewTreeObserver.OnGlobalLayoutListener getGlobalLayoutListener(final View decorView, final View contentView) {
        return new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                decorView.getWindowVisibleDisplayFrame(r);

                int height = decorView.getContext().getResources().getDisplayMetrics().heightPixels;
                int diff = height - r.bottom;

                if (diff != 0) {
                    if (contentView.getPaddingBottom() != diff) {
                        contentView.setPadding(0, 0, 0, diff);
//                        listViewAdapter3.notifyDataSetChanged();
//                        list.setSelection(listBean.size());
                    }
                } else {
                    if (contentView.getPaddingBottom() != 0) {
                        contentView.setPadding(0, 0, 0, 0);
//                        listViewAdapter3.notifyDataSetChanged();
//                        list.setSelection(listBean.size());
                    }
                }
            }
        };
    }


    @OnClick({R.id.iv_go_back, R.id.ic_go_yuyin, R.id.ic_go_wenzi, R.id.addmor, R.id.send, R.id.iv_right_action})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_go_back:
                finish();
                break;
            case R.id.ic_go_yuyin:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        requestRuntimePermission(new String[]{
//                                Manifest.permission.CALL_PHONE,//打电话权限
//                    Manifest.permission.ACCESS_FINE_LOCATION,//精准定位权限
//                                Manifest.permission.CAMERA,//相机权限
//                                Manifest.permission.WRITE_EXTERNAL_STORAGE,//写数据权限
//                                Manifest.permission.READ_EXTERNAL_STORAGE,//读数据权限
                                Manifest.permission.RECORD_AUDIO//录音权限
                        }, new PermissionListener() {
                            //授权后的回调方法
                            @Override
                            public void onGranted() {
                                ic_go_wenzi.setVisibility(View.VISIBLE);
                                mAudioRecorderButton.setVisibility(View.VISIBLE);
                                ic_go_yuyin.setVisibility(View.GONE);
                                et_context.setVisibility(View.GONE);
                            }

                            //权限被拒绝的回调方法
                            @Override
                            public void onDenied(List<String> deniedPermission) {
                                for (String permission : deniedPermission) {
                                    Toast.makeText(SayListActivity.this, "被拒绝权限：" + permission, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });
                break;
            case R.id.ic_go_wenzi:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ic_go_wenzi.setVisibility(View.GONE);
                        mAudioRecorderButton.setVisibility(View.GONE);
                        ic_go_yuyin.setVisibility(View.VISIBLE);
                        et_context.setVisibility(View.VISIBLE);
                    }
                });
                break;
            case R.id.addmor:

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tv_no_data.setVisibility(View.VISIBLE);
                        shensu2Dialog = new Shensu2Dialog(SayListActivity.this);
                        shensu2Dialog.show();
                        shensu2Dialog.setOnButtonClickListener(onButtonClickListener);
                    }
                });

                break;

            case R.id.send:
                value = et_context.getText().toString();
                send();
                break;

            case R.id.iv_right_action:
                //查看群聊
                Intent i1 = new Intent(SayListActivity.this, SeeListActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putInt("target_id", target_id);
                i1.putExtras(bundle1);
                startActivity(i1);
                finish();
                break;
        }
    }

    //发送消息
    private void send() {
        String json1 = "{\"event_id\":10008";
        String json22 = ",\"target_type\":" + target_type;
        String json23 = ",\"target_id\":" + target_id;
        String json222 = ",\"value_type\":" + value_type;
        String json223 = ",\"value\":\"";
        String json3 = "\"}";
        if (value_type == 2 || value_type == 3) {
            value = img.getData().getPath();
        }
        String json = json1 + json22 + json23 + json222 + json223 + value + json3;

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
                System.out.println("aaaaaaaaa====" + msg);
                if (msg.contains("onConnected")) {
                } else if (msg.contains("onDisconnect")) {
                } else if (msg.contains("\"ret_code\":10000")) {
                    Gson gson = new Gson();
                    if (msg.contains("\"event_id\":10008")) {
                        send10008 = gson.fromJson(msg, Send10008.class);
                        value = null;
                        et_context.setText("");
                        call(true);
                    } else if (msg.contains("\"event_id\":10007")) {
                        call(true);
                    } else if (msg.contains("\"event_id\":10024")) {
                        call(true);
                    } else if (msg.contains("\"event_id\":10023")) {
                        call(true);
                    } else if (msg.contains("\"event_id\":10024")) {
                        call(true);
                    } else if (msg.contains("\"event_id\":10033")) {
                        //打开红包后的动作
                        Open10033 pen10033 = gson.fromJson(msg, Open10033.class);
                        hongbaoDialog = new HongbaoDialog(SayListActivity.this);
                        hongbaoDialog.setContent("￥" + pen10033.getData().getMoney() + "元");
                        hongbaoDialog.show();
                        hongbaoDialog.setOnPositiveListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                call(true);
                                hongbaoDialog.dismiss();
                            }
                        });
                        listViewAdapter3.notifyDataSetChanged();
                        list.setSelection(listBean.size());
                    } else if (msg.contains("\"event_id\":10020")) {
                        call = gson.fromJson(msg, Call.class);
                        if (shuaxin) {
                            list.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, 0, 0, 0));
                            listBean.clear();
                            index = 1;
                        } else {
//                            Collections.reverse(listBean);
                        }
                        if (msg.contains("array")) {
                            if (call.getData().getArray().size() > 0) {
                                for (int i = 0; i < call.getData().getArray().size(); i++) {
                                    listBean.add(0, call.getData().getArray().get(i));
                                }
//                                Collections.reverse(listBean);
                                listViewAdapter3.notifyDataSetChanged();
                                if (shuaxin) {
                                    list.setSelection(listBean.size());
                                }
                            }
                        }

                    } else if (msg.contains("\"event_id\":10032")) {
                        trycod = gson.fromJson(msg, Trycod.class);
                        ToastUtil.show(SayListActivity.this, trycod.getTips());
                    }
                } else {
                    Gson gson = new Gson();
                    nodata = gson.fromJson(msg, Nodata.class);
                    ToastUtil.show(SayListActivity.this, nodata.getTips());
                }
            }

        });
    }


    protected void onResume() {
        super.onResume();
        call(true);

    }

    //获取聊天记录
    private void call(boolean s) {

        shuaxin = s;
        if (s) {
            index = 1;
        }
        String json1 = "{\"event_id\":10020";
        String json22 = ",\"index\":" + index;
        String json23 = ",\"chat_type\":" + target_type;
        String json222 = ",\"target_id\":" + target_id;
        String json3 = "}";
        String json = json1 + json22 + json23 + json222 + json3;
        //发送 String 数据
        WebSocketHandler.getDefault().send(json);
    }


    private void onLoad() {
        list.stopRefresh();
        list.stopLoadMore();
        list.setRefreshTime(getTime());
    }


    /**
     * dialog的点击事件
     */
    Shensu2Dialog.OnButtonClickListener onButtonClickListener = new Shensu2Dialog.OnButtonClickListener() {
        @Override
        public void onbt_xiangceClick(View view) {
            PermissionGen.needPermission(SayListActivity.this,
                    LQRPhotoSelectUtils.REQ_SELECT_PHOTO,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}
            );
            init1();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    tv_no_data.setVisibility(View.GONE);
                    shensu2Dialog.dismiss();

                }
            });


        }

        @Override
        public void ontv_paizaoClick(View view) {
            PermissionGen.with(SayListActivity.this)
                    .addRequestCode(LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
                    .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                    ).request();
            init1();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    tv_no_data.setVisibility(View.GONE);
                    shensu2Dialog.dismiss();
                }
            });
        }

        @Override
        public void bt_hongbaoClick(View view) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    tv_no_data.setVisibility(View.GONE);
                    shensu2Dialog.dismiss();

                    Intent i1 = new Intent(SayListActivity.this, HongbaoActivity.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt("target_id", target_id);
                    bundle1.putInt("target_type", target_type);
                    i1.putExtras(bundle1);
                    startActivity(i1);

                }
            });
        }
    };


    private void init1() {
        // 1、创建LQRPhotoSelectUtils（一个Activity对应一个LQRPhotoSelectUtils）
        mLqrPhotoSelectUtils = new LQRPhotoSelectUtils(this, new LQRPhotoSelectUtils.PhotoSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
                is_outputUri = outputUri;
                map1 = null;
                map111 = null;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            if (map111 != null)
                                break;
                            map111 = new StringBuilder();
                            Bitmap bitmap = null;
                            Uri uri = null;
                            try {
                                uri = Uri.parse((String) is_outputUri.toString());
                                ContentResolver resolver = getContentResolver();
                                bitmap = MediaStore.Images.Media.getBitmap(resolver, uri);//获得图片
                                int wid = bitmap.getWidth();
                                int hei = bitmap.getHeight();
                                if (wid > 1000 || hei > 1000) {
                                    wid = wid / 2;
                                    hei = hei / 2;
                                    System.out.println("aaaa圖片截斷");
                                }
                                bitmap = MapToString.comp(bitmap, wid, hei);//图片缩放


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            map1 = MapToString.convertIconToString(bitmap);//图片转字符

                            map111.append("data:image/png;base64," + map1);


                            StringBuilder temp = new StringBuilder();
                            char ctr = ' ';
                            for (int i = 0; i < map111.length(); i++) {
                                ctr = map111.charAt(i);
                                if ((int) ctr == 10) {
                                } else {
                                    temp.append(ctr);
                                }
                            }
                            //"data:image/png;base64,"
                            map111 = temp;

                            //上传图片
                            outimg();

                        }
                    }
                }, 1000);//1秒后执行Runnable中的run方法


            }
        }, true);//true裁剪，false不裁剪

    }


    //上传头像
    public void outimg() {
        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=--------------------------142890637432470731177857");
//        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient okHttpClient = new OkHttpClient();
        //申明给服务端传递一个json串
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
//        RequestBody requestBody = RequestBody.create(JSON, json);
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("img", map111 + "")
                .build();

        //创建一个请求对象
        String urlapi = MyApp.imgrul;
        System.out.println("/n需要发送的地址" + urlapi);
        Request request = new Request.Builder()
                .url(urlapi)
                .method("POST", requestBody)
                .addHeader("Content-Type", "multipart/form-data; boundary=--------------------------142890637432470731177857")
                .build();

        okhttp3.Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            //请求失败时调用
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

                runOnUiThread(new Runnable() {//子线程中不能直接吐司
                    @Override
                    public void run() {

                        Toast.makeText(SayListActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e(TAG, "onFailure: " + e);
            }

            //请求成功时调用
            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    System.out.println("/njsonData===" + jsonData);

                    //使用Handler更新了界面
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            img = gson.fromJson(jsonData, Img.class);
                            if (jsonData.contains("\"code\":10000")) {
                                value = img.getData().getShow_path();
                                value_type = 2;
                                send();
                            } else {
                                Toast.makeText(SayListActivity.this, img.getMsg(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }

            }

        });
    }


    //上传音频
    public void outvideo(String s) {
        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=--------------------------142890637432470731177857");
//        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient okHttpClient = new OkHttpClient();
        //申明给服务端传递一个json串
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
//        RequestBody requestBody = RequestBody.create(JSON, json);
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("voice", s)
                .build();

        //创建一个请求对象
        String urlapi = MyApp.diveorul;
        System.out.println("/n需要发送的地址" + urlapi);
        Request request = new Request.Builder()
                .url(urlapi)
                .method("POST", requestBody)
                .addHeader("Content-Type", "multipart/form-data; boundary=--------------------------142890637432470731177857")
                .build();

        okhttp3.Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            //请求失败时调用
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

                runOnUiThread(new Runnable() {//子线程中不能直接吐司
                    @Override
                    public void run() {
                        System.out.println("网络异常");
                        Toast.makeText(SayListActivity.this, "网络异常", Toast.LENGTH_SHORT).show();

                    }
                });
                Log.e(TAG, "onFailure: " + e);
            }

            //请求成功时调用
            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    System.out.println("/njsonData===" + jsonData);

                    //使用Handler更新了界面
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            img = gson.fromJson(jsonData, Img.class);
                            if (jsonData.contains("\"code\":10000")) {
                                value = img.getData().getShow_path();
                                value_type = 3;
                                send();
                            } else {
                                Toast.makeText(SayListActivity.this, img.getMsg(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }

            }

        });
    }


    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
    private void takePhoto() {
        mLqrPhotoSelectUtils.takePhoto();
    }

    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_SELECT_PHOTO)
    private void selectPhoto() {
        mLqrPhotoSelectUtils.selectPhoto();
    }

    @PermissionFail(requestCode = LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
    private void showTip1() {
        showDialog();
    }

    @PermissionFail(requestCode = LQRPhotoSelectUtils.REQ_SELECT_PHOTO)
    private void showTip2() {
        showDialog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mLqrPhotoSelectUtils.attachToActivityForResult(requestCode, resultCode, data);
    }

    public void showDialog() {
        //创建对话框创建器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置对话框显示小图标
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        //设置标题
        builder.setTitle("权限申请");
        //设置正文
        builder.setMessage("在设置-应用-虎嗅-权限 中开启相机、存储权限，才能正常使用拍照或图片选择功能");

        //添加确定按钮点击事件
        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {//点击完确定后，触发这个事件

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //这里用来跳到手机设置页，方便用户开启权限
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + SayListActivity.this.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //添加取消按钮点击事件
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        //使用构建器创建出对话框对象
        AlertDialog dialog = builder.create();
        dialog.show();//显示对话框
    }


    /**
     *      * 将音频转化为base64位编码
     *      *
     */
    public static String encodeBase64File(String path) throws Exception {

        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return new Base64Encoder().encode(buffer);


    }


}