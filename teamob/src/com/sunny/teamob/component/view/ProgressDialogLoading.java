package com.sunny.teamob.component.view;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogLoading {
	private ProgressDialog progressDialog = null;
	private Context context;
	private int titleResouce;
	private int tipResouce;
	
	public ProgressDialogLoading(Context context, int titleResouce, int tipResouce) {
		this.context = context;
		this.titleResouce = titleResouce;
		this.tipResouce = tipResouce;
	}

	public void showProDlg(String msg) {
		if(progressDialog==null){
			progressDialog = ProgressDialog.show(context, context.getString(titleResouce), msg);
		}
	}
	
	public void showProDlg(int stringResouceId){
		if(progressDialog==null){
			progressDialog = ProgressDialog.show(context, context.getString(titleResouce), context.getString(stringResouceId));
		}
	}
	
	public void showProDlg() {
		if(progressDialog==null){
			progressDialog = ProgressDialog.show(context, context.getString(titleResouce), context.getString(tipResouce));
		}
	}
	
	public void dismissProDlg(){
		if(progressDialog!=null){
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
}
