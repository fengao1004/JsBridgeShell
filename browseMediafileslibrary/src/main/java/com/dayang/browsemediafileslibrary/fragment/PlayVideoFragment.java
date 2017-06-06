package com.dayang.browsemediafileslibrary.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import com.dayang.browsemediafileslibrary.R;
import com.dayang.browsemediafileslibrary.util.CommonUtil;
import com.dayang.browsemediafileslibrary.view.CustomMediaController;
import com.dayang.browsemediafileslibrary.view.CustomVideoView;

/**
 * Created by 冯傲 on 2017/3/21.
 * e-mail 897840134@qq.com
 */

public class PlayVideoFragment extends Fragment {

    private String path;
    private Activity activity;
    private static final String TAG = "fengao";
    private CustomVideoView vv_player;// 自定义的视频播放控件
    private CustomMediaController mediaController;// 自定义的视频播放控件控制器
    private RelativeLayout rl_load;// 视频缓冲层
    private RelativeLayout iv_error;// 视频错误层

    public static PlayVideoFragment newInstance(String path) {
        PlayVideoFragment newFragment = new PlayVideoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        path = getArguments().getString("path");
        View inflate = inflater.inflate(R.layout.fragment_browse_video, container, false);
        vv_player = (CustomVideoView) inflate.findViewById(R.id.vv_player);
        rl_load = (RelativeLayout) inflate.findViewById(R.id.rl_load);
        iv_error = (RelativeLayout) inflate.findViewById(R.id.iv_error);
        init();
        return inflate;
    }

    private void init() {
        vv_player.setVisibility(View.VISIBLE);// 显示视频控件
        iv_error.setVisibility(View.GONE);// 开始不显示错误层
        rl_load.setVisibility(View.VISIBLE);// 显示视频加载
        Window window = activity.getWindow();
        mediaController = new CustomMediaController(activity,
                true, window);// 绑定控制器
        mediaController.setFullScreenEnable(true);
        mediaController.setOnFullScreenListener(new CustomMediaController.onFullScreenListener() {
            @Override
            public void onFullScreen(View v) {
                // 横竖屏切换时，会重走ListFragment的生命周期，缓存任务类型，为重走生命周期刷新数据准备
                if (CommonUtil.isScreenOriatationPortrait(activity)) {// 当屏幕是竖屏时
                    // 点击后变横屏
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 设置当前activity为横屏
                } else {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 设置当前activity为竖屏
                }
            }
        });
        vv_player.setMediaController(mediaController);// 视频准备好监听
        vv_player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.i(TAG, "onPrepared: ");
                rl_load.setVisibility(View.GONE);// 取消视频加载
            }
        });
        // 视频播放错误监听
        vv_player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                iv_error.setVisibility(View.VISIBLE);// 显示错误层
                vv_player.setVisibility(View.GONE);// 隐藏视频空间
                rl_load.setVisibility(View.GONE);// 取消视频加载
                return false;
            }
        });
        vv_player.setVideoPath(path);
        Log.i(TAG, "init完毕");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(!isVisibleToUser&&mediaController!=null){
            mediaController.hide();
            vv_player.pause();
        }else if(mediaController!=null){
            mediaController.show();
        }
    }
}
