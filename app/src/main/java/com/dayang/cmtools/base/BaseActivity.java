package com.dayang.cmtools.base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.dayang.cmtools.utils.AppManager;

/**
 * Created by 冯傲 on 2017/3/16.
 * e-mail 897840134@qq.com
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().addActivity(this);
    }
}
