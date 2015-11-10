package com.sunny.teamob.util;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.ConnectionPool;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
/**
 * OkHttp工具类。
 * @author ljl
 *
 */
public final class OkHttpUtil {
	private final static OkHttpClient mOkHttpClient = new OkHttpClient();
	
	static {
		mOkHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
		mOkHttpClient.setWriteTimeout(10, TimeUnit.SECONDS);
		mOkHttpClient.setReadTimeout(10, TimeUnit.SECONDS);
		mOkHttpClient.setConnectionPool(new ConnectionPool(5, 5*1000));
	}

	/**
	 * 同步网络请求。
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static Response execute(Request request) throws IOException {
		return mOkHttpClient.newCall(request).execute();
	}

	/**
	 * 开启异步线程访问网络
	 * 
	 * @param request
	 * @param responseCallback
	 */
	public static void asyncExecute(Request request, Callback responseCallback) {
		mOkHttpClient.newCall(request).enqueue(responseCallback);
	}

	/**
	 * 开启异步线程访问网络, 且不在意返回结果（实现空callback）
	 * 
	 * @param request
	 */
	public static void asyncExecute(Request request) {
		mOkHttpClient.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Response arg0) throws IOException {

			}

			@Override
			public void onFailure(Request arg0, IOException arg1) {

			}
		});
	}

	public static String getStringFromServer(String url) throws IOException {
		Request request = new Request.Builder().url(url).build();
		Response response = execute(request);
		if (response.isSuccessful()) {
			String responseUrl = response.body().string();
			return responseUrl;
		} else {
			throw new IOException("Unexpected code " + response);
		}
	}
	
	public static OkHttpClient getMOkHttpClient(){
		return mOkHttpClient;
	}
}