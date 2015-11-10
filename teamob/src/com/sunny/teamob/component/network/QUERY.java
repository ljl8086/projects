package com.sunny.teamob.component.network;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/**
 * 请求URL.
 * @author ljl
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface QUERY {
	String value();
}
