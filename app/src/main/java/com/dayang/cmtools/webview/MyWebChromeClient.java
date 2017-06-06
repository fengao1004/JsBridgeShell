package com.dayang.cmtools.webview;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.WindowManager;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.dayang.cmtools.bean.CommonResultInfo;
import com.dayang.cmtools.bean.WebviewIds;
import com.dayang.cmtools.dialog.AlertDialog;
import com.dayang.cmtools.utils.GsonUtils;
import com.dayang.cmtools.utils.JsonUtils;
import com.dayang.cmtools.utils.LogUtils;
import com.dayang.cmtools.utils.SharedPreferencesUtils;
import com.dayang.cmtools.widget.WidgetManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.dayang.cmtools.widget.activity.MessageActivity.activity;

/**
 * Created by 冯傲 on 2017/3/29.
 * e-mail 897840134@qq.com
 */

public class MyWebChromeClient extends WebChromeClient {
    private Activity activity;
    private MyWebView webView;
    private WidgetManager manager;
    private final Map<String, CallBack> handlerMap;

    public MyWebChromeClient(WidgetManager manager, MyWebView webView) {
        handlerMap = new HashMap<>();
        this.manager = manager;
        this.activity = manager.getActvity();
        this.webView = webView;
        initHandlerMap();
    }

