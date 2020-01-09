package com.work.mtmessenger.ui.myactivity;


import android.Manifest;
import android.app.Activity;
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

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.work.mtmessenger.MyApp;
import com.work.mtmessenger.R;
import com.work.mtmessenger.etil.Img;
import com.work.mtmessenger.etil.Newfrend10004;
import com.work.mtmessenger.etil.Nodata;
import com.work.mtmessenger.etil.SettingPhot;
import com.work.mtmessenger.etil.asd;
import com.work.mtmessenger.ui.MainActivity;
import com.work.mtmessenger.ui.PaypassActivity;
import com.work.mtmessenger.ui.SayActivity;
import com.work.mtmessenger.ui.SearchActivity;
import com.work.mtmessenger.ui.login.BaseActivity;
import com.work.mtmessenger.ui.login.LoginActivity;
import com.work.mtmessenger.util.ActivityCollector;
import com.work.mtmessenger.util.LQRPhotoSelectUtils;
import com.work.mtmessenger.util.MapToString;
import com.work.mtmessenger.util.StatusBarUtil;
import com.work.mtmessenger.util.ToastUtil;
import com.work.mtmessenger.util.image.MyImageView;
import com.zhangke.websocket.SimpleListener;
import com.zhangke.websocket.SocketListener;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.WebSocketManager;
import com.zhangke.websocket.WebSocketSetting;
import com.zhangke.websocket.response.ErrorResponse;

import org.afinal.simplecache.ACache;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
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
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;


public class SetingActivity extends BaseActivity {

    @BindView(R.id.iv_go_back)
    ImageView ivGoBack;
    @BindView(R.id.tv_title_column)
    TextView tvTitleColumn;
    @BindView(R.id.myiv_phot)
    MyImageView myivPhot;
    @BindView(R.id.ll_phot)
    LinearLayout llPhot;
    @BindView(R.id.nicname)
    TextView nicname;
    @BindView(R.id.tv_go_esc)
    TextView tv_go_esc;
    private StringBuilder map111, map222, map333;
    private static Uri is_outputUri;
    @BindView(R.id.id_tv_name)
    LinearLayout idTvName;
    private PromptDialog promptDialog;
    private LQRPhotoSelectUtils mLqrPhotoSelectUtils;
    private String map1 = "";
    private Handler handler = new Handler();
    private Img img;
    private String imgurl;
    private SettingPhot settingPhot;
    private WebSocketManager manager;
    private ACache acache;
    private Nodata nodata;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seting);
        ButterKnife.bind(this);
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBarColor(this, R.color.main);

        tvTitleColumn.setText("个人信息");
        ivGoBack.setVisibility(View.VISIBLE);
        acache = ACache.get(this);//创建ACache组件


        //创建对象
        promptDialog = new PromptDialog(this);

        //设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);
        //        //通过此方法获取默认的 WebSocketManager 对象
        manager = WebSocketHandler.getDefault();
        manager.addListener(socketListener);
    }


    @OnClick({R.id.iv_go_back, R.id.ll_phot, R.id.id_tv_name, R.id.tv_go_esc})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_go_back:
                finish();
                break;
            case R.id.ll_phot:
                PromptButton cancle = new PromptButton("取消", null);
                cancle.setTextColor(Color.parseColor("#0076ff"));

                //调用本地相机、相册
                promptDialog.showAlertSheet("", true, cancle,
                        new PromptButton("相机", new PromptButtonListener() {
                            @Override
                            public void onClick(PromptButton button) {
                                // 3、调用拍照方法
                                PermissionGen.with(SetingActivity.this)
                                        .addRequestCode(LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
                                        .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                Manifest.permission.CAMERA
                                        ).request();

                            }
                        }), new PromptButton("相册", new PromptButtonListener() {
                            @Override
                            public void onClick(PromptButton button) {

                                PermissionGen.needPermission(SetingActivity.this,
                                        LQRPhotoSelectUtils.REQ_SELECT_PHOTO,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                );

                            }

                        }), new PromptButton("默认头像", new PromptButtonListener() {
                            @Override
                            public void onClick(PromptButton button) {
                                startActivity(new Intent(SetingActivity.this, MoRenActivity.class));

                            }

                        })

                );

                init1();


                break;
            case R.id.id_tv_name:
                //跳转
                startActivity(new Intent(SetingActivity.this, SetingnameActivity.class));
                break;

            case R.id.tv_go_esc:
                //请求退出登录
                String json1 = "{\"event_id\":10042";
                String json3 = "\"}";
                String json = json1 + json3;
                //发送 String 数据
                WebSocketHandler.getDefault().send(json);
                acache.remove("token");
                finish();
                startActivity(new Intent(SetingActivity.this, LoginActivity.class));
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


    protected void onResume() {
        super.onResume();
        if (MoRenActivity.isuirl) {
            imgurl = MyApp.head_url;
            settingphot();
        }
        myivPhot.setImageURL(MyApp.head_url);
        nicname.setText(MyApp.nick_name);
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
                        Toast.makeText(SetingActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
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
                            Gson gson = new Gson();
                            img = gson.fromJson(jsonData, Img.class);
                            if (jsonData.contains("\"code\":10000")) {
                                imgurl = img.getData().getPath();
                                myivPhot.setImageURL(img.getData().getShow_path());
                                settingphot();
                            } else {
                                Toast.makeText(SetingActivity.this, img.getMsg(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });
    }

    //修改头像 接口
    private void settingphot() {
        String json1 = "{\"event_id\":10022";
        String json22 = ",\"url\":\"";
        String json3 = "\"}";
        String json = json1 + json22 + imgurl + json3;
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

    private void appendMsgDisplay(String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (msg.contains("onConnected")) {
                } else if (msg.contains("onDisconnect")) {
                } else if (msg.contains("\"ret_code\":0")) {

                } else if (msg.contains("\"ret_code\":10000")) {
                    Gson gson = new Gson();
                    if (msg.contains("\"event_id\":10022")) {
                        settingPhot = gson.fromJson(msg, SettingPhot.class);
                        MyApp.head_url = img.getData().getShow_path();
                        ToastUtil.show(SetingActivity.this, settingPhot.getTips());
                    }

                } else {
                    Gson gson = new Gson();
                    nodata = gson.fromJson(msg, Nodata.class);
                    ToastUtil.show(SetingActivity.this, nodata.getTips());
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
                intent.setData(Uri.parse("package:" + SetingActivity.this.getPackageName()));
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