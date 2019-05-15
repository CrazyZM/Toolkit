package com.android.toolkitlibrary.network.http;

import com.android.toolkitlibrary.network.base.FFApplication;
import com.android.toolkitlibrary.network.base.FFResponseCode;
import com.android.toolkitlibrary.network.utils.FFLogUtil;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.Map;


public class FFNetWorkRequest<T> {
    private static final String TAG = "FFNetWorkRequest";
    private Object[] params;
    private String url;
    private FFNetWorkCallBack<T> callBack;
    private FFResponseCode status;
    private T entity;
    private FFNetWork net;
    private String word;
    private String errMessage;
    private Map<String, File> files;

    private boolean is_synchronized;

    private Class<T> clazz;

    public FFNetWorkRequest(FFNetWork net, String word, String url,
                            FFNetWorkCallBack<T> callBack, Class<T> clazz, Map<String, File> files, Object[] param,
                            boolean is_synchronized) {
        this.clazz = clazz;
        this.is_synchronized = is_synchronized;
        this.url = url;
        this.net = net;
        if (word != null && word.length() == 0) {
            word = "加载中..";
        }
        this.word = word;
        this.callBack = callBack;
        this.params = param;
        this.setStatus(FFResponseCode.UNSET, "未知");
        this.files = files;
        excute();
    }

    public void excute() {
        if (is_synchronized) {
            try {
                requestNet();
            } catch (Exception e) {
                e.printStackTrace();
                FFLogUtil.e(TAG, e);
                return;
            }
            try {
                end();
            } catch (Exception e) {
                e.printStackTrace();
                FFLogUtil.e(TAG, e);
                return;
            }
            return;
        }
        if (net.getDestroyed()) {
            return;
        }
        net.es1.submit(new Runnable() {
            public void run() {
                if (net.getDestroyed()) {
                    return;
                }
                showDialog();
                requestNet();
                FFApplication.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissDialog();
                        try {
                            end();
                        } catch (Exception e) {
                            e.printStackTrace();
                            FFLogUtil.e(TAG, e);
                            return;
                        }
                    }

                });
            }


        });
    }

    private void showDialog() {
        FFApplication.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (word != null && !net.getDestroyed()
                        && net.activity != null) {
                    net.activity.showProgressDialog(word);
                }
            }
        });
    }

    private void dismissDialog() {
        if (word != null && net.activity != null) {
            net.activity.dismissProgressDialog();
        }
    }

    private void requestNet() {

        int retryTime = 0;
        boolean success = false;
        boolean needRetry = true;
        while (retryTime < 3 && !success && needRetry) {
            retryTime++;
            try {
                net.excuteHttp(FFNetWorkRequest.this);
                if (status == FFResponseCode.SUCCESS || status == FFResponseCode.ERROR_BY_SERVICE) {
                    success = true;
                    needRetry = false;
                }
            } catch (SocketTimeoutException e) {
                setStatus(FFResponseCode.ERROR_NET_TIMEOUT_S, "响应超时");
                FFLogUtil.e(TAG, e);
                FFLogUtil.e(TAG, "SocketTimeoutException超时重试");
                needRetry = false;
            } catch (ConnectTimeoutException e) {
                setStatus(FFResponseCode.ERROR_NET_TIMEOUT_R, "请求超时");
                FFLogUtil.e(TAG, e);
                FFLogUtil.e(TAG, "ConnectTimeoutException超时重试");
                needRetry = true;
            } catch (UnsupportedEncodingException e) {
                FFLogUtil.e(TAG, e);
                needRetry = false;
            } catch (IOException e) {
                setStatus(FFResponseCode.ERROR_IO, "IO异常" + e.getMessage());
                FFLogUtil.e(TAG, e);
                FFLogUtil.e(TAG, "IOException 重试");
                needRetry = false;
            } catch (Exception e) {
                FFLogUtil.e(TAG, e);
                FFLogUtil.e(TAG, "Exception 重试");
                needRetry = false;
            }
        }
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public FFNetWorkCallBack<T> getCallBack() {
        return callBack;
    }

    public void setCallBack(FFNetWorkCallBack<T> callBack) {
        this.callBack = callBack;
    }

    public FFResponseCode getStatus() {
        return status;
    }

    public void setStatus(FFResponseCode status, String message) {
        this.errMessage = message;
        this.status = status;
    }

    public void end() {
        if (net.getDestroyed()) {
            return;
        }
        FFResponseCode code = getStatus();
        if (code == FFResponseCode.SUCCESS) {
            callBack.success(getEntity());
        } else {
            String msg = callBack.fail(this, getEntity());
            FFApplication.showToast(msg);
        }
    }

    public Map<String, File> getFiles() {
        return files;
    }

    public void setFiles(Map<String, File> files) {
        this.files = files;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

}
