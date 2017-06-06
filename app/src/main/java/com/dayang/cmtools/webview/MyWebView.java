package com.dayang.cmtools.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.AttributeSet;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.dayang.cmtools.R;
import com.dayang.cmtools.bean.CommonResultInfo;
import com.dayang.cmtools.dialog.AlertDialog;
import com.dayang.cmtools.dialog.UpdateDialog;
import com.dayang.cmtools.service.UpdateService;
import com.dayang.cmtools.utils.GsonUtils;
import com.dayang.cmtools.utils.JsonUtils;
import com.dayang.cmtools.utils.LogUtils;
import com.dayang.cmtools.utils.PermissionUtil;
import com.dayang.cmtools.widget.WidgetManager;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;

import static android.R.attr.description;
import static com.dayang.cmtools.widget.activity.MessageActivity.activity;

/**
 * Created by 冯傲 on 2017/1/9.
 * e-mail 897840134@qq.com
 */

public class MyWebView extends BridgeWebView {

    private WidgetManager manager;
    private Context context;
    private Activity actvity;
    private MediaPlugin mediaPlugin;
    private PermissionUtil permissionUtil;

    public MyWebView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public MyWebView(Context context, WidgetManager manager) {
        super(context);
        this.context = context;
        this.manager = manager;
        init();
    }


    public MyWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    public void setMyWebViewManager(WidgetManager manager) {
        this.manager = manager;
        init();
    }

