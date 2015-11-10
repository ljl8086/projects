package com.sunny.teamob.component.network;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/**
 * 消息ID.
 * @author ljl
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MESSAGE {
	int success();
	int fail();
}
