package com.sunny.teamob.component.Layout;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunny.teamob.annontation.ContentView;
import com.sunny.teamob.annontation.EventBase;
import com.sunny.teamob.component.log.LoggerFactory;
import com.sunny.teamob.component.log.SimpleLog;

/**
 * 布局与Activity的关联工具。
 * 
 * @author ljl
 *
 */
public class LayoutInject {
	private static SimpleLog log = LoggerFactory.getLog(LayoutInject.class);
	
	/**
	 * activity的注入。<br>
	 * <ol>
	 * <li>布局文件</li>
	 * <li>布局文件中的组件</li>
	 * <li>事件的注入</li>
	 * </ol>
	 * @param o
	 */
	public static <T> void inject(Activity activity){
		LayoutInject.injetLayoutContent(activity);
		LayoutInject.injectLayout(activity);
		LayoutInject.injectEvents(activity);
	}
	

	/**
	 * 布局文件的资源的注入。
	 */
	public static <T> void injectLayout(Object o) {
		try {
			Class<? extends Object> cls = o.getClass();
			Field[] fields = cls.getDeclaredFields();
			for (Field field : fields) {
				ContentView fieldAnnontion = field.getAnnotation(ContentView.class);
				if (fieldAnnontion != null) {
					field.setAccessible(true);
					View view = null;
					if(o instanceof Activity){
						view = ((Activity)o).findViewById(fieldAnnontion.value());
						field.set(o, view);
					}else if(o instanceof Fragment){
						view = ((Fragment)o).getView().findViewById(fieldAnnontion.value());
						if(field.getType().isAssignableFrom(view.getClass())){
							field.set(o, view);
						}else{
							log.error("布局资源注入失败(类型不匹配)："+field.getName());
						}
						
					}else{
						log.error("布局资源注入失败："+field.getName());
					}
					
				}
			}
		} catch (Exception e) {
			log.error("布局资源注入失败", e);
		}
	}

	/**
	 * 布局文件的注入。
	 * 
	 * @param o
	 */
	public static void injetLayoutContent(Activity activity) {
		Class<? extends Object> cls = activity.getClass();
		ContentView contentView = cls.getAnnotation(ContentView.class);
		if (contentView != null) {
			activity.setContentView(contentView.value());
		}
	}

	/**
	 * 布局文件的注入。
	 * @param o
	 */
	public static View injetLayoutContentFragment(Fragment fragment,LayoutInflater inflater,ViewGroup container) {
		Class<? extends Object> cls = fragment.getClass();
		ContentView contentView = cls.getAnnotation(ContentView.class);
		if(contentView!=null){
			return inflater.inflate(contentView.value(), container, false);
		}else{
			return null;
		}
	}

	/**
	 * 注入所有的事件
	 * 
	 * @param activity
	 */
	public static void injectEvents(Object activity) {
		Class<? extends Object> clazz = activity.getClass();
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			Annotation[] annotations = method.getAnnotations();
			for (Annotation annotation : annotations) {
				Class<? extends Annotation> annotationType = annotation.annotationType();
				EventBase eventBaseAnnotation = annotationType.getAnnotation(EventBase.class);
				if (eventBaseAnnotation != null) {
					String listenerSetter = eventBaseAnnotation.listenerSetter();
					Class<?> listenerType = eventBaseAnnotation.listenerType();
					String methodName = eventBaseAnnotation.methodName();

					try {
						Method aMethod = annotationType.getDeclaredMethod("value");
						int[] viewIds = (int[]) aMethod.invoke(annotation);
						DynamicHandler handler = new DynamicHandler(activity);
						handler.addMethod(methodName, method);
						Object listener = Proxy.newProxyInstance(listenerType.getClassLoader(),
								new Class<?>[] { listenerType }, handler);
						for (int viewId : viewIds) {
							View view = null;
							if(activity instanceof Activity){
								view = ((Activity)activity).findViewById(viewId);
							}else if(activity instanceof Fragment){
								view = ((Fragment)activity).getView().findViewById(viewId);
							}
							
							Method setEventListenerMethod = view.getClass().getMethod(listenerSetter, listenerType);
							setEventListenerMethod.invoke(view, listener);
						}

					} catch (Exception e) {
						log.error("事件注入失败", e);
					}
				}

			}
		}
	}
}
