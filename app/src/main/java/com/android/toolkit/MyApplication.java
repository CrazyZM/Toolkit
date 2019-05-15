package com.android.toolkit;


import android.os.Handler;
import android.os.Looper;

import com.android.toolkitlibrary.network.base.FFApplication;


public class MyApplication extends FFApplication {
    public static MyApplication app;
    private static Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        init();
        setDEBUG(true);
    }

    public static MyApplication getInstance() {
        return app;
    }

    public void init() {
        handler = new Handler(Looper.getMainLooper());
    }

    public static void removeCallbacks(Runnable task) {
        handler.removeCallbacks(task);
    }

    public static void post(Runnable task) {
        handler.post(task);
    }

    public static void postDelay(Runnable task, long delay) {
        handler.postDelayed(task, delay);
    }

}
