package com.example.forzhihu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {
	private GestureDetector mGestureDetector;
	private OnLoadListener mOnLoadListener;

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGestureDetector = new GestureDetector(context, new YScrollDetector());
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		View view = (View) getChildAt(getChildCount() - 1);
		int d = view.getBottom();
		d -= (getHeight() + getScrollY());
		if (d == 0) {
			loadData();
		} else
			super.onScrollChanged(l, t, oldl, oldt);
	}

	public static interface OnLoadListener {
		public void onLoad();
	}

	public void setOnLoadListener(OnLoadListener loadListener) {
		mOnLoadListener = loadListener;
	}

	private void loadData() {
		if (mOnLoadListener != null) {
			mOnLoadListener.onLoad();
		}
	}

	class YScrollDetector extends SimpleOnGestureListener {

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			/**
			 * 如果我们滚动更接近水平方向,返回false,让子视图来处理它
			 */
			return (Math.abs(distanceY) > Math.abs(distanceX));
		}
	}
}
