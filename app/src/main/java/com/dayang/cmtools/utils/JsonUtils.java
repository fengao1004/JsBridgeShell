package com.dayang.cmtools.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 冯傲 on 2017/3/13.
 * e-mail 897840134@qq.com
 */

public class JsonUtils {
    public static String getString(String json, String key) {
        String value = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            value = jsonObject.getString(key);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.i("get: " + e.toString());
            return value;
        }
    }
}
