package com.dayang.pickmediafile;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.Timestamp;

import static org.junit.Assert.assertEquals;

/**
 * Created by 冯傲 on 2017/4/7.
 * e-mail 897840134@qq.com
 */

public class TestDemo {
    @Test
    public void addition_isCorrect() throws Exception {

        String json = "{\"a\":\"a\",\"b\":\"b\"}";
//        Gson gson = new GsonBuilder().serializeNulls().create();
//        Testtt test = gson.fromJson(json, Testtt.class);
//        String s = gson.toJson(test);
        Gson gson = new GsonBuilder().registerTypeAdapter( Testtt.class,new TimestampAdapter()).create();
//        Gson gson = new GsonBuilder().create();
//        Gson gson  = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
//然后用上面一行写的gson来序列化和反序列化实体类type
        Testtt test = gson.fromJson(json, Testtt.class);
        String s = gson.toJson(test);
        System.out.println("1111    "+test.toString());
        System.out.println("2222    "+s);
    }

    class Testtt {
        String a = "";
        String b = "";
        String c = "";

        @Override
        public String toString() {
            return "Testtt{" +
                    "a='" + a + '\'' +
                    ", b='" + b + '\'' +
                    ", c='" + c + '\'' +
                    '}';
        }
    }

    class TimestampAdapter implements JsonSerializer<Testtt>, JsonDeserializer<Testtt> {

        @Override
        public Testtt deserialize(JsonElement json, Type typeOfT,JsonDeserializationContext context) throws JsonParseException {
            System.out.println(json.toString());
            if(json == null){
                return null;
            } else {
                try {
                    return new Testtt();
                } catch (Exception e) {
                    return null;
                }
            }
        }

        @Override
        public JsonElement serialize(Testtt src, Type typeOfSrc,
                                     JsonSerializationContext context) {
            System.out.println(12313123);
            String value = "";
            if(src != null){
                value = String.valueOf(src);
            }
            return new JsonPrimitive(value);
        }

    }
}
