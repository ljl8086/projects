package com.sunny.teamob.component.network;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.squareup.okhttp.Response;
import com.sunny.teamob.component.log.LoggerFactory;
import com.sunny.teamob.component.log.SimpleLog;
import com.sunny.teamob.util.HttpClientCallback;
import com.sunny.teamob.util.HttpUtil;
import com.sunny.teamob.util.JsonUtil;
import com.sunny.teamob.util.MessageCallback;
import com.sunny.teamob.util.ReflectUtil;
import com.sunny.teamob.util.SimpleResponse;
/**
 * 网络请求数据封装。
 * @author ljl
 */

public class DynamicHandler implements InvocationHandler {
	private final static SimpleLog log = LoggerFactory.getLog(DynamicHandler.class);
	public final static ConcurrentLinkedQueue<String> httpCache = new ConcurrentLinkedQueue<String>();
	
	/**代理的目标类*/
	private Class<?> proxyClass = null;
	private PATH pathAnnotation = null;
	private String baseUrl = null;
	
	public DynamicHandler(Class<?> proxyClass,String url){
		this.proxyClass = proxyClass;
		this.pathAnnotation = proxyClass.getAnnotation(PATH.class);
		this.baseUrl = url;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		POST post = method.getAnnotation(POST.class);
		MAP map = method.getAnnotation(MAP.class);
		MESSAGE message = method.getAnnotation(MESSAGE.class);
		//构造请求参数
		String url = getRequestUrl(method, post);
		
		List<String[]> paras = getRequestPara(method, args);
		
		HttpClientCallback<?> callback = getHttpClientCallBack(args);
		
		MessageCallback msgCallback = getMessageCallBack(args);
		
		String httpRequestId = getHttpRequestId(url, paras);
		if(httpCache.contains(httpRequestId)){
			log.debug("网络请求尚未完成，本次请求取消。URL:"+httpRequestId);
			return null;
		}
		
		httpCache.add(httpRequestId);
		if(callback!=null){
			callback.setMap(map);
			callback.setReturnType(method.getReturnType());
			callback.setMethod(method);
			callback.setHttpRequestId(httpRequestId);
			HttpUtil.asyncHttpClient(url, paras.toArray(new String[1][2]),callback);
		}else if(msgCallback!=null){
			msgCallback.setMap(map);
			msgCallback.setReturnType(method.getReturnType());
			msgCallback.setMethod(method);
			msgCallback.setHttpRequestId(httpRequestId);
			msgCallback.setMessage(message);
			HttpUtil.asyncHttpClient(url, paras.toArray(new String[1][2]),msgCallback);
		}else{
			try{
				Response res = HttpUtil.httpClient(url, paras.toArray(new String[1][2]));
				if(res.isSuccessful()){
					String body = res.body().string();
					if(method.isAnnotationPresent(BODY.class)){
						return body;
					}else if(method.getReturnType().isAssignableFrom(SimpleResponse.class)){
						return SimpleResponse.getSimpleResponse(body);
					}else{
						return JsonUtil.convertJson2Object(body, method.getReturnType(), map);
					}
				}else{
					log.info("网络请求失败。。。");
					return null;
				}
			}catch(Exception e){
				log.error(e.getMessage(),e);
			}
		}
		return null;
	}

	/**
	 * 根据url和请求参数，生成带参数的url。
	 * @param url
	 * @param paras
	 * @return
	 */
	private String getHttpRequestId(String url, List<String[]> paras) {
		StringBuilder httpRequestId = new StringBuilder(url).append("?");
		for(String[] item : paras){
			httpRequestId.append(item[0]).append("=").append(item[1]).append("&");
		}
		return httpRequestId.toString();
	}

	/**
	 * 根据请求参数生成网络请求参数列表。
	 * @param method
	 * @param args
	 * @return
	 */
	private List<String[]> getRequestPara(Method method, Object[] args) {
		List<String[]> paras = new ArrayList<String[]>();
		for(int i=0;i<method.getParameterAnnotations().length;i++){
			Annotation[] annotations = method.getParameterAnnotations()[i];
			if(annotations!=null && annotations.length>0){
				if(annotations[0] instanceof QUERY){
					QUERY query = (QUERY)annotations[0];
					if(query!=null && args[i]!=null){
						paras.add(new String[]{query.value(),args[i]+""});
					}
				}else if(annotations[0] instanceof ENTITY){
					Class<?> cls = args[i].getClass();
					Field[] fields = cls.getDeclaredFields();
					for(Field field:fields){
						Object fieldVal = ReflectUtil.getFieldVal(args[i], field);
						if(fieldVal!=null){
							paras.add(new String[]{field.getName(),fieldVal+""});
						}
					}
				}else{
					log.error("不能识别该注解："+annotations[0]);
				}
			}
		}
		return paras;
	}

	/**
	 * 根据类和方法上的注解标记生成请求URL。
	 * @param method
	 * @param post
	 * @return
	 */
	private String getRequestUrl(Method method, POST post) {
		StringBuilder url = new StringBuilder(baseUrl).append(pathAnnotation!=null?pathAnnotation.value():"");
		if(post==null || post.value().trim().length()==0){
			url.append("/").append(method.getName()).append(pathAnnotation.suffix()).toString();
		}else{
			url.append(post.value());
		}
		return url.toString();
	}

	/**
	 * 取得请求参数中的Callback类实体。
	 * @param args
	 * @return
	 */
	private HttpClientCallback<?> getHttpClientCallBack(Object[] args) {
		HttpClientCallback<?> callback = null;
		for(Object o : args){
			if(o instanceof HttpClientCallback){
				callback = (HttpClientCallback<?>)o;
				break;
			}
		}
		return callback;
	}

	/**
	 * 取得请求参数中的Callback类实体。
	 * @param args
	 * @return
	 */
	private MessageCallback getMessageCallBack(Object[] args) {
		MessageCallback callback = null;
		for(Object o : args){
			if(o instanceof MessageCallback){
				callback = (MessageCallback)o;
				break;
			}
		}
		return callback;
	}
	
	
	@SuppressWarnings("unchecked")
	public static <T> T createInstance(Class<T> cls,String url){
		DynamicHandler handler = new DynamicHandler(cls,url);
		T obj = (T)Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, handler);
		return obj;
	}

	public Class<?> getProxyClass() {
		return proxyClass;
	}

	public void setProxyClass(Class<?> proxyClass) {
		this.proxyClass = proxyClass;
	}
}
