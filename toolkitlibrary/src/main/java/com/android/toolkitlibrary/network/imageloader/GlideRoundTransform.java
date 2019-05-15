package com.android.toolkitlibrary.network.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import java.security.MessageDigest;

/***
 * Created by ZM_Crazy on 2018/11/2.
 */
public class GlideRoundTransform extends BitmapTransformation {
    private float mRadius;
    private boolean mCornerType;
    private final int VERSION = 1;
    private final String ID = "GlideRoundTransform." + VERSION;
    private final byte[] ID_BYTES = ID.getBytes(CHARSET);


    GlideRoundTransform(float radius, boolean type) {
        super();
        mRadius = radius;//dp ->px
        mCornerType = type;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap bitmap = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight);
        return roundCrop(pool, bitmap);
    }

    private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null) {
            return null;
        }
        int width = source.getWidth();
        int height = source.getHeight();
        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config
                    .ARGB_8888);
        }
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader
                .TileMode.CLAMP));
        paint.setAntiAlias(true);
        Path path = new Path();
        drawRoundRect(canvas, paint, path, width, height);

        return result;
    }

    private void drawRoundRect(Canvas canvas, Paint paint, Path path, int width, int height) {
        float[] rids;
        if (mCornerType) {
            rids = new float[]{mRadius, mRadius, mRadius, mRadius, mRadius, mRadius, mRadius, mRadius};
            drawPath(rids, canvas, paint, path, width, height);
        } else {
            rids = new float[]{mRadius, mRadius, mRadius, mRadius, 0.0f, 0.0f, 0.0f, 0.0f};
            drawPath(rids, canvas, paint, path, width, height);
        }

    }


    /**
     * @param rids 圆角的半径，依次为左上角xy半径，右上角，右下角，左下角
     */
    private void drawPath(float[] rids, Canvas canvas, Paint paint, Path path, int width, int height) {
        path.addRoundRect(new RectF(0, 0, width, height), rids, Path.Direction.CW);
        canvas.drawPath(path, paint);
    }


    @Override
    public boolean equals(Object o) {
        return o instanceof GlideRoundTransform;
    }


    @Override
    public int hashCode() {
        return ID.hashCode();
    }


    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }
}
//        extends BitmapTransformation {
//
//    private float radius = 0f;
//
//    GlideRoundTransform(Context context) {
//        this(context, 4);
//    }
//
//    GlideRoundTransform(Context context, float dp) {
//        super(context);
//        this.radius = dp;
//    }
//
//    @Override
//    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
//        Bitmap bitmap = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight);
//        return roundCrop(pool, bitmap);
//    }
//
//    private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
//        if (source == null) return null;
//
//        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
//        if (result == null) {
//            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
//        }
//
//        Canvas canvas = new Canvas(result);
//        Paint paint = new Paint();
//        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
//        paint.setAntiAlias(true);
//        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
//        canvas.drawRoundRect(rectF, radius, radius, paint);
//        return result;
//    }
//
//    public String getId() {
//        return getClass().getName() + Math.round(radius);
//    }
//
//    @Override
//    public void updateDiskCacheKey(MessageDigest messageDigest) {
//
//    }
//
//
//}
