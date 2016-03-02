package com.example.forzhihu.activity;

import java.io.File;

import com.example.forzhihu.R;
import com.example.forzhihu.async.Asyn_Init;
import com.example.forzhihu.async.Async_Themes;
import com.example.forzhihu.utils.Info;
import com.example.forzhihu.utils.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

@SuppressLint("HandlerLeak")
public class InitActivity extends Activity{
	
	private ImageView image_init;
	private int sccrenWidth;
	private int sccrenHeight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Info.currentActivity = this;
		Info.currentContext = this;
		Utils.transStatus(this);
		setContentView(R.layout.activity_init);
		DisplayMetrics dm = this.getResources().getDisplayMetrics();
		sccrenWidth = dm.widthPixels;
		sccrenHeight = dm.heightPixels;
		initView();
	}
	@SuppressLint("NewApi")
	private void initView() {
		image_init = (ImageView) findViewById(R.id.image_init);
		File dir = getFilesDir();
		final File imgFile = new File(dir, "start.jpg");
        if (imgFile.exists()) {
        	image_init.setBackground(new BitmapDrawable(getResources(), BitmapFactory.decodeFile(imgFile.getAbsolutePath())));
        } else {
        	image_init.setBackgroundResource(R.drawable.start);
        }
        animation();
	}
	
	private void animation(){ 	
		ScaleAnimation sAnimation = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f, 
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f); 
		sAnimation.setDuration(3000);
		sAnimation.setFillAfter(true);
		sAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				String url = Info.httpUrl + "4/start-image/" + sccrenWidth + "*" + sccrenHeight;
				Utils.Log("url = " + url);
				new Asyn_Init(url).execute();
				new Async_Themes(Info.httpUrl + "4/themes").execute();
			}
		});
		image_init.startAnimation(sAnimation);
	}
}
