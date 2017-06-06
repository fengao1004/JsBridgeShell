package com.dayang.cmtools.widget;

import android.content.Context;

import com.dayang.cmtools.bean.MeetingListInfo;

import java.util.ArrayList;

/**
 * Created by 冯傲 on 2017/3/22.
 * e-mail 897840134@qq.com
 */

public interface MeetingListViewInterface {
    void setListData(int code, ArrayList<MeetingListInfo> list);

    Context getContext();
}
