package com.dayang.cmtools.utils;

import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;

import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * Created by 冯傲 on 2016/8/13.
 * e-mail 897840134@qq.com
 */
public class OkHttpUtil {
    private static final String TAG = "fengao";
    OkHttpCallBack ocb;
    private final OkHttpClient client = new OkHttpClient();
    private static final MediaType MEDIA_OBJECT_STREAM = MediaType.parse("application/octet-stream");
    public static final MediaType JSON = MediaType.parse("text/plain; charset=utf-8");

    public void call(String url, String json, OkHttpCallBack ocb) {
        this.ocb = ocb;
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call1 = client.newCall(request);
        call1.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                OkHttpUtil.this.ocb.error(request, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    OkHttpUtil.this.ocb.success(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void callGet(String url, OkHttpCallBack ocb) {
        this.ocb = ocb;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call1 = client.newCall(request);
        call1.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                OkHttpUtil.this.ocb.error(request, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    OkHttpUtil.this.ocb.success(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    public void downLoadFile(String url, String filePath, OkHttpCallBack ocb) {
        this.ocb = ocb;
        final File file = new File(filePath);
        RequestBody body = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MEDIA_OBJECT_STREAM;
            }

            @Override
            public long contentLength() throws IOException {
                return file.length();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source;
                try {
                    source = Okio.source(file);
                    Buffer buf = new Buffer();
                    long current = 0;
                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readCount);
                        current += readCount;
                        Log.e(TAG, "current------>" + current);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        });
    }


    public interface OkHttpCallBack {
        void success(Response response) throws Exception;

        void error(Request request, IOException e);
    }
}
