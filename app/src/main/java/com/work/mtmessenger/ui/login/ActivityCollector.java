package com.work.mtmessenger.ui.login;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhenQi on 2016/12/27.
 * Activity的管理类，方面统一管理Activity
 */

public class ActivityCollector {

    private static List<Activity> activityList = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    public static Activity getTopActivity() {
        if (activityList.isEmpty()) {
            return null;
        } else {
            return activityList.get(activityList.size() - 1);
        }
    }

}
