package com.android.toolkitlibrary.network.base;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.android.toolkitlibrary.network.http.FFNetWork;
import com.android.toolkitlibrary.network.http.FFNetWorkCallBack;
import com.android.toolkitlibrary.network.utils.FFLogUtil;
import com.android.toolkitlibrary.network.utils.StringUtils;


public abstract class FFApplication extends Application {

    public static FFApplication app;
    private static Thread mUiThread;
    public static boolean DEBUG = true;
    public static String preFileName = "toolkit";
    public FFNetWork net;

    @Override
    public void onCreate() {
        super.onCreate();
        net = new FFNetWork(null);
        handler = new Handler();
        mUiThread = Thread.currentThread();
        app = this;
    }

    public static Activity getTopActivity() {

        return FFActivity.allActivities.isEmpty() ? null
                : FFActivity.allActivities.get(0);
    }

    public static <T> void post(String url, Class<T> clazz,
                                FFNetWorkCallBack<T> callBack, Object... param) {
        app.net.post(url, null, callBack, clazz, param);
    }

    public static <T> void get(String url, Class<T> clazz,
                               FFNetWorkCallBack<T> callBack, Object... param) {
        app.net.get(url, null, callBack, clazz, param);
    }

    public static <T> void post_synchronized(String url, Class<T> clazz,
                                             FFNetWorkCallBack<T> callBack, Object... param) {
        app.net.post_synchronized(url, null, callBack, clazz, param);
    }

    public static <T> void get_synchronized(String url, Class<T> clazz,
                                            FFNetWorkCallBack<T> callBack, Object... param) {
        app.net.get_synchronized(url, null, callBack, clazz, param);
    }

    public static void showToast(final String msg) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            FFLogUtil.e("FFApplication", "主线程");
            if (!StringUtils.isEmpty(msg)) {
                Toast.makeText(app, msg, Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            getHandler().post(new Runnable() {
                @Override
                public void run() {
                    FFLogUtil.e("FFApplication", "非主线程");
                    if (msg != null) {
                        Toast.makeText(app, msg,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public static void runOnUiThread(Runnable runnable) {
        if (Thread.currentThread() == mUiThread) {
            FFLogUtil.e("FFApplication", "主线程");
            runnable.run();
        } else {
            getHandler().post(runnable);
        }
    }

    private static Handler handler;

    public static Handler getHandler() {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        return handler;
    }

    public static boolean isDEBUG() {
        return DEBUG;
    }

    public static void setDEBUG(boolean DEBUG) {
        FFApplication.DEBUG = DEBUG;
    }

    public static String getPreFileName() {
        return preFileName;
    }

    public static void setPreFileName(String preFileName) {
        FFApplication.preFileName = preFileName;
    }
}
