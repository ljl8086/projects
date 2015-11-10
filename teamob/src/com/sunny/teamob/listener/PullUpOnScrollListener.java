package com.sunny.teamob.listener;

import android.widget.AbsListView;

/**
 * 上拉加载更数据监听器。
 * 
 * @author ljl
 *
 */
public abstract class PullUpOnScrollListener implements android.widget.AbsListView.OnScrollListener {
	private boolean isLast = false;
	private boolean isLoadIng = false;

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (isLast & scrollState==AbsListView.OnScrollListener.SCROLL_STATE_IDLE & !isLoadIng) {
			isLoadIng=true;
			onLast(view);
			isLoadIng=false;
			isLast=false;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		isLast = (firstVisibleItem + visibleItemCount) == totalItemCount;
	}

	/**
	 * 滚动条滚到最后时，触发该方法。
	 * @param view
	 */
	public abstract void onLast(AbsListView view);

}
