package com.example.forzhihu.view;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.forzhihu.R;
import com.example.forzhihu.utils.Utils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;
import java.util.List;

public class SlidingSwitcherView extends FrameLayout implements OnClickListener {
	private int count;
	private ImageLoader mImageLoader;
	private List<View> imageViews;
	private Context context;
	private ViewPager vp;
	private boolean isAutoPlay;
	private int currentItem;
	private LinearLayout ll_dot;
	private List<ImageView> iv_dots;
	private Handler handler = new Handler();
	private OnItemClickListener mItemClickListener;

	public SlidingSwitcherView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initImageLoader(context);
		initData();
	}

	public SlidingSwitcherView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlidingSwitcherView(Context context) {
		this(context, null);
	}

	private void initData() {
		imageViews = new ArrayList<View>();
		iv_dots = new ArrayList<ImageView>();
	}

	public void setImagesUrl(String[] imagesUrl, String[] titles) {
		initLayout();
		initImgFromNet(imagesUrl, titles);
		showTime();
	}

	public void setImagesRes(int[] imagesRes) {
		initLayout();
		initImgFromRes(imagesRes);
		showTime();
	}

	private void initLayout() {
		imageViews.clear();
		View view = LayoutInflater.from(context).inflate(R.layout.kanner_layout, this, true);
		vp = (ViewPager) view.findViewById(R.id.vp);
		ll_dot = (LinearLayout) view.findViewById(R.id.ll_dot);
		ll_dot.removeAllViews();
	}

	private void initImgFromRes(int[] imagesRes) {
		count = imagesRes.length;
		for (int i = 0; i < count; i++) {
			ImageView iv_dot = new ImageView(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			params.leftMargin = 5;
			params.rightMargin = 5;
			iv_dot.setImageResource(R.drawable.dot_blur);
			ll_dot.addView(iv_dot, params);
			iv_dots.add(iv_dot);
		}
		iv_dots.get(0).setImageResource(R.drawable.dot_focus);

		for (int i = 0; i <= count + 1; i++) {
			ImageView iv = new ImageView(context);
			iv.setScaleType(ScaleType.FIT_XY);
			iv.setBackgroundResource(R.drawable.start);
			if (i == 0) {
				iv.setImageResource(imagesRes[count - 1]);
			} else if (i == count + 1) {
				iv.setImageResource(imagesRes[0]);
			} else {
				iv.setImageResource(imagesRes[i - 1]);
			}
			imageViews.add(iv);
		}
	}

	private void initImgFromNet(String[] imagesUrl, String[] titles) {
		count = imagesUrl.length;
		for (int i = 0; i < count; i++) {
			ImageView iv_dot = new ImageView(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			params.leftMargin = 5;
			params.rightMargin = 5;
			iv_dot.setImageResource(R.drawable.dot_blur);
			ll_dot.addView(iv_dot, params);
			iv_dots.add(iv_dot);
		}
		iv_dots.get(0).setImageResource(R.drawable.dot_focus);

		for (int i = 0; i <= count + 1; i++) {
			View view = inflate(context, R.layout.kanner_items, null);
			ImageView iv = (ImageView) view.findViewById(R.id.image_banner_items);
			TextView tv = (TextView) view.findViewById(R.id.tx_banner_items);
			iv.setBackgroundResource(R.drawable.start);
			if (i == 0) {
				mImageLoader.displayImage(imagesUrl[count - 1], iv);
				tv.setText(titles[count - 1]);
			} else if (i == count + 1) {
				mImageLoader.displayImage(imagesUrl[0], iv);
				tv.setText(titles[0]);
			} else {
				mImageLoader.displayImage(imagesUrl[i - 1], iv);
				tv.setText(titles[i - 1]);
			}
			iv.setOnClickListener(this);
			imageViews.add(view);
		}
	}

	private void showTime() {
		vp.setAdapter(new KannerPagerAdapter());
		vp.setFocusable(true);
		vp.setCurrentItem(1);
		currentItem = 1;
		vp.setOnPageChangeListener(new MyOnPageChangeListener());
		vp.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		startPlay(true);
	}

	public void startPlay(boolean isAutoPlay) {
		this.isAutoPlay = isAutoPlay;
		handler.postDelayed(task, 2000);
	}

	public void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs().build();
		ImageLoader.getInstance().init(config);
		mImageLoader = ImageLoader.getInstance();
	}

	private final Runnable task = new Runnable() {

		@Override
		public void run() {
			if (isAutoPlay) {
				currentItem = currentItem % (count + 1) + 1;
				if (currentItem == 1) {
					vp.setCurrentItem(currentItem, false);
					handler.post(task);
				} else {
					vp.setCurrentItem(currentItem);
					handler.postDelayed(task, 3000);
				}
			} else {
				handler.removeCallbacks(task);  
			}
		}
	};

	public List<View> getView() {
		if (imageViews != null) {
			return imageViews;
		}
		return null;
	}

	class KannerPagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return imageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(imageViews.get(position));
			return imageViews.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(imageViews.get(position));
		}
	}

	class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			switch (arg0) {
			case 1:
				isAutoPlay = false;
				break;
			case 2:
				isAutoPlay = true;
				break;
			case 0:
				if (vp.getCurrentItem() == 0) {
					vp.setCurrentItem(count, false);
				} else if (vp.getCurrentItem() == count + 1) {
					vp.setCurrentItem(1, false);
				}
				currentItem = vp.getCurrentItem();
				Utils.Log("当前广告>>>>>>>>>>>>>>>>>>> = " + currentItem);
				isAutoPlay = true;
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			for (int i = 0; i < iv_dots.size(); i++) {
				if (i == arg0 - 1) {
					iv_dots.get(i).setImageResource(R.drawable.dot_focus);
				} else {
					iv_dots.get(i).setImageResource(R.drawable.dot_blur);
				}
			}
		}
	}

	public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
		Utils.Log("setOnItemClickListener");
		this.mItemClickListener = mItemClickListener;
	}

	public interface OnItemClickListener {
		public void click(View v, int id);
	}

	@Override
	public void onClick(View v) {
		if (mItemClickListener != null) {
			mItemClickListener.click(v, (vp.getCurrentItem() - 1));
		}
	}
}