package com.sunny.teamob.util;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 网络请求响应类。
 * @author ljl
 *
 */
public class SimpleResponse implements Serializable{
	private static final long serialVersionUID = 1L;
	/**成功*/
	public final static int STATUS_SUCCCESS = 0;
	/**失败*/
	public final static int STATUS_FAIL = 1;
	/**网络超时*/
	public final static int STATUS_SOCKET_TIMEOUT = 3;
	/**其它异常*/
	public final static int STATUS_OTHER_EXP = 2;

	
	private Integer status;
	private String message;
	
	public SimpleResponse(Integer status, String message) {
		super();
		this.status = status;
		this.message = message;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "SimpeResponse [status=" + status + ", message=" + message + "]";
	}
	
	public static SimpleResponse getSimpleResponse(String json){
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			return new SimpleResponse(jsonObject.optInt("status"), jsonObject.optString("message"));
		} catch (JSONException e) {
			return  new SimpleResponse(-501, e.getMessage());
		}
	}
	
}
