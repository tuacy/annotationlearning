package com.tuacy.annotationprocess;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

public class FactoryAnnotatedClass {

	// 添加注解的类(TypeElement表示是一个类或者接口)
	private TypeElement mAnnotatedClassElement;
	// Factory type参数对应的类的路径+名字(父类的完整路径)
	private String      mQualifiedSuperClassName;
	// Factory type参数对应的类的名字
	private String      mSimpleTypeName;
	// Factory id参数
	private String      mId;

	public FactoryAnnotatedClass(TypeElement classElement) throws IllegalArgumentException {
		this.mAnnotatedClassElement = classElement;
		Factory annotation = classElement.getAnnotation(Factory.class);

		//获取Factory注解的id
		mId = annotation.id();
		// id 不能为null
		if (mId.length() == 0) {
			throw new IllegalArgumentException(
				String.format("mId() in @%s for class %s is null or empty! that's not allowed", Factory.class.getSimpleName(),
							  classElement.getQualifiedName().toString()));
		}

		try {
			// 获取到注解对应的类
			Class<?> clazz = annotation.type();
			// 包路径加类的名字
			mQualifiedSuperClassName = clazz.getCanonicalName();
			// 类的名字
			mSimpleTypeName = clazz.getSimpleName();
		} catch (MirroredTypeException mte) {
			// 目标源码还没有编译成字节码，通过MirroredTypeException去获取mQualifiedSuperClassName和mSimpleTypeName
			DeclaredType classTypeMirror = (DeclaredType) mte.getTypeMirror();
			TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
			mQualifiedSuperClassName = classTypeElement.getQualifiedName().toString();
			mSimpleTypeName = classTypeElement.getSimpleName().toString();
		}
	}

	/**
	 * 获取在{@link Factory#id()}中指定的id
	 * return the mId
	 */
	public String getId() {
		return mId;
	}

	/**
	 * 获取在{@link Factory#type()}指定的类型合法全名
	 *
	 * @return qualified name
	 */
	public String getQualifiedFactoryGroupName() {
		return mQualifiedSuperClassName;
	}


	/**
	 * 获取在{@link Factory#type()}{@link Factory#type()}指定的类型的简单名字
	 *
	 * @return qualified name
	 */
	public String getSimpleFactoryGroupName() {
		return mSimpleTypeName;
	}

	/**
	 * 获取被@Factory注解的原始元素
	 */
	public TypeElement getTypeElement() {
		return mAnnotatedClassElement;
	}

}
