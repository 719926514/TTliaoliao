package com.work.mtmessenger.util.image;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//非圆角
@SuppressLint("AppCompatCustomView")
public class MyImage extends ImageView {
    private String imagePath;
    //是否启用缓存
    public boolean isUseCache = true;

    public static final int GET_DATA_SUCCESS = 1;
    public static final int NETWORK_ERROR = 2;
    public static final int SERVER_ERROR = 3;
    //子线程不能操作UI，通过Handler设置图片
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_DATA_SUCCESS:
                    Bitmap bitmap = (Bitmap) msg.obj;
//                    bitmap=toRoundBitmap(bitmap);//转换成圆角的方法
                    setImageBitmap(bitmap);
                    break;
                case NETWORK_ERROR:
//                    Toast.makeText(getContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
                    break;
                case SERVER_ERROR:
//                    Toast.makeText(getContext(), "服务器发生错误", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public MyImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyImage(Context context) {
        super(context);
    }

    public MyImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //设置网络图片
    public void setImageURL(String path) {
        imagePath = path;
        if (isUseCache){
            useCacheImage();
        }else {
            useNetWorkImage();
        }
    }

    //使用网络图片显示
    public void useNetWorkImage(){
        //开启一个线程用于联网
        new Thread() {
            @Override
            public void run() {
                try {
                    //把传过来的路径转成URL
                    URL url = new URL(imagePath);
                    //获取连接
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //使用GET方法访问网络
                    connection.setRequestMethod("GET");
                    //超时时间为10秒
                    connection.setConnectTimeout(10000);
                    //获取返回码
                    int code = connection.getResponseCode();
                    if (code == 200) {
                        Bitmap bitmap;
                        //获取网络输入流
                        InputStream inputStream = connection.getInputStream();

                        //判断是否使用缓存图片
                        if (isUseCache){
                            //因为InputStream要使用两次，但是使用一次就无效了，所以需要复制两个
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            try {
                                byte[] buffer = new byte[1024];
                                int len;
                                while ((len = inputStream.read(buffer)) > -1) {
                                    baos.write(buffer, 0, len);
                                }
                                baos.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            //复制新的输入流
                            InputStream is = new ByteArrayInputStream(baos.toByteArray());
                            InputStream is2 = new ByteArrayInputStream(baos.toByteArray());

                            //调用压缩方法显示图片
                             bitmap = getCompressBitmap(is);
//                            bitmap=toRoundBitmap(bitmap);
                            //调用缓存图片方法
                            cacheImage(is2);
                        }else {
                            //调用压缩方法
                             bitmap = getCompressBitmap(inputStream);
                        }

                        //利用Message把图片发给Handler
                        Message msg = Message.obtain();
                        msg.obj = bitmap;
                        msg.what = GET_DATA_SUCCESS;
                        handler.sendMessage(msg);
                        inputStream.close();
                    } else {
                        //服务启发生错误
                        handler.sendEmptyMessage(SERVER_ERROR);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    //网络连接错误
                    handler.sendEmptyMessage(NETWORK_ERROR);
                }
            }
        }.start();
    }

    //使用缓存图片
    public void useCacheImage() {
        //创建路径一样的文件
        File file = new File(getContext().getCacheDir(), getURLPath());
        //判断文件是否存在
        if (file != null && file.length() > 0) {
            //使用本地图片
            try {
                InputStream inputStream = new FileInputStream(file);
                //调用压缩方法显示图片
                Bitmap bitmap = getCompressBitmap(inputStream);
                //利用Message把图片发给Handler
                Message msg = Message.obtain();
                msg.obj = bitmap;
                msg.what = GET_DATA_SUCCESS;
                handler.sendMessage(msg);
                Log.e("MyImageView","使用缓存图片");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            //使用网络图片
            useNetWorkImage();
            Log.e("MyImageView","使用网络图片");
        }
    }

    /**
     * 缓存网络的图片
     * @param inputStream 网络的输入流
     */
    public void cacheImage(InputStream inputStream) {
        try {
            File file = new File(getContext().getCacheDir(), getURLPath());
            FileOutputStream fos = new FileOutputStream(file);
            int len;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            Log.e("MyImageView","缓存成功");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MyImageView","缓存失败");
        }
    }

    /**
     * 根据网址生成一个文件名
     * @return 文件名
     */
    public String getURLPath() {
        StringBuilder urlStr2 = new StringBuilder();
        String[] strings = imagePath.split("\\/");
        for (String string : strings) {
            urlStr2.append(string);
        }
        Log.e("MyImageView","文件名："+urlStr2.toString());
        return urlStr2.toString();
    }


    /**
     * 根据输入流返回一个压缩的图片
     *
     * @param input 图片的输入流
     * @return 压缩的图片
     */
    public Bitmap getCompressBitmap(InputStream input) {
        //因为InputStream要使用两次，但是使用一次就无效了，所以需要复制两个
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //复制新的输入流
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        InputStream is2 = new ByteArrayInputStream(baos.toByteArray());

        //只是获取网络图片的大小，并没有真正获取图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);
        //获取图片并进行压缩
        options.inSampleSize = getInSampleSize(options);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(is2, null, options);
    }

    /**
     * 获得需要压缩的比率
     *
     * @param options 需要传入已经BitmapFactory.decodeStream(is, null, options);
     * @return 返回压缩的比率，最小为1
     */
    public int getInSampleSize(BitmapFactory.Options options) {
        int inSampleSize = 1;
        int realWith = realImageViewWith();
        int realHeight = realImageViewHeight();

        int outWidth = options.outWidth;
        Log.e("网络图片实际的宽度", String.valueOf(outWidth));
        int outHeight = options.outHeight;
        Log.e("网络图片实际的高度", String.valueOf(outHeight));

        //获取比率最大的那个
        if (outWidth > realWith || outHeight > realHeight) {
            int withRadio = Math.round(outWidth / realWith);
            int heightRadio = Math.round(outHeight / realHeight);
            inSampleSize = withRadio > heightRadio ? withRadio : heightRadio;
        }
        Log.e("压缩比率", String.valueOf(inSampleSize));
        return inSampleSize;
    }


    /**
     * 获取ImageView实际的宽度
     *
     * @return 返回ImageView实际的宽度
     */
    public int realImageViewWith() {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        ViewGroup.LayoutParams layoutParams = getLayoutParams();

        //如果ImageView设置了宽度就可以获取实在宽带
        int width = getWidth();
        if (width <= 0) {
            //如果ImageView没有设置宽度，就获取父级容器的宽度
            width = layoutParams.width;
        }
        if (width <= 0) {
            //获取ImageView宽度的最大值
            width = getMaxWidth();
        }
        if (width <= 0) {
            //获取屏幕的宽度
            width = displayMetrics.widthPixels;
        }
        Log.e("ImageView实际的宽度", String.valueOf(width));
        return width;
    }

    /**
     * 获取ImageView实际的高度
     *
     * @return 返回ImageView实际的高度
     */
    public int realImageViewHeight() {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        ViewGroup.LayoutParams layoutParams = getLayoutParams();

        //如果ImageView设置了高度就可以获取实在宽度
        int height = getHeight();
        if (height <= 0) {
            //如果ImageView没有设置高度，就获取父级容器的高度
            height = layoutParams.height;
        }
        if (height <= 0) {
            //获取ImageView高度的最大值
            height = getMaxHeight();
        }
        if (height <= 0) {
            //获取ImageView高度的最大值
            height = displayMetrics.heightPixels;
        }
        Log.e("ImageView实际的高度", String.valueOf(height));
        return height;
    }
// 转换图片成圆形
//   * @param bitmap
//	 传入Bitmap对象
//	 * @return

    public Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;

            left = 0;
            top = 0;
            right = width;
            bottom = width;

            height = width;

            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;

            float clip = (width - height) / 2;

            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;

            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);// 设置画笔无锯齿

        canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas

        // 以下有两种方法画圆,drawRounRect和drawCircle
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
        // canvas.drawCircle(roundPx, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
        canvas.drawBitmap(bitmap, src, dst, paint); // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle

        return output;
    }
}