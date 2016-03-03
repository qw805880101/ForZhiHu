package com.example.forzhihu.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

public class CustomSwipeToRefresh extends SwipeRefreshLayout {
	/**
	 * 滑动到最下面时的上拉操作
	 */

	private int mTouchSlop;

	private float mPrevX;

	/**
	 * @param context
	 */
	public CustomSwipeToRefresh(Context context) {
		this(context, null);
	}

	public CustomSwipeToRefresh(Context context, AttributeSet attrs) {
		super(context, attrs);
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	@SuppressLint("Recycle")
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mPrevX = MotionEvent.obtain(event).getX();
				break;

			case MotionEvent.ACTION_MOVE:
				final float eventX = event.getX();
				float xDiff = Math.abs(eventX - mPrevX);

				if (xDiff > mTouchSlop) {
					return false;
				}
		}

		return super.onInterceptTouchEvent(event);
	}

}
