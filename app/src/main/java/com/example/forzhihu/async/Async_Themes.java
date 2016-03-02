package com.example.forzhihu.async;

import com.example.forzhihu.activity.MainActivity;
import com.example.forzhihu.http.HttpUtils;
import com.example.forzhihu.info.ThemesInfo;
import com.example.forzhihu.utils.Info;
import com.example.forzhihu.utils.Utils;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

public class Async_Themes extends AsyncTask<Integer, Integer, ThemesInfo> {
	private String url;

	public Async_Themes(String url) {
		this.url = url;
	}

	protected ThemesInfo doInBackground(Integer... params) {

		return HttpUtils.getJsonStr(url, ThemesInfo.class);
	}

	protected void onPostExecute(ThemesInfo themesInfo) {
		if (themesInfo != null) {
			Utils.Log("themesInfos.size = " + themesInfo.getOthers().size());
			Intent intent = new Intent(Info.currentActivity, MainActivity.class);
			intent.putExtra("theme", themesInfo);
//			String url = Info.httpUrl + "4/news/latest";
//			new Async_Latest(url, intent).execute();
			Info.currentActivity.startActivity(intent);
			Info.currentActivity.finish();
		} else {
			if (!Info.Erro.equals("")) {
				Toast.makeText(Info.currentActivity, Info.Erro, Toast.LENGTH_SHORT).show();
				Utils.Log("´íÎó~~~~~~~~~~~~~~~~~" + Info.Erro);
			}
		}
	}
}
