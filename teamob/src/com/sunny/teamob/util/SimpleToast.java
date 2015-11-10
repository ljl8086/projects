package com.sunny.teamob.util;

import android.content.Context;
import android.widget.Toast;
/**
 * 消息提示框。
 * @author ljl
 *
 */
public class SimpleToast {
	/**
	 * 长时消息提示框。
	 * @param context
	 * @param msg 消息内容
	 */
	public static void show(Context context,String msg){
		Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 短暂消息提示框。
	 * @param context
	 * @param msg 消息内容
	 */
	public static void showShort(Context context,String msg){
		Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
	}
}
