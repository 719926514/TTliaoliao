package com.work.mtmessenger.ui.login;

import java.util.List;

/**
 * Created by zhenQi on 2016/12/27.
 * 定义回调接口，授权的方法，和拒绝的方法
 */

public interface PermissionListener {

    void onGranted();

    void onDenied(List<String> deniedPermission);

}
