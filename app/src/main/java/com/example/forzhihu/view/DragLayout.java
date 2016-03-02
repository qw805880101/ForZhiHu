package com.example.forzhihu.view;

import com.example.forzhihu.R;
import com.nineoldandroids.view.ViewHelper;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * ʹ��ViewRragHelperʵ�ֲ໬Ч������
 */
public class DragLayout extends FrameLayout {
    private boolean isShowShadow = true;
    //���ƴ�����
    private GestureDetectorCompat gestureDetector;
    //��ͼ��ק�ƶ�������
    private ViewDragHelper dragHelper;
    //����������
    private DragListener dragListener;
    //ˮƽ��ק�ľ���
    private int range;
    //���
    private int width;
    //�߶�
    private int height;
    //main��ͼ������ViewGroup������ߵľ���
    private int mainLeft;
    private Context context;
    private ImageView iv_shadow;
    //��಼��
    private LinearLayout vg_left;
    //�Ҳ�(�����沼��)
    private CustomRelativeLayout vg_main;
    //ҳ��״̬ Ĭ��Ϊ�ر�
    private Status status = Status.Close;

    public DragLayout(Context context) {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        gestureDetector = new GestureDetectorCompat(context, new YScrollDetector());
        dragHelper = ViewDragHelper.create(this, dragHelperCallback);
    }

