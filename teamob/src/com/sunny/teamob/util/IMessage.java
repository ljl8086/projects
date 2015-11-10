package com.sunny.teamob.util;

/**
 * 用于网络请求的handle处理器。
 * @author ljl
 *
 */
public interface IMessage<T> {
	public void handleMessageDQC(T response);
}
