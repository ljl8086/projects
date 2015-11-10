package com.sunny.teamob.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
/**
 * 反射工具类。
 * @author ljl
 *
 */
public class ReflectUtil {
	public static String genSetName(String fieldName){
		StringBuilder sb = new StringBuilder("set").append(fieldName);
		sb.setCharAt(3, Character.toUpperCase(sb.charAt(3)));
		return sb.toString();
	}

	public static String genGetName(String fieldName){
		StringBuilder sb = new StringBuilder("get").append(fieldName);
		sb.setCharAt(3, Character.toUpperCase(sb.charAt(3)));
		return sb.toString();
	}
	
	public static Object getFieldVal(Object obj,Field field){
		try{
			if(!Modifier.isStatic(field.getModifiers())){
				String setMethod = genGetName(field.getName());
				Method method = obj.getClass().getDeclaredMethod(setMethod, new Class[]{});
				return method.invoke(obj);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static void setFieldVal(Object obj,Field field,Object val) throws Exception{
		String methodName = ReflectUtil.genSetName(field.getName());
		Method method = obj.getClass().getMethod(methodName, field.getType());
		method.invoke(obj, val);
	}
}
