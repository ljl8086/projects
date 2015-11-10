package com.sunny.teamob.annontation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import android.widget.AdapterView.OnItemLongClickListener;
/**
 * listView 控件的长按点击事件。
 * @author ljl
 */
@Target(ElementType.METHOD)  
@Retention(RetentionPolicy.RUNTIME)  
@EventBase(listenerType = OnItemLongClickListener.class, listenerSetter = "setOnItemLongClickListener", methodName = "onItemLongClick")  
public @interface OnItemLongClick  
{  
    int[] value();  
}