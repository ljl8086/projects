package com.sunny.teamob.util;

import java.io.Serializable;

import android.app.Activity;
import android.content.Intent;

public class IntentUtil {
	/**
	 * 简化Activity的切换与传递。<br>
	 * 示例：<br>
	 * <code>
	 * 	IntentUtil.startActivity(this, HorizontalMainActivity.class, new Serializable[][]{{"key","val"}});
	 * </code>
	 * @param myself
	 * @param target
	 * @param para
	 */
	public static <T> void startActivity(Activity myself,Class<T> target,Serializable[]... para){
		Intent intent = new Intent();
		intent.setClass(myself, target);
		for(Serializable[] item : para){
			intent.putExtra((String)item[0], item[1]);
		}
		myself.startActivity(intent);
	}
}
