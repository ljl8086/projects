package com.sunny.teamob.component.network;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/**
 * 查询串.
 * @author ljl
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface PATH {
	/**请求路径*/
	String value();
	/**请求后缀*/
	String suffix() default "";
}
