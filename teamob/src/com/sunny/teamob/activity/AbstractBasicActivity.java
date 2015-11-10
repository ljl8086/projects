package com.sunny.teamob.activity;

import android.app.Activity;
import android.os.Bundle;

import com.sunny.teamob.component.Layout.LayoutInject;
import com.sunny.teamob.component.log.LoggerFactory;
import com.sunny.teamob.component.log.SimpleLog;

/**
 * Activity的基类，配合注解的使用，将完成布局文件与Activity类的关联。
 * 
 * @author ljl
 *
 */
public abstract class AbstractBasicActivity extends Activity {
	protected final SimpleLog log = LoggerFactory.getLog(this.getClass());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LayoutInject.injetLayoutContent(this);
		LayoutInject.injectLayout(this);
		LayoutInject.injectEvents(this);
	}

}
