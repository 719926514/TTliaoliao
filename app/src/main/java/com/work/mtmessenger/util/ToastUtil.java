package com.work.mtmessenger.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import java.util.List;

/**
 * @desc: 作用描述 提示工具类
 * @projectName:Future
 * @author:xuwh
 * @date:2019/3/20 9:38
 * @UpdateUser： 更新者
 * @UpdateDate: 2019/3/20 9:38
 * @UpdateRemark: 更新说明
 * @version:
 */
public class ToastUtil {

    private static Toast mToast;
    private static CharSequence lastText = "";

    public static void show(Context context, CharSequence info) {
        if (mToast == null) {
            if (context instanceof Activity) {
                context = context.getApplicationContext();
            }
            mToast = Toast.makeText(context, info, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(info);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        if (!lastText.equals(info)) {// 相同内容不弹出
            mToast.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    lastText = "";// 清除前一次弹出文字信息，这样下次有同意信息才会弹出
                }
            }, 1000);
        }
        lastText = info;
    }

    public static void show(Context context, int info) {
        show(context, context.getResources().getString(info));
    }





    /**
     *判断当前应用程序处于前台还是后台
     */
    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }
}
