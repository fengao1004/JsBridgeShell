package com.dayang.pickmediafile.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dayang.pickmediafile.R;
import com.dayang.pickmediafile.adapter.ImageAdapter;
import com.dayang.pickmediafile.bean.FileDir;
import com.dayang.pickmediafile.dialog.ActionSheetDialog;
import com.dayang.pickmediafile.presenter.PickMediaFilePresenterImpl;

import java.util.ArrayList;
import java.util.List;

public class PickMediaFileActivity extends AppCompatActivity implements PickMediaFileViewInterface {

    private RecyclerView rc_files;
    private TextView tv_complete;
    private int max;
    public static final int FILE_SELECT_RESULT = 2346;
    int filesNum = 0;
    private PickMediaFilePresenterImpl pickMediaFilePresenter;
    private TextView preview;
    private TextView tv_dirname;
    private ArrayList<FileDir> list;
    private ProgressBar choose_progress;
    private ActionSheetDialog dialog;
    private List<String> fileList;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_media_file);
        max = getIntent().getIntExtra("imgNum", 1);
        tv_complete = (TextView) findViewById(R.id.tv_complete);
        tv_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filesNum == 0) {
                    return;
                } else {
                    pickMediaFilePresenter.setResult(PickMediaFileActivity.this, FILE_SELECT_RESULT);
                }
            }
        });
        preview = (TextView) findViewById(R.id.yl);
        rc_files = (RecyclerView) findViewById(R.id.rc);
        rc_files.setItemViewCacheSize(100);
        choose_progress = (ProgressBar) findViewById(R.id.choose_progress);
        tv_dirname = (TextView) findViewById(R.id.tv_dirname);
        choose_progress.setVisibility(View.VISIBLE);
        rc_files.setVisibility(View.GONE);
        rc_files.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        fileList = new ArrayList<>();
        refreshCompleteButton(0);
        pickMediaFilePresenter = new PickMediaFilePresenterImpl(this);
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickMediaFilePresenter.preview();
            }
        });
    }

    @Override
    public void refreshRecyclerview(FileDir files) {
        rc_files.setAdapter(new ImageAdapter(files.files, this, pickMediaFilePresenter));
    }

    @Override
    public void showLoading() {
        choose_progress.setVisibility(View.VISIBLE);
        rc_files.setVisibility(View.GONE);
    }

    @Override
    public void removeLoading() {
        choose_progress.setVisibility(View.GONE);
        rc_files.setVisibility(View.VISIBLE);
    }

    @Override
    public void setDirNames(ArrayList<FileDir> list) {
        this.list = list;
        initDialog();
        tv_dirname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    private void initDialog() {
        dialog = new ActionSheetDialog(list, PickMediaFileActivity.this).builder();
        dialog.setOnClickListener(new ActionSheetDialog.OnClickListener() {
            @Override
            public void onClick(int position) {
                pickMediaFilePresenter.showDir(position);
            }
        });
    }

    @Override
    public int getMaxFileNum() {
        return max;
    }

    @Override
    public void refreshCompleteButton(int filesNum) {
        this.filesNum = filesNum;
        if (filesNum == 0) {
            preview.setText("预览");
            tv_complete.setBackground(getResources().getDrawable(R.drawable.shap_complete_n));
            preview.setTextColor(Color.parseColor("#77FFFFFF"));
            tv_complete.setText("完成");
            tv_complete.setTextColor(Color.parseColor("#77FFFFFF"));
        } else {
            preview.setText(filesNum + "/" + max + "预览");
            preview.setTextColor(Color.parseColor("#FFFFFF"));
            tv_complete.setText(filesNum + "/" + max + "完成");
            tv_complete.setBackground(getResources().getDrawable(R.drawable.shap_complete_y));
            tv_complete.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    public void back(View v) {
        finish();
    }

}
