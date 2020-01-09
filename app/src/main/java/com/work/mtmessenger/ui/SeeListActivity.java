package com.work.mtmessenger.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.work.mtmessenger.MyApp;
import com.work.mtmessenger.R;
import com.work.mtmessenger.etil.EscClass;
import com.work.mtmessenger.etil.Img;
import com.work.mtmessenger.etil.Nodata;
import com.work.mtmessenger.etil.Seelist;
import com.work.mtmessenger.etil.SettingPhot;
import com.work.mtmessenger.etil.SortModel;
import com.work.mtmessenger.ui.login.BaseActivity;
import com.work.mtmessenger.ui.login.LoginActivity;
import com.work.mtmessenger.ui.myactivity.MoRen2Activity;
import com.work.mtmessenger.ui.myactivity.MoRenActivity;
import com.work.mtmessenger.ui.myactivity.SetingActivity;
import com.work.mtmessenger.ui.myactivity.Setingname2Activity;
import com.work.mtmessenger.util.LQRPhotoSelectUtils;
import com.work.mtmessenger.util.MapToString;
import com.work.mtmessenger.util.StatusBarUtil;
import com.work.mtmessenger.util.ToastUtil;
import com.work.mtmessenger.util.image.MyImage;
import com.zhangke.websocket.SimpleListener;
import com.zhangke.websocket.SocketListener;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.WebSocketManager;
import com.zhangke.websocket.response.ErrorResponse;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;


