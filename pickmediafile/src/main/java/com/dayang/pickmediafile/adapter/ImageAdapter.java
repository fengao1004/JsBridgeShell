package com.dayang.pickmediafile.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.dayang.pickmediafile.R;
import com.dayang.pickmediafile.common.PickFileManager;
import com.dayang.pickmediafile.presenter.PickMediaFilePresenterInterface;
import com.dayang.pickmediafile.util.ThumbnailUtil;
import com.dayang.pickmediafile.util.TypeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 冯傲 on 2016/10/11.
 * e-mail 897840134@qq.com
 */
public class ImageAdapter extends RecyclerView.Adapter<MyHolder> {
    private final PickMediaFilePresenterInterface presenterInterface;
    Activity activity;
    int width;
    private final List<String> files;
    private final PickFileManager.OnClickFileListener playMediaFileListener;

    public ImageAdapter(List<String> files, Activity activity, PickMediaFilePresenterInterface presenterInterface) {
        this.files = files;
        this.activity = activity;
        this.presenterInterface = presenterInterface;
        playMediaFileListener = PickFileManager.getInstance().onClickFileListener;
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_file_list, parent, false);
        return new MyHolder(view);
    }


    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        holder.ima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playMediaFileListener != null) {
                    String s = files.get(position);
                    Log.i("fengao", "onClick: "+s);
                    playMediaFileListener.onClickFile(files.get(position));
                }
            }
        });
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.ima.getLayoutParams();
        layoutParams.width = width / 3;
        layoutParams.height = width / 3;
        holder.ima.setLayoutParams(layoutParams);
        holder.ima.setImageResource(R.drawable.white);
        final int index = position;
        int fileType = TypeUtils.getFileType(files.get(position));
        if (fileType == TypeUtils.ADIOU) {
            String[] split = files.get(position).split("/");
            holder.adiouName.setText(split[split.length - 1]);
            holder.adiouName.setVisibility(View.VISIBLE);
        } else {
            holder.adiouName.setVisibility(View.GONE);
        }
        if (fileType == TypeUtils.VIDIO) {
            holder.play.setVisibility(View.VISIBLE);
        } else {
            holder.play.setVisibility(View.GONE);
        }
        ThumbnailUtil.displayThumbnail(holder.ima, files.get(position));
        holder.che.setChecked(presenterInterface.isHasFile(files.get(index)));
        holder.che.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean b = presenterInterface.addMediaFile(files.get(index));
                Log.i("fengao", "onClick: "+files.get(index));
                holder.che.setChecked(b);
            }
        });
    }

    @Override
    public int getItemCount() {
        return files.size();
    }


}
