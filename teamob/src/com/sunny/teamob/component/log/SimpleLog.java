package com.sunny.teamob.component.log;

import android.util.Log;
/**
 * 简单的日志文件。
 * @author ljl
 *
 */
public class SimpleLog {
	private String tag = null;
	/**级别:0 不打印    1 error  2 info   3 debug*/
	private int level = 0;
	
	private SimpleLog(String tag,int level){
		this.tag = tag;
		this.level = level;
	}
	
	public static <T> SimpleLog getLog(Class<T> cls,int level){
		return new SimpleLog(cls.getSimpleName(),level);
	}
	
	public void error(Object msg,Throwable throwable){
		if(level==1 || level==2 || level==3) Log.e(tag, msg+"", throwable);
	}
	
	public void error(Object msg){
		if(level==1 || level==2 || level==3) Log.e(tag, msg+"");
	}
	
	public void info(Object msg){
		if(level==2 || level==3) Log.i(tag, msg+"");
	}
	
	public void debug(Object msg){
		if(level==3) Log.d(tag, msg+"");
	}
	
}
