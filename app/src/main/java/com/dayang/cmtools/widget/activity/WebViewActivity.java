package com.dayang.cmtools.widget.activity;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dayang.cmtools.R;
import com.dayang.cmtools.base.BaseActivity;
import com.dayang.cmtools.utils.AppManager;
import com.dayang.cmtools.utils.JsonUtils;
import com.dayang.cmtools.utils.LogUtils;
import com.dayang.cmtools.utils.PermissionUtil;
import com.dayang.cmtools.webview.ActivityResultReceive;
import com.dayang.cmtools.webview.MyWebChromeClient;
import com.dayang.cmtools.webview.MyWebView;
import com.dayang.cmtools.widget.WidgetManager;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class WebViewActivity extends BaseActivity implements WidgetManager {

    private MyWebView launcherWebView;
    private MyWebView indexWebView;
    private FrameLayout rootView;
    private ActivityResultReceive activityResultReceive;
    private HashMap<String, MyWebView> webViewMap;
    private HashMap<MyWebView, String> backJSMap;
    private LinearLayout ll_loading;
    private TextView tv_loading;
    private PermissionUtil permissionUtil;
    private String messageContent;
    private String url;
    private String content;
    private String appId;
    private String classifyId;
    private String guid;
    private String classifyName;
    private boolean shouldSkip;
    private Button refresh_webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            messageContent = getIntent().getStringExtra("content");
            if (messageContent != null && !messageContent.equals("")) {
                enterUrl(messageContent);
            }
        } catch (Exception e) {
            messageContent = "";
        }
        launcherWebView = (MyWebView) findViewById(R.id.launcher_webView);
        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
        tv_loading = (TextView) findViewById(R.id.tv_loading);
        rootView = (FrameLayout) findViewById(R.id.root_view);
