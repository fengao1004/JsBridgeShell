package com.dayang.browsemediafileslibrary.fragment;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.dayang.browsemediafileslibrary.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.Date;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Created by 冯傲 on 2017/3/20.
 * e-mail 897840134@qq.com
 */

public class ImageFragment extends Fragment {
    private String thumbnailPath;
    private String path;
    private ImageView iv_image_thumbnail;
    private PhotoView iv_image;
    private ProgressBar progress_thumbnail_loading;
    private View inflate;
    private long timeEnd;
    private long timeStart;
    private float screenWidth;
    private float screenHeight;

    public static ImageFragment newInstance(String path, String thumbnailPath, float screenWidth, float screenHeight) {
        ImageFragment newFragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        bundle.putFloat("screenWidth", screenWidth);
        bundle.putFloat("screenHeight", screenHeight);
        bundle.putString("thumbnailPath", thumbnailPath);
        newFragment.setArguments(bundle);
        return newFragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("fengao", "onCreateView: " + (savedInstanceState == null));
        path = getArguments().getString("path");
        thumbnailPath = getArguments().getString("thumbnailPath");
        screenWidth = getArguments().getFloat("screenWidth");
        screenHeight = getArguments().getFloat("screenHeight");
        inflate = inflater.inflate(R.layout.fragment_browse_image, container, false);
        iv_image = (PhotoView) inflate.findViewById(R.id.iv_image);
        progress_thumbnail_loading = (ProgressBar) inflate.findViewById(R.id.progress_thumbnail_loading);
        iv_image_thumbnail = (ImageView) inflate.findViewById(R.id.iv_image_thumbnail);
        initImage();
        return inflate;
    }

    private void initImage() {
        iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        iv_image.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                getActivity().finish();
            }
        });
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        if (thumbnailPath!=null||!thumbnailPath.equals("")) {
            ImageLoader.getInstance().displayImage(path, iv_image_thumbnail);
        }
        if (!path.startsWith("http")) {
            path = "file://" + path;
        }
        ImageLoader.getInstance().displayImage(path, iv_image, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                timeStart = new Date().getTime();
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                loadingFailed();
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                timeEnd = new Date().getTime();
                iv_image.setVisibility(View.VISIBLE);
                iv_image_thumbnail.setVisibility(View.GONE);
                progress_thumbnail_loading.setVisibility(View.GONE);
                executeAnimation();
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });

    }

    private void executeAnimation() {
        iv_image.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final float viewWidth = iv_image.getMeasuredWidth();
                float multiple = screenWidth / viewWidth;
                ValueAnimator anim = ValueAnimator.ofFloat(1f, multiple);
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float fra = (Float) animation.getAnimatedValue();
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_image.getLayoutParams();
                        layoutParams.width = (int) (viewWidth * fra);
                        layoutParams.height = (int) (viewWidth * fra * (screenHeight / screenWidth));
                        iv_image.setLayoutParams(layoutParams);
                    }
                });
                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ImageLoader.getInstance().displayImage(path, iv_image);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                anim.setDuration(250);
                anim.start();
                iv_image.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

    }

    private void loadingFailed() {
        //TODO 加载失败
    }

}
