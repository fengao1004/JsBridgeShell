package com.dayang.cmtools.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by 冯傲 on 2017/3/20.
 * e-mail 897840134@qq.com
 */

public class GsonUtils {
    /**
     * @param bean
     * @return String 返回类型
     * @Title: toJson
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @throws：
     */
    public static String toJson(Object bean) {
        Gson gson = new GsonBuilder()
                .create();
        return gson.toJson(bean);
    }

    public static String toJson(Object bean, Type type) {
        Gson gson = new GsonBuilder()
                .create();
        return gson.toJson(bean, type);
    }

    /**
     * @param <T>
     * @param json
     * @param type
     * @return T 返回类型
     * @Title: fromJson
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @throws：
     */
    public static Object fromJson(String json, Type type) {
        Gson gson = new GsonBuilder()
                .create();
        return gson.fromJson(json, type);
    }

    /**
     * @param <T>
     * @param json
     * @param classOfT
     * @return T 返回类型
     * @Title: fromJson
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @throws：
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        Gson gson  = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
        return gson.fromJson(json, classOfT);
    }

    static class StringNullAdapter extends TypeAdapter<String> {
        @Override
        public String read(JsonReader reader) throws IOException {
            // TODO Auto-generated method stub
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return "";
            }
            return reader.nextString();
        }

        @Override
        public void write(JsonWriter writer, String value) throws IOException {
            // TODO Auto-generated method stub
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }
    }

    static class NullStringToEmptyAdapterFactory<T> implements TypeAdapterFactory {
        @SuppressWarnings("unchecked")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType != String.class) {
                return null;
            }
            return (TypeAdapter<T>) new StringNullAdapter();
        }
    }
}
