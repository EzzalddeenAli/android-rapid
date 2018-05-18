package com.android.frameproj.library.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by lh on 2018/5/18.
 */

public class GsonUtil<T> {

    public <T> T fromJson(String string) {
        Type listType = new TypeToken<T>() {
        }.getType();
        T t = new Gson().fromJson(string, listType);
        return t;
    }

}
