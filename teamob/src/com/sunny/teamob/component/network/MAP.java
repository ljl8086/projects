package com.sunny.teamob.component.network;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/**
 * 结果集映射.
 * @author ljl
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MAP {
	/**
	 * JSON串的KEY.
	 * @return
	 */
	String value();
	
	Class<?> type() default Class.class;
}
