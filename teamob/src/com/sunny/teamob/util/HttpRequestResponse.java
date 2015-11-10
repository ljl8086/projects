package com.sunny.teamob.util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 网络请求响应类。
 * 
 * @author ljl
 * 
 */
public class HttpRequestResponse {
	/** 网络请求错误 */
	public static int NETWORK_ERROR = -1;
	/** 网络请求成功 */
	public static int NETWORK_SUCCESS = 200;
	/** 网络请求是否成功。-1 为失败，200 成功 , 500 服务端内部故障, 404 找不到网页 */
	private int status = -1;
	/** 请求ID。 */
	private int requestId = -1;
	/** 响应报文 */
	private String responseTxt;
	/**附加对象*/
	private Object tag;

	public int getStatus() {
		return status;
	}

	public String getResponseTxt() {
		return responseTxt;
	}

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setResponseTxt(String responseTxt) {
		this.responseTxt = responseTxt;
	}

	@Override
	public String toString() {
		return "HttpRequestResponse [status=" + status + ", responseTxt="
				+ responseTxt + "]";
	}

	public JSONObject getResponseJson() throws JSONException {
		return new JSONObject(responseTxt);
	}

	public Object getTag() {
		return tag;
	}

	public void setTag(Object tag) {
		this.tag = tag;
	}
}
