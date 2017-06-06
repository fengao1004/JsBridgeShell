package com.dayang.browsemediafileslibrary.activity;

import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dayang.browsemediafileslibrary.R;
import com.dayang.browsemediafileslibrary.adapter.FreagmentAdapter;
import com.dayang.browsemediafileslibrary.fragment.ImageFragment;
import com.dayang.browsemediafileslibrary.fragment.PlayAudioFragment;
import com.dayang.browsemediafileslibrary.fragment.PlayVideoFragment;
import com.dayang.browsemediafileslibrary.util.TypeUtils;

import java.util.ArrayList;

public class BrowseActivity extends AppCompatActivity {

    private ViewPager vp_browse_view_pager;
    private LinearLayout ll_points;
    private ArrayList<String> pathList;
    private ArrayList<String> thumbnailPathList;
    private ArrayList<Fragment> fragments;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        vp_browse_view_pager = (ViewPager) findViewById(R.id.vp_browse_view_pager);
        ll_points = (LinearLayout) findViewById(R.id.ll_points);
        Bundle bundle = getIntent().getExtras();
        pathList = bundle.getStringArrayList("pathList");
        thumbnailPathList = bundle.getStringArrayList("thumbnailPathList");
        index = bundle.getInt("index");
        init();
    }

    private void init() {
        fragments = new ArrayList<>();
        Point point = new Point();
        getWindow().getWindowManager().getDefaultDisplay().getSize(point);
        float screenWidth = point.x;
        float screenHeight = point.y;
        for (int i = 0; i < pathList.size(); i++) {
            int fileType = TypeUtils.getFileType(pathList.get(i));
            if (fileType == TypeUtils.IMAGE) {
                fragments.add(ImageFragment.newInstance(pathList.get(i), thumbnailPathList.get(i), screenWidth, screenHeight));
            } else if (fileType == TypeUtils.ADIOU) {
                fragments.add(PlayAudioFragment.newInstance(pathList.get(i)));
            } else if (fileType == TypeUtils.VIDIO) {
                fragments.add(PlayVideoFragment.newInstance(pathList.get(i)));
            }
        }
        vp_browse_view_pager.setAdapter(new FreagmentAdapter(getSupportFragmentManager(), fragments));
        vp_browse_view_pager.setCurrentItem(index, false);
        vp_browse_view_pager.setOffscreenPageLimit(2);
        initPoints();
    }

    private void initPoints() {
        int fileType = TypeUtils.getFileType(pathList.get(index));
        if (fileType != TypeUtils.IMAGE) {
            ll_points.setVisibility(View.GONE);
        } else {
            ll_points.setVisibility(View.VISIBLE);
        }
        ll_points.removeAllViews();
        for (int i = 0; i < pathList.size(); i++) {
            ll_points.addView(getImageView());
        }
        View childAt = ll_points.getChildAt(index);
        childAt.setBackgroundResource(R.drawable.shape_point_light);
        vp_browse_view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int fileType = TypeUtils.getFileType(pathList.get(position));
                if (fileType != TypeUtils.IMAGE) {
                    ll_points.setVisibility(View.GONE);
                } else {
                    ll_points.setVisibility(View.VISIBLE);
                }
                ll_points.getChildAt(position).setBackgroundResource(R.drawable.shape_point_light);
                for (int i = 0; i < ll_points.getChildCount(); i++) {
                    if (i == position)
                        continue;
                    ll_points.getChildAt(i).setBackgroundResource(R.drawable.shape_point_dark);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public ImageView getImageView() {
        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20, 0, 20, 0);
        imageView.setLayoutParams(layoutParams);
        imageView.setBackgroundResource(R.drawable.shape_point_dark);
        return imageView;
    }
}
