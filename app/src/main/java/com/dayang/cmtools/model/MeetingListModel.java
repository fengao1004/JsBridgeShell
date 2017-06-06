package com.dayang.cmtools.model;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.dayang.cmtools.bean.MeetingListInfo;
import com.dayang.cmtools.bean.MeetingListResponseInfo;
import com.dayang.cmtools.bean.PortalResponseInfo;
import com.dayang.cmtools.utils.OkHttpUtil;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 冯傲 on 2017/3/22.
 * e-mail 897840134@qq.com
 */

public class MeetingListModel {
    private MeetingListResponseInfo meetingListResponseInfo;
    String meetingListRequestUrl;
    String workNo;
    public static final int MEETIONGLISTGETSUCCESS = 123;
    public static final int EMPTYDATA = 0;
    public static final int REQUESTERROR = -1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case MEETIONGLISTGETSUCCESS:
                    ArrayList<MeetingListInfo> list = new ArrayList<>();
                    List<MeetingListResponseInfo.RecordsEntity> records = meetingListResponseInfo.getRecords();
                    for (int i = 0; i < records.size(); i++) {
                        list.add(new MeetingListInfo(records.get(i).getName(), records.get(i).getAccount(), records.get(i).getPcode()));
                    }
                    if (list.size() == 0 && dataListener != null) {
                        dataListener.callData(EMPTYDATA, null);
                    } else if (list.size() != 0 && dataListener != null) {
                        dataListener.callData(MEETIONGLISTGETSUCCESS, list);
                    }
                    break;
                case REQUESTERROR:
                    if (dataListener != null) {
                        dataListener.callData(REQUESTERROR, null);
                    }
                    break;
                case EMPTYDATA:
                    if (dataListener != null) {
                        dataListener.callData(EMPTYDATA, null);
                    }
                    break;
            }
        }
    };
    private DataListener dataListener;

    public void getData(DataListener dataListener) {
        this.dataListener = dataListener;
        getMeetingListRequestUrl();
    }

    private void getMeetingListRequestUrl() {
        if (meetingListRequestUrl.equals("")) {
            OkHttpUtil okHttpUtil = new OkHttpUtil();
            okHttpUtil.call("https://apphttps.dayang.com:9443/portal/api/projService", "{\"domainName\":\"houjian.com\",\"productCode\":\"cmtools\"}", new OkHttpUtil.OkHttpCallBack() {
                @Override
                public void success(Response response) throws Exception {
                    Gson gson = new Gson();
                    try {
                        PortalResponseInfo portalResponseInfo = gson.fromJson(response.body().string(), PortalResponseInfo.class);
                        meetingListRequestUrl = portalResponseInfo.getData().getCasservice();
                        response.body().close();
                        meetingListRequestUrl = meetingListRequestUrl + "?workNo=" + workNo;
                        if (meetingListRequestUrl.equals("")) {
                            handler.sendEmptyMessage(REQUESTERROR);
                        } else {
                            getMeetingList();
                        }
                    } catch (Exception e) {
                        handler.sendEmptyMessage(EMPTYDATA);
                    }
                }

                @Override
                public void error(Request request, IOException e) {
                    handler.sendEmptyMessage(REQUESTERROR);
                }
            });
        } else {
            getMeetingList();
        }
    }

    private void getMeetingList() {
        OkHttpUtil okHttpUtil = new OkHttpUtil();
        okHttpUtil.callGet(meetingListRequestUrl, new OkHttpUtil.OkHttpCallBack() {
            @Override
            public void success(Response response) throws Exception {
                try {
                    String json = response.body().string();
                    Gson gson = new Gson();
                    meetingListResponseInfo = gson.fromJson(json, MeetingListResponseInfo.class);
                    Message message = new Message();
                    message.what = MEETIONGLISTGETSUCCESS;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    handler.sendEmptyMessage(REQUESTERROR);
                }

            }

            @Override
            public void error(Request request, IOException e) {
                handler.sendEmptyMessage(REQUESTERROR);
            }
        });
    }

    public interface DataListener {
        void callData(int code, ArrayList<MeetingListInfo> list);
    }

}
