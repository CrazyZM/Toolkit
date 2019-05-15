package com.android.toolkitlibrary.network.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.toolkitlibrary.network.base.FFApplication;


/**
 * Created by Administrator on 2016/6/5.
 */
public class PreUtils {

    private static SharedPreferences get() {
        return FFApplication.app.getSharedPreferences(FFApplication.preFileName, Context.MODE_PRIVATE);
    }

    public static void setString(String key, String value) {
        if (StringUtils.isEmpty(key)) {
            return;
        }
        get().edit().putString(key, value).apply();
    }

    public static String getString(String key, String defaultValue) {
        if (StringUtils.isEmpty(key)) {
            return defaultValue;
        }
        return get().getString(key, defaultValue);
    }

    public static void setInt(String key, int value) {
        if (StringUtils.isEmpty(key)) {
            return;
        }
        get().edit().putInt(key, value).apply();
    }

    public static int getInt(String key, int defaultValue) {
        if (StringUtils.isEmpty(key)) {
            return defaultValue;
        }
        return get().getInt(key, defaultValue);
    }

    public static void setFloat(String key, float value) {
        if (StringUtils.isEmpty(key)) {
            return;
        }
        get().edit().putFloat(key, value).apply();
    }

    public static float getFloat(String key, float defaultValue) {
        if (StringUtils.isEmpty(key)) {
            return defaultValue;
        }
        return get().getFloat(key, defaultValue);
    }

    public static void setLong(String key, long value) {
        if (StringUtils.isEmpty(key)) {
            return;
        }
        get().edit().putLong(key, value).apply();
    }

    public static long getLong(String key, long defaultValue) {
        if (StringUtils.isEmpty(key)) {
            return defaultValue;
        }
        return get().getLong(key, defaultValue);
    }

    public static void setBoolean(String key, boolean value) {
        if (StringUtils.isEmpty(key)) {
            return;
        }
        get().edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        if (StringUtils.isEmpty(key)) {
            return defaultValue;
        }
        return get().getBoolean(key, defaultValue);
    }

    public static void clearAll() {
        get().edit().clear().apply();
    }

    public static void romoveKey(String key) {
        if (!StringUtils.isEmpty(key)) {
            get().edit().remove(key).apply();
        }

    }
}
