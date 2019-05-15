package com.android.toolkitlibrary.network.base;

import android.app.Activity;

import com.android.toolkitlibrary.network.http.FFNetWorkCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;


public interface FFActivity {
    public final static ArrayList<Activity> allActivities = new ArrayList<Activity>();
    public final static ArrayList<Activity> transactionActivities = new ArrayList<Activity>();



    /**
     * 判断当前activity是否finish了
     *
     * @return
     */
    boolean getDestroyed();

    /**
     * 显示吐司
     * @param msg
     */
    void showToast(Object msg);

    void removeAllMenu();

    /**
     * 显示ProgressDialog
     *
     * @param word
     */
    void showProgressDialog(String word);

    /**
     * 使ProgressDialog消失
     */
    void dismissProgressDialog();

    /**
     * 使用logUtil打日志
     *
     * @param log
     */
    void d(String log);

    /**
     * 使用logUtil打日志
     *
     * @param log
     */
    void e(String log);

    /**
     * 使用logUtil打日志
     *
     * @param log
     */
    void i(String log);

    /**
     * 获得ContentView
     *
     * @return
     */
    int getContentViewId();

    /**
     * findView
     */
    void findView();

    /**
     * 初始化所有View
     */
    void initView();

    /**
     * 设置监听
     */
    void setListener();

    /**
     * Create之后执行
     */
    void afterCreate();

    /**
     * 异步网络请求post
     *
     * @param url
     * @param words    要在ProgressDialog显示的文字
     * @param clazz    请求获取的结果类型
     * @param callBack
     * @param param    K，V，K，V...
     */
    <T> void post(String url, String words, Class<T> clazz, FFNetWorkCallBack<T> callBack,
                  Object... param);

    /**
     * 异步网络上传文件
     * @param url
     * @param words    要在ProgressDialog显示的文字
     * @param clazz    请求获取的结果类型
     * @param callBack
     * @param param    K，V，K，V...
     */
    <T> void postFile(String url, String words, Map<String, File> files, Class<T> clazz, FFNetWorkCallBack<T> callBack,
                      Object... param);

    /**
     * 异步网络请求get
     *
     * @param url
     * @param words    要在ProgressDialog显示的文字
     * @param clazz    请求获取的结果类型
     * @param callBack
     * @param param
     */
    <T> void get(String url, String words, Class<T> clazz, FFNetWorkCallBack<T> callBack,
                 Object... param);

    /**
     * 开启事务 完成后回到开始时的Activity
     *
     * @param containThis 完成时是否关闭当前Activity true为关闭
     */
    public void startTransaction(boolean containThis);

    /**
     * 结束事务
     *
     * @param finishThis 结束事务时是否关闭当前activity
     */
    public void endTransaction(boolean finishThis);
}
