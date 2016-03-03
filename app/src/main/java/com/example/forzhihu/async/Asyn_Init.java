package com.example.forzhihu.async;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.example.forzhihu.http.HttpUtils;
import com.example.forzhihu.info.InitBitmapInfo;
import com.example.forzhihu.utils.Info;
import com.example.forzhihu.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class Asyn_Init extends AsyncTask<Integer, Integer, InitBitmapInfo> {
	private String url = "";
	public Asyn_Init(String url) {
		this.url = url;
	}

	protected InitBitmapInfo doInBackground(Integer... params) {

		return HttpUtils.getJsonStr(url, InitBitmapInfo.class);
	}

	protected void onPostExecute(InitBitmapInfo initBitmap) {
		if (initBitmap != null) {
			if (!initBitmap.getImg().equals("")) {
				Utils.Log(initBitmap.getImg());
				Utils.Log("bitmapUrl = " + initBitmap.getImg());
				ImageLoader.getInstance().loadImage(initBitmap.getImg(), new SimpleImageLoadingListener() {
					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						super.onLoadingComplete(imageUri, view, loadedImage);
						File dir = Info.currentActivity.getFilesDir();
						final File imgFile = new File(dir, "start.jpg");
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						loadedImage.compress(Bitmap.CompressFormat.PNG, 100, bos);
						Utils.saveImage(imgFile, bos.toByteArray());
					}
				});
			}
		} else {
			if (!Info.Erro.equals("")) {
				Toast.makeText(Info.currentActivity, Info.Erro, Toast.LENGTH_SHORT).show();
				Utils.Log("错误~~~~~~~~~~~~~~~~~" + Info.Erro);
			}
		}
	}
}
