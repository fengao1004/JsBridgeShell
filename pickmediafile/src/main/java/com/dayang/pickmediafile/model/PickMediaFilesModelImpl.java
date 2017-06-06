package com.dayang.pickmediafile.model;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.dayang.pickmediafile.bean.FileDir;
import com.dayang.pickmediafile.util.TypeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by 冯傲 on 2017/3/17.
 * e-mail 897840134@qq.com
 */

public class PickMediaFilesModelImpl {
    private final Context context;

    public PickMediaFilesModelImpl(Context context) {
        this.context = context;
    }

    public ArrayList<FileDir> getDirs() {
        ArrayList<FileDir> fileDirs = new ArrayList<>();
        ArrayList<String> fileList = getFileList();
        ArrayList<String> fileListByDate = getFileListByDate(fileList);
        FileDir fileDir = new FileDir();
        fileDir.name = "最近文件";
        try {
            fileDir.files = fileListByDate.subList(0, 100);
        } catch (Exception e) {
            fileDir.files = fileListByDate;
        }
        fileDirs.add(fileDir);
        Map<String, ArrayList<String>> map = new TreeMap<>();
        for (int i = 0; i < fileListByDate.size(); i++) {
            String dirName = getFileDirName(fileListByDate.get(i));
            ArrayList<String> dir = map.get(dirName);
            if (dir == null) {
                dir = new ArrayList<>();
                dir.add(fileListByDate.get(i));
                map.put(dirName, dir);
            } else {
                dir.add(fileListByDate.get(i));
            }
        }
        Set<String> keys = map.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String dirname = iterator.next();
            fileDirs.add(new FileDir(map.get(dirname), dirname));
        }
        return fileDirs;
    }

    public ArrayList<String> getFileList() {
        ArrayList<String> fileList = new ArrayList<>();
        try {
            Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            Intent intent2 = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            Intent intent3 = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            Uri uri1 = intent1.getData();
            Uri uri2 = intent2.getData();
            Uri uri3 = intent3.getData();
            String[] proj = {MediaStore.Images.Media.DATA};
            String[] proj2 = {MediaStore.Video.Media.DATA};
            String[] proj3 = {MediaStore.Audio.Media.DATA};
            Cursor cursor1 = context.getContentResolver().query(uri1, proj, null, null, MediaStore.Images.Media.DATE_ADDED + " DESC");//managedQuery(uri, proj, null, null, null);
            Cursor cursor2 = context.getContentResolver().query(uri2, proj2, null, null, MediaStore.Video.Media.DATE_ADDED + " DESC");//managedQuery(uri, proj, null, null, null);
            Cursor cursor3 = context.getContentResolver().query(uri3, proj3, null, null, MediaStore.Audio.Media.DATE_ADDED + " DESC");//managedQuery(uri, proj, null, null, null);
            while (cursor1.moveToNext()) {
                String path = cursor1.getString(0);
                int fileType = TypeUtils.getFileType(path);
                if (fileType == TypeUtils.ADIOU || fileType == TypeUtils.IMAGE || fileType == TypeUtils.VIDIO) {
                    fileList.add(new File(path).getAbsolutePath());
                }
            }
            while (cursor2.moveToNext()) {
                String path = cursor2.getString(0);
                int fileType = TypeUtils.getFileType(path);
                if (fileType == TypeUtils.ADIOU || fileType == TypeUtils.IMAGE || fileType == TypeUtils.VIDIO) {
                    fileList.add(new File(path).getAbsolutePath());
                }
            }
            while (cursor3.moveToNext()) {
                String path = cursor3.getString(0);
                int fileType = TypeUtils.getFileType(path);
                if (fileType == TypeUtils.ADIOU || fileType == TypeUtils.IMAGE || fileType == TypeUtils.VIDIO) {
                    fileList.add(new File(path).getAbsolutePath());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileList;
    }

    /**
     * @param parameter 文件集合
     * @return 对文件进行时间排序后的集合
     * 因为文件数目比较大可能会比较浪费时间
     */
    public ArrayList<String> getFileListByDate(ArrayList<String> fileList) {
        Collections.sort(fileList, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                File filel = new File(lhs);
                File filer = new File(rhs);
                long l = filel.lastModified();
                long r = filer.lastModified();
                long l1 = l - r;
                if (l1 > 0) {
                    return -1;
                } else if (l1 == 0) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        return fileList;
    }

    /**
     * @param parameter
     * @return 返回文件夹的名称
     */
    public String getFileDirName(String data) {
        String filename[] = data.split("/");
        if (filename != null) {
            return filename[filename.length - 2];
        }
        return null;
    }
}