//        refresh_webview = (Button) findViewById(R.id.refresh_webview);
        activityResultReceive = ActivityResultReceive.getInstance();//activityForResult 返回处理器
        permissionUtil = new PermissionUtil(this); //生成本activity的申请权限的监听器
        launcherWebView.setMyWebViewManager(this);  //设置webview管理器
        indexWebView = null;
        initAnim();//初始化动画
        initRootMap();//初始化容器
        initWebview();
        MessageActivity.activity = this;
        //TODO 恢复缓存
        AppManager.getAppManager().addActivity(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    private void initWebview() {
        launcherWebView.setDefaultHandler(new DefaultHandler());
        WebSettings settings = launcherWebView.getSettings();
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        launcherWebView.setWebChromeClient(new MyWebChromeClient(this, launcherWebView));
//        launcherWebView.loadUrl("http://100.0.1.248:8020/JSBridge/index.html");
        launcherWebView.loadUrl("file:///android_asset/test/index.html");
    }

    /**
     * 初始化webView容器
     *
     * @return
     */
    private void initRootMap() {
        backJSMap = new HashMap<>();
        webViewMap = new HashMap<>();
        webViewMap.put("launcherView", launcherWebView);
//        refresh_webview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                WebView childAt = (WebView) rootView.getChildAt(rootView.getChildCount() - 1);
//                childAt.reload();
//            }
//        });

    }

    /**
     * 初始化容器动画
     *
     * @return
     */
    private void initAnim() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        LayoutTransition transition = new LayoutTransition();
        rootView.setLayoutTransition(transition);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(null, "translationX", width, 0).setDuration(1000);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(null, "translationX", 0, width).setDuration(1000);
        transition.setAnimator(LayoutTransition.APPEARING, animator1);
        transition.setAnimator(LayoutTransition.DISAPPEARING, animator2);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        permissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        activityResultReceive.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public MyWebView createWebview(String id, String url) {
        MyWebView myWebView = getMyWebView();
        myWebView.loadUrl(url);
        webViewMap.put(id, myWebView);
        return myWebView;
    }

    @Override
    public void removeWebview(String id) {
        MyWebView myWebView = webViewMap.get(id);
        if (myWebView == null) {
            LogUtils.e("removeWebview: id输入错误");
            return;
        }
        if (webViewMap.size() == 1) {
            AppManager.getAppManager().finishActivity(this);
        } else {
            rootView.removeView(myWebView);
            webViewMap.remove(id);
//            myWebView.removeAllViews();
//            myWebView.destroy();
        }
    }

    @Override
    public void clearWebviews(String id) {
        Set<String> strings = webViewMap.keySet();
        ArrayList<String> strings1 = new ArrayList<>(strings);
        for (String webId : strings1) {
            if (!id.equals(webId)) {
                removeWebview(webId);
            }
        }
    }

    @Override
    public void quit() {
        finish();
    }

    @Override
    public Set<String> getAllWebviews() {
        Set<String> strings = webViewMap.keySet();
        return strings;
    }

    @Override
    public MyWebView getWebviewById(String id) {
        return webViewMap.get(id);
    }

    @Override
    public boolean showWebveiw(String id, int zinde) {
        MyWebView myWebView = webViewMap.get(id);
        if (myWebView == null) {
            LogUtils.e("showWebveiw: id输入错误");
            return false;
        }
        int i = rootView.indexOfChild(myWebView);
        if (i == -1) {
            if (zinde == -1) {
                rootView.addView(myWebView, 0);
            } else if (zinde == 0) {
                rootView.addView(myWebView, rootView.getChildCount() - 1);
            } else {
                rootView.addView(myWebView);
            }
        } else {
            LogUtils.e("showWebveiw: 该webview已经存在");
            myWebView.bringToFront();
            rootView.requestLayout();
            rootView.invalidate();
        }
        return true;
    }

    @Override
    public void showLoading(String text) {
        tv_loading.setText(text);
        ll_loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void webviewGoTop(MyWebView myWebView) {
        myWebView.bringToFront();
        rootView.requestLayout();
        rootView.invalidate();
    }

    @Override
    public void setWebviewInvisible(MyWebView myWebView) {
        myWebView.setVisibility(View.GONE);
    }

    @Override
    public void setWebviewVisible(MyWebView myWebView) {
        myWebView.setVisibility(View.VISIBLE);
    }

    @Override
    public void removeLoading() {
        ll_loading.setVisibility(View.GONE);
    }

    @Override
    public PermissionUtil getPermissionUtil() {
        return permissionUtil;
    }

    public MyWebView getMyWebView() {
        MyWebView webView = new MyWebView(this, this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(params);
        //不允许缓存
        WebSettings settings = webView.getSettings();
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setWebChromeClient(new MyWebChromeClient(this, webView));
        return webView;
    }

    public void enterUrl(final String json) {
        messageContent = json;
        content = json;
        url = "";
        content = JsonUtils.getString(json, "content");
        url = JsonUtils.getString(json, "url");
        appId = JsonUtils.getString(json, "appId");
        guid = JsonUtils.getString(json, "guid");
        classifyId = JsonUtils.getString(json, "classifyId");
        classifyName = JsonUtils.getString(json, "classifyName");
        if (!url.equals("") && url.contains("account=") && url.contains("pcode=")) {
            String[] split = url.split("&");
            String[] split1 = split[0].split("=");
            String[] split2 = split[1].split("=");
            String account = split1[0].equals("account") ? split1[1] : split2[1];
            String password = split2[0].equals("pcode") ? split2[1] : split1[1];
//            ConferenceReq req = new ConferenceReq("", account, password);
//            req.setCallSelf(false);
//            TangInterface.joinConference(this, req, new TangCallback() {
//                @Override
//                public void onCallback(boolean b, String s, String s1) {
//                    if (b == false) {
//                        Toast.makeText(SDK_WebApp.this, s, Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
            //TODO 进入全时会议
        } else {
            showMessage(url, appId, classifyId, guid, classifyName);
        }
    }

    @Override
    public void showMessage(String url, String appId, String classifyId, String guid, String classifyName) {
        LogUtils.i("showMessage: " + url);
        if (indexWebView != null) {
            String js = "openMsgUrl(\"" + url + "\",\"" + appId + "\",\"" + classifyId + "\",\"" + guid + "\",\"" + classifyName + "\")";
            indexWebView.loadUrl("javascript:" + js);
            shouldSkip = false;
        } else {
            shouldSkip = true;
        }
    }

    @Override
    public void setIndexWebview(MyWebView webview) {
        indexWebView = webview;
    }

    @Override
    public boolean shouldSkip() {
        //TODO 返回跳转地址
        boolean b = shouldSkip;
        shouldSkip = false;
        return b;
    }

    @Override
    public String getMessageContent() {
        return messageContent;
    }

    @Override
    public MyWebView getIndexWebview() {
        return indexWebView;
    }

    @Override
    public Activity getActvity() {
        return this;
    }

    @Override
    public String getWebviewId(MyWebView webView) {
        Set<String> strings = webViewMap.keySet();
        for (String id : strings) {
            MyWebView myWebView = webViewMap.get(id);
            if (myWebView == webView) {
                return id;
            }
        }
        return null;
    }

    @Override
    public void setOverWriteAndroidBackKey(MyWebView webview, String js) {
        backJSMap.put(webview, js);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MessageActivity.activity = null;
        indexWebView = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            int childCount = rootView.getChildCount();
            MyWebView topView = (MyWebView) rootView.getChildAt(childCount - 1);
            if (topView != null) {
                String js = backJSMap.get(topView);
                if (js != null && !js.equals("")) {
                    topView.loadUrl("javascript:" + js);
                } else {
                    removeWebview(getWebviewId(topView));
                }
            } else {
                super.onKeyDown(keyCode, event);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
