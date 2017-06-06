package com.dayang.cmtools.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dayang.cmtools.R;
import com.dayang.cmtools.utils.JsonUtils;
import com.dayang.cmtools.utils.LogUtils;
import com.dayang.cmtools.widget.activity.MessageActivity;
import com.google.gson.JsonObject;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;

import java.util.Date;

/**
 * Created by 冯傲 on 2017/4/6.
 * e-mail 897840134@qq.com
 */

public class GTReceiveMessageService extends GTIntentService {
    @Override
    public void onReceiveServicePid(Context context, int i) {

    }

    @Override
    public void onReceiveClientId(Context context, String s) {
        LogUtils.i("onReceiveClientId: " + s);
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage gtTransmitMessage) {
        LogUtils.i("onReceiveMessageData: " + new String(gtTransmitMessage.getPayload()));
        String data = new String(gtTransmitMessage.getPayload());
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        Intent mainIntent ;
        mainIntent = new Intent(context, MessageActivity.class);
        mainIntent.putExtra("content", data);
        Log.i(TAG, "onReceive: " + data + "   " + new Date().getTime());
        String content = JsonUtils.getString(data, "content");
        int id = (int) (new Date().getTime() % 100000);
        mBuilder.setContentTitle("记者助手")//设置通知栏标题
                .setContentText(content) ///< span style = "font-family: Arial;" >/设置通知栏显示内容</span >
                .setContentIntent(PendingIntent.getActivity(context, id, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT)) //设置通知栏点击意图 id是用来区分intent的 防止多个通知覆盖intent
                .setGroupSummary(true)
                .setTicker("记者助手") //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                .setSmallIcon(R.drawable.ic_launcher);//设置通知小ICON
        mNotificationManager.notify(id, mBuilder.build());
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {

    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {
    }

}
