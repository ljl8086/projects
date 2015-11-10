package com.sunny.teamob.view;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
/**
 * 图片导航组件。
 * @author ljl
 *
 */
public class SimplePagerImages implements OnPageChangeListener{
	private LinearLayout linearLayout = null;
	private List<ImageView> imageViews = new ArrayList<ImageView>();
	private Integer pageImage = null;
	private Integer pageNowImage = null;
	
	public SimplePagerImages(LinearLayout linearLayout,int size,int pageImage,int pageNowImage){
		this.linearLayout = linearLayout;
		this.pageImage = pageImage;
		this.pageNowImage = pageNowImage;
		
		createImages(size);
	}

	private void createImages(int size) {
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(5, 0, 0, 0);
		
		for(int i=0;i<size;i++){
			ImageView imageView = new ImageView(linearLayout.getContext());
			imageView.setLayoutParams(layoutParams);
			imageView.setBackgroundResource(i==0?pageNowImage : pageImage);
			linearLayout.addView(imageView);
			imageViews.add(imageView);
		}
	}

	@Override
	public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {
	}

	@Override
	public void onPageSelected(int paramInt) {
		for(ImageView item : imageViews){
			item.setBackgroundResource(pageImage);
		}
		imageViews.get(paramInt).setBackgroundResource(pageNowImage);
	}

	@Override
	public void onPageScrollStateChanged(int paramInt) {
	}
	

}