    private void initHandlerMap() {
        handlerMap.put("saveKVData", new CallBack() {
            @Override
            public void handler(String params, JsPromptResult result) {
                saveKVData(params, result);
            }
        });
        handlerMap.put("getKVData", new CallBack() {
            @Override
            public void handler(String params, JsPromptResult result) {
                getKVData(params, result);
            }
        });
        handlerMap.put("delKVData", new CallBack() {
            @Override
            public void handler(String params, JsPromptResult result) {
                delKVData(params, result);
            }
        });
        handlerMap.put("alert", new CallBack() {
            @Override
            public void handler(String params, JsPromptResult result) {
                alert(params, result);
            }
        });
        handlerMap.put("createWindow", new CallBack() {
            @Override
            public void handler(String data, JsPromptResult result) {
                String webUrl = webView.getUrl();
                String id = JsonUtils.getString(data, "id");
                String overrideBackKey = JsonUtils.getString(data, "overrideBackKey");
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
                    result.confirm(GsonUtils.toJson(new CommonResultInfo("false", "id存在直接刷新网页")));
                } else {
                    MyWebView webview = manager.createWebview(id, url);
                    if (overrideBackKey.equals("false")) {
                        manager.setOverWriteAndroidBackKey(webview, ";");
                    }
                    result.confirm(GsonUtils.toJson(new CommonResultInfo("true", "创建成功")));
                }
            }
        });
        handlerMap.put("showWindow", new CallBack() {
            @Override
            public void handler(String data, JsPromptResult result) {
                String id = JsonUtils.getString(data, "id");
                String zindex = JsonUtils.getString(data, "zindex");
                int index = 1;
                if (zindex.equals("0")) {
                    index = 0;
                }
                if (zindex.equals("-1")) {
                    index = -1;
                }
                boolean b = manager.showWebveiw(id, index);
                if (b) {
                    result.confirm(GsonUtils.toJson(new CommonResultInfo("true", "创建成功")));
                } else {
                    result.confirm(GsonUtils.toJson(new CommonResultInfo("false", "id错误")));
                }
            }
        });
        handlerMap.put("closeWindow", new CallBack() {
            @Override
            public void handler(String data, JsPromptResult result) {
                if (data == null) {
                    String webviewId = manager.getWebviewId(webView);
                    manager.removeWebview(webviewId);
                } else {
                    String id = "";
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(data);
                        JSONArray ids = jsonObject.getJSONArray("ids");
                        for (int i = 0; i < ids.length(); i++) {
                            String string = ids.getString(i);
                            manager.removeWebview(string);
                        }
                        if (ids.length() == 0) {
                            throw new Exception("没有ids数据");
                        }
                        result.confirm(GsonUtils.toJson(new CommonResultInfo("true", "关闭成功")));
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (id.equals("")) {
                        id = manager.getWebviewId(webView);
                    }
                    manager.removeWebview(id);
                }
                result.confirm(GsonUtils.toJson(new CommonResultInfo("true", "关闭成功")));
            }
        });
        handlerMap.put("getAllWebview", new CallBack() {
            @Override
            public void handler(String data, JsPromptResult result) {
                JSONObject jsonObject = new JSONObject();
                Set<String> allWebviews = manager.getAllWebviews();
                List<String> list = new ArrayList<String>();
                for (String s : allWebviews) {
                    list.add(s);
                }
                WebviewIds ids = new WebviewIds();
                ids.setIds(list);
                result.confirm(GsonUtils.toJson(ids));
            }
        });
        handlerMap.put("getWebviewId", new CallBack() {
            @Override
            public void handler(String params, JsPromptResult result) {
                String webviewId = manager.getWebviewId(webView);
                if (webviewId == null) {
                    webviewId = "";
                }
                result.confirm("{\"id\":\"" + webviewId + "\"}");
            }
        });
        handlerMap.put("alert", new CallBack() {
            @Override
            public void handler(String params, JsPromptResult result) {
                alert(params, result);
            }
        });
        handlerMap.put("getNetworkState", new CallBack() {
            @Override
            public void handler(String params, JsPromptResult result) {
                boolean b = checkNetWorking();
                result.confirm("{\"networkState\":\"" + b + "\"}");
            }
        });
    }

    public boolean checkNetWorking() {
        boolean isWork = false;
        ConnectivityManager connectivity = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        if (info == null) {
            return isWork;
        } else {
            isWork = info.isAvailable();
        }
        return isWork;
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        if (message.startsWith("jsBridge://")) {
            LogUtils.i("onJsPrompt: message   " + message);
            String substring = message.substring(11);
            LogUtils.i("onJsPrompt: substring   " + substring);
            String funName = JsonUtils.getString(substring, "funName");
            String params = JsonUtils.getString(substring, "params");
            LogUtils.i("onJsPrompt: funName  " + funName);
            LogUtils.i("onJsPrompt: params  " + params);
            CallBack callBack = handlerMap.get(funName);
            if (callBack != null) {
                callBack.handler(params, result);
                return true;
            } else {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        } else {
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }
    }

    /**
     * 存储键值对
     *
     * @return
     */
    public void saveKVData(String params, JsPromptResult jr) {
        String key = JsonUtils.getString(params, "key");
        String value = JsonUtils.getString(params, "value");
        SharedPreferencesUtils.setParam(activity, key, value);
        jr.confirm("{\"success\":\"true\"}");
    }

    /**
     * 获取键值对
     *
     * @return
     */
    public void getKVData(String params, JsPromptResult jr) {
        String key = JsonUtils.getString(params, "key");
        String param = SharedPreferencesUtils.getParam(activity, key, "");
        GetKVDataInfo info = new GetKVDataInfo();
        info.setSuccess("true");
        info.setDescription("获取成功");
        info.setContent(param);
        String s = GsonUtils.toJson(info);
        jr.confirm(s);
    }

    /**
     * 删除键值对
     *
     * @return
     */
    public void delKVData(String params, JsPromptResult jr) {
        String key = JsonUtils.getString(params, "key");
        SharedPreferencesUtils.setParam(activity, key, "");
        jr.confirm("{\"success\":\"true\"}");
    }

    /**
     * 删除键值对
     *
     * @return
     */
    public void alert(String params, final JsPromptResult jr) {
        if (params == null) {
            params = "";
        }
        String text = JsonUtils.getString(params, "text");
        if (text.equals("")) {
            text = params;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setContant(text);
        builder.setTitle("记者助手");
        final AlertDialog dialog = builder.create();
        builder.setOnClick(new AlertDialog.onUpClickListener() {
            @Override
            public void onEnterClick() {
                dialog.dismiss();
                jr.confirm("");
            }
        });
        dialog.show();
    }

    interface CallBack {
        void handler(String params, JsPromptResult result);
    }

    class GetKVDataInfo {
        /**
         * success : true
         * description :
         * content : 先前存入的value的值
         */

        private String success;
        private String description;
        private String content;

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

}
