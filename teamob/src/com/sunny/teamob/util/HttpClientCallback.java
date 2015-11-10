package com.sunny.teamob.util;

import java.io.IOException;
import java.lang.reflect.Method;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.sunny.teamob.component.network.BODY;
import com.sunny.teamob.component.network.DynamicHandler;
import com.sunny.teamob.component.network.MAP;

public abstract class HttpClientCallback<T> implements Callback{
	private Class<?> returnType;
	private MAP map;
	/**当前调用的方法*/
	private Method method;
	/**请求ID*/
	private String httpRequestId;
	
	@Override
	public void onFailure(Request request, IOException paramIOException) {
		final SimpleResponse simpeResponse = new SimpleResponse(2, paramIOException.getMessage());
		onFailure(simpeResponse);
		DynamicHandler.httpCache.remove(httpRequestId);
	}

	public void onFailure(final SimpleResponse simpeResponse) {
		final Handler handler = new Handler(Looper.getMainLooper()){
			@Override
			public void handleMessage(Message msg) {
				Looper.getMainLooper();
				SimpleResponse response = (SimpleResponse)msg.obj;
				HttpClientCallback.this.onFail(response);
			}
		};
		handler.obtainMessage(0, simpeResponse).sendToTarget();
	}

	@Override
	public void onResponse(final Response response) throws IOException {
		String body = response.body().string();
		onResponseString(body);
		DynamicHandler.httpCache.remove(httpRequestId);
	}

	@SuppressWarnings("unchecked")
	public void onResponseString(String body) throws IOException {
		
		final Handler handler = new Handler(Looper.getMainLooper()){
			@Override
			public void handleMessage(Message msg) {
				Looper.getMainLooper();
				try{
					String body = (String)msg.obj;
					T temp = null;
					if(method.isAnnotationPresent(BODY.class)){
						temp = (T)body;
					}else if(method.getReturnType().isAssignableFrom(SimpleResponse.class)){
						temp = (T)SimpleResponse.getSimpleResponse(body);
					}else{
						temp = (T)JsonUtil.convertJson2Object(body, returnType, map);
					}
					
					HttpClientCallback.this.onResponse(temp);
				}catch(Exception e){
					SimpleResponse simpeResponse = new SimpleResponse(2, e.getMessage());
					HttpClientCallback.this.onFail(simpeResponse);
					e.printStackTrace();
				}
				
			}
		};
		handler.obtainMessage(0, body).sendToTarget();
	}
	
	public abstract void onResponse(T responseObj);
	
	public abstract void onFail(SimpleResponse response);
	
	public Class<?> getReturnType() {
		return returnType;
	}

	public void setReturnType(Class<?> returnType) {
		this.returnType = returnType;
	}

	public MAP getMap() {
		return map;
	}

	public void setMap(MAP map) {
		this.map = map;
	}
	
	public void setMethod(Method method){
		this.method = method;
	}

	public String getHttpRequestId() {
		return httpRequestId;
	}

	public void setHttpRequestId(String httpRequestId) {
		this.httpRequestId = httpRequestId;
	}

}
