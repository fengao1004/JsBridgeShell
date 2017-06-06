package com.dayang.cmtools.presenter.impl;

import android.widget.Toast;

import com.dayang.cmtools.bean.MeetingListInfo;
import com.dayang.cmtools.model.MeetingListModel;
import com.dayang.cmtools.presenter.MeetingListPresenterInterface;
import com.dayang.cmtools.widget.MeetingListViewInterface;

import java.util.ArrayList;

/**
 * Created by 冯傲 on 2017/3/22.
 * e-mail 897840134@qq.com
 */

public class MeetingListPresenterImpl implements MeetingListPresenterInterface {
    private final MeetingListViewInterface meetingListViewInterface;

    @Override
    public void loadMeetingListData() {
        new MeetingListModel().getData(new MeetingListModel.DataListener() {
            @Override
            public void callData(int code, ArrayList<MeetingListInfo> list) {
               meetingListViewInterface.setListData(code,list);
            }
        });
    }

    public MeetingListPresenterImpl(MeetingListViewInterface meetingListViewInterface) {
        this.meetingListViewInterface = meetingListViewInterface;
    }
}
