package com.dayang.cmtools.application;

import android.app.Application;

import com.dayang.cmtools.service.GTReceiveMessageService;
import com.dayang.cmtools.service.GTService;
import com.dayang.cmtools.utils.LogUtils;
import com.dayang.pickmediafile.common.PickFileManager;
import com.igexin.sdk.PushManager;

/**
 * Created by 冯傲 on 2017/3/13.
 * e-mail 897840134@qq.com
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.TAG = "fengao";
        LogUtils.allowPrint = true;
        PickFileManager.getInstance().init(this);
        PushManager.getInstance().initialize(this.getApplicationContext(), GTService.class);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), GTReceiveMessageService.class);
    }
}
