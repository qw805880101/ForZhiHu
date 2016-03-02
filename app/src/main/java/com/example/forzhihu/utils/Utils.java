package com.example.forzhihu.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class Utils {
	private final static String TAG = "tcc";

	public static void Log(String msg) {
		android.util.Log.i(TAG, msg);
	}

	/**
	 * 对网络连接状态进行判断
	 * 
	 * @return true, 可用； false， 不可用
	 */
	public static boolean isOpenNetwork() {
		ConnectivityManager connManager = (ConnectivityManager) Info.currentActivity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connManager.getActiveNetworkInfo() != null) {
			return connManager.getActiveNetworkInfo().isAvailable();
		}
		return false;
	}

	public static void saveImage(File imgFile, byte[] byteArray) {
		FileOutputStream fos = null;
		try {
			imgFile.delete();
			fos = new FileOutputStream(imgFile);
			fos.write(byteArray);
			fos.flush();
			fos.close();
			Utils.Log("图片存储成功");
		} catch (IOException e) {
			Utils.Log("图片存储错误~~~~~~~~~~~~~~~~~" + e);
		} finally {
			if (fos != null) {
				fos = null;
			}
		}
	}

	@SuppressLint("NewApi")
	public static void transStatus(Activity activity) {
		activity.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
			Window window = activity.getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
					| WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);
			window.setNavigationBarColor(Color.TRANSPARENT);
		}
	}

	@SuppressLint("SimpleDateFormat")
	public static String getTodayNews() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}

	public static String StrToDate(String str) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Date date = null;
		String str2 = null;
		try {
			date = format.parse(str);
			format.applyPattern("yyyy年MM月dd日");
			str2 = format.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str2;
	}

	/**
	 * 获取指定日后 前dayAddNum 天的 日期
	 * 
	 * @param day
	 *            日期，格式为String："2013-9-3";
	 * @param dayAddNum
	 *            增加天数 格式为int;
	 * @return
	 */
	public static String getDateStr(String day, int dayAddNum) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		Date nowDate = null;
		try {
			nowDate = df.parse(day);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date newDate2 = new Date(nowDate.getTime() - dayAddNum * 24 * 60 * 60 * 1000);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		String dateOk = simpleDateFormat.format(newDate2);
		return dateOk;
	}

}
