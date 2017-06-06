package com.dayang.pickmediafile.view;

import android.content.Context;

import com.dayang.pickmediafile.bean.FileDir;

import java.util.ArrayList;

/**
 * Created by 冯傲 on 2017/3/17.
 * e-mail 897840134@qq.com
 */

public interface PickMediaFileViewInterface {
    void refreshRecyclerview(FileDir files);

    void showLoading();

    void removeLoading();

    void setDirNames(ArrayList<FileDir> list);

    int getMaxFileNum();

    void refreshCompleteButton(int filesNum);

    Context getContext();
}
