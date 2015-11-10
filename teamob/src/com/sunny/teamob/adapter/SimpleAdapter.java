package com.sunny.teamob.adapter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.sunny.teamob.component.log.LoggerFactory;
import com.sunny.teamob.component.log.SimpleLog;

public class SimpleAdapter<T> extends BaseAdapter {
	private final SimpleLog log = LoggerFactory.getLog(this.getClass()); 
	protected List<T> dataList = new ArrayList<T>();
	protected Integer layoutResouce = null;
	protected DisplayImageOptions imageOptions = null;
	protected LayoutInflater inflater = null;

	public SimpleAdapter(Context context, Integer layoutResouce, List<T> dataList) {
		this.layoutResouce = layoutResouce;
		this.dataList = dataList;
		this.imageOptions = new DisplayImageOptions.Builder()
//		.showImageForEmptyUri(R.drawable.moreng)// 设置图片Uri为空或是错误的时候显示的图片
//		.showImageOnFail(R.drawable.moreng)// 设置图片加载/解码过程中错误时候显示的图片
		.cacheInMemory(true)// 是否緩存都內存中
		.displayer(new RoundedBitmapDisplayer(20)).build();
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public T getItemObj(int position) {
		return dataList.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(layoutResouce, parent, false);
		}
		layoutObjectMap(position, convertView);
		return convertView;
	}

	/**
	 * 页面控件与对象的关联。
	 * @param position
	 * @param convertView
	 */
	protected void layoutObjectMap(int position, View convertView) {
		if(position>dataList.size()-1){
			return;
		}
		T obj = dataList.get(position);
		if(obj instanceof Map){
			Map<String,?> objMap = (Map<String,?>)obj;
			for(Map.Entry<String, ?> item : objMap.entrySet()){
				writeView(convertView, item.getValue(), item.getKey());
			}
		}else{
			writeViews(convertView,obj);
		}
	}

	protected void writeView(View convertView, Object val, String name) {
		View view = convertView.findViewWithTag(name);
		if (view != null) {
			try {
				if (view instanceof TextView) {
					TextView txtView = (TextView) view;
					txtView.setText(val + "");
				}else if(view instanceof ImageView){
					if(val!=null){
						if(val instanceof String){
							ImageLoader.getInstance().displayImage(val+"", (ImageView)view, imageOptions);
						}else if(val instanceof Boolean){
							view.setVisibility((Boolean)val ? View.GONE : View.VISIBLE);
						}else if(val instanceof Integer){
							StringBuilder sb = new StringBuilder("drawable://").append(val);
							ImageLoader.getInstance().displayImage(sb.toString(), (ImageView)view, imageOptions);
						}
						
					}
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}
	
	/**
	 * 填充view下所有有tag标识的控件。
	 * @param rootView 根控件
	 * @param obj
	 */
	private void writeViews(View rootView,Object obj){
		if(rootView instanceof ViewGroup){
			ViewGroup group = (ViewGroup)rootView;
			for(int i=0;i<group.getChildCount();i++){
				View item = group.getChildAt(i);
				writeViews(item,obj);
			}
		}else{
			if(rootView.getTag()!=null){
				if(rootView.getTag() instanceof String){
					String tag = (String)rootView.getTag();
					String[] tags = tag.split("\\.");
					try{
						Object val = getObjectVal(obj,tags,0);
						writeView(rootView, val);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * 反射获取指定字段的值。
	 * @param obj 根据对象
	 * @param tags 可以有多级。如member.mobile
	 * @param level 层级
	 * @return
	 * @throws Exception
	 */
	private Object getObjectVal(Object obj,String[] tags,int level) throws Exception{
		char[] methodName = tags[level].toCharArray();
		methodName[0] = Character.toUpperCase(methodName[0]);
		Method method = obj.getClass().getMethod("get"+new String(methodName), new Class[0]);
		Object val = method.invoke(obj);
		if(val==null)return null;
		
		if(level>=tags.length-1){
			try{
				return val;
			}catch(Exception e){
				return null;
			}
		}else{
			return getObjectVal(val, tags, ++level);
		}
	}
	
	
	protected void writeView(View view, Object val) {
		if (view != null) {
			try {
				if (view instanceof TextView) {
					TextView txtView = (TextView) view;
					txtView.setText(val + "");
				}else if(view instanceof ImageView){
					if(val!=null){
						if(val instanceof String){
							ImageLoader.getInstance().displayImage(val+"", (ImageView)view, imageOptions);
						}else if(val instanceof Boolean){
							view.setVisibility((Boolean)val ? View.GONE : View.VISIBLE);
						}else if(val instanceof Integer){
//							StringBuilder sb = new StringBuilder("drawable://").append(val);
//							ImageLoader.getInstance().displayImage(sb.toString(), (ImageView)view, imageOptions);
							ImageView imageView = (ImageView)view;
							imageView.setImageResource((Integer)val);
						}
					}
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}

}
