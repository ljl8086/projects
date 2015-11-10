package com.sunny.teamob.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SimplePagerAdapter<T> extends FragmentPagerAdapter{
	
	private List<Fragment> fragmentList = new ArrayList<Fragment>();
	private List<String> titleList = new ArrayList<String>();
	
	public SimplePagerAdapter(FragmentManager fm,List<T> dataList,List<String> titleList) {
		super(fm);
		this.fragmentList.addAll(fragmentList);
		this.titleList.addAll(titleList);
	}
	
	public SimplePagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	public void add(String title, Fragment fragment){
		titleList.add(title);
		fragmentList.add(fragment);
	}
	
	@Override
	public Fragment getItem(int position) {
		return fragmentList.get(position);
	}

	@Override
	public int getCount() {
		return fragmentList.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return titleList.get(position);
	}

	public List<Fragment> getFragmentList() {
		return fragmentList;
	}

	public void setFragmentList(List<Fragment> fragmentList) {
		this.fragmentList = fragmentList;
	}

	public List<String> getTitleList() {
		return titleList;
	}

	public void setTitleList(List<String> titleList) {
		this.titleList = titleList;
	}
}
