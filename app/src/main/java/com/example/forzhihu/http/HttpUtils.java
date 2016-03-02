package com.example.forzhihu.http;

import com.example.forzhihu.sax.JsonUtils;
import com.example.forzhihu.utils.Info;
import com.example.forzhihu.utils.Utils;
import com.google.gson.reflect.TypeToken;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * �������
 * 
 * @author tc
 *
 */
public class HttpUtils {
	private static OkHttpClient mHttpClient;
	private static Request request;
	private static Call call;
	private static String res;

	public static <T> T getJsonStr(String url, Class<T> cla) {
		Utils.Log("��ʼ����");
		res = startHttp(url);
		if (res == null) {
			return null;
		} else {
			try {
				return JsonUtils.fromJson(res, cla);
			} catch (Exception e) {
				Info.Erro = "���ݽ�������";
				return null;
			}
		}
	}

	public static <T> T getJsonStr(String url, TypeToken<T> token) {
		Utils.Log("��ʼ����");
		res = startHttp(url);
		if (res == null) {
			return null;
		} else {
			try {
				return JsonUtils.fromJson(res, token);
			} catch (Exception e) {
				Info.Erro = "���ݽ�������";
				return null;
			}
		}
	}

	private static String startHttp(String url) {
		try {
			boolean flag = true;
			int index = 0;
			mHttpClient = new OkHttpClient();
			request = new Request.Builder().url(url).build();
			call = mHttpClient.newCall(request);
			Response response = null;
			if (call != null) {
				response = call.execute();
			}
			while (flag) {
				index++;
				System.out.println("index = " + index);
				if (index > 61) {
					Info.Erro = "�������ӳ�ʱ";
					return null;
				} else {
					res = response.body().string();
					if (!res.equals("")) {
						flag = false;
						Utils.Log("res = " + res);
					}
				}
				Thread.sleep(1000);
			}
			return res;
		} catch (Exception e) {
			Info.Erro = "�������Ӵ���";
			return null;
		} finally {
			if (mHttpClient != null) {
				mHttpClient = null;
			}
			if (request != null) {
				request = null;
			}
			if (call != null) {
				call = null;
			}
		}
	}
}