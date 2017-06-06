package com.dayang.cmtools.widget;

import android.app.Activity;
import android.webkit.WebView;

import com.dayang.cmtools.utils.PermissionUtil;
import com.dayang.cmtools.webview.MyWebView;

import java.util.Set;

/**
 * 主页面提供webview的各种管理功能
 * Created by 冯傲 on 2017/3/13.
 * e-mail 897840134@qq.com
 */

public interface WidgetManager {
    MyWebView createWebview(String id, String url);

    void removeWebview(String id);

    void clearWebviews(String id);

    void quit();

    Set<String> getAllWebviews();

    MyWebView getWebviewById(String id);

    boolean showWebveiw(String id,int zinde);

    void showLoading(String text);

    void webviewGoTop(MyWebView myWebView);

    void setWebviewInvisible(MyWebView myWebView);

    void setWebviewVisible(MyWebView myWebView);

    void removeLoading();

    PermissionUtil getPermissionUtil();

    void showMessage(String url, String appId, String classifyId, String guid, String classifyName);

    void setIndexWebview(MyWebView webview);

    boolean shouldSkip();

    String getMessageContent();

    MyWebView getIndexWebview();

    Activity getActvity();

    String getWebviewId(MyWebView webView);

    void setOverWriteAndroidBackKey(MyWebView webview, String js);
}
