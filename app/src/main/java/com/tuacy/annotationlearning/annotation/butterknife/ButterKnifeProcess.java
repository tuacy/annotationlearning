package com.tuacy.annotationlearning.annotation.butterknife;

import android.app.Activity;
import android.app.Fragment;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ButterKnifeProcess {

	/**
	 * 绑定Activity
	 */
	public static void bind(final Activity activity) {
		Class annotationParent = activity.getClass();
		Field[] fields = annotationParent.getDeclaredFields();
		Method[] methods = annotationParent.getDeclaredMethods();
		// OnClick
		for (final Method method : methods) {
			OnClick clickMethod = method.getAnnotation(OnClick.class);
			if (clickMethod != null && clickMethod.value().length != 0) {
				for (int id : clickMethod.value()) {
					final View view = activity.findViewById(id);
					view.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							try {
								method.invoke(activity, view);
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
		}
		//BindView BindString
		try {
			for (final Field field : fields) {
				BindView bindView = field.getAnnotation(BindView.class);
				if (bindView != null) {
					View view = activity.findViewById(bindView.value());
					field.setAccessible(true);
					field.set(activity, view);

				}
				BindString bindString = field.getAnnotation(BindString.class);
				if (bindString != null) {
					String valueString = activity.getString(bindString.value());
					field.setAccessible(true);
					field.set(activity, valueString);

				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 绑定android.app.Fragment
	 */
	public static void bind(final Fragment fragment, View contentView) {
		Class annotationParent = fragment.getClass();
		Field[] fields = annotationParent.getDeclaredFields();
		Method[] methods = annotationParent.getDeclaredMethods();
		// OnClick
		for (final Method method : methods) {
			OnClick clickMethod = method.getAnnotation(OnClick.class);
			if (clickMethod != null && clickMethod.value().length != 0) {
				for (int id : clickMethod.value()) {
					final View view = contentView.findViewById(id);
					view.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							try {
								method.invoke(fragment, view);
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
		}

		//BindView BindString
		try {
			for (final Field field : fields) {
				BindView bindView = field.getAnnotation(BindView.class);
				if (bindView != null) {
					View view = contentView.findViewById(bindView.value());
					field.setAccessible(true);
					field.set(fragment, view);

				}
				BindString bindString = field.getAnnotation(BindString.class);
				if (bindString != null) {
					String valueString = contentView.getContext().getString(bindString.value());
					field.setAccessible(true);
					field.set(fragment, valueString);

				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 绑定android.support.v4.app.Fragment
	 */
	public static void bind(final android.support.v4.app.Fragment fragment, View contentView) {
		Class annotationParent = fragment.getClass();
		Field[] fields = annotationParent.getDeclaredFields();
		Method[] methods = annotationParent.getDeclaredMethods();
		// OnClick
		for (final Method method : methods) {
			OnClick clickMethod = method.getAnnotation(OnClick.class);
			if (clickMethod != null && clickMethod.value().length != 0) {
				for (int id : clickMethod.value()) {
					final View view = contentView.findViewById(id);
					view.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							try {
								method.invoke(fragment, view);
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
		}

		//BindView BindString
		try {
			for (final Field field : fields) {
				BindView bindView = field.getAnnotation(BindView.class);
				if (bindView != null) {
					View view = contentView.findViewById(bindView.value());
					field.setAccessible(true);
					field.set(fragment, view);

				}
				BindString bindString = field.getAnnotation(BindString.class);
				if (bindString != null) {
					String valueString = contentView.getContext().getString(bindString.value());
					field.setAccessible(true);
					field.set(fragment, valueString);

				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}
