package com.android.toolkitlibrary.network.imageloader;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.widget.ImageView;

import com.android.toolkitlibrary.R;
import com.android.toolkitlibrary.network.utils.FFLogUtil;
import com.android.toolkitlibrary.network.utils.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;

import java.io.File;

public class FFImageLoader {
    /**
     * 加载网络图片带参数（加载中，加载失败，的默认图标）
     */
    public static void loadImage(Context context, ImageView imageView, String url, int loading, int error) {
        Glide.with(context).load(url).into(imageView);
    }

    /**
     * 加载网络图片
     */
    public static void loadImage(Context context, ImageView imageView, String url, String tag) {
        FFLogUtil.d("image", url + tag);
        RequestOptions options = new RequestOptions().skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).signature(new ObjectKey(tag));
        Glide.with(context).asDrawable()
                .apply(options).load(url).into(imageView);
    }

    /**
     * 加载网络图片是否是半圆角和圆角
     */
    public static void loadImage(Context context, ImageView imageView, String url, String tag, boolean Round) {
        FFLogUtil.d("image", url + tag);
        RequestOptions options = new RequestOptions().skipMemoryCache(true)
                .transform(new GlideRoundTransform(context.getResources().getDimension(R.dimen.size_dp_5), Round))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).signature(new ObjectKey(tag));
        Glide.with(context).asDrawable()
                .apply(options).load(url).into(imageView);
    }


    /**
     * 加载网络不需要缓存图片
     */
    public static void loadImage(Context context, ImageView imageView, String url) {
        if (StringUtils.isEmpty(url)) {
            return;
        }
        RequestOptions options = new RequestOptions().skipMemoryCache(true);
        Glide.with(context).asDrawable().apply(options).load(url).into(imageView);
    }

    /**
     * 加载网络不需要缓存图片
     */
    public static void loadCircleImage(Context context, ImageView imageView, String url) {
        if (StringUtils.isEmpty(url)) {
            return;
        }
        Glide.with(context).load(url).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(imageView);
    }

    /**
     * 加载网络图片是否是半圆角和圆角
     */
    public static void loadImageNoCache(Context context, ImageView imageView, String url, boolean Round) {
        FFLogUtil.d("image", url);
        RequestOptions options = new RequestOptions().skipMemoryCache(true)
                .transform(new GlideRoundTransform(context.getResources().getDimension(R.dimen.size_dp_5), Round));
        Glide.with(context).asDrawable()
                .apply(options).load(url).into(imageView);
    }

    /**
     * 加载本地图片
     */
    public static void loadImage(Context context, ImageView imageView, String filePath, boolean sd) {
        File file = new File(Environment.getExternalStorageDirectory(), filePath);
        if (file != null && file.exists()) {
            Glide.with(context).load(file).into(imageView);
        }

    }

    /**
     * 加载应用资源
     */
    public static void loadImage(Context context, ImageView imageView, int resource) {
        Glide.with(context).load(resource).into(imageView);


    }

    /**
     * 加载Uri对象
     */
    public static void loadImage(Context context, ImageView imageView, Uri imageUri) {
        Glide.with(context).load(imageUri).into(imageView);
    }
}
