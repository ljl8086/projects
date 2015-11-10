package com.sunny.teamob.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.sunny.teamob.component.Layout.LayoutInject;
/**
 * Activity的基类，配合注解的使用，将完成布局文件与Activity类的关联。
 * 
 * @author ljl
 */
public abstract class AbstractBasicFragmentActivity extends FragmentActivity{

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		LayoutInject.injetLayoutContent(this);
		LayoutInject.injectLayout(this);
		LayoutInject.injectEvents(this);
	}

}
