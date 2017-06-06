package com.dayang.pickmediafile.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.dayang.pickmediafile.bean.FileDir;
import com.dayang.pickmediafile.common.PickFileManager;
import com.dayang.pickmediafile.model.PickMediaFilesModelImpl;
import com.dayang.pickmediafile.view.PickMediaFileViewInterface;

import java.util.ArrayList;

/**
 * Created by 冯傲 on 2017/3/17.
 * e-mail 897840134@qq.com
 */

public class PickMediaFilePresenterImpl implements PickMediaFilePresenterInterface {
    private Context context;
    private PickMediaFilesModelImpl pickMediaFilesModel;
    private PickMediaFileViewInterface pickMediaFileViewImpl;
    final int LOADED = 1;
    private ArrayList<FileDir> dirs;
    private ArrayList<String> pathList;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOADED:
                    pickMediaFileViewImpl.refreshRecyclerview(dirs.get(0));
                    pickMediaFileViewImpl.setDirNames(dirs);
                    pickMediaFileViewImpl.removeLoading();
                    break;
            }
        }
    };
    private int maxFileNum;

    public PickMediaFilePresenterImpl(PickMediaFileViewInterface pickMediaFileViewInterface) {
        this.pickMediaFileViewImpl = pickMediaFileViewInterface;
        init();
    }

    private void init() {
        context = pickMediaFileViewImpl.getContext();
        maxFileNum = pickMediaFileViewImpl.getMaxFileNum();
        pickMediaFilesModel = new PickMediaFilesModelImpl(context);
        pathList = new ArrayList<>();
        pickMediaFileViewImpl.showLoading();
        new Thread() {
            @Override
            public void run() {
                super.run();
                dirs = pickMediaFilesModel.getDirs();
                handler.sendEmptyMessage(LOADED);
            }
        }.start();
    }

    @Override
    public void setResult(Activity activity, int Code) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("files", pathList);
        intent.putExtras(bundle);
        activity.setResult(Code, intent);
        activity.finish();
    }

    @Override
    public void showDir(int position) {
        pickMediaFileViewImpl.refreshRecyclerview(dirs.get(position));
    }

    @Override
    public boolean addMediaFile(String filePath) {
        if (pathList.size() + 1 > maxFileNum) {
            Toast.makeText(context, "文件已经选够了", 0).show();
            return false;
        } else if (!pathList.contains(filePath)) {
            pathList.add(filePath);
            pickMediaFileViewImpl.refreshCompleteButton(pathList.size());
            return true;
        } else {
            pathList.remove(filePath);
            pickMediaFileViewImpl.refreshCompleteButton(pathList.size());
            return false;
        }
    }

    @Override
    public boolean isHasFile(String filePath) {

        return pathList.contains(filePath);
    }

    @Override
    public void preview() {
        PickFileManager.PreviewListener previewListener = PickFileManager.getInstance().previewListener;
        if (previewListener != null && pathList.size() != 0) {
            previewListener.preview(pathList);
        }
    }

}
