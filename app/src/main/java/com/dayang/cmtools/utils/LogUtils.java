package com.dayang.cmtools.utils;

import android.util.Log;

/**
 * Created by 冯傲 on 2017/3/13.
 * e-mail 897840134@qq.com
 */

public class LogUtils {
    public static boolean allowPrint = true;
    public static  String TAG = "";

    public static void e(String log) {
        if(allowPrint){
            Log.e(TAG, log);
        }
    }
    public static void e(String TAG,String log) {
        if(allowPrint){
            Log.e(TAG, log);
        }
    }
    public static void d(String log) {
        if(allowPrint){
            Log.d(TAG, log);
        }
    }
    public static void d(String TAG,String log) {
        if(allowPrint){
            Log.d(TAG, log);
        }
    }
    public static void i(String log) {
        if(allowPrint){
            Log.i(TAG, log);
        }
    }
    public static void i(String TAG,String log) {
        if(allowPrint){
            Log.i(TAG, log);
        }
    }
    public static void w(String log) {
        if(allowPrint){
            Log.w(TAG, log);
        }
    }
    public static void w(String TAG,String log) {
        if(allowPrint){
            Log.w(TAG, log);
        }
    }

}
