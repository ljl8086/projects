package com.sunny.teamob.annontation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 页面资源注解。
 * @author ljl
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ContentView {
	int value();
}
