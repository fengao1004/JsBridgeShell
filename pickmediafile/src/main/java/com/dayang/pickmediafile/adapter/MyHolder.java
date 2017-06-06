package com.dayang.pickmediafile.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.dayang.pickmediafile.R;

/**
 * Created by 冯傲 on 2017/3/18.
 * e-mail 897840134@qq.com
 */

public class MyHolder extends RecyclerView.ViewHolder {
    public ImageView ima;
    public ImageView play;
    public TextView adiouName;

    public CheckBox che;

    public MyHolder(View itemView) {
        super(itemView);
        ima = (ImageView) itemView.findViewById(R.id.image_file);
        che = (CheckBox) itemView.findViewById(R.id.cb);
        play = (ImageView) itemView.findViewById(R.id.iv_play);
        adiouName = (TextView) itemView.findViewById(R.id.tv_audioname);
    }

}
