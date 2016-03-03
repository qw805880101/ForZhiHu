package com.example.forzhihu.async;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.forzhihu.http.HttpUtils;
import com.example.forzhihu.info.NewsLatest;
import com.example.forzhihu.utils.Info;
import com.example.forzhihu.utils.Utils;

public class Async_Latest extends AsyncTask<Integer, Integer, NewsLatest> {
	private String url;
	private Intent intent;
	private Handler mHandler;

	public Async_Latest(String url, Handler mHandler) {
		this.url = url;
		this.mHandler = mHandler;
	}

	protected NewsLatest doInBackground(Integer... params) {

		return HttpUtils.getJsonStr(url, NewsLatest.class);
	}

	protected void onPostExecute(NewsLatest newsLatest) {
		if (newsLatest != null) {
//			Utils.Log("themesInfos.size = " + newsLatest.getTop_stories().size());
			if (intent != null) {
				intent.putExtra("latest", newsLatest);
				Info.currentActivity.startActivity(intent);
				Info.currentActivity.finish();
			} else if (mHandler != null) {
				Message message = new Message();

				message.obj = newsLatest;
				mHandler.sendMessage(message);
			}
		} else {
			if (!Info.Erro.equals("")) {
				Toast.makeText(Info.currentActivity, Info.Erro, Toast.LENGTH_SHORT).show();
				Utils.Log("错误~~~~~~~~~~~~~~~~~" + Info.Erro);
				if (mHandler != null) {
					Message message = new Message();
					message.obj = null;
					mHandler.sendMessage(message);
				}
			}
		}
	}
}
