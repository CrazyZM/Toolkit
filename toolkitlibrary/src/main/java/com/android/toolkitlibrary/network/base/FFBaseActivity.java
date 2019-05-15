package com.android.toolkitlibrary.network.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.toolkitlibrary.R;
import com.android.toolkitlibrary.dialog.Loading;
import com.android.toolkitlibrary.network.http.FFNetWork;
import com.android.toolkitlibrary.network.http.FFNetWorkCallBack;
import com.android.toolkitlibrary.network.utils.DensityUtils;
import com.android.toolkitlibrary.network.utils.FFLogUtil;
import com.android.toolkitlibrary.network.utils.FFUtils;
import com.android.toolkitlibrary.network.widgets.ResizeLayout;

import java.io.File;
import java.util.Map;


public abstract class FFBaseActivity extends AppCompatActivity implements FFActivity {

    private FFNetWork mNet;
    private boolean mIsDestroyed = false;
    private String mTAG;
    static boolean hasTransaction = false;
    public static boolean containsFirst;
    private int activity_base = R.layout.activity_base;
    private Loading pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //适配方案，更改密度
        DensityUtils.setCustomDensity(this);
        // 初始化TAG
        mTAG = getClass().getSimpleName();
        mNet = new FFNetWork(this);
        e("onCreate");
        // 如果已开启事务将当前activity加入事务队列
        if (FFBaseActivity.hasTransaction) {
            transactionActivities.add(this);
        }
        // 加入所有activity集合
        allActivities.add(this);
        // 初始化contentView
        super.setContentView(activity_base);
        int contentViewId = getContentViewId();
        if (contentViewId != 0) {
            setContentView(contentViewId);
        } else {
            View contentViewView = getContentViewView();
            if (contentViewView != null) {
                setContentView(contentViewView);
            }
        }
        getContainer().setOnResizeListener(new ResizeLayout.OnResizeListener() {

            @Override
            public void OnResize(int w, int h, int oldw, int oldh) {
                int abs = Math.abs(oldh - h);
                if (abs > FFUtils.getDisHight() * 0.7) {
                    return;
                }

                if (abs < FFUtils.getPx(80)) {
                    return;
                }
                if (oldh < h) {// 软键盘消失
                    onSoftInputMethInvis(abs);
                }
                if (oldh > h) {// 软键盘显示
                    onSoftInputMethVis(abs);
                }
            }

            @Override
            public void OnInterceptTouchEvent(MotionEvent ev) {
                FFBaseActivity.this.OnInterceptTouchEvent(ev);
            }
        });
        e(getTitle().toString());
        // 设置title文字
        try {
            ActivityInfo activityInfo = getPackageManager().getActivityInfo(
                    getComponentName(), PackageManager.GET_META_DATA);
            if (activityInfo.labelRes != 0) {
                String title = getString(activityInfo.labelRes);
                if (title != null && title.length() > 0) {
                    setTitle(title);
                }
            } else {
                setTitle(getString(R.string.app_name));
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        setTitle(getTitle());

        // 设置返回键可用
        setBackButtonVisible(true);

        // 初始化View
        findView();
        initView();
        setListener();
        afterCreate();
    }

    public void OnInterceptTouchEvent(MotionEvent ev) {
    }

    @Override
    protected void onStart() {
        super.onStart();
        d("onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        d("onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //适配方案，更改密度
        DensityUtils.setCustomDensity(this);
        d("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        d("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        d("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        d("onDestroy");
        allActivities.remove(this);
        transactionActivities.remove(this);
        setDestroyed(true);
        mNet.onDestory();
        int size = transactionActivities.size();
        if (FFBaseActivity.hasTransaction) {
            if (FFBaseActivity.containsFirst) {
                if (size <= 1) {
                    FFBaseActivity.hasTransaction = false;
                    transactionActivities.clear();
                }
            } else {
                if (size == 0) {
                    FFBaseActivity.hasTransaction = false;
                }
            }
        }
        e("当前" + (FFBaseActivity.hasTransaction ? "有" : "没有")
                + "事务");
    }

    /**
     * 当actionBar上的返回键被点击
     *
     * @param v
     */
    public void onBackPressed(View v) {
        onBackPressed();
    }

    @Override
    public void showToast(Object msg) {
        if (msg != null) {
            FFApplication.showToast(msg.toString());
        }
    }

    @Override
    public boolean getDestroyed() {
        return mIsDestroyed;
    }

    public void setDestroyed(boolean isDestroyed) {
        this.mIsDestroyed = isDestroyed;
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
    public void setContentView(int layoutResID) {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.baseActivity_content);
        frameLayout.removeAllViews();
        frameLayout.addView(getLayoutInflater().inflate(layoutResID, null));
    }

    @Override
    public void setContentView(View view) {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.baseActivity_content);
        frameLayout.removeAllViews();
        frameLayout.addView(view);
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.baseActivity_content);
        frameLayout.removeAllViews();
        frameLayout.addView(view);
    }

    /**
     * 显示隐藏返回键
     */
    public void setBackButtonVisible(boolean visiblity) {
        View backIcon = findViewById(R.id.actionBar_back_backIcon);
        if (visiblity) {
            backIcon.setVisibility(View.VISIBLE);
            findViewById(R.id.actionBar_back_ll).setClickable(true);
        } else {
            backIcon.setVisibility(View.GONE);
            findViewById(R.id.actionBar_back_ll).setClickable(false);
        }
    }

    public View getContentViewView() {
        return null;
    }

    /**
     * 设置标题
     */
    public void setTitle(CharSequence title) {
        ((TextView) findViewById(R.id.actionBar_title)).setText(title);
    }

    /**
     * 设置contentView是否在状态栏和actionBar下面
     */
    public void setActionBarOverlap() {
        activity_base = R.layout.activity_fullscreen_base;
    }

    /**
     * 获取底部栏 仅在setActionBarOverlap之后有效 否则会导致崩溃
     *
     * @return
     */
    public FrameLayout getBottomBar() {
        return (FrameLayout) findViewById(R.id.baseActivity_bottom_bar);
    }

    /**
     * 获取整个activity的ContentView的父控件
     *
     * @return
     */
    public ResizeLayout getContainer() {
        return (ResizeLayout) findViewById(R.id.baseActivity_content);
    }

    /**
     * 添加一个ActionBar Menu
     *
     * @param iconId   Menu的图片资源Id
     * @param listener Menu的点击事件监听
     * @return
     */
    public ImageButton addMenu(int iconId, OnClickListener listener) {
        ImageButton menu = (ImageButton) getLayoutInflater().inflate(
                R.layout.view_menu, null);
        menu.setLayoutParams(new LinearLayout.LayoutParams(getResources()
                .getDimensionPixelSize(R.dimen.actionBar_menuWidth),
                getResources().getDimensionPixelSize(R.dimen.actionbarHight)));
        int padding = (getResources().getDimensionPixelSize(
                R.dimen.actionbarHight) - getResources().getDimensionPixelSize(
                R.dimen.actionBar_icon_size)) / 2;
        menu.setPadding(padding, padding, padding, padding);
        menu.setOnClickListener(listener);
        menu.setImageResource(iconId);
        // menu.setId(listener.hashCode());
        ((LinearLayout) findViewById(R.id.actionBar_menu_container))
                .addView(menu);
        return menu;
    }

    /**
     * 添加一个ActionBar Menu
     *
     * @param text     Menu文字
     * @param listener Menu的点击事件监听
     * @return
     */
    public Button addMenu(String text, OnClickListener listener) {
        Button menu = (Button) getLayoutInflater().inflate(
                R.layout.view_menu_text, null);
        int actionBarHight = getResources().getDimensionPixelSize(
                R.dimen.actionbarHight);
        int border = getResources().getDimensionPixelSize(R.dimen.border);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, actionBarHight);
        // params.setMargins(0, border, border, border);
        menu.setLayoutParams(params);
        menu.setPadding(border, 0, border, 0);
        menu.setMinimumWidth(actionBarHight);
        menu.setOnClickListener(listener);
        menu.setText(text);
        // menu.setId(listener.hashCode());
        ((LinearLayout) findViewById(R.id.actionBar_menu_container))
                .addView(menu);
        return menu;
    }

    /**
     * 删除Menu
     *
     * @param id
     */
    public void removeMenu(int id) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.actionBar_menu_container);
        linearLayout.removeView(linearLayout.findViewById(id));
    }

    /**
     * 设置为没有ActionBar
     */
    public void setNoTitle() {
        getActionBarView().setVisibility(View.GONE);
    }

    /**
     * 获取ActionBar的整个View
     *
     * @return
     */
    public View getActionBarView() {
        return findViewById(R.id.actionBar_ll);
    }

    /**
     * @param activity
     * @return
     */
    public boolean isFrom(Class<? extends Activity> activity) {
        return activity.getName().equals(getOrigin());
    }

    private String getOrigin() {
        return getIntent().getStringExtra("origin_baseActivity");
    }

    public static void skipTo(Context activity, Class<?> targetActivity,
                              Bundle extras) {
        if (extras == null) {
            extras = new Bundle();
        }
        Intent intent = new Intent();
        extras.putString("origin_baseActivity", activity.getClass().getName());
        intent.putExtras(extras);
        activity.startActivity(intent.setClass(activity, targetActivity));
    }


    @Override
    public void showProgressDialog(String word) {
        if (pd == null) {
            pd = new Loading(this);
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

    /**
     * 关闭所有Activity
     */
    public static void removeAllActivity() {
        for (Activity activity : allActivities) {
            activity.finish();
        }
    }

    @Override
    public void startTransaction(boolean containThis) {
        FFBaseActivity.hasTransaction = true;
        FFBaseActivity.containsFirst = containThis;
        if (containThis && !transactionActivities.contains(this)) {
            transactionActivities.add(this);
        }
    }

    @Override
    public void endTransaction(boolean finishThis) {
        if (finishThis) {
            finish();
        }
        for (Activity activity : transactionActivities) {
            if (activity != this) {
                activity.finish();
            }
        }
        FFBaseActivity.containsFirst = false;
        FFBaseActivity.hasTransaction = false;
    }

    public void onSoftInputMethVis(int softInputHight) {
        e("软键盘弹出");
    }

    public void onSoftInputMethInvis(int softInputHight) {
        e("软键盘消失");
    }

    public void removeAllMenu() {
        ((LinearLayout) findViewById(R.id.actionBar_menu_container)).removeAllViews();
    }

}
