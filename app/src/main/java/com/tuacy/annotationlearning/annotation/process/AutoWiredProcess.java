package com.tuacy.annotationlearning.annotation.process;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class AutoWiredProcess {

	public static void bind(final Object object) {
		Class parentClass = object.getClass();
		Field[] fields = parentClass.getFields();
		for (final Field field : fields) {
			AutoWired autoWiredAnnotation = field.getAnnotation(AutoWired.class);
			if (autoWiredAnnotation != null) {
				field.setAccessible(true);
				try {
					Class autoCreateClass = Class.forName(field.getType().getName());
					Constructor autoCreateConstructor = autoCreateClass.getConstructor();
					Object autoCrateObject = autoCreateConstructor.newInstance();
					field.set(object, autoCrateObject);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}

			}
		}
	}

}
