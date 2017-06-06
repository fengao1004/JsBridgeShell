package com.dayang.cmtools.webview;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.dayang.browsemediafileslibrary.activity.BrowseActivity;
import com.dayang.cmtools.R;
import com.dayang.cmtools.bean.BindGeTuiBean;
import com.dayang.cmtools.bean.BrowerBase;
import com.dayang.cmtools.bean.BrowseMediaInfo;
import com.dayang.cmtools.bean.CommonResultInfo;
import com.dayang.cmtools.bean.Duration;
import com.dayang.cmtools.bean.FileAndIndexInfo;
import com.dayang.cmtools.bean.FileInfo;
import com.dayang.cmtools.bean.FileInfos;
import com.dayang.cmtools.bean.LocationResultInfo;
import com.dayang.cmtools.bean.MediaFileBase64Info;
import com.dayang.cmtools.bean.PlayFileInfo;
import com.dayang.cmtools.bean.ShellFileUploadInfo;
import com.dayang.cmtools.bean.ShellGetThumbnailsInfo;
import com.dayang.cmtools.dialog.AlertDialog;
import com.dayang.cmtools.dialog.UpdateDialog;
import com.dayang.cmtools.service.UpdateService;
import com.dayang.cmtools.upload.FtpUpload;
import com.dayang.cmtools.upload.HttpUpload;
import com.dayang.cmtools.upload.NewHttpUpload;
import com.dayang.cmtools.upload.UploadFileThread;
import com.dayang.cmtools.utils.ApkUpdateUtil;
import com.dayang.cmtools.utils.Constants;
import com.dayang.cmtools.utils.CustomBitmapFactory;
import com.dayang.cmtools.utils.DataCleanManager;
import com.dayang.cmtools.utils.GsonUtils;
import com.dayang.cmtools.utils.HtmlUpdateUtil;
import com.dayang.cmtools.utils.JsonUtils;
import com.dayang.cmtools.utils.LocationUtil;
import com.dayang.cmtools.utils.LogUtils;
import com.dayang.cmtools.utils.OkHttpUtil;
import com.dayang.cmtools.utils.PermissionUtil;
import com.dayang.cmtools.utils.SharedPreferencesUtils;
import com.dayang.cmtools.widget.WidgetManager;
import com.dayang.cmtools.widget.activity.MeetingListActivity;
import com.dayang.cmtools.widget.activity.PreviewEditActivity;
import com.dayang.cmtools.widget.activity.RecordAudioActivity;
import com.dayang.pickmediafile.common.PickFileManager;
import com.dayang.pickmediafile.util.MediaFile;
import com.dayang.pickmediafile.view.PickMediaFileActivity;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.igexin.sdk.PushManager;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.R.attr.description;
import static com.dayang.cmtools.widget.activity.MessageActivity.activity;
import static com.igexin.push.config.m.i;

/**
 * Created by 冯傲 on 2017/3/13.
 * e-mail 897840134@qq.com
 */

public class MediaPlugin {
    Activity activity;
    private MediaFileBase64Info info;
    private boolean async;
    private UploadFileListener uploadFileListener;
    public static File cameraFile;
    public static File cropFile;
    public static String fileType;
    public static final String shellVersion = "1.0.20170324";

