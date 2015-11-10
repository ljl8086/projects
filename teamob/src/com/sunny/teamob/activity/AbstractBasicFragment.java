package com.sunny.teamob.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunny.teamob.component.Layout.LayoutInject;
/**
 * 所有Fragment都继承本基类，再配合注解实现资源的自动注入。
 * @author ljl
 *
 */
public abstract class AbstractBasicFragment extends Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return LayoutInject.injetLayoutContentFragment(this, inflater, container);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		LayoutInject.injectLayout(this);
		LayoutInject.injectEvents(this);
		super.onActivityCreated(savedInstanceState);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

}
