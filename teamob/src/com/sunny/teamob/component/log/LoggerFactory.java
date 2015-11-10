package com.sunny.teamob.component.log;
/**
 * 日志工厂。
 * @author ljl
 *
 */
public class LoggerFactory {
	
	public static <T> SimpleLog getLog(Class<T> cls){
		return SimpleLog.getLog(cls,3);
	}
	
	public static <T> SimpleLog getLog(Class<T> cls,int level){
		return SimpleLog.getLog(cls,level);
	}
}
