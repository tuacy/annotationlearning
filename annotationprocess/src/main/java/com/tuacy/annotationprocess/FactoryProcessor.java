package com.tuacy.annotationprocess;

import com.google.auto.service.AutoService;

import java.util.LinkedHashSet;
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
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
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

		}
		return false;
	}

	private void error(Element e, String msg, Object... args) {
		mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
	}


}