    public MediaPlugin(Activity activity) {
        this.activity = activity;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            JsonObject returnJsonObject = new JsonObject();
            if (msg.what == Constants.UPLOADSUCCESS) {
                Toast.makeText(activity, "上传成功", 8000).show();
                returnJsonObject.addProperty("success", "true");
                returnJsonObject.addProperty("description", "上传成功!");
            } else if (msg.what == Constants.UPLOADFAILTURE) {
                Toast.makeText(activity, "上传失败", 8000).show();
                returnJsonObject.addProperty("success", "false");
                returnJsonObject.addProperty("description", "上传失败!");
            }
            if (uploadFileListener != null) {
                uploadFileListener.fileUploadEnd(returnJsonObject.toString());
            }
        }
    };


    /**
     * 存储键值对
     *
     * @param parameter
     * @return
     */
    public void saveKVData(String params, CallBackFunction cb) {
        if (params == null || params.equals("")) {
            cb.onCallBack(GsonUtils.toJson(new CommonResultInfo("false", "参数错误")));//TODO 实现返回结果
        }
        String key = JsonUtils.getString(params, "key");
        String value = JsonUtils.getString(params, "value");
        SharedPreferencesUtils.setParam(activity, key, value);
        cb.onCallBack("{\"success\":\"true\"}");
    }

    /**
     * 获取键值对
     *
     * @param parameter
     * @return
     */
    public void getKVData(String params, CallBackFunction cb) {
        if (params == null || params.equals("")) {
            cb.onCallBack(GsonUtils.toJson(new CommonResultInfo("false", "参数错误")));//TODO 实现返回结果
        }
        String key = JsonUtils.getString(params, "key");
        String param = SharedPreferencesUtils.getParam(activity, key, "");
        cb.onCallBack("{\"success\":\"true\",\"value\":\"" + param + "\"}");
    }

    /**
     * 删除键值对
     *
     * @param parameter
     * @return
     */
    public void delKVData(String params, CallBackFunction cb) {
        if (params == null || params.equals("")) {
            cb.onCallBack(GsonUtils.toJson(new CommonResultInfo("false", "参数错误")));//TODO 实现返回结果
        }
        String key = JsonUtils.getString(params, "key");
        SharedPreferencesUtils.setParam(activity, key, "undefined");
        cb.onCallBack("{\"success\":\"true\"}");
    }

    /**
     * 选择文件
     *
     * @param parameter
     * @return
     */
    public void selectFiles(String params, final CallBackFunction cb, PermissionUtil permissionUtil) {
        LogUtils.i("selectFiles: " + params);
        if (params == null || params.equals("")) {
            cb.onCallBack(GsonUtils.toJson(new CommonResultInfo("false", "参数错误")));//TODO 实现返回结果
        }
        String allowSelectNum = JsonUtils.getString(params, "allowSelectNum");
        if (allowSelectNum.equals("")) {
            LogUtils.e("selectFiles: " + "输入参数错误");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("description", "参数有误");
                jsonObject.put("success", "false");
                cb.onCallBack("");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return;
        }
        final Intent intent = new Intent(activity, PickMediaFileActivity.class);
        Integer integer = new Integer(allowSelectNum);
        intent.putExtra("imgNum", integer);
        PickFileManager.getInstance().setOnclickListener(new PickFileManager.OnClickFileListener() {
            @Override
            public void onClickFile(String filePath) {
                ArrayList<String> pathList = new ArrayList<>();
                ArrayList<String> thumbnailPathList = new ArrayList<>();
                int index = 0;
                pathList.add(filePath);
                thumbnailPathList.add("");
                Intent intent = new Intent(activity, BrowseActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("pathList", pathList);
                bundle.putStringArrayList("thumbnailPathList", thumbnailPathList);
                bundle.putInt("index", index);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });
        PickFileManager.getInstance().setPreviewListener(new PickFileManager.PreviewListener() {
            @Override
            public void preview(ArrayList<String> pathList) {
                ArrayList<String> thumbnailPathList = new ArrayList<>();
                int index = 0;
                for (int i = 0; i < pathList.size(); i++) {
                    thumbnailPathList.add("");
                }
                Intent intent = new Intent(activity, BrowseActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("pathList", pathList);
                bundle.putStringArrayList("thumbnailPathList", thumbnailPathList);
                bundle.putInt("index", index);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });
        permissionUtil.checkPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionUtil.PermissionListener() {
            @Override
            public void permissionAllowed() {
                ActivityResultReceive.getInstance().startActivityForResult(intent, ActivityResultReceive.SELECTFILE, cb, activity);
            }

            @Override
            public void permissionRefused() {
                Toast.makeText(activity, "没有足够的权限，请进入权限设置更改权限", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 手动检测版本
     *
     * @param parameter
     * @return
     */
    public void checkUpdate(String params, CallBackFunction cb) {
        ApkUpdateUtil.updateMode = ApkUpdateUtil.NORMALUPDATE;//手动升级
        new ApkUpdateUtil(activity).checkApkVersion();
    }

    /**
     * 文件上传功能实现 服务化可视化
     *
     * @param parameter
     * @return
     */
    public void uploadFiles(String params, final CallBackFunction cb, final WidgetManager manager) {
        LogUtils.i("selectFiles: " + params);
        if (params == null || params.equals("")) {
            cb.onCallBack(GsonUtils.toJson(new CommonResultInfo("false", "参数错误")));//TODO 实现返回结果
        }
        ShellFileUploadInfo info = GsonUtils.fromJson(params, ShellFileUploadInfo.class);
        //对文件做非空判断
        List<ShellFileUploadInfo.FilesLocalPathArrEntity> list = info.getFilesLocalPathArr();
        if (list == null || list.size() == 0) {
            LogUtils.i("uploadFiles: 上传文件就夹为空");
            return;
        }
        String filePath = list.get(0).getPath();
        File file = new File(filePath);
        if (file == null || file.length() == 0) {
            LogUtils.i("uploadFiles: 上传文件为空");
            return;
        }
        async = info.getAsync();
        if (!async) {
            this.uploadFileListener = new UploadFileListener() {
                @Override
                public void fileUploadEnd(String json) {
                    cb.onCallBack(json);
                    manager.removeLoading();
                }
            };
            manager.showLoading("上传中");
        } else {
            CommonResultInfo resultInfo = new CommonResultInfo();
            resultInfo.setSuccess("true");
            resultInfo.setDescription("上传中");
            cb.onCallBack(GsonUtils.toJson(resultInfo));
        }
        UploadFileThread uploadFileThread = null;
        List<FileAndIndexInfo> uploadFileList = new ArrayList<FileAndIndexInfo>();
        for (int i = 0; i < list.size(); i++) {
            String path = list.get(i).getPath();
            String indexNo = list.get(i).getIndexNO() + "";
            boolean isRename = list.get(i).getIsRename();
            String fileSessionId = list.get(i).getFileSessionId();
            uploadFileList.add(new FileAndIndexInfo(path, indexNo, fileSessionId, isRename));
        }
        String storageURL = info.getStorageURL();
        String fileStatusNotifyURL = info.getFileStatusNotifyURL();
        String taskId = info.getTaskId();
        String tenantId = info.getTenantId();
        String uploadTrunkInfoURL = info.getUploadTrunkInfoURL();
        if (storageURL.startsWith("http")) {
            URL url = null;
            try {
                url = new URL(storageURL);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            if (storageURL.contains("?")) {
                storageURL = url.getProtocol() + "://" + url.getAuthority() + url.getPath();
                String remoteRootPath = url.getQuery().split("=")[1];
                if (!remoteRootPath.startsWith("/")) {
                    remoteRootPath = "/" + remoteRootPath;
                }
                if (!remoteRootPath.endsWith("/")) {
                    remoteRootPath = remoteRootPath + "/";
                }
                if (!tenantId.equals("")) {
                    remoteRootPath = "/" + tenantId + remoteRootPath;
                }
                remoteRootPath = remoteRootPath + taskId;
                uploadFileThread = new NewHttpUpload(storageURL,
                        fileStatusNotifyURL, handler,
                        Constants.UPLOADMUTIPLE, null, uploadFileList,
                        taskId, activity, tenantId, remoteRootPath, uploadTrunkInfoURL);
                uploadFileThread.start();
            } else {
                uploadFileThread = new HttpUpload(storageURL,
                        fileStatusNotifyURL, handler,
                        Constants.UPLOADMUTIPLE, null, uploadFileList,
                        taskId, activity, tenantId);
                uploadFileThread.start();
            }
        } else if (storageURL.startsWith("ftp")) {
            uploadFileThread = new FtpUpload(storageURL,
                    fileStatusNotifyURL, handler,
                    Constants.UPLOADMUTIPLE, null, uploadFileList,
                    taskId, activity, tenantId);
            uploadFileThread.start();
        }
    }

    /**
     * 输入文件地址返回文件的base64缩略图
     *
     * @param parameter
     * @return
     */
    public void getMediaBase64Infos(String params, CallBackFunction cb) {
        LogUtils.i("selectFiles: " + params);
        if (params == null || params.equals("")) {
            cb.onCallBack(GsonUtils.toJson(new CommonResultInfo("false", "参数错误")));//TODO 实现返回结果
        }
        MediaFileBase64Info info = null;
        try {
            info = GsonUtils.fromJson(params, MediaFileBase64Info.class);
        } catch (Exception e) {
            LogUtils.e("getMediaBase64Info: 参数错误");
        }
        List<String> fileNamePath = info.getFilesLocalPathArr();
        BrowerBase returnObject = new BrowerBase();
        returnObject.setTaskId(info.getTaskId());
        List<FileInfos> fileinfoList = new ArrayList<FileInfos>();
        for (int i = 0; i < fileNamePath.size(); i++) {
            String base64code = null;
            String fileName = fileNamePath.get(i);
            File file = new File(fileName);
            FileInfos fileinfos = new FileInfos();
            if (MediaFile.isImageFileType(file.getAbsolutePath())) {
                fileinfos.setFileType(Constants.FILE_IMAGE_TYPE);
                base64code = imageToBase64(file);
            } else if (MediaFile
                    .isVideoFileType(file.getAbsolutePath())) {
                fileinfos.setFileType(Constants.FILE_VIDEO_TYPE);
                Bitmap firstPicture = getVideoThumbnail(
                        file.getAbsolutePath(), 100, 100,
                        MediaStore.Images.Thumbnails.MICRO_KIND);
                base64code = bitmap2Base64(firstPicture);
            } else {
                fileinfos.setFileType(Constants.FILE_AUDIO_TYPE);
            }
            fileinfos.setLocalPath(file.getAbsolutePath());
            fileinfos.setName(file.getName());
            fileinfos.setFileSize(file.length());
            fileinfos.setImageBase64(base64code);
            fileinfoList.add(fileinfos);
        }
        returnObject.setSuccess("true");
        returnObject.setDescription("返回成功！");
        returnObject.setFileInfos(fileinfoList);
        String json = GsonUtils.toJson(returnObject);
        cb.onCallBack(json);
    }

    /**
     * 橱窗显示图片，视频，音乐
     *
     * @param
     * @return
     */

    public void browseMedia(String params, CallBackFunction cb) {
        LogUtils.i("selectFiles: " + params);
        if (params == null || params.equals("")) {
            cb.onCallBack(GsonUtils.toJson(new CommonResultInfo("false", "参数错误")));//TODO 实现返回结果
        }
        BrowseMediaInfo info = null;
        CommonResultInfo resultInfo = new CommonResultInfo();
        try {
            info = GsonUtils.fromJson(params, BrowseMediaInfo.class);
        } catch (Exception e) {
            LogUtils.e("browseMedia: 参数错误");
            resultInfo.setDescription("参数错误");
            resultInfo.setSuccess("false");
            cb.onCallBack(GsonUtils.toJson(resultInfo));
            return;
        }
        List<BrowseMediaInfo.AllFilesEntity> allFiles = info.getAllFiles();
        ArrayList<String> pathList = new ArrayList<>();
        ArrayList<String> thumbnailPathList = new ArrayList<>();
        int index = -1;
        for (int i = 0; i < allFiles.size(); i++) {
            pathList.add(allFiles.get(i).getFilePath());
            thumbnailPathList.add(allFiles.get(i).getThumbPath());
            if (allFiles.get(i).getFilePath().equals(info.getCurrentClick().getFilePath())) {
                index = i;
            }
        }
        if (index != -1 && pathList.size() != 0 && thumbnailPathList.size() != 0) {
            Intent intent = new Intent(activity, BrowseActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("pathList", pathList);
            bundle.putStringArrayList("thumbnailPathList", thumbnailPathList);
            bundle.putInt("index", index);
            intent.putExtras(bundle);
            activity.startActivity(intent);
            resultInfo.setDescription("播放正确");
            resultInfo.setSuccess("true");
            cb.onCallBack(GsonUtils.toJson(resultInfo));
            return;
        } else {
            resultInfo.setDescription("播放错误");
            resultInfo.setSuccess("false");
            cb.onCallBack(GsonUtils.toJson(resultInfo));
            return;
        }

    }

    /**
     * 获取手机当前位置
     *
     * @param parameter
     * @return
     */
    public void getLocations(String params, final CallBackFunction cb) {
        String userName = JsonUtils.getString(params, "userName");
        String userId = JsonUtils.getString(params, "userId");
        String tenantId = JsonUtils.getString(params, "tenantId");
        String locationUrl = JsonUtils.getString(params, "locationUrl");
        LocationUtil instance = LocationUtil.getInstance(activity, userName, locationUrl, tenantId, userId);
        instance.getLocations(new LocationUtil.LocationListener() {
            @Override
            public void onLocationListener(double latitude, double longitude, String address) {
                LocationResultInfo locationResultInfo = new LocationResultInfo();
                if (latitude != 0.0) {
                    List<LocationResultInfo.LocationsEntity> locationsEntities = new ArrayList<>();
                    LocationResultInfo.LocationsEntity locationsEntity = new LocationResultInfo.LocationsEntity();
                    locationsEntity.setLocationName(address);
                    locationsEntities.add(locationsEntity);
                    locationResultInfo.setDescription("定位成功");
                    locationResultInfo.setSuccess("true");
                    locationResultInfo.setLocations(locationsEntities);
                    LocationResultInfo.LocationCoordinateEntity locationCoordinateEntity = new LocationResultInfo.LocationCoordinateEntity();
                    locationCoordinateEntity.setLatitude("" + latitude);
                    locationCoordinateEntity.setLongitude("" + longitude);
                    locationResultInfo.setLocationCoordinate(locationCoordinateEntity);
                    String json = GsonUtils.toJson(locationResultInfo);
                    LogUtils.i("onLocationListener: 返回GPS信息" + json);
                    cb.onCallBack(json);
                } else {
                    CommonResultInfo info = new CommonResultInfo();
                    info.setSuccess("false");
                    info.setDescription("定位失败");
                    String json = GsonUtils.toJson(info);
                    LogUtils.i("onLocationListener: 返回GPS信息" + json);
                    cb.onCallBack(json);
                }
            }
        });
    }

    /**
     * 根据文件路径获取200*200的缩略图
     *
     * @param parameter
     * @return
     */
    public void getThumbnails(String params, CallBackFunction cb) {
        if (params == null || params.equals("")) {
            cb.onCallBack(GsonUtils.toJson(new CommonResultInfo("false", "参数错误")));//TODO 实现返回结果
        }
        ShellGetThumbnailsInfo info = GsonUtils.fromJson(params, ShellGetThumbnailsInfo.class);
        BrowerBase returnObject = new BrowerBase();
        List<FileInfos> fileinfoList = new ArrayList<>();
        try {
            if (info != null) {
                for (int i = 0; i < info.getFilesLocalPathArr().size(); i++) {
                    String base64code = null;
                    String fileName = info.getFilesLocalPathArr().get(i).getPath();
                    String indexNO = info.getFilesLocalPathArr().get(i).getIndexNO() + "";
                    File file = new File(fileName);
                    if (!file.exists()) {
                        throw new Exception("文件不存在");
                    }
                    FileInfos fileinfos = new FileInfos();
                    fileinfos.setIndexNO(indexNO);
                    if (MediaFile.isImageFileType(file.getAbsolutePath())) {
                        fileinfos.setFileType(Constants.FILE_IMAGE_TYPE);
                        base64code = imageToBase64thumbnail(file, 200, 200);
                    } else if (MediaFile
                            .isVideoFileType(file.getAbsolutePath())) {
                        fileinfos.setFileType(Constants.FILE_VIDEO_TYPE);
                        Bitmap firstPicture = getVideoThumbnail(
                                file.getAbsolutePath(),
                                Constants.THUMBNAILWIDTH,
                                Constants.THUMBNAILHEIGHT,
                                MediaStore.Images.Thumbnails.MICRO_KIND);
                        base64code = bitmap2Base64(firstPicture);
                        String[] returnoObjects = MediaFile.getPlayTime(file
                                .getAbsolutePath());
                        fileinfos.setDuration(new Duration(
                                (String) returnoObjects[0],
                                (String) returnoObjects[1]));
                    } else {
                        fileinfos.setFileType(Constants.FILE_AUDIO_TYPE);
                    }
                    fileinfos.setLocalPath(file.getAbsolutePath());
                    fileinfos.setName(file.getName());
                    fileinfos.setFileSize(file.length());
                    fileinfos.setThumbnail(base64code);
                    fileinfoList.add(fileinfos);
                }
                returnObject.setSuccess("true");
                returnObject.setDescription("返回成功！");
                returnObject.setFileInfos(fileinfoList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setSuccess("false");
            returnObject.setDescription("返回失败！");
        }
        String s = GsonUtils.toJson(returnObject);
        cb.onCallBack(s);
    }

    /**
     * 通过拍照或者图库选择图片进行裁剪后返回
     *
     * @param parameter
     * @return
     */
    public void setUserProfile(String params, CallBackFunction cb, PermissionUtil permissionUtil) {
        if (params == null || params.equals("")) {
            cb.onCallBack(GsonUtils.toJson(new CommonResultInfo("false", "参数错误")));//TODO 实现返回结果
        }
        String actionName = JsonUtils.getString(params, "actionName");
        try {
            if (actionName.equals("takePhoto")) {
                this.cameraFile = takePhotoCrop(cb, permissionUtil);
            } else if (actionName.equals("pickMediaFile")) {
                takeLocalImageCrop(cb, permissionUtil);
            }
        } catch (Exception e) {

        }

        this.fileType = Constants.FILE_IMAGE_TYPE;
    }

    /**
     * 获取媒体文件 拍照 录制 录音 选择文件
     *
     * @param parameter
     * @return
     */
    public void doMedia(String params, final CallBackFunction cb, PermissionUtil permissionUtil) {
        if (params == null || params.equals("")) {
            cb.onCallBack(GsonUtils.toJson(new CommonResultInfo("false", "参数错误")));//TODO 实现返回结果
        }
        try {
            String actionName = JsonUtils.getString(params, "actionName");
            if (actionName.equals(Constants.TAKE_PHOTO)) {
                this.cameraFile = takePhoto(cb, permissionUtil);
                this.fileType = Constants.FILE_IMAGE_TYPE;
            } else if (actionName.equals(Constants.RECORD_VIDEO)) {
                this.cameraFile = recordVideo(cb, permissionUtil);
                this.fileType = Constants.FILE_VIDEO_TYPE;
            } else if (actionName.equals(Constants.RECORD_AUDIO)) {
                this.fileType = Constants.FILE_AUDIO_TYPE;
                final Intent recordIntent = new Intent(activity,
                        RecordAudioActivity.class);
                permissionUtil.checkPermission(new String[]{Manifest.permission.RECORD_AUDIO}, new PermissionUtil.PermissionListener() {
                    @Override
                    public void permissionAllowed() {
                        ActivityResultReceive.getInstance().startActivityForResult(recordIntent, ActivityResultReceive.RECORD_STANDARDAUDIO_REQUEST, cb, activity);
                    }

                    @Override
                    public void permissionRefused() {
                        Toast.makeText(activity, "缺少相关权限，无法打开此功能", 0).show();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    /**
     * 播放单个媒体文件
     *
     * @param parameter
     * @return
     */
    public void playMedia(String params, CallBackFunction cb) {
        if (params == null || params.equals("")) {
            cb.onCallBack(GsonUtils.toJson(new CommonResultInfo("false", "参数错误")));//TODO 实现返回结果
        }
        PlayFileInfo info = GsonUtils.fromJson(params, PlayFileInfo.class);
        CommonResultInfo resultInfo = new CommonResultInfo();
        ArrayList<String> pathList = new ArrayList<>();
        ArrayList<String> thumbnailPathList = new ArrayList<>();
        int index = 0;
        pathList.add(info.getFileInfo().getFilePath());
        thumbnailPathList.add("");
        Intent intent = new Intent(activity, BrowseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("pathList", pathList);
        bundle.putStringArrayList("thumbnailPathList", thumbnailPathList);
        bundle.putInt("index", index);
        intent.putExtras(bundle);
        activity.startActivity(intent);
        resultInfo.setDescription("播放正确");
        resultInfo.setSuccess("true");
        cb.onCallBack(GsonUtils.toJson(resultInfo));
        return;
    }

    /**
     * 清理缓存
     *
     * @param parameter
     * @return
     */

    public void clearCache(String params, CallBackFunction cb, MyWebView webView) {
        JsonObject returnJsonObject = new JsonObject();
        try {
            String databasecache = "/data/data/"
                    + webView.getContext().getPackageName() + "/databases/";
            String cacheDirPath = activity.getFilesDir().getAbsolutePath()
                    + Constants.APP_CACAHE_DIRNAME;
            DataCleanManager.cleanApplicationData(
                    activity.getApplicationContext(), databasecache,
                    cacheDirPath);
            returnJsonObject.addProperty("success", "true");
            returnJsonObject.addProperty("description", "清除缓存成功！");
        } catch (Exception e) {
            returnJsonObject.addProperty("success", "false");
            returnJsonObject.addProperty("description", "清除缓存失败！");
        }
        String returnStr = returnJsonObject.toString();
        cb.onCallBack(returnStr);
    }

    /**
     * 检查壳版本和js版本是否一致
     *
     * @param params js携带参数
     * @param cb     回调函数
     * @return void
     */
    public void checkjsVersion(String params, CallBackFunction cb) {
//        if (params == null || params.equals("")) {
//            cb.onCallBack(GsonUtils.toJson(new CommonResultInfo("false", "参数错误")));//TODO 实现返回结果
//        }
        JsonObject returnJsonObject = new JsonObject();
        returnJsonObject.addProperty("success", "true");
        returnJsonObject.addProperty("description", "");
        String returnStr = returnJsonObject.toString();
        cb.onCallBack(returnStr);
//        try {
//            String jsVersion = JsonUtils.getString(params, "jsVersion");
//            String jsVersionNum = "";
//            String shellVersionNum = "";
//            if (jsVersion != null && !jsVersion.equals("")) {
//                jsVersionNum = getVersionNum(jsVersion);
//            }
//            shellVersionNum = getVersionNum(shellVersion);
//            if (jsVersionNum.equals(shellVersionNum)) {
//                returnJsonObject.addProperty("success", "true");
//                returnJsonObject.addProperty("description",
//                        "匹配成功。设备类型：ANDROID，js版本：" + jsVersion + "，shell版本："
//                                + shellVersion + "!");
//            } else {
//                returnJsonObject.addProperty("success", "false");
//                returnJsonObject.addProperty("description",
//                        "匹配失败。设备类型：ANDROID，js版本：" + jsVersion + "，shell版本："
//                                + shellVersion + "!");
//            }
//        } catch (Exception e) {
//            returnJsonObject.addProperty("success", "false");
//            returnJsonObject.addProperty("description", "匹配失败" + e.toString());
//        }

    }

    /**
     * 剪辑视频
     *
     * @param parameter
     * @return
     */
    public void videoPreviewEdit(String params, CallBackFunction cb) {
        if (params == null || params.equals("")) {
            cb.onCallBack(GsonUtils.toJson(new CommonResultInfo("false", "参数错误")));//TODO 实现返回结果
        }
        String indexNO = JsonUtils.getString(params, "indexNO");
        String filePath = JsonUtils.getString(params, "filePath");
        Intent intent = null;
        try {
            intent = new Intent(activity, PreviewEditActivity.class);
            intent.putExtra("path", filePath);
            intent.putExtra("indexNO", indexNO);
            ActivityResultReceive.getInstance().startActivityForResult(intent, ActivityResultReceive.VIDEOPREVIEWEDIT, cb, activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 拨打电话
     *
     * @param parameter
     * @return
     */
    public void dialNumberUrl(String params, CallBackFunction cb, PermissionUtil permissionUtil) {
        if (params == null || params.equals("")) {
            cb.onCallBack(GsonUtils.toJson(new CommonResultInfo("false", "参数错误")));//TODO 实现返回结果
        }
        CommonResultInfo info = new CommonResultInfo();
        final String Number = JsonUtils.getString(params, "phoneNumber");
        final Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Number));
        permissionUtil.checkPermission(new String[]{Manifest.permission.CALL_PHONE}, new PermissionUtil.PermissionListener() {
            @Override
            public void permissionAllowed() {
                activity.startActivity(intent);
            }

            @Override
            public void permissionRefused() {

            }
        });
        String returnStr = GsonUtils.toJson(info);
        cb.onCallBack(returnStr);
    }

    /**
     * 发送短信
     *
     * @param parameter
     * @return
     */
    public void sendMessageUrl(String params, CallBackFunction cb, PermissionUtil permissionUtil) {
        if (params == null || params.equals("")) {
            cb.onCallBack(GsonUtils.toJson(new CommonResultInfo("false", "参数错误")));//TODO 实现返回结果
        }
        CommonResultInfo info = new CommonResultInfo();
        final String Number = JsonUtils.getString(params, "phoneNumber");
        Uri smsToUri = Uri.parse("smsto:" + Number);
        final Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri); // 传递收短信的地址,需要知道
        permissionUtil.checkPermission(new String[]{Manifest.permission.SEND_SMS}, new PermissionUtil.PermissionListener() {
            @Override
            public void permissionAllowed() {
                activity.startActivity(intent);
            }

            @Override
            public void permissionRefused() {

            }
        });
        info.setSuccess("true");
        info.setDescription("拨打成功");
        String returnStr = GsonUtils.toJson(info);
        cb.onCallBack(returnStr);
    }

    /**
     * 查询是否应该做跳转（从个推通知进来需要跳转）
     *
     * @param parameter
     * @return
     */
    public void querySkip(String params, CallBackFunction cb, WidgetManager widgetManager) {
        boolean b = widgetManager.shouldSkip();
        JsonObject returnJsonObject = new JsonObject();
        if (b) {
            String json = widgetManager.getMessageContent();
            returnJsonObject.addProperty("loadMsg", "true");
            returnJsonObject.addProperty("messageUrl", JsonUtils.getString(json, "url"));
            returnJsonObject.addProperty("classifyId", JsonUtils.getString(json, "classifyId"));
            returnJsonObject.addProperty("classifyName", JsonUtils.getString(json, "classifyName"));
            returnJsonObject.addProperty("guid", JsonUtils.getString(json, "guid"));
            returnJsonObject.addProperty("appId", JsonUtils.getString(json, "appId"));
        } else {
            returnJsonObject.addProperty("loadMsg", "false");
            returnJsonObject.addProperty("messageUrl", "");
            returnJsonObject.addProperty("appId", "");
        }
        cb.onCallBack(returnJsonObject.toString());
    }

    /**
     * 设置index的webview
     *
     * @param parameter
     * @return
     */
    public void setWebview(String params, CallBackFunction cb, MyWebView myWebView, WidgetManager widgetManager) {
        widgetManager.setIndexWebview(myWebView);
    }

    /**
     * 获取login的路径 portal可升级的基础
     *
     * @param parameter
     * @return
     */
    public void getLoginPath(String params, CallBackFunction cb) {
        String htmlLoginPath = new HtmlUpdateUtil(activity).getHtmlLoginPath();
        JSONObject returnJsonObject = new JSONObject();
        try {
            returnJsonObject.put("path", htmlLoginPath);
            returnJsonObject.put("success", true);
            cb.onCallBack(returnJsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 进入连线列表
     *
     * @param parameter
     * @return
     */
    public void listMeeting(final String params, final CallBackFunction cb, PermissionUtil permissionUtil) {
        if (params == null || params.equals("")) {
            cb.onCallBack(GsonUtils.toJson(new CommonResultInfo("false", "参数错误")));//TODO 实现返回结果
        }
        permissionUtil.checkPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionUtil.PermissionListener() {
            @Override
            public void permissionAllowed() {
                String workNo = JsonUtils.getString(params, "workNo");
                Intent intent = new Intent(activity, MeetingListActivity.class);
                intent.putExtra("workNo", workNo);
                activity.startActivity(intent);
            }

            @Override
            public void permissionRefused() {
                Toast.makeText(activity, "没有足够的权限，请进入权限设置更改权限", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 绑定个推
     *
     * @param parameter
     * @return
     */
    public void bindGeTuiCid(String params, final CallBackFunction cb) {
        if (params == null || params.equals("")) {
            cb.onCallBack(GsonUtils.toJson(new CommonResultInfo("false", "参数错误")));//TODO 实现返回结果
        }
        LogUtils.i("bindGeTuiCid: ");
        final JsonObject returnJsonObject = new JsonObject();
        String userId = JsonUtils.getString(params, "userid");
        String workNo = JsonUtils.getString(params, "workNo");
        String locationUrl = JsonUtils.getString(params, "bindGeTuiUrl");
        String tenantCode = JsonUtils.getString(params, "tenantCode");
        if (locationUrl.contains("/")) {
        } else {
            locationUrl = "http://" + locationUrl + "/messagecenter/api/app/alias/bind";
        }
        String cid = PushManager.getInstance().getClientid(activity);
        BindGeTuiBean bindInfo = new BindGeTuiBean(Constants.GETUI_APP_ID, userId, cid, workNo, tenantCode);
        String json = new Gson().toJson(bindInfo);
        OkHttpUtil okHttpUtil = new OkHttpUtil();
        okHttpUtil.call(locationUrl, json, new OkHttpUtil.OkHttpCallBack() {
            @Override
            public void success(Response response) {
                String string = "";
                try {
                    string = response.body().string();
                    LogUtils.i("success: " + string);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (string.contains("true")) {
                    returnJsonObject.addProperty("success", "true");
                    returnJsonObject.addProperty("description", "");
                } else {
                    returnJsonObject.addProperty("success", "false");
                    returnJsonObject.addProperty("description", "绑定失败");
                }
                cb.onCallBack(returnJsonObject.toString());
            }

            @Override
            public void error(Request request, IOException e) {
                returnJsonObject.addProperty("success", "false");
                returnJsonObject.addProperty("description", "绑定失败");
                cb.onCallBack(returnJsonObject.toString());
            }
        });
    }

    /**
     * 开始上传gps信息
     *
     * @param parameter
     * @return
     */
    public void startUpdateLocation(String params, CallBackFunction cb) {
        if (params == null || params.equals("")) {
            cb.onCallBack(GsonUtils.toJson(new CommonResultInfo("false", "参数错误")));//TODO 实现返回结果
        }
        String userName = JsonUtils.getString(params, "userName");
        String userId = JsonUtils.getString(params, "userId");
        String tenantId = JsonUtils.getString(params, "tenantId");
        String locationUrl = JsonUtils.getString(params, "locationURL");
        LocationUtil.getInstance(activity, userName, locationUrl, tenantId, userId).startLocation();
    }

    /**
     * 停止上传GPS信息
     *
     * @param parameter
     * @return
     */
    public void stopUpdateLocation(String params, CallBackFunction cb) {
        LocationUtil.getInstance(activity).stopLocation();
    }
    //TODO 实现两个监听器 权限回调 activityForResult 回调

    /**
     * 根据图像文件转换成图像的Base64
     *
     * @param imgfile
     * @return
     */
    public String imageToBase64(File imgfile) {
        InputStream in = null;
        String strBase64 = null;
        byte[] data = null;
        try {
            in = new FileInputStream(imgfile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        strBase64 = Base64.encodeToString(data, Base64.NO_WRAP);
        return strBase64;// 返回Base64编码过的字节数组字符串
    }

    /**
     * 把bitmap的图片转换成Base64串
     *
     * @param bitmap
     * @return
     */
    public String bitmap2Base64(Bitmap bitmap) {
        byte[] bytes = null;
        try {
            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bStream);
            bStream.flush();
            bStream.close();
            bytes = bStream.toByteArray();
        } catch (Exception e) {
            Log.e("debug", e.toString());
        }
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    /**
     * 根据视频路径抽取该视频的首帧图片
     *
     * @param videoPath
     * @param width
     * @param height
     * @param kind
     * @return
     */
    private Bitmap getVideoThumbnail(String videoPath, int width, int height,
                                     int kind) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * 把图像缩放成指定大小，然后转换为Base64串返回
     *
     * @param imgfile
     * @param width
     * @param height
     * @return
     */
    public String imageToBase64thumbnail(File imgfile, int width, int height) {
        String base64str = null;
        try {
            Bitmap bmp = CustomBitmapFactory.decodeBitmap(imgfile
                    .getAbsolutePath());
            Bitmap thumbnail = scaleBitMap(bmp, width, height);
            base64str = bitmap2Base64(thumbnail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64str;
    }


    /**
     * 把Bitmap的图像缩放成指定宽度和高度的图像
     *
     * @param source
     * @param width
     * @param height
     * @return
     */
    public Bitmap scaleBitMap(Bitmap source, int width, int height) {
        Bitmap target = null;
        try {
            target = Bitmap.createBitmap(width, height, source.getConfig());
            Canvas canvas = new Canvas(target);
            canvas.drawBitmap(source, null, new Rect(0, 0, target.getWidth(),
                    target.getHeight()), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return target;

    }

    /**
     * 摄像
     *
     * @return
     * @throws InterruptedException
     */
    public File takePhoto(final CallBackFunction cb, PermissionUtil permissionUtil) throws InterruptedException {
        Log.e("debug", "开始摄像并调用摄像机");
        File imageFile = this.getFileByDate("images", "jpg");
        final Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 构造intent
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
        permissionUtil.checkPermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionUtil.PermissionListener() {
            @Override
            public void permissionAllowed() {
                ActivityResultReceive.getInstance().startActivityForResult(cameraIntent, ActivityResultReceive.CAMERA_REQUEST, cb, activity);
            }

            @Override
            public void permissionRefused() {
                Toast.makeText(activity, "缺少相关权限", 0).show();
            }
        });
        return imageFile;
    }

    /**
     * 调用裁剪功能的拍照
     *
     * @return
     * @throws InterruptedException
     */
    public File takePhotoCrop(final CallBackFunction cb, PermissionUtil permissionUtil) throws InterruptedException {
        Log.e("debug", "开始摄像并调用摄像机");
        File imageFile = this.getFileByDate("images", "jpg");
        this.cameraFile = imageFile;
        final Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 构造intent
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
        permissionUtil.checkPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionUtil.PermissionListener() {
            @Override
            public void permissionAllowed() {
                ActivityResultReceive.getInstance().startActivityForResult(cameraIntent, ActivityResultReceive.CROP_CAMERIMAGE_REQUEST, cb, activity);
            }

            @Override
            public void permissionRefused() {
                Toast.makeText(activity, "缺少相关权限无法使用此功能", 0).show();
            }
        });
        return imageFile;
    }

    /**
     * 调用系统本地相册
     *
     * @return
     * @throws InterruptedException
     */
    public void takeLocalImageCrop(final CallBackFunction cb, PermissionUtil permissionUtil) throws InterruptedException {
        final Intent imageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        permissionUtil.checkPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionUtil.PermissionListener() {
            @Override
            public void permissionAllowed() {
                ActivityResultReceive.getInstance().startActivityForResult(imageIntent, ActivityResultReceive.CROP_LOCALIMGES_REQUEST, cb, activity);
            }

            @Override
            public void permissionRefused() {
                Toast.makeText(activity, "缺少相关权限无法使用此功能", 0).show();
            }
        });
    }

    /**
     * 录制视频
     *
     * @return
     * @throws InterruptedException
     */
    public File recordVideo(final CallBackFunction cb, PermissionUtil permissionUtil) throws InterruptedException {
        final Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);// 构造intent
        File out = getFileByDate("videos", "mp4");
        Uri uri = Uri.fromFile(out);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        permissionUtil.checkPermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionUtil.PermissionListener() {
            @Override
            public void permissionAllowed() {
                ActivityResultReceive.getInstance().startActivityForResult(cameraIntent, ActivityResultReceive.CAMERA_VIDEO_REQUEST, cb, activity);
            }

            @Override
            public void permissionRefused() {
                Toast.makeText(activity, "缺少相关权限", 0).show();
            }
        });
        return out;
    }

    /**
     * 根据输入的文件夹和文件后缀名生成文件
     *
     * @param fileDir
     * @param fileExtType
     * @return
     */
    public File getFileByDate(String fileDir, String fileExtType) {
        File fileout = null;
        try {
            File out = null;
            SimpleDateFormat sDateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            String date = sDateFormat.format(new Date());
            date = date.replaceAll(" |:|-", "");
            String uploadPath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + File.separator + fileDir + "/";
            out = new File(uploadPath);
            if (!out.exists()) {
                out.mkdirs();
            }
            String uplaodFileName = date.toString() + "." + fileExtType;
            fileout = new File(uploadPath, uplaodFileName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return fileout;
    }

    public String getVersionNum(String version) {
        String versionarr[];
        String versionNum = "";
        if (version != null && !version.equals("")) {
            versionarr = version.split("\\.");
            versionNum = versionarr[0] + "." + versionarr[1];
        }
        return versionNum;
    }

    public void exit(String data, CallBackFunction function) {
        //TODO 一些退出操作 indexWebview置空 停止上传gps 解绑个推 个推跳转标识置空
    }

    public void updatePortal(String data, CallBackFunction function) {
        new HtmlUpdateUtil(activity).checkPortalVersion();
    }


    interface UploadFileListener {
        void fileUploadEnd(String json);
    }
}