package com.dayang.cmtools.widget.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

public class MessageActivity extends Activity {


    private static final String TAG =  "fengao";
    private FrameLayout fl;
    public static WebViewActivity activity;

    static  public void  setActivity(WebViewActivity sdk_activit) {
        activity = sdk_activit;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String content;
        try {
            content = intent.getStringExtra("content");
        } catch (Exception e) {
            content = "";
        }
        Log.i(TAG, "onCreate: "+activity);
        finish();
        if(activity==null){
            Intent adkIntent = new Intent(this,WebViewActivity.class);
            adkIntent.putExtra("content",content);
            startActivity(adkIntent);
        }else {
            activity.enterUrl(content);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }
}
