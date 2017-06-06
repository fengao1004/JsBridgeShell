package com.dayang.cmtools.widget.activity;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.dayang.cmtools.R;
import com.dayang.cmtools.adapter.MeetingListAdapter;
import com.dayang.cmtools.bean.MeetingListInfo;
import com.dayang.cmtools.model.MeetingListModel;
import com.dayang.cmtools.presenter.MeetingListPresenterInterface;
import com.dayang.cmtools.presenter.impl.MeetingListPresenterImpl;
import com.dayang.cmtools.widget.MeetingListViewInterface;

import java.util.ArrayList;

public class MeetingListActivity extends AppCompatActivity implements MeetingListViewInterface, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipe_refresh_widget;
    private RecyclerView meeting_list;
    private boolean fristRequest;
    private String workNo;
    private MeetingListPresenterInterface meetingListPresenterInterface;
    boolean refreshing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_list);
        meeting_list = (RecyclerView) findViewById(R.id.meeting_list);
        swipe_refresh_widget = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_widget);
        findViewById(R.id.back_from_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        meeting_list.setLayoutManager(new LinearLayoutManager(this));
        swipe_refresh_widget.setColorSchemeResources(R.color.cmtools_blue,
                R.color.audio_background, R.color.bule_overlay
        );
        fristRequest = true;
        workNo = getIntent().getStringExtra("workNo");
        swipe_refresh_widget.setOnRefreshListener(this);
        swipe_refresh_widget.post(new Runnable() {
            @Override
            public void run() {
                swipe_refresh_widget.setRefreshing(true);
            }
        });
        meetingListPresenterInterface = new MeetingListPresenterImpl(this);
        meetingListPresenterInterface.loadMeetingListData();
        refreshing = true;
    }

    @Override
    public void setListData(int code, ArrayList<MeetingListInfo> list) {
        refreshing = false;
        if (fristRequest) {
            swipe_refresh_widget.post(new Runnable() {
                @Override
                public void run() {
                    swipe_refresh_widget.setRefreshing(false);
                }
            });
            fristRequest = false;
        } else {
            swipe_refresh_widget.setRefreshing(false);
        }
        switch (code) {
            case MeetingListModel.EMPTYDATA:
                Toast.makeText(getContext(), "暂无和您相关的连线", 0).show();
                break;
            case MeetingListModel.REQUESTERROR:
                Toast.makeText(getContext(), "暂时没有获取到连写列表请稍后再试", 0).show();
                break;
            case MeetingListModel.MEETIONGLISTGETSUCCESS:
                MeetingListAdapter meetingListAdapter = new MeetingListAdapter(this, list);
                meeting_list.setAdapter(meetingListAdapter);
                break;
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onRefresh() {
        if (refreshing) {
            return;
        }
        swipe_refresh_widget.setRefreshing(true);
        meetingListPresenterInterface.loadMeetingListData();
    }
}
