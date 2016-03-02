package com.example.forzhihu.application;



import com.example.forzhihu.utils.Info;
import com.example.forzhihu.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Application;

public class MyApplication extends Application{
	@Override
	public void onCreate() {
		super.onCreate();
		ImageLoaderConfiguration imageLoaderConfiguration = ImageLoaderConfiguration.createDefault(this);
		ImageLoader.getInstance().init(imageLoaderConfiguration);
		Info.date = Utils.getTodayNews();
	}
}
