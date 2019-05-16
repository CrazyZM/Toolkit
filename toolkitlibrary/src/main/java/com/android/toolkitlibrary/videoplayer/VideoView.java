package com.android.toolkitlibrary.videoplayer;

import android.content.Context;
import android.util.AttributeSet;

import cn.jzvd.JzvdStd;

public class VideoView extends JzvdStd {

    public VideoView(Context context) {
        super(context);
    }

    public VideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setUp(String url, String title, int screen) {
        topContainer.setVisibility(GONE);
        bottomContainer.setVisibility(GONE);
        titleTextView.setVisibility(GONE);
        bottomProgressBar.setVisibility(GONE);
        textureViewContainer.setFocusable(false);
        textureViewContainer.setClickable(false);
        super.setUp(url, title, screen);
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
        bottomProgressBar.setVisibility(GONE);
    }
}
