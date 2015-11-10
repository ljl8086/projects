package com.sunny.teamob.db;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sunny.teamob.component.log.LoggerFactory;
import com.sunny.teamob.component.log.SimpleLog;
import com.sunny.teamob.util.ReflectUtil;

public class DBUtil {
	final static SimpleLog log = LoggerFactory.getLog(DBUtil.class);

	public static void add(SQLiteDatabase db, Object obj) {
		Field[] fields = obj.getClass().getDeclaredFields();
		String tableName = obj.getClass().getSimpleName();
		if (obj.getClass().isAnnotationPresent(Table.class)) {
			tableName = ((Table) obj.getClass().getAnnotation(Table.class)).value();
		}
		ContentValues values = new ContentValues();
		for (Field f : fields) {
			Object val = ReflectUtil.getFieldVal(obj, f);
			String name = f.getName();
			if (f.isAnnotationPresent(NoDbField.class)) {
				continue;
			}

			if (val != null) {
				if (val instanceof Byte) {
					values.put(name, (Byte) val);
				} else if (val instanceof Short) {
					values.put(name, (Short) val);
				} else if (val instanceof Integer) {
					values.put(name, (Integer) val);
				} else if (val instanceof Long) {
					values.put(name, (Long) val);
				} else if (val instanceof Float) {
					values.put(name, (Float) val);
				} else if (val instanceof Double) {
					values.put(name, (Double) val);
				} else if (val instanceof Float) {
					values.put(name, (Float) val);
				} else if (val instanceof Boolean) {
					values.put(name, (Boolean) val);
				} else if (val instanceof Byte[]) {
					values.put(name, (byte[]) val);
				} else if (val instanceof String) {
					values.put(name, (String) val);
				} else {
					log.error("字段：" + name + " 的类型暂不支持");
				}
			}
		}
		db.insert(tableName, null, values);
	}

	public static <T> List<T> getList(Cursor cursor, T p) {
		List<T> list = new ArrayList<T>();
		while (cursor.moveToNext()) {
			T obj = get(cursor, p);
			list.add(obj);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(Cursor cursor, T p) {
		try {
			if(cursor.isBeforeFirst()){
				if(!cursor.moveToNext())return null;
			}
			
			T obj = (T) p.getClass().newInstance();
			Method[] methods = p.getClass().getMethods();
			for (Method method : methods) {
				if (method.getName().startsWith("set") && method.getParameterTypes().length == 1) {
					char[] names = method.getName().toCharArray();
					names[3] = Character.toLowerCase(names[3]);
					String name = new String(names, 3, names.length - 3);
					Object temp = getColumnVal(cursor, name);
					Class<?> para = method.getParameterTypes()[0];
					if (para.isAssignableFrom(Long.class)) {
						if (temp instanceof Integer) {
							temp = ((Integer) temp).longValue();
						} else if (temp instanceof Short) {
							temp = ((Short) temp).longValue();
						} else if (temp instanceof Byte) {
							temp = ((Byte) temp).longValue();
						}
					}
					if (temp != null) {
						method.invoke(obj, temp);
					}
				}
			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object getColumnVal(Cursor cursor, String columnName) {
		Object temp = null;
		int index = cursor.getColumnIndex(columnName);
		if (index >= 0) {
			int type = cursor.getType(index);
			switch (type) {
			case Cursor.FIELD_TYPE_FLOAT:
				temp = cursor.getFloat(index);
				break;
			case Cursor.FIELD_TYPE_BLOB:
				temp = cursor.getBlob(index);
				break;
			case Cursor.FIELD_TYPE_INTEGER:
				temp = cursor.getInt(index);
				break;
			case Cursor.FIELD_TYPE_NULL:
				break;
			case Cursor.FIELD_TYPE_STRING:
				temp = cursor.getString(index);
				break;
			}
		}
		return temp;
	}

}
