package com.sunny.teamob.util;

import java.io.IOException;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.sunny.teamob.component.log.LoggerFactory;
import com.sunny.teamob.component.log.SimpleLog;

/**
 * OkHttp工具类。
 * 
 * @author ljl
 *
 */
public final class HttpUtil {
	final private static SimpleLog log = LoggerFactory.getLog(HttpUtil.class); 

	public static Response httpClient(String url,String[]... para) throws IOException {
		FormEncodingBuilder builder = new FormEncodingBuilder();
		for (String[] item : para) {
			builder.add(item[0], item[1]);
		}
		logHttpReq(url, para);
		Request request = new Request.Builder().url(url).post(builder.build()).build();
		Response res = OkHttpUtil.execute(request);
		return res;
	}

	public static void asyncHttpClient(String url, String[][] para,Callback callback) {
		try {
			FormEncodingBuilder builder = new FormEncodingBuilder();
			StringBuilder tag = new StringBuilder(url).append("?");
			for (String[] item : para) {
				builder.add(item[0], item[1]);
				tag.append(item[0]).append("=").append(item[1]).append("&");
			}
			log.info(tag.toString());
			RequestBody rb = (para==null || para.length==0) ? null : builder.build();
			Request request = new Request.Builder().url(url).post(rb).tag(tag.toString()).build();
			OkHttpUtil.asyncExecute(request, callback);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private static void logHttpReq(String url, String[][] para) {
		StringBuilder urlWithPara = new StringBuilder(url).append("?");
		for (String[] item : para) {
			urlWithPara.append(item[0]).append("=").append(item[1]).append("&");
		}
		log.info(urlWithPara.toString());
	}
}