package com.sunny.teamob.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sunny.teamob.component.log.LoggerFactory;
import com.sunny.teamob.component.log.SimpleLog;
import com.sunny.teamob.component.network.DynamicHandler;
import com.sunny.teamob.component.network.MAP;

public class JsonUtil {
	private final static SimpleLog log = LoggerFactory.getLog(DynamicHandler.class);

	public static Object convertJson(String json, Class<?> descClass) {
		try {
			JSONObject jo = new JSONObject(json);
			return convertJson(jo, descClass);
		} catch (Exception e) {
			log.error("JSON转换对象异常", e);
			return null;
		}
	}

	public static <T> T convertJson(JSONObject json, Class<T> descClass) {
		String methodName = null;
		try {
			T descObj = descClass.newInstance();
			Field[] fields = descClass.getDeclaredFields();
			Method[] methods = descClass.getDeclaredMethods();

			for (Field item : fields) {
				methodName = genSetName(item.getName());
				Method method = null;
				for (Method m : methods) {
					if (m.getName().equals(methodName)) {
						method = m;
						break;
					}
				}

				if (method != null) {
					if (!json.isNull(item.getName())) {
						Object object = json.opt(item.getName());
						if (object != null) {
							Class<?> paraType = method.getParameterTypes()[0];
							if (paraType.isAssignableFrom(Integer.class)) {
								method.invoke(descObj, Integer.parseInt(object + ""));
							} else if (paraType.isAssignableFrom(Long.class)) {
								method.invoke(descObj, Long.parseLong(object + ""));
							} else if (paraType.isAssignableFrom(Float.class)) {
								method.invoke(descObj, Float.parseFloat(object + ""));
							} else if (paraType.isAssignableFrom(Double.class)) {
								method.invoke(descObj, Double.parseDouble(object + ""));
							} else if(paraType.isAssignableFrom(String.class)){
								method.invoke(descObj, object);
							}else if(paraType.isAssignableFrom(Date.class) || paraType.isAssignableFrom(java.sql.Date.class)){
								SimpleDateFormat sf = null;
								String temp = (String)object;
								if(temp.length()==19 & temp.indexOf('-')>0){
									sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
								}else if(temp.length()==19 & temp.indexOf('/')>0){
									sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss",Locale.CHINA);
								}
								if(sf==null){
									log.error(methodName+ "日期格式暂不支持");
								}else{
									method.invoke(descObj, sf.parse(temp));
								}
							} else {
								JSONObject jObj = (JSONObject)object;
								Object temp = convertJson(jObj, paraType);
								method.invoke(descObj, temp);
							}
						}
					}
				}
			}
			return descObj;
		} catch (Exception e) {
			log.error("JSON转换对象异常方法:" + methodName, e);
			return null;
		}
	}

	/**
	 * 将对象转换为MAP。
	 * 
	 * @param json
	 * @return
	 */
	public static Map<String, Object> convertJson2Map(JSONObject json) {
		Map<String, Object> res = new HashMap<String, Object>();
		Iterator<String> iterator = json.keys();
		while (iterator.hasNext()) {
			String key = iterator.next();
			res.put(key, json.opt(key));
		}
		return res;
	}

	private static String genSetName(String fieldName) {
		StringBuilder sb = new StringBuilder("set").append(fieldName);
		sb.setCharAt(3, Character.toUpperCase(sb.charAt(3)));
		return sb.toString();
	}

	/**
	 * 将JSON数组转换为JAVA对象数组。
	 * 
	 * @param array
	 * @param descClass
	 * @return
	 */
	public static Object[] convertJsonArray(JSONArray array, Class<?> descClass) {
		try {
			Object[] descObjs = new Object[array.length()];
			for (int i = 0; i < array.length(); i++) {
				descObjs[i] = convertJson(array.getJSONObject(i), descClass);
			}
			return descObjs;
		} catch (Exception e) {
			log.error("JSON转换对象异常", e);
			return null;
		}
	}

	// /**
	// * 将JSON数组转换为JAVA对象list。
	// * @param array
	// * @param descClass
	// * @return
	// */
	// @SuppressWarnings({ "rawtypes", "unchecked" })
	// public static List convertJsonList23(JSONArray array,Class<?> descClass){
	// try{
	// List descObjs = new ArrayList();
	// for(int i=0;i<array.length();i++){
	// descObjs.add(convertJson(array.getJSONObject(i), descClass));
	// }
	// return descObjs;
	// }catch(Exception e){
	// Log.e(JsonUtil.class.getName(), "JSON转换对象异常",e);
	// return null;
	// }
	// }

	/**
	 * 将JSON数组转换为JAVA对象list。
	 * 
	 * @param array
	 * @param descClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> convertJsonList(JSONArray array, Class<T> descClass) {
		if (array == null)
			return null;
		try {
			List<T> descObjs = new ArrayList<T>();
			for (int i = 0; i < array.length(); i++) {
				if (descClass.isAssignableFrom(Map.class)) {
					descObjs.add((T) convertJson2Map(array.getJSONObject(i)));
				} else {
					descObjs.add(convertJson(array.getJSONObject(i), descClass));
				}
			}
			return descObjs;
		} catch (Exception e) {
			log.error("JSON转换对象异常", e);
			return null;
		}
	}

	public static <T> String convertList2Json(List<T> list) {
		JSONArray array = new JSONArray(list);
		return array.toString();
	}

	public static Object convertJson2Object(String body, Class<?> returnType, MAP map) throws JSONException {
		JSONObject object = new JSONObject(body);
		Class<?> descClass = map.type().equals(Class.class) ? returnType : map.type();
		if (object.optInt("status") == 0) {
			JSONArray response = object.optJSONArray(map.value());
			if (map.type().isAssignableFrom(Map.class)) {
				if (returnType.isAssignableFrom(Map.class)) {
					log.error("JSON串转换错误:结果集为Map的暂未实现");
				} else if (returnType.isAssignableFrom(List.class)) {
					return JsonUtil.convertJsonList(response, Map.class);
				} else {
					log.error("JSON串转换错误");
				}
			} else if (Collection.class.isAssignableFrom(returnType)) {
				List<?> list = JsonUtil.convertJsonList(response, descClass);
				return list;
			} else {
				if(response!=null){
					List<?> list = JsonUtil.convertJsonList(response, descClass);
					return list.get(0);
				}else{
					JSONObject jObj = object.optJSONObject(map.value());
					return JsonUtil.convertJson(jObj, descClass);
				}
			}
			return null;

		} else {
			throw new SimpleException(new SimpleResponse(object.optInt("status"), object.optString("message")));
		}
	}
}
