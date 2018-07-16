package com.tuacy.annotationprocess;

import com.google.auto.service.AutoService;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

// https://blog.csdn.net/github_35180164/article/details/52055994
@AutoService(Processor.class)
public class FactoryProcessor extends AbstractProcessor {

	private Types    mTypeUtils;
	private Filer    mFiler;
	private Messager mMessager;
	private Elements mElementUtils;
	private Map<String, FactoryGroupedClasses> factoryClasses = new LinkedHashMap<String, FactoryGroupedClasses>();

	@Override
	public synchronized void init(ProcessingEnvironment processingEnvironment) {
		super.init(processingEnvironment);
		mTypeUtils = processingEnvironment.getTypeUtils();
		mFiler = processingEnvironment.getFiler();
		mMessager = processingEnvironment.getMessager();
		mElementUtils = processingEnvironment.getElementUtils();
	}

	/**
	 * 设置只处理Factory的注解
	 */
	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> annotations = new LinkedHashSet<>();
		annotations.add(Factory.class.getCanonicalName());
		return annotations;
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}

	@Override
	public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
		// 遍历所有被注解了@Factory的元素
		for (Element annotatedElement : roundEnvironment.getElementsAnnotatedWith(Factory.class)) {
			// 检查被注解为@Factory的元素是否是一个类
			if (annotatedElement.getKind() != ElementKind.CLASS) {
				error(annotatedElement, "Only classes can be annotated with @%s", Factory.class.getSimpleName());
				return true;
			}
			// 因为我们已经知道它是ElementKind.CLASS类型，所以可以直接强制转换
			TypeElement typeElement = (TypeElement) annotatedElement;

			try {
				FactoryAnnotatedClass annotatedClass = new FactoryAnnotatedClass(typeElement); // throws IllegalArgumentException
				if (!isValidClass(annotatedClass)) {
					return true; // 已经打印了错误信息，退出处理过程
				}
				// 所有检查都没有问题，所以可以添加了
				FactoryGroupedClasses factoryClass = factoryClasses.get(annotatedClass.getQualifiedFactoryGroupName());
				if (factoryClass == null) {
					String qualifiedGroupName = annotatedClass.getQualifiedFactoryGroupName();
					factoryClass = new FactoryGroupedClasses(qualifiedGroupName);
					factoryClasses.put(qualifiedGroupName, factoryClass);
				}
				// 如果和其他的@Factory标注的类的id相同冲突，
				// 抛出IdAlreadyUsedException异常
				factoryClass.add(annotatedClass);
			} catch (IllegalArgumentException e) {
				// @Factory.id()为空 --> 打印错误信息
				error(typeElement, e.getMessage());
				return true;
			}
		}
		// 生成代码
		try {
			for (FactoryGroupedClasses factoryClass : factoryClasses.values()) {
				factoryClass.generateCode(mElementUtils, mFiler);
			}
			// 清除factoryClasses
			factoryClasses.clear();
		} catch (IOException e) {
			error(null, e.getMessage());
		}

		return true;

	}

	private void error(Element e, String msg, Object... args) {
		mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
	}

	private boolean isValidClass(FactoryAnnotatedClass item) {
		// 转换为TypeElement, 含有更多特定的方法
		TypeElement classElement = item.getTypeElement();

		if (!classElement.getModifiers().contains(Modifier.PUBLIC)) {
			error(classElement, "The class %s is not public.", classElement.getQualifiedName().toString());
			return false;
		}

		// 检查是否是一个抽象类
		if (classElement.getModifiers().contains(Modifier.ABSTRACT)) {
			error(classElement, "The class %s is abstract. You can't annotate abstract classes with @%",
				  classElement.getQualifiedName().toString(), Factory.class.getSimpleName());
			return false;
		}
		// 检查继承关系: 必须是@Factory.type()指定的类型子类
		TypeElement superClassElement = mElementUtils.getTypeElement(item.getQualifiedFactoryGroupName());
		if (superClassElement.getKind() == ElementKind.INTERFACE) {
			// 检查接口是否实现了
			if (!classElement.getInterfaces().contains(superClassElement.asType())) {
				error(classElement, "The class %s annotated with @%s must implement the interface %s",
					  classElement.getQualifiedName().toString(), Factory.class.getSimpleName(), item.getQualifiedFactoryGroupName());
				return false;
			}
		} else {
			// 检查子类
			TypeElement currentClass = classElement;
			while (true) {
				TypeMirror superClassType = currentClass.getSuperclass();

				if (superClassType.getKind() == TypeKind.NONE) {
					// 到达了基本类型(java.lang.Object), 所以退出
					error(classElement, "The class %s annotated with @%s must inherit from %s", classElement.getQualifiedName().toString(),
						  Factory.class.getSimpleName(), item.getQualifiedFactoryGroupName());
					return false;
				}

				if (superClassType.toString().equals(item.getQualifiedFactoryGroupName())) {
					// 找到了要求的父类
					break;
				}

				// 在继承树上继续向上搜寻
				currentClass = (TypeElement) mTypeUtils.asElement(superClassType);
			}
		}

		// 检查是否提供了默认公开构造函数
		for (Element enclosed : classElement.getEnclosedElements()) {
			if (enclosed.getKind() == ElementKind.CONSTRUCTOR) {
				ExecutableElement constructorElement = (ExecutableElement) enclosed;
				if (constructorElement.getParameters().size() == 0 && constructorElement.getModifiers().contains(Modifier.PUBLIC)) {
					// 找到了默认构造函数
					return true;
				}
			}
		}
		// 没有找到默认构造函数
		error(classElement, "The class %s must provide an public empty default constructor", classElement.getQualifiedName().toString());
		return false;
	}


}

