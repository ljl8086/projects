package com.sunny.teamob.util;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.SocketTimeoutException;

import android.os.Handler;
import android.os.Message;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.sunny.teamob.component.network.BODY;
import com.sunny.teamob.component.network.DynamicHandler;
import com.sunny.teamob.component.network.MAP;
import com.sunny.teamob.component.network.MESSAGE;

public abstract class MessageCallback extends Handler implements Callback{
	private Class<?> returnType;
	private MAP map;
	/**当前调用的方法*/
	private Method method;
	private MESSAGE message;
	/**请求ID*/
	private String httpRequestId;
	
	@Override
	public void onFailure(Request request, IOException paramIOException) {
		SimpleResponse simpeResponse = null;
		if(paramIOException instanceof SocketTimeoutException){
			simpeResponse = new SimpleResponse(SimpleResponse.STATUS_SOCKET_TIMEOUT, "网速太慢，请稍候重新提交");
		}else{
			simpeResponse = new SimpleResponse(SimpleResponse.STATUS_OTHER_EXP, paramIOException.getMessage());
		}
		super.obtainMessage(message.fail(), simpeResponse).sendToTarget();
		DynamicHandler.httpCache.remove(httpRequestId);
	}

	@Override
	public void onResponse(final Response response) throws IOException {
		try{
			String body = response.body().string();
			Object temp = null;
			if(method.isAnnotationPresent(BODY.class)){
				temp = body;
			}else if(method.getReturnType().isAssignableFrom(SimpleResponse.class)){
				temp = SimpleResponse.getSimpleResponse(body);
			}else{
				temp = JsonUtil.convertJson2Object(body, returnType, map);
			}
			super.obtainMessage(message.success(), temp).sendToTarget();
		}catch(SimpleException e){
			e.printStackTrace();
			SimpleResponse simpeResponse = e.getResponse();
			super.obtainMessage(message.fail(), simpeResponse).sendToTarget();;
		}catch(Exception e){
			e.printStackTrace();
			SimpleResponse simpeResponse = new SimpleResponse(2, e.getMessage());
			super.obtainMessage(message.fail(), simpeResponse).sendToTarget();;
		}finally{
			DynamicHandler.httpCache.remove(httpRequestId);
		}
	}

	@Override
	public abstract void handleMessage(Message msg);

	public void setReturnType(Class<?> returnType) {
		this.returnType = returnType;
	}

	public void setMap(MAP map) {
		this.map = map;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public void setMessage(MESSAGE message) {
		this.message = message;
	}

	public String getHttpRequestId() {
		return httpRequestId;
	}

	public void setHttpRequestId(String httpRequestId) {
		this.httpRequestId = httpRequestId;
	}

}
