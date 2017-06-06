package com.dayang.test;

import android.app.Application;
import android.app.Instrumentation;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by 冯傲 on 2017/5/22.
 * e-mail 897840134@qq.com
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            Log.i("fengao", "1111");
            attactContext();
        } catch (Exception e) {
            Log.e("fengao", e.toString());
            e.printStackTrace();
        }
    }

    public static void attactContext() throws Exception {
        // 先获取到当前的ActivityThread对象
        Log.i("fengao", "2222");
        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
//        Field[]   fields  =   activityThreadClass.getDeclaredFields();
//        Field.setAccessible(fields,   true);
//        try{
//            for   (int   i   =   0;   i   <   fields.length;   i++)   {
//                System.out.println("fengao ^^^^^^^^^^^^^^^^^^^^^^^    "+fields[i].getName()   +   "-> ");
//            }
//        }
//        catch(Exception   e){
//            e.printStackTrace();
//        }
        Field currentActivityThreadField = activityThreadClass.getDeclaredField("sCurrentActivityThread");
        currentActivityThreadField.setAccessible(true);
        Object currentActivityThread = currentActivityThreadField.get(null);
        Log.i("fengao", "3333");
        // 拿到原始的 mInstrumentation字段
        Field mInstrumentationField = activityThreadClass.getDeclaredField("mInstrumentation");
        mInstrumentationField.setAccessible(true);
        Instrumentation mInstrumentation = (Instrumentation) mInstrumentationField.get(currentActivityThread);
        // 创建代理对象
        Instrumentation evilInstrumentation = new EvilInstrumentation(mInstrumentation);
        // 偷梁换柱
        Log.i("fengao", "4444");
        mInstrumentationField.set(currentActivityThread, evilInstrumentation);
        Log.i("fengao", "attactContext: asdasdasdad");
    }
}
