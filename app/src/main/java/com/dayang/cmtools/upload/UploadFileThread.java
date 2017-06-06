package com.dayang.cmtools.upload;

import android.app.Activity;
import android.util.Log;

import com.dayang.cmtools.utils.JsonUtils;
import com.google.gson.JsonObject;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 上传的抽象类
 *
 * @author renyuwei
 */
public abstract class UploadFileThread extends Thread {
    private static final String TAG = "fengao";
    public Activity activity;
    String tenantId = "";

    public abstract boolean uploadAllFiles();

    public abstract boolean uploadSingleFile();


    public UploadFileThread(Activity activity) {
        this.activity = activity;
    }

    /**
     * 上传成功后向服务后台发起通知的请求参数封装
     *
     * @param uploadStatus
     * @param localpath
     * @param taskid
     * @return
     */
    public String getNotifyRequestParam(boolean uploadStatus, String localpath, String taskid) {
        JsonObject jsonObject = new JsonObject();
        try {
            int uploadStatusInt = 1;
            jsonObject.addProperty("taskId", taskid);
            jsonObject.addProperty("localPath", localpath);
            if (uploadStatus) {
                uploadStatusInt = 0;
            } else {
                uploadStatusInt = 1;
            }
            jsonObject.addProperty("fileStatus", uploadStatusInt);
            if (!(tenantId == null || tenantId.equals(""))) {
                jsonObject.addProperty("tenantId", tenantId);
            }
        } catch (Exception e) {
            Log.d("debug", e.toString());
        }
        return jsonObject.toString();
    }

    public String getNewHttpNotifyRequestParam(String fileSessionId, String fileNewName, boolean uploadStatus, String localpath, String taskid) {
        JsonObject jsonObject = new JsonObject();
        try {
            int uploadStatusInt = 1;
            jsonObject.addProperty("taskId", taskid);
            jsonObject.addProperty("localPath", localpath);
            if (uploadStatus) {
                uploadStatusInt = 0;
            } else {
                uploadStatusInt = 1;
            }
            jsonObject.addProperty("fileStatus", uploadStatusInt);
            jsonObject.addProperty("fileSessionId", fileSessionId);
            jsonObject.addProperty("fileNewName", fileNewName);
            if (!(tenantId == null || tenantId.equals(""))) {
                jsonObject.addProperty("tenantId", tenantId);
            }
        } catch (Exception e) {
            Log.d("debug", e.toString());
        }
        return jsonObject.toString();
    }

    /**
     * 向服务后台通知上传状态
     *
     * @param url          通知的接口地址
     * @param requestParam 通知的请求参数
     * @return
     */
    public boolean fileStatusNotifyCallBack(String url, String requestParam) {
        boolean notifystatus = false;
        HttpClient httpclient = new DefaultHttpClient();
        Log.e("debug", "开始上传通知");
        try {
            BufferedReader br = null;
            HttpPost httppost = new HttpPost(url);
            Log.i(TAG, "fileStatusNotifyCallBack: " + requestParam);
            StringEntity reqEntity = new StringEntity(requestParam, "utf-8");
            reqEntity.setContentEncoding("UTF-8");
            reqEntity.setContentType("application/String");
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                StringBuffer bufferStr = new StringBuffer();
                InputStream inputStream = response.getEntity().getContent();
                br = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = br.readLine()) != null) {
                    bufferStr.append(line);
                }
                String s = bufferStr.toString();
                String success = JsonUtils.getString(s, "success");
                String description = JsonUtils.getString(s, "description");
                if (!success.equals("true")) {
                    throw new Exception("通知" + description);
                } else {
                    notifystatus = true;
                    Log.e("debug", "通知" + description);
                }

            }
        } catch (Exception e) {
            Log.e("debug", "上传通知" + e.toString());
        } finally {
            try {
                httpclient.getConnectionManager().shutdown();
            } catch (Exception ignore) {
                Log.e("debug", "上传通知" + ignore.toString());
            }
        }
        return notifystatus;
    }
    public String getFileNameFromPath(String path) {
        String[] split = path.split("/");
        String s = split[split.length - 1];
        return s;
    }

}
