package com.tuacy.annotationprocess;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Factory {

	/**
	 * 工厂的名字
	 */
	Class type();

	/**
	 * 用来表示生成哪个对象的唯一id
	 */
	String id();


}
