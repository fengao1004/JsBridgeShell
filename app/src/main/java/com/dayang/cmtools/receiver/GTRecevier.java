package com.dayang.cmtools.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dayang.cmtools.R;
import com.dayang.cmtools.utils.JsonUtils;
import com.dayang.cmtools.widget.activity.MessageActivity;
import com.dayang.cmtools.widget.activity.WebViewActivity;
import com.igexin.sdk.PushConsts;

import java.util.Date;

/**
 * Created by 冯傲 on 2016/8/20.
 * e-mail 897840134@qq.com
 */
public class GTRecevier extends BroadcastReceiver {
    private static final String TAG = "fengao";

    public static void setSdk_activity(WebViewActivity sdk_activity) {
        GTRecevier.sdk_activity = sdk_activity;
    }

    public static WebViewActivity sdk_activity = null;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        this.context = context;
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_CLIENTID:
                break;
            case PushConsts.GET_MSG_DATA:
                final byte[] payload = bundle.getByteArray("payload");
                String data = new String(payload);
                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
                Intent mainIntent = null;
                mainIntent = new Intent(context, MessageActivity.class);
                mainIntent.putExtra("content", data);
                Log.i(TAG, "onReceive: " + data + "   " + new Date().getTime());
                String content = JsonUtils.getString(data,"content");
                int id = (int) (new Date().getTime() % 100000);
                mBuilder.setContentTitle("记者助手")
                        .setContentText(content)
                        .setContentIntent(PendingIntent.getActivity(context, id, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                        .setGroupSummary(true)
                        .setTicker("记者助手")
                        .setWhen(System.currentTimeMillis())
                        .setPriority(Notification.PRIORITY_DEFAULT)
                        .setAutoCancel(true)
                        .setOngoing(false)
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                        .setSmallIcon(R.drawable.ic_launcher);
                mNotificationManager.notify(id, mBuilder.build());
                break;
            default:
                break;
        }

    }
}
