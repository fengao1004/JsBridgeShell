package com.dayang.cmtools.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.dayang.cmtools.R;
import com.dayang.cmtools.bean.ApkUpdateInfo;
import com.dayang.cmtools.dialog.UpdateDialog;
import com.dayang.cmtools.service.UpdateService;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 冯傲 on 2017/1/5.
 * e-mail 897840134@qq.com
 */

public class ApkUpdateUtil {
    public static final int AUTOUPDATE = 1;
    public static final int NORMALUPDATE = 2;
    final int NEEDUPDATA = 1425;
    final int NOTNEEDUPDATA = 14425;
    final int Error = -111;
    public static int updateMode;
    private Activity activity;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NEEDUPDATA:
                    update((Object[]) msg.obj);
                    break;
                case NOTNEEDUPDATA:
                    if (msg.what == NOTNEEDUPDATA && updateMode == ApkUpdateUtil.NORMALUPDATE) {
                        Toast.makeText(activity, "当前已是最新版本", 0).show();
                    } else if (msg.what == Error && updateMode == ApkUpdateUtil.NORMALUPDATE) {
                        Toast.makeText(activity, "获取最新版本失败,请稍后再试", 0).show();
                    }
            }
        }
    };
    private String checkUrl = "http://appservice.dayang.com/versionupdate/version.json";

    public ApkUpdateUtil(Activity activity) {
        this.activity = activity;
    }

    /**
     * @param handler
     * @return 检查当前版本是否是最新版
     */
    public void checkApkVersion() {
        //TODO 更换新的http请求框架
        OkHttpUtil okHttpUtil = new OkHttpUtil();
        okHttpUtil.callGet(checkUrl, new OkHttpUtil.OkHttpCallBack() {
            @Override
            public void success(Response response) throws Exception {
                boolean needUpdate = false;
                String string = response.body().string();
                ApkUpdateInfo info = GsonUtils.fromJson(string, ApkUpdateInfo.class);
                String version = info.getVersion();
                String filePath = info.getFilePath();
                List<String> updateLog = info.getDescription();
                String localVersion = getAppVersionName(activity);
                int localVersionCode = getVersionCodeFromVersionName(localVersion);
                int netVersionCode = getVersionCodeFromVersionName(version);
                if (localVersionCode != 0) {
                    if (netVersionCode > localVersionCode) {
                        needUpdate = true;
                    }
                } else {
                    needUpdate = true;
                }
                if (needUpdate) {
                    Message mes = new Message();
                    mes.what = NEEDUPDATA;
                    mes.obj = new Object[]{filePath, updateLog};
                    handler.sendMessage(mes);
                } else {
                    Message mes = new Message();
                    mes.what = NOTNEEDUPDATA;
                    handler.sendMessage(mes);
                }
            }

            @Override
            public void error(Request request, IOException e) {
                Message mes = new Message();
                mes.what = Error;
                handler.sendMessage(mes);
            }
        });
    }

    private int getVersionCodeFromVersionName(String localVersion) {
        if (localVersion.equals("")) {
            return 0;
        }
        String[] split = localVersion.split("\\.");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            sb.append(split[i]);
        }
        try {
            int i = Integer.parseInt(sb.toString());
            return i;
        } catch (Exception e) {
            LogUtils.i("getVersionCodeFromVersionName: 版本号格式异常");
        }
        return 0;
    }

    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    private void update(Object[] obj) {
        final String path = (String) obj[0];
        List<String> description = (List<String>) obj[1];
        final UpdateDialog.Builder builder = new UpdateDialog.Builder(activity);
        builder.setContant("发现新版本是否更新");
        builder.setTitle("更新提示");
        final UpdateDialog dialog = builder.create();
        builder.setUpdateLog(description);
        builder.setOnClick(new UpdateDialog.onUpClickListener() {
            @Override
            public void onEnterClick() {
                Intent intent = new Intent(activity, UpdateService.class);
                intent.putExtra("app_name", R.string.app_name);
                intent.putExtra("downurl", path);
                activity.startService(intent);
                dialog.dismiss();
            }

            @Override
            public void onCancelClick() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
