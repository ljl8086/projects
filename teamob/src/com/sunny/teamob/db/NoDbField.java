package com.sunny.teamob.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 非数据库字段。
 * @author ljl
 */
@Target(ElementType.FIELD)  
@Retention(RetentionPolicy.RUNTIME)  
public @interface NoDbField {

}