    public void init() {
        if (manager == null) {
            return;
        }
        WebSettings settings = getSettings();
        settings.setDomStorageEnabled(true);
        actvity = manager.getActvity();
        permissionUtil = manager.getPermissionUtil();
        mediaPlugin = new MediaPlugin(actvity);
        this.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return false;
            }
        });
        registerHandler("saveKVData", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    mediaPlugin.saveKVData(data, function);
                } catch (Exception e) {
                    LogUtils.e("handler: " + e.toString());
                }
            }
        });
        registerHandler("getKVData", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    mediaPlugin.getKVData(data, function);
                } catch (Exception e) {
                    LogUtils.e("handler: " + e.toString());
                }
            }
        });
        registerHandler("delKVData", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    mediaPlugin.delKVData(data, function);
                } catch (Exception e) {
                    LogUtils.e("handler: " + e.toString());
                }
            }
        });
        registerHandler("selectFiles", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    mediaPlugin.selectFiles(data, function, permissionUtil);
                } catch (Exception e) {
                    LogUtils.e("handler: " + e.toString());
                }
            }
        });
        registerHandler("joinMeeting", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                //TODO joinMeeting
            }
        });
        registerHandler("checkUpdate", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    mediaPlugin.checkUpdate(data, function);
                } catch (Exception e) {
                    LogUtils.e("handler: " + e.toString());
                }
            }
        });
        registerHandler("uploadFiles", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    mediaPlugin.uploadFiles(data, function, manager);
                } catch (Exception e) {
                    LogUtils.e("handler: " + e.toString());
                }
            }
        });
        registerHandler("getMediaBase64Infos", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    mediaPlugin.getMediaBase64Infos(data, function);
                } catch (Exception e) {
                    LogUtils.e("handler: " + e.toString());
                }
            }
        });
        registerHandler("browseMedia", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    mediaPlugin.browseMedia(data, function);
                } catch (Exception e) {
                    LogUtils.e("handler: " + e.toString());
                }
            }
        });
        registerHandler("getLocations", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    mediaPlugin.getLocations(data, function);
                } catch (Exception e) {
                    LogUtils.e("handler: " + e.toString());
                }
            }
        });
        registerHandler("getThumbnails", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    mediaPlugin.getThumbnails(data, function);
                } catch (Exception e) {
                    LogUtils.e("handler: " + e.toString());
                }
            }
        });
        registerHandler("setUserProfile", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    mediaPlugin.setUserProfile(data, function, permissionUtil);
                } catch (Exception e) {
                    LogUtils.e("handler: " + e.toString());
                }
            }
        });
        registerHandler("takeMedia", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    mediaPlugin.doMedia(data, function, permissionUtil);
                } catch (Exception e) {
                    LogUtils.e("handler: " + e.toString());
                }
            }
        });
        registerHandler("pickMediaFiles", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    mediaPlugin.selectFiles(data, function, permissionUtil);
                } catch (Exception e) {
                    LogUtils.e("handler: " + e.toString());
                }
            }
        });
        registerHandler("playMedia", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    mediaPlugin.playMedia(data, function);
                } catch (Exception e) {
                    LogUtils.e("handler: " + e.toString());
                }
            }
        });
        registerHandler("doMedia", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    mediaPlugin.doMedia(data, function, permissionUtil);
                } catch (Exception e) {
                    LogUtils.e("handler: " + e.toString());
                }
            }
        });
        registerHandler("clearCache", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    mediaPlugin.clearCache(data, function, MyWebView.this);
                } catch (Exception e) {
                    LogUtils.e("handler: " + e.toString());
                }
            }
        });
        registerHandler("checkjsVersion", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    mediaPlugin.checkjsVersion(data, function);
                } catch (Exception e) {
                    LogUtils.e("handler: " + e.toString());
                }
            }
        });
        registerHandler("videoPreviewEdit", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    mediaPlugin.videoPreviewEdit(data, function);
                } catch (Exception e) {
                    LogUtils.e("handler: " + e.toString());
                }
            }
        });
        registerHandler("dialNumberUrl", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    mediaPlugin.dialNumberUrl(data, function, permissionUtil);
                } catch (Exception e) {
                    LogUtils.e("handler: " + e.toString());
                }
            }
        });
        registerHandler("sendMessageUrl", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    mediaPlugin.sendMessageUrl(data, function, permissionUtil);
                } catch (Exception e) {
                    LogUtils.e("handler: " + e.toString());
                }
            }
        });
        registerHandler("startUpdateLocation", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    mediaPlugin.startUpdateLocation(data, function);
                } catch (Exception e) {
                    LogUtils.e("handler: " + e.toString());
                }
            }
        });
        registerHandler("stopUpdateLocation", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    mediaPlugin.stopUpdateLocation(data, function);
                } catch (Exception e) {
                    LogUtils.e("handler: " + e.toString());
                }
            }
        });
        registerHandler("querySkip", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    mediaPlugin.querySkip(data, function, manager);
                } catch (Exception e) {
                    LogUtils.e("handler: " + e.toString());
                }
            }
        });
        registerHandler("setWebview", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    mediaPlugin.setWebview(data, function, MyWebView.this, manager);
                } catch (Exception e) {
                    LogUtils.e("handler: " + e.toString());
                }
            }
        });
        registerHandler("exit", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    mediaPlugin.exit(data, function);
                } catch (Exception e) {
                    LogUtils.e("handler: " + e.toString());
                }
            }
        });
        registerHandler("updatePortal", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    mediaPlugin.updatePortal(data, function);
                } catch (Exception e) {
                    LogUtils.e("handler: " + e.toString());
                }
            }
        });
        registerHandler("getLoginPath", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    mediaPlugin.getLoginPath(data, function);
                } catch (Exception e) {
                    LogUtils.e("handler: " + e.toString());
                }
            }
        });
        registerHandler("listMeeting", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    mediaPlugin.listMeeting(data, function, permissionUtil);
                } catch (Exception e) {
                    LogUtils.e("handler: " + e.toString());
                }
            }
        });
        registerHandler("bindGeTuiCid", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    mediaPlugin.bindGeTuiCid(data, function);
                } catch (Exception e) {
                    LogUtils.e("handler: " + e.toString());
                }
            }
        });
        registerHandler("setScrollIndicator", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    mediaPlugin.setUserProfile(data, function, permissionUtil);
                } catch (Exception e) {
                    LogUtils.e("handler: " + e.toString());
                }
            }
        });
        registerHandler("setScrollIndicator", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                if (data == null || data.equals("")) {
                    function.onCallBack(GsonUtils.toJson(new CommonResultInfo("false", "参数错误")));
                    return;
                }
                String scrollIndicator = JsonUtils.getString(data, "scrollIndicator");
                switch (scrollIndicator) {
                    case "":
                        function.onCallBack(GsonUtils.toJson(new CommonResultInfo("false", "参数错误")));
                        break;
                    case "all":
                        setHorizontalScrollBarEnabled(true);
                        setVerticalScrollBarEnabled(true);
                        break;
                    case "vertical":
                        setHorizontalScrollBarEnabled(false);
                        setVerticalScrollBarEnabled(true);
                        break;
                    case "horizontal":
                        setHorizontalScrollBarEnabled(true);
                        setVerticalScrollBarEnabled(false);
                        break;
                    case "none":
                        setHorizontalScrollBarEnabled(false);
                        setVerticalScrollBarEnabled(false);
                        break;
                }
            }
        });
        registerHandler("createWindow", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                if (data == null || data.equals("")) {
                    function.onCallBack(GsonUtils.toJson(new CommonResultInfo("false", "参数错误")));
                    return;
                }
                String webUrl = getUrl();
                String id = JsonUtils.getString(data, "id");
                MyWebView webviewById = manager.getWebviewById(id);
                String url = JsonUtils.getString(data, "url");
                if (url.startsWith("https://") || url.startsWith("http://") || url.startsWith("file://")) {

                } else if (url.startsWith("../")) {
                    char[] chars = webUrl.toCharArray();
                    ArrayList<Integer> intList = new ArrayList<>();
                    for (int i = 0; i < chars.length; i++) {
                        if (chars[i] == '/') {
                            intList.add(i);
                        }
                    }
                    int count = 0;
                    while (url.startsWith("../")) {
                        url = url.substring(3);
                        count++;
                    }
                    int index = intList.get(intList.size() - 1 - count);
                    String substring = webUrl.substring(0, index);
                    url = substring + "/" + url;
                } else {
                    char[] chars = webUrl.toCharArray();
                    int index = chars.length;
                    for (int i = 0; i < chars.length; i++) {
                        if (chars[i] == '/') {
                            index = i;
                        }
                    }
                    String substring = webUrl.substring(0, index);
                    url = substring + "/" + url;
                    LogUtils.i("handler: " + url);
                }
                if (webviewById != null) {
                    webviewById.loadUrl(url);
                } else {
                    manager.createWebview(id, url);
                }
            }
        });
        registerHandler("showWindow", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                String id = JsonUtils.getString(data, "id");
                String zindex = JsonUtils.getString(data, "zindex");
                int index = 1;
                if (zindex.equals("0")) {
                    index = 0;
                }
                if (zindex.equals("-1")) {
                    index = -1;
                }
                manager.showWebveiw(id, index);
            }
        });
        registerHandler("goHome", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Set<String> allWebviews = manager.getAllWebviews();
                ArrayList<String> strings1 = new ArrayList<>(allWebviews);
                for (String webId : strings1) {
                    if (!webId.equals("index")) {
                        manager.removeWebview(webId);
                    }
                }
            }
        });
        registerHandler("closeWindow", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                if (data == null) {
                    String webviewId = manager.getWebviewId(MyWebView.this);
                    manager.removeWebview(webviewId);
                } else {
                    String id = JsonUtils.getString(data, "id");
                    if (id.equals("")) {
                        id = manager.getWebviewId(MyWebView.this);
                    }
                    manager.removeWebview(id);
                }
            }
        });
        registerHandler("evalJS", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                String id = JsonUtils.getString(data, "id");
                String js = JsonUtils.getString(data, "js");
                MyWebView webviewById = manager.getWebviewById(id);
                if (webviewById != null) {
                    webviewById.loadUrl("javascript:" + js);
                }
            }
        });
        registerHandler("getAllWebview", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                JSONObject jsonObject = new JSONObject();
                Set<String> allWebviews = manager.getAllWebviews();
                try {
                    jsonObject.put("ids", allWebviews);
                    function.onCallBack(jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        registerHandler("getWebviewById", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {

            }
        });
        registerHandler("getWebviewId", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                String webviewId = manager.getWebviewId(MyWebView.this);
                if (webviewId == null) {
                    webviewId = "";
                }
                function.onCallBack("{\"id\":\"" + webviewId + "\"}");
            }
        });
        registerHandler("showWaiting", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                String text = JsonUtils.getString(data, "text");
                manager.showLoading(text);
            }
        });
        registerHandler("removeWaiting", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                manager.removeLoading();
            }
        });
        registerHandler("webviewGoTop", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                String id = JsonUtils.getString(data, "id");
                if (!id.equals("")) {
                    MyWebView webviewById = manager.getWebviewById(id);
                    if (webviewById != null) {
                        manager.webviewGoTop(webviewById);
                    }
                } else {
                    manager.webviewGoTop(MyWebView.this);
                }
            }
        });
        registerHandler("setWebviewInvisible", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                manager.setWebviewInvisible(MyWebView.this);
            }
        });
        registerHandler("setWebviewVisible", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                manager.setWebviewVisible(MyWebView.this);
            }
        });
        registerHandler("quit", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                manager.quit();
            }
        });
        registerHandler("overWriteAndroidBackKey", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                String id = JsonUtils.getString(data, "id");
                String js = JsonUtils.getString(data, "js");
                if (id.equals("")) {
                    manager.setOverWriteAndroidBackKey(MyWebView.this, js);
                } else {
                    MyWebView webviewById = manager.getWebviewById(id);
                    if (webviewById != null) {
                        manager.setOverWriteAndroidBackKey(webviewById, js);
                    } else {
                        LogUtils.e("handler: id输入错误");
                    }
                }
            }
        });
        registerHandler("clearWebviews", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                String webviewId = manager.getWebviewId(MyWebView.this);
                manager.clearWebviews(webviewId);
                return;
            }
        });
        registerHandler("toast", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                if (data == null) {
                    data = "";
                }
                String text = JsonUtils.getString(data, "text");
                Toast.makeText(actvity, text, 0).show();
            }
        });
    }
}
