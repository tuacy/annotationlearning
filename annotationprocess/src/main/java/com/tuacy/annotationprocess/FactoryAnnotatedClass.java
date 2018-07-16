package com.tuacy.annotationprocess;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

public class FactoryAnnotatedClass {

	private TypeElement annotatedClassElement;
	private String      qualifiedSuperClassName;
	private String      simpleTypeName;
	private String      id;

	public FactoryAnnotatedClass(TypeElement classElement) throws IllegalArgumentException {
		this.annotatedClassElement = classElement;
		Factory annotation = classElement.getAnnotation(Factory.class);
		id = annotation.id();

		if (id.length() == 0) {
			throw new IllegalArgumentException(
				String.format("id() in @%s for class %s is null or empty! that's not allowed", Factory.class.getSimpleName(),
							  classElement.getQualifiedName().toString()));
		}

		// Get the full QualifiedTypeName
		try {
			Class<?> clazz = annotation.type();
			qualifiedSuperClassName = clazz.getCanonicalName();
			simpleTypeName = clazz.getSimpleName();
		} catch (MirroredTypeException mte) {
			DeclaredType classTypeMirror = (DeclaredType) mte.getTypeMirror();
			TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
			qualifiedSuperClassName = classTypeElement.getQualifiedName().toString();
			simpleTypeName = classTypeElement.getSimpleName().toString();
		}
	}

	/**
	 * 获取在{@link Factory#id()}中指定的id
	 * return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * 获取在{@link Factory#type()}指定的类型合法全名
	 *
	 * @return qualified name
	 */
	public String getQualifiedFactoryGroupName() {
		return qualifiedSuperClassName;
	}


	/**
	 * 获取在{@link Factory#type()}{@link Factory#type()}指定的类型的简单名字
	 *
	 * @return qualified name
	 */
	public String getSimpleFactoryGroupName() {
		return simpleTypeName;
	}

	/**
	 * 获取被@Factory注解的原始元素
	 */
	public TypeElement getTypeElement() {
		return annotatedClassElement;
	}

}
