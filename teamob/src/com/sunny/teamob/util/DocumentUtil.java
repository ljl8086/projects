package com.sunny.teamob.util;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public final class DocumentUtil {
	/**
	 * 选择图片如果用的是“最近图片”来选的话，返回的是图片ID，而不是图片本身，通过本工具类，可以解决此问题。
	 * @param context
	 * @param uri
	 * @return
	 */
	public static Uri getUri(Context context, Uri uri){
		Uri temp = uri;
		String documents = uri.getPathSegments().get(0);
		if(documents!=null && documents.equals("document")){
			String wholeID = uri.getPathSegments().get(uri.getPathSegments().size()-1);
			String id = wholeID.split(":")[1];
			String[] column = { MediaStore.Images.Media.DATA };
			String sel = MediaStore.Images.Media._ID + "=?";
			Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,column, sel, new String[]{ id }, null);
			int columnIndex = cursor.getColumnIndex(column[0]);
			if (cursor.moveToFirst()) {
			//DATA字段就是本地资源的全路径
				String filePath = cursor.getString(columnIndex);
				temp = Uri.fromFile(new File(filePath));
			}
			//关闭游标
			cursor.close();
		}
		return temp;
	}
}
