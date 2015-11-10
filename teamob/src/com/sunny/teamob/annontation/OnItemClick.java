package com.sunny.teamob.annontation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import android.widget.AdapterView.OnItemClickListener;
/**
 * listView 控件的点击事件。
 * @author ljl
 */
@Target(ElementType.METHOD)  
@Retention(RetentionPolicy.RUNTIME)  
@EventBase(listenerType = OnItemClickListener.class, listenerSetter = "setOnItemClickListener", methodName = "onItemClick")  
public @interface OnItemClick  
{  
    int[] value();  
}