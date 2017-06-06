package com.dayang.cmtools.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;


import java.util.ArrayList;

/**
 * Created by 冯傲 on 2017/2/22.
 * e-mail 897840134@qq.com
 * 需要再activity的onRequestPermissionsResult 中调用onRequestPermissionsResult
 */

public class PermissionUtil {
    Activity activity;
    private PermissionListener listener;
    private int requestCode;
    boolean requesting = false;

    public PermissionUtil(Activity activity) {
        this.activity = activity;
    }

    public void checkPermission(String[] permission, final PermissionListener listener) {
        requesting = true;
        this.listener = listener;
        this.requestCode = (int) (Math.random() * 100);
        ArrayList<String> permissionList = new ArrayList<>();
        for (int i = 0; i < permission.length; i++) {
            if (ContextCompat.checkSelfPermission(activity, permission[i]) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission[i]);
            }
        }
        if (permissionList.size() == 0) {
            listener.permissionAllowed();
        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                listener.permissionRefused();
                return;
            }
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (this.requestCode == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                listener.permissionAllowed();
            } else {
                listener.permissionRefused();
            }
        }
    }

    public interface PermissionListener {
        void permissionAllowed();

        void permissionRefused();
    }
}