    class YScrollDetector extends SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx, float dy) {
            return Math.abs(dy) <= Math.abs(dx);
        }
    }
    /**
     * ʵ����View����ק������ʵ��Callback������صķ���
     */
    private ViewDragHelper.Callback dragHelperCallback = new ViewDragHelper.Callback() {
        /**
         * ˮƽ�����ƶ�
         * @param child Child view being dragged
         * @param left Attempted motion along the X axis
         * @param dx Proposed change in position for left
         * @return
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (mainLeft + dx < 0) {
                return 0;
            } else if (mainLeft + dx > range) {
                return range;
            } else {
                return left;
            }
        }

        /**
         * �������е���View
         * @param child Child the user is attempting to capture
         * @param pointerId ID of the pointer attempting the capture
         * @return
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }
        /**
         * ����ˮƽ���򻬶�����Զ����
         * @param child Child view to check  ��Ļ���
         * @return
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return width;
        }

        /**
         * ����ק����View�������ͷŵ�ʱ��ص��ķ����� Ȼ������󻬻����һ��ľ�������жϴ򿪻��߹ر�
         * @param releasedChild
         * @param xvel
         * @param yvel
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (xvel > 0) {
                open();
            } else if (xvel < 0) {
                close();
            } else if (releasedChild == vg_main && mainLeft > range * 0.3) {
                open();
            } else if (releasedChild == vg_left && mainLeft > range * 0.7) {
                open();
            } else {
                close();
            }
        }

        /**
         * ��View����ק �ƶ���ʱ��ص��ķ���
         * @param changedView View whose position changed
         * @param left New X coordinate of the left edge of the view
         * @param top New Y coordinate of the top edge of the view
         * @param dx Change in X position from the last call
         * @param dy Change in Y position from the last call
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top,
                int dx, int dy) {
            if (changedView == vg_main) {
                mainLeft = left;
            } else {
                mainLeft = mainLeft + left;
            }
            if (mainLeft < 0) {
                mainLeft = 0;
            } else if (mainLeft > range) {
                mainLeft = range;
            }

            if (isShowShadow) {
                iv_shadow.layout(mainLeft, 0, mainLeft + width, height);
            }
            if (changedView == vg_left) {
                vg_left.layout(0, 0, range, height);
                vg_main.layout(mainLeft, 0, mainLeft + width, height);
            }

            dispatchDragEvent(mainLeft);
        }
    };

    /**
     * ������ػص��ӿ�
     */
    public interface DragListener {
        //�����
        public void onOpen();
        //����ر�
        public void onClose();
        //���滬��������
        public void onDrag(float percent);
    }
    public void setDragListener(DragListener dragListener) {
        this.dragListener = dragListener;
    }

    /**
     * ���ּ�����ɻص�
     * ��һЩ��ʼ���Ĳ���
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (isShowShadow) {
            iv_shadow = new ImageView(context);
            iv_shadow.setImageResource(R.drawable.shadow);
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            addView(iv_shadow, 1, lp); 
        }
        //������
        vg_left = (LinearLayout) getChildAt(0);
        //�Ҳ�(��)����
        vg_main = (CustomRelativeLayout) getChildAt(isShowShadow ? 2 : 1);
        vg_main.setDragLayout(this);
        vg_left.setClickable(true);
        vg_main.setClickable(true);
    }

    public ViewGroup getVg_main() {
        return vg_main;
    }

    public ViewGroup getVg_left() {
        return vg_left;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = vg_main.getMeasuredWidth();
        height = vg_main.getMeasuredHeight();
        //����ˮƽ��ק�����ľ��� һ��Ϊ��Ļ��ȵ�80%
        range = (int) (width * 0.85f);
    }

    /**
     * ���ý���left��main ��ͼ����λ�ò���
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        vg_left.layout(0, 0, range, height);
        vg_main.layout(mainLeft, 0, mainLeft + width, height);
    }

    /**
     * ���ش����¼�
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
    	float x = ev.getX();
    	if (x > 150) {
			return false;
		} else {
			try {
				return dragHelper.shouldInterceptTouchEvent(ev) && gestureDetector.onTouchEvent(ev);
			} catch (Exception e) {
			}
			return false;
		}
    }

    /**
     * �����صĵ��¼���ViewDragHelper���д���
     * @param e
     * @return
     */
    @SuppressLint("ClickableViewAccessibility")
	@Override
    public boolean onTouchEvent(MotionEvent e) {
        try {
            dragHelper.processTouchEvent(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * ���д�����ק�¼�
     * @param mainLeft
     */
    private void dispatchDragEvent(int mainLeft) {
        if (dragListener == null) {
            return;
        }
        float percent = mainLeft / (float) range;
        //��������Ч��
        animateView(percent);
        //���лص������İٷֱ�
        dragListener.onDrag(percent);
        Status lastStatus = status;
        if (lastStatus != getStatus() && status == Status.Close) {
            dragListener.onClose();
        } else if (lastStatus != getStatus() && status == Status.Open) {
            dragListener.onOpen();
        }
    }

    /**
     * ���ݻ����ľ���ı���,����ƽ�ƶ���
     * @param percent
     */
    private void animateView(float percent) {
        float f1 = 1 - percent * 0.5f;

        ViewHelper.setTranslationX(vg_left, -vg_left.getWidth() / 2.5f + vg_left.getWidth() / 2.5f * percent);
        if (isShowShadow) {
            //��ӰЧ����ͼ��С��������
            ViewHelper.setScaleX(iv_shadow, f1 * 1.2f * (1 - percent * 0.10f));
            ViewHelper.setScaleY(iv_shadow, f1 * 1.85f * (1 - percent * 0.10f));
        }
    }
    /**
     * �м��ٶ�,������ֹͣ������ʱ�򣬸ò�������ֹͣ����Ч��
     */
    @Override
    public void computeScroll() {
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * ҳ��״̬(����,��,�ر�)
     */
    public enum Status {
        Drag, Open, Close
    }

    /**
     * ҳ��״̬����
     * @return
     */
    public Status getStatus() {
        if (mainLeft == 0) {
            status = Status.Close;
        } else if (mainLeft == range) {
            status = Status.Open;
        } else {
            status = Status.Drag;
        }
        return status;
    }

    public void open() {
        open(true);
    }

    public void open(boolean animate) {
        if (animate) {
            //��������
            if (dragHelper.smoothSlideViewTo(vg_main, range, 0)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            vg_main.layout(range, 0, range * 2, height);
            dispatchDragEvent(range);
        }
    }

    public void close() {
        close(true);
    }

    public void close(boolean animate) {
        if (animate) {
            //��������
            if (dragHelper.smoothSlideViewTo(vg_main, 0, 0)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            vg_main.layout(0, 0, width, height);
            dispatchDragEvent(0);
        }
    }
}
