package com.android.toolkitlibrary.network.http;

import android.os.Environment;

import com.alibaba.fastjson.JSON;
import com.android.toolkitlibrary.network.base.FFActivity;
import com.android.toolkitlibrary.network.base.FFApplication;
import com.android.toolkitlibrary.network.base.FFResponseCode;
import com.android.toolkitlibrary.network.utils.FFLogUtil;
import com.android.toolkitlibrary.network.utils.FFUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class FFNetWork {
    private static final String TAG = "FFNetWork";
    public ExecutorService es1;
    FFActivity activity;

    public FFNetWork(FFActivity activity) {
        this.activity = activity;
        es1 = Executors.newFixedThreadPool(5);

    }

    public Object a(Class<?> aa) throws InstantiationException, IllegalAccessException {
        return aa.newInstance();
    }

    public <T> void get(String url, String words, FFNetWorkCallBack<T> callBack, Class<T> clazz,
                        Object... params) {
        if (params != null && params.length % 2 == 1) {
            throw new RuntimeException("网络请求传入了单数个参数");
        }
        url = url + FFNetWorkUtils.getGetString(params);
        new FFNetWorkRequest<>(this, words, url, callBack, clazz, null, null, false);
    }

    public <T> void get_synchronized(String url, String words, FFNetWorkCallBack<T> callBack, Class<T> clazz,
                                     Object... params) {
        if (params != null && params.length % 2 == 1) {
            throw new RuntimeException("网络请求传入了单数个参数");
        }
        url = url + FFNetWorkUtils.getGetString(params);
        new FFNetWorkRequest<T>(this, words, url, callBack, clazz, null, null, true);
    }

    public <T> void post(String url, String words, FFNetWorkCallBack<T> callBack, Class<T> clazz,
                         Object... param) {
        if (param != null && param.length % 2 == 1) {
            throw new RuntimeException("网络请求传入了单数个参数");
        }
        new FFNetWorkRequest<>(this, words, url, callBack, clazz, null, param, false);
    }

    public <T> void post_synchronized(String url, String words, FFNetWorkCallBack<T> callBack, Class<T> clazz,
                                      Object... param) {
        if (param != null && param.length % 2 == 1) {
            throw new RuntimeException("网络请求传入了单数个参数");
        }
        new FFNetWorkRequest<>(this, words, url, callBack, clazz, null, param, true);
    }

    public <T> void postFile(String url, String words, Map<String, File> files, FFNetWorkCallBack<T> callBack, Class<T> clazz,
                             Object... param) {
        if (param != null && param.length % 2 == 1) {
            throw new RuntimeException("网络请求传入了单个文件");
        }
        new FFNetWorkRequest<>(this, words, url, callBack, clazz, files, param, false);
    }


    public void downFile(final String url, final String destFileDir, final OnDownloadListener listener) {
        es1.submit(new Runnable() {
            public void run() {
                download_Asyn(url, destFileDir, listener);
            }
        });

    }

    public <T> void excuteHttp(FFNetWorkRequest<T> mRequest)
            throws IOException {
        FFLogUtil.e(TAG + "请求网址", mRequest.getUrl());
        if (!FFUtils.checkNet()) {
            mRequest.setStatus(FFResponseCode.ERROR_NATIVE_NET_CLOST, "网络未连");
            return;
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request;
        if (null == mRequest.getParams() && null == mRequest.getFiles()) {
            //get请求
            request = new Request.Builder().url(mRequest.getUrl())
                    .build();
        } else if (null != mRequest.getFiles()) {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            if (mRequest.getParams() != null) {
                StringBuilder log = new StringBuilder();
                for (int i = 0; i < mRequest.getParams().length; i++) {
                    builder.addFormDataPart(mRequest.getParams()[i].toString(), mRequest.getParams()[i + 1].toString());
                    log.append("{").append(mRequest.getParams()[i]).append(":").append(mRequest.getParams()[i + 1].toString()).append("}");
                    i++;
                }
                FFLogUtil.e("文件参数：", log.toString());
            }

            for (Map.Entry<String, File> entry : mRequest.getFiles().entrySet()) {
                File file = entry.getValue();
                String fileName = file.getName();
                RequestBody requestBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + entry.getKey() + "\"; filename=\"" + fileName + "\""),
                        requestBody);//“f”:是key值
                FFLogUtil.e("上传文件：", fileName);
            }
            RequestBody requestBody = builder.build();
            request = new Request.Builder()
                    .url(mRequest.getUrl())
                    .post(requestBody)
                    .build();

        } else {
            // post请求
            FormBody.Builder builder = new FormBody.Builder();
            StringBuilder post = new StringBuilder();
            for (int i = 0; i < mRequest.getParams().length; i++) {
                builder.add(mRequest.getParams()[i].toString(), mRequest.getParams()[i + 1].toString());
                post.append("{").append(mRequest.getParams()[i]).append(":").append(mRequest.getParams()[i + 1].toString()).append("}");
                i++;
            }
            RequestBody requestBody = builder.build();
            request = new Request.Builder()
                    .url(mRequest.getUrl())
                    .post(requestBody)
                    .build();
            FFLogUtil.e("请求参数：", post.toString());
        }
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        int responseCode = response.code();
        if (responseCode == 200) {
            T responseString;
            assert response.body() != null;
            String string = response.body().string();
            if (FFApplication.DEBUG) {
                FFLogUtil.e("网络请求返回数据:", string);

            }
            try {
                responseString = JSON.parseObject(string, mRequest.getClazz());
            } catch (Exception e) {
                FFLogUtil.e("服务器返回数据解析失败", e);
                mRequest.setStatus(FFResponseCode.ERROR_ANALYSIS, "服务器返回数据解析失败");
                return;
            }
            if (responseString instanceof FFBaseBean
                    && !((FFBaseBean) responseString).judge()) {
                mRequest.setStatus(FFResponseCode.ERROR_BY_SERVICE,
                        ((FFBaseBean) responseString).getErrorMessage());
                mRequest.setEntity(responseString);
            } else {
                mRequest.setStatus(FFResponseCode.SUCCESS, "请求成功：服务器");
                mRequest.setEntity(responseString);
            }
        } else if (responseCode == 404) {
            mRequest.setStatus(FFResponseCode.ERROR_NET_404, "状态码404");
        } else if (responseCode == 505) {
            mRequest.setStatus(FFResponseCode.ERROR_SITE_505, "状态码505");
        } else if (responseCode == 400) {
            mRequest.setStatus(FFResponseCode.ERROR_NET_400, "状态码400");
        } else if (responseCode == 500) {
            mRequest.setStatus(FFResponseCode.ERROR_NET_500, "状态码500远程服务器连接失败");
        } else if (responseCode == 403) {
            mRequest.setStatus(FFResponseCode.ERROR_NET_403, "状态码403服务器内部错误");
        } else {
            mRequest.setStatus(FFResponseCode.ERROR_SITE_XXX, "状态码"
                    + responseCode);
        }
    }

    /**
     * 异步下载文件
     *
     * @param destFileDir 本地文件存储的文件夹
     */
    private static void download_Asyn(final String url, final String destFileDir, final OnDownloadListener listener) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        final Call call = new OkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onDownloadFailed(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) {
                InputStream is = null;
                byte[] buf = new byte[4096];
                int len;
                FileOutputStream fos = null;
                try {
                    assert response.body() != null;
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(Environment.getExternalStorageDirectory(), destFileDir);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        listener.onDownloading(progress);
                        listener.onDownloading(len, sum, total);
                    }
                    fos.flush();
                    // 下载完成
                    listener.onDownloadSuccess();
                    //如果下载文件成功，第一个参数为文件的绝对路径
                } catch (IOException e) {
                    listener.onDownloadFailed(e.toString());
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException ignored) {
                    }

                }

            }
        });
    }

    private void stopAll() {
        es1.shutdown();
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    private static String getBytes(long byteSum) {
        if (byteSum < 1024) {
            return byteSum + "字节";
        }
        if (byteSum < 1024 * 1024) {
            return FFUtils.getSubFloat(byteSum / 1024f) + "K";
        }
        if (byteSum < 1024 * 1024 * 1024) {
            return FFUtils.getSubFloat(byteSum / 1024f / 1024f) + "M";
        }
        return byteSum + "字节";
    }


    /**
     * 当前对象
     */
    public void onDestory() {
        stopAll();
    }

    /**
     * 本实例所对应的Activity是否已经finished
     */
    boolean getDestroyed() {
        if (activity == null) {
            return false;
        }
        return activity.getDestroyed();
    }

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess();

        /**
         * @param progress 下载进度
         */
        void onDownloading(int progress);


        void onDownloading(long complete, long progress, long all);

        /**
         * 下载失败
         */
        void onDownloadFailed(String err);
    }
}