public class SeeListActivity extends BaseActivity {
    @BindView(R.id.iv_go_back)
    ImageView ivGoBack;
    @BindView(R.id.tv_title_column)
    TextView tvTitleColumn;
    @BindView(R.id.tv_delete_class)
    TextView tv_delete_class;
    @BindView(R.id.tv_group_user_count)
    TextView tv_group_user_count;
    @BindView(R.id.ll_1)
    LinearLayout ll1;
    @BindView(R.id.ll_2)
    LinearLayout ll2;
    @BindView(R.id.ll_3)
    LinearLayout ll3;
    @BindView(R.id.ll_4)
    LinearLayout ll4;
    @BindView(R.id.ll_5)
    LinearLayout ll5;
    @BindView(R.id.ll_6)
    LinearLayout ll6;
    @BindView(R.id.ll_classname)
    LinearLayout ll_classname;
    @BindView(R.id.class_phot)
    LinearLayout class_phot;
    @BindView(R.id.myiv_phot)
    MyImage myiv_phot;
    @BindView(R.id.tv_name)
    TextView tv_name;
    public static int groupid = 0;
    private Nodata nodata;
    private Handler handler = new Handler();
    private int target_id;
    private Seelist seelist;
    private EscClass escClass;
    private String s;
    private PromptDialog promptDialog;
    private LQRPhotoSelectUtils mLqrPhotoSelectUtils;
    private String map1 = "";
    private Img img;
    private String imgurl;
    private static Uri is_outputUri;
    private StringBuilder map111;
    private SettingPhot settingPhot;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seelist);
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
        tvTitleColumn.setText("查看群聊");

        //创建对象
        promptDialog = new PromptDialog(this);
        //设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);
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
                    Gson gson = new Gson();
                    if (msg.contains("\"event_id\":10018")) {
                        seelist = gson.fromJson(msg, Seelist.class);
                        tv_group_user_count.setText("(" + seelist.getData().getGroup_user_count() + ")");
                        s = seelist.getData().getMe_role();
                        tv_name.setText(seelist.getData().getName());
                        myiv_phot.setImageURL(seelist.getData().getHead_url());
                        if (s.contains("3")) {
                            ll3.setVisibility(View.VISIBLE);
                        }
                        if (s.contains("4")) {
                            ll4.setVisibility(View.VISIBLE);
                        }
                        if (s.contains("5")) {
                            ll5.setVisibility(View.VISIBLE);
                            tv_delete_class.setText("解散群组");
                            ll_classname.setVisibility(View.VISIBLE);
                            class_phot.setVisibility(View.VISIBLE);
                        }
                        if (s.contains("6")) {
                            ll6.setVisibility(View.VISIBLE);
                            tv_delete_class.setText("解散群组");
                        }
                    } else if (msg.contains("\"event_id\":10016")) {
                        seelist = gson.fromJson(msg, Seelist.class);
                        ToastUtil.show(SeeListActivity.this, seelist.getTips());
                        startActivity(new Intent(SeeListActivity.this, MainActivity.class));
                        groupid = target_id;
                        finish();

                    } else if (msg.contains("\"event_id\":10034")) {
                        nodata = gson.fromJson(msg, Nodata.class);
                        ToastUtil.show(SeeListActivity.this, nodata.getTips());
                        startActivity(new Intent(SeeListActivity.this, MainActivity.class));
                        groupid = target_id;
                        finish();
                    } else if (msg.contains("\"event_id\":10041")) {
                        sed();
                    }
                } else {
                    Gson gson = new Gson();
                    nodata = gson.fromJson(msg, Nodata.class);
                    ToastUtil.show(SeeListActivity.this, nodata.getTips());
                }
            }
        });
    }


    private void sed() {
        String json1 = "{\"event_id\":" + 10018;
        String json2 = ",\"group_id\":" + target_id;
        String json3 = "}";
        String json = json1 + json2 + json3;

        //发送 String 数据
        WebSocketHandler.getDefault().send(json);

    }


    @OnClick({R.id.iv_go_back, R.id.ll_1, R.id.ll_2, R.id.ll_3, R.id.ll_4, R.id.ll_5, R.id.ll_6, R.id.tv_delete_class, R.id.ll_classname, R.id.class_phot})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_go_back:
                finish();
                break;
            case R.id.ll_1:
                Intent i1 = new Intent(SeeListActivity.this, SeeListAllActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putInt("target_id", target_id);
                i1.putExtras(bundle1);
                startActivity(i1);
                break;
            case R.id.ll_2:
                Intent i = new Intent(SeeListActivity.this, LiaisonMan2Activity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("target_id", target_id);
                bundle.putString("title", "邀请成员");
                i.putExtras(bundle);
                startActivity(i);

                break;
            case R.id.ll_3:
                Intent i3 = new Intent(SeeListActivity.this, SettingclassActivity.class);
                Bundle bundle3 = new Bundle();
                bundle3.putInt("target_id", target_id);
                bundle3.putInt("title", 3);
                i3.putExtras(bundle3);
                startActivity(i3);
                break;
            case R.id.ll_4:
                Intent i4 = new Intent(SeeListActivity.this, SettingclassActivity.class);
                Bundle bundle4 = new Bundle();
                bundle4.putInt("target_id", target_id);
                bundle4.putInt("title", 4);
                i4.putExtras(bundle4);
                startActivity(i4);

                break;
            case R.id.ll_5:
                Intent i5 = new Intent(SeeListActivity.this, SettingclassActivity.class);
                Bundle bundle5 = new Bundle();
                bundle5.putInt("target_id", target_id);
                bundle5.putInt("title", 5);
                i5.putExtras(bundle5);
                startActivity(i5);
                break;
            case R.id.ll_6:
                Intent i6 = new Intent(SeeListActivity.this, SettingclassActivity.class);
                Bundle bundle6 = new Bundle();
                bundle6.putInt("target_id", target_id);
                bundle6.putInt("title", 6);
                i6.putExtras(bundle6);
                startActivity(i6);
                break;

            case R.id.tv_delete_class:
                if (s.contains("5")) {
                    String json1 = "{\"event_id\":" + 10034;
                    String json2 = ",\"group_id\":" + target_id;
                    String json3 = "}";
                    String json = json1 + json2 + json3;
                    //发送 String 数据
                    WebSocketHandler.getDefault().send(json);
                } else {
                    String json1 = "{\"event_id\":" + 10016;
                    String json2 = ",\"group_id\":" + target_id;
                    String json3 = "}";
                    String json = json1 + json2 + json3;
                    //发送 String 数据
                    WebSocketHandler.getDefault().send(json);
                }
                break;
            case R.id.class_phot:
                PromptButton cancle = new PromptButton("取消", null);
                cancle.setTextColor(Color.parseColor("#0076ff"));

                //调用本地相机、相册
                promptDialog.showAlertSheet("", true, cancle,
                        new PromptButton("相机", new PromptButtonListener() {
                            @Override
                            public void onClick(PromptButton button) {
                                // 3、调用拍照方法
                                PermissionGen.with(SeeListActivity.this)
                                        .addRequestCode(LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
                                        .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                Manifest.permission.CAMERA
                                        ).request();

                            }
                        }), new PromptButton("相册", new PromptButtonListener() {
                            @Override
                            public void onClick(PromptButton button) {

                                PermissionGen.needPermission(SeeListActivity.this,
                                        LQRPhotoSelectUtils.REQ_SELECT_PHOTO,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                );

                            }

                        }), new PromptButton("默认头像", new PromptButtonListener() {
                            @Override
                            public void onClick(PromptButton button) {

                                Intent i1 = new Intent(SeeListActivity.this, MoRen2Activity.class);
                                Bundle bundle1 = new Bundle();
                                bundle1.putInt("target_id", target_id);
                                i1.putExtras(bundle1);
                                startActivity(i1);

                            }

                        })

                );

                init1();
                break;
            case R.id.ll_classname:
                Intent i0 = new Intent(SeeListActivity.this, Setingname2Activity.class);
                Bundle bundle10 = new Bundle();
                bundle10.putString("name", seelist.getData().getName());
                bundle10.putInt("target_id", target_id);
                i0.putExtras(bundle10);
                startActivity(i0);
                break;
        }
    }

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
                            System.out.println("字符串格式的图片：" + map1);
                            map111.append("data:image/png;base64," + map1);
                            System.out.println("字符串格式的图片1长度：" + map111.length());

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
                            System.out.println("字符串格式的图片123：" + map111);
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


        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            //请求失败时调用
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {//子线程中不能直接吐司
                    @Override
                    public void run() {
                        System.out.println("网络异常");
                        Toast.makeText(SeeListActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e(TAG, "onFailure: " + e);
            }

            //请求成功时调用
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    System.out.println("/njsonData===" + jsonData);

                    //使用Handler更新了界面
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (jsonData.contains("\"code\":10000")) {
                                Gson gson = new Gson();
                                img = gson.fromJson(jsonData, Img.class);
                                imgurl = img.getData().getPath();
                                myiv_phot.setImageURL(img.getData().getShow_path());
                                settingphot();
                            } else {
                                Toast.makeText(SeeListActivity.this, img.getMsg(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }

            }

        });
    }


    //修改头像 接口
    private void settingphot() {
        String json1 = "{\"event_id\":10041";
        String json2 = ",\"group_id\":";
        String json22 = ",\"url\":\"";
        String json3 = "\"}";
        String json = json1 + json2 + target_id + json22 + imgurl + json3;
        //发送 String 数据
        WebSocketHandler.getDefault().send(json);
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        sed();

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
        if (resultCode == 404) {
            Bundle bundle = data.getExtras();
            String str = bundle.getString("back");
            System.out.println("huidian====" + str);
            imgurl = str;
        }

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
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + SeeListActivity.this.getPackageName()));
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
}
