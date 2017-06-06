package com.dayang.cmtools.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dayang.cmtools.R;
import com.dayang.cmtools.bean.MeetingListInfo;
import com.dayang.cmtools.utils.PermissionUtil;
import com.dayang.cmtools.viewholder.MeetingListHolder;

import java.util.ArrayList;

/**
 * Created by 冯傲 on 2017/3/6.
 * e-mail 897840134@qq.com
 */

public class MeetingListAdapter extends RecyclerView.Adapter<MeetingListHolder> {
    ArrayList<MeetingListInfo> list;
    Context context;

    public MeetingListAdapter(Context context, ArrayList<MeetingListInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MeetingListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_meeting_list, parent, false);
        return new MeetingListHolder(view);
    }

    @Override
    public void onBindViewHolder(MeetingListHolder holder, final int position) {
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = list.get(position).password;
                String account = list.get(position).account;
                //TODO 加入全时会议
            }
        });
        MeetingListInfo meetingListInfo = list.get(position);
        holder.text.setText("来自" + meetingListInfo.name + "的连线");
        holder.iamge.setImageResource(R.drawable.meetinglist);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
