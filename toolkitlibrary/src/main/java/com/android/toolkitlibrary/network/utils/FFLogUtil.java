package com.android.toolkitlibrary.network.utils;

import android.util.Log;

import com.android.toolkitlibrary.network.base.FFApplication;


public class FFLogUtil {
    public static void i(String tag, String message) {
        if (FFApplication.DEBUG) {
            Log.i(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (FFApplication.DEBUG) {
            Log.e(tag, message);
        }
    }

    public static void e(String tag, Throwable ex) {
        e(tag, ex, false);
    }

    private static void e(String tag, Throwable ex, boolean cause) {
        if (FFApplication.DEBUG) {
            FFLogUtil.i(tag, cause ? "Cause by: " + ex.toString() : ex.toString());
            StackTraceElement[] ss = ex.getStackTrace();
            for (StackTraceElement s : ss) {
                String info = s.toString();
                if (info.startsWith("android.app.ActivityThread.access")) {
                    FFLogUtil.i(tag, "...more");
                    break;
                }
                FFLogUtil.i(tag, info);
            }
            Throwable c = ex.getCause();
            if (c != null) {
                e(tag, c, true);
            }
        }
    }

    public static void d(String tag, String message) {
        if (FFApplication.DEBUG) {
            Log.d(tag, message);
        }
    }


}
