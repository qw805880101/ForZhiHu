package com.example.forzhihu.view;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager {

	/** ����ʱ���µĵ� **/
	PointF downP = new PointF();
	/** ����ʱ��ǰ�ĵ� **/
	PointF curP = new PointF();

	/** �Զ������� **/
	private GestureDetector mGestureDetector;

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGestureDetector = new GestureDetector(context, new XScrollDetector());
	}

	public MyViewPager(Context context) {
		super(context);

		mGestureDetector = new GestureDetector(context, new XScrollDetector());
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev);// default
		// �����ش����¼������λ�õ�ʱ�򣬷���true��
		// ˵����onTouch�����ڴ˿ؼ�������ִ�д˿ؼ���onTouchEvent
		// return true;
		// �ӽ�ˮƽ����ʱ�ӿؼ�������¼������򽻸����ؼ�����
		// return mGestureDetector.onTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// ÿ�ν���onTouch�¼�����¼��ǰ�İ��µ�����
		curP.x = ev.getX();
		curP.y = ev.getY();

		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			// ��¼����ʱ�������
			// �мǲ����� downP = curP �������ڸı�curP��ʱ��downPҲ��ı�
			downP.x = ev.getX();
			downP.y = ev.getY();
			// �˾������Ϊ��֪ͨ���ĸ�ViewPager���ڽ��е��Ǳ��ؼ��Ĳ�������Ҫ���ҵĲ������и���
			getParent().requestDisallowInterceptTouchEvent(true);
		}

		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			float distanceX = curP.x - downP.x;
			float distanceY = curP.y - downP.y;
			// �ӽ�ˮƽ������ViewPager�ؼ��������ƣ�ˮƽ����
			if (Math.abs(distanceX) > Math.abs(distanceY)) {
				// �˾������Ϊ��֪ͨ���ĸ�ViewPager���ڽ��е��Ǳ��ؼ��Ĳ�������Ҫ���ҵĲ������и���
				getParent().requestDisallowInterceptTouchEvent(true);
			} else {
				// �ӽ���ֱ�������������ؼ�����
				getParent().requestDisallowInterceptTouchEvent(false);
			}
		}

		return super.onTouchEvent(ev);
	}

	private class XScrollDetector extends GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			// return super.onScroll(e1, e2, distanceX, distanceY);

			// �ӽ�ˮƽ����ʱ�ӿؼ�������¼������򽻸����ؼ�����
			return (Math.abs(distanceX) > Math.abs(distanceY));
		}
	}
}