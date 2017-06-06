package com.dayang.pickmediafile.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.dayang.pickmediafile.view.PickMediaFileActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

/**
 * Created by 冯傲 on 2017/3/18.
 * e-mail 897840134@qq.com
 */

public class PickFileManager {
    public OnClickFileListener onClickFileListener;
    public static PickFileManager instance;
    public ResultListener resultListener;
    public PreviewListener previewListener;

    public void init(Context context) {
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);
        ImageLoader.getInstance().init(configuration);
    }

    public static PickFileManager getInstance() {
        if (instance == null) {
            instance = new PickFileManager();
        }
        return instance;
    }

    public void pickFile(Activity activity, int max, int requestCode) {
        this.resultListener = null;
        Intent intent = new Intent(activity, PickMediaFileActivity.class);
        intent.putExtra("imgNum", max);
        activity.startActivityForResult(intent, requestCode);
    }

    public void pickFile(Activity activity, int max, ResultListener resultListener) {
        this.resultListener = resultListener;
        Intent intent = new Intent(activity, PickMediaFileActivity.class);
        intent.putExtra("imgNum", max);
        activity.startActivity(intent);
    }

    public void setPreviewListener(PreviewListener previewListener) {
        this.previewListener = previewListener;
    }
    public void setOnclickListener(OnClickFileListener onClickFileListener) {
        this.onClickFileListener = onClickFileListener;
    }
    public interface OnClickFileListener {
        void onClickFile(String filePath);
    }

    public interface ResultListener {
        void playMediaFileEnd(ArrayList<String> pathList);
    }

    public interface PreviewListener {
        void preview(ArrayList<String> pathList);
    }
}
