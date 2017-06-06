package com.dayang.pickmediafile.presenter;

import android.app.Activity;
import android.content.Context;

/**
 * Created by 冯傲 on 2017/3/17.
 * e-mail 897840134@qq.com
 */

public interface PickMediaFilePresenterInterface {
    //for view
    void setResult(Activity activity,int Code);
    void showDir(int position);
    boolean addMediaFile(String filePath);
    boolean isHasFile(String filePath);
    void preview();
}
