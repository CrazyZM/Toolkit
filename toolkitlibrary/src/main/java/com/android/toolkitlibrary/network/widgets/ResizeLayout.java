package com.android.toolkitlibrary.network.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class ResizeLayout extends FrameLayout {
	private OnResizeListener mListener;

	public interface OnResizeListener {
		void OnResize(int w, int h, int oldw, int oldh);
		void OnInterceptTouchEvent(MotionEvent ev);
	}

	public void setOnResizeListener(OnResizeListener l) {
		mListener = l;
	}

	public ResizeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (mListener != null) {
			mListener.OnInterceptTouchEvent(ev);
		}
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		if (mListener != null) {
			mListener.OnResize(w, h, oldw, oldh);
		}
	}
}