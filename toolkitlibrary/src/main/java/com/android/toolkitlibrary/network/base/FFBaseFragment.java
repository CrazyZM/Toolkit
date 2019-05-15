package com.android.toolkitlibrary.network.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.toolkitlibrary.dialog.Loading;
import com.android.toolkitlibrary.network.http.FFNetWork;
import com.android.toolkitlibrary.network.http.FFNetWorkCallBack;
import com.android.toolkitlibrary.network.utils.FFLogUtil;

import java.io.File;
import java.util.Map;

public abstract class FFBaseFragment extends Fragment implements FFActivity {
    private FFNetWork mNet;
    private boolean mIsDestroyed = false;
    private String mTAG;
    private Loading pd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int contentViewId = getContentViewId();
        mTAG = getClass().getSimpleName();
        mNet = new FFNetWork(this);
        e("onCreate");
        // 初始化View
        findView();
        initView();
        setListener();
        afterCreate();
        return inflater.inflate(contentViewId, container, false);
    }


    @Override
    public void showToast(Object msg) {
        if (msg != null) {
            FFApplication.showToast(msg.toString());
        }
    }


    @Override
    public void showProgressDialog(String word) {
        if (pd == null) {
            pd = new Loading(getContext());
        }
        pd.setMessage(word);
        if (!pd.isShowing() && !mIsDestroyed) {
            pd.show();
        }
    }

    @Override
    public void dismissProgressDialog() {
        if (pd != null && pd.isShowing()) {
            try {
                pd.dismiss();
            } catch (IllegalArgumentException e) {
                pd = null;
            }
        }
    }

    @Override
    public void d(String log) {
        FFLogUtil.d(mTAG, log);
    }

    @Override
    public void e(String log) {
        FFLogUtil.e(mTAG, log);
    }

    @Override
    public void i(String log) {
        FFLogUtil.i(mTAG, log);
    }

    @Override
    public <T> void post(String url, String words, Class<T> clazz,
                         FFNetWorkCallBack<T> callBack, Object... param) {
        mNet.post(url, words, callBack, clazz, param);
    }

    @Override
    public <T> void postFile(String url, String words, Map<String, File> files, Class<T> clazz,
                             FFNetWorkCallBack<T> callBack, Object... param) {
        mNet.postFile(url, words, files, callBack, clazz, param);
    }

    @Override
    public <T> void get(String url, String words, Class<T> clazz,
                        FFNetWorkCallBack<T> callBack, Object... param) {
        mNet.get(url, words, callBack, clazz, param);
    }

    public void downFile(final String url, final String destFileDir, final FFNetWork.OnDownloadListener listener) {
        mNet.downFile(url, destFileDir, listener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        d("onDestroy");
        setDestroyed(true);
        mNet.onDestory();
    }

    @Override
    public boolean getDestroyed() {
        return mIsDestroyed;
    }

    public void setDestroyed(boolean isDestroyed) {
        this.mIsDestroyed = isDestroyed;
    }

    @Override
    public void removeAllMenu() {

    }

    @Override
    public void startTransaction(boolean containThis) {

    }

    @Override
    public void endTransaction(boolean finishThis) {

    }
}
