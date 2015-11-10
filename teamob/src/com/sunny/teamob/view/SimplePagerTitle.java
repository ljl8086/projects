package com.sunny.teamob.view;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunny.teamob.adapter.SimplePagerAdapter;

/**
 * 标题导航组件。
 * 
 * @author ljl
 *
 */
public class SimplePagerTitle implements OnPageChangeListener {
	private ViewGroup viewGroup = null;
	private List<TextView> titleTextView = new ArrayList<TextView>();
	private ViewPager viewPager = null;
	private SimplePagerAdapter<Object> pagerAdapter;
	private HorizontalScrollView horizontalScrollView;
	private Margins margins = new Margins(25, 0, 25, 0);
	/**
	 * 构造标题导航组件。
	 * @param viewGroup 装载标题栏的容器
	 * @param pagerAdapter 导航组件的适配器
	 * @param viewPager 
	 * @param horizontalScrollView 滚动条组件
	 */
	public SimplePagerTitle(ViewGroup viewGroup, SimplePagerAdapter<Object> pagerAdapter, ViewPager viewPager,
			HorizontalScrollView horizontalScrollView) {
		this.viewGroup = viewGroup;
		this.pagerAdapter = pagerAdapter;
		this.viewPager = viewPager;
		this.horizontalScrollView = horizontalScrollView;
		createViews();
	}

	/**
	 * 构造标题导航组件。
	 * @param viewGroup 装载标题栏的容器
	 * @param pagerAdapter 导航组件的适配器
	 * @param viewPager 
	 * @param horizontalScrollView 滚动条组件
	 */
	public SimplePagerTitle(ViewGroup viewGroup, SimplePagerAdapter<Object> pagerAdapter, ViewPager viewPager,
			HorizontalScrollView horizontalScrollView,Margins margins) {
		this.viewGroup = viewGroup;
		this.pagerAdapter = pagerAdapter;
		this.viewPager = viewPager;
		this.horizontalScrollView = horizontalScrollView;
		this.margins = margins;
		createViews();
	}
	
	public void setMargins(Margins margins){
		this.margins = margins;
	}

	private void createViews() {
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(margins.left, margins.top, margins.right, margins.bottom);

		for (int i = 0; i < pagerAdapter.getCount(); i++) {
			final int index = i;
			TextView textView = new TextView(viewGroup.getContext());
			textView.setLayoutParams(layoutParams);
			textView.setText(pagerAdapter.getTitleList().get(i));
			textView.setTextSize(18);
			textView.setPadding(10, 5, 10, 5);
			textView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View paramView) {
					viewPager.setCurrentItem(index);
				}
			});

			viewGroup.addView(textView);
			titleTextView.add(textView);
			onPageSelected(0);
		}
	}

	@Override
	public void onPageScrolled(int onPageScrolled, float paramFloat, int paramInt2) {
	}

	@Override
	public void onPageSelected(int paramInt) {
		for (TextView item : titleTextView) {
			item.setTextColor(Color.BLACK);
		}
		titleTextView.get(paramInt).setTextColor(Color.RED);
		
		int left = 0;
		for(int i=0;i<paramInt;i++){
			left += titleTextView.get(i).getWidth() + 50;
		}
		horizontalScrollView.smoothScrollTo(left, 0);
	}

	@Override
	public void onPageScrollStateChanged(int paramInt) {
	}
	
	class Margins{
		
		public Margins(int left, int top, int right, int bottom) {
			super();
			this.left = left;
			this.top = top;
			this.right = right;
			this.bottom = bottom;
		}
		public int left;
		public int top;
		public int right;
		public int bottom;
	}

}
