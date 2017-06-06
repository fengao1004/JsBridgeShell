package com.dayang.browsemediafileslibrary.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dayang.browsemediafileslibrary.activity.BrowseActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

/**
 * Created by 冯傲 on 2017/3/20.
 * e-mail 897840134@qq.com
 */

public class BrowseManager {
    static Context context;
    static BrowseManager manager;

    private BrowseManager() {

    }

    public static BrowseManager getInstance() {
        if (manager == null) {
            manager = new BrowseManager();
        }
        return manager;
    }

    public void init(Context context1) {
        context = context1;
        boolean inited = ImageLoader.getInstance().isInited();
        if (!inited) {
            ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);
            ImageLoader.getInstance().init(configuration);
        }
    }

    public void browse(Activity activity, ArrayList<String> pathList, ArrayList<String> thumbnailPathList, int index) {
        Intent intent = new Intent(activity, BrowseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("pathList", pathList);
        bundle.putStringArrayList("thumbnailPathList", thumbnailPathList);
        bundle.putInt("index", index);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }
}
