//package com.work.mtmessenger.widgets.xlistview.manage;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.text.TextUtils;
//
//
///**
// * 保存用户信息的管理类
// * Created by libin
// */
//
//public class UserManage {
//
//    private static UserManage instance;
//
//    private UserManage() {
//    }
//
//    public static UserManage getInstance() {
//        if (instance == null) {
//            instance = new UserManage();
//        }
//        return instance;
//    }
//
//
//    /**
//     * 保存自动登录的用户信息
//     */
//    public void saveUserInfo(Context context, String username, String password, String uid) {
//        SharedPreferences sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);//Context.MODE_PRIVATE表示SharePrefences的数据只有自己应用程序能访问。
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString("USER_NAME", username);
//        editor.putString("PASSWORD", password);
//        editor.putString("ID", uid);
//        editor.commit();
//    }
//
//
//    /**
//     * 获取用户信息model
//     *
//     * @param context
//     * @param
//     * @param
//     */
//    public UserInfo getUserInfo(Context context) {
//        SharedPreferences sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
//        UserInfo userInfo = new UserInfo();
//        userInfo.setUserName(sp.getString("USER_NAME", ""));
//        userInfo.setPassword(sp.getString("PASSWORD", ""));
//        userInfo.setId(sp.getString("ID", ""));
//        return userInfo;
//    }
//
//
//    /**
//     * userInfo中是否有数据
//     */
//    public boolean hasUserInfo(Context context) {
//        UserInfo userInfo = getUserInfo(context);
//        if (userInfo != null) {
//            if ((!TextUtils.isEmpty(userInfo.getUserName())) && (!TextUtils.isEmpty(userInfo.getPassword()))&& (!TextUtils.isEmpty(userInfo.getId()))) {//有数据
//                return true;
//            } else {
//                return false;
//            }
//        }
//        return false;
//    }
//
//}
