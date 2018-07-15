package com.tuacy.annotationprocess;

import java.util.LinkedHashMap;
import java.util.Map;

public class FactoryGroupedClasses {
	private String qualifiedClassName;

	private Map<String, FactoryAnnotatedClass> itemsMap =
		new LinkedHashMap<String, FactoryAnnotatedClass>();

	public FactoryGroupedClasses(String qualifiedClassName) {
		this.qualifiedClassName = qualifiedClassName;
	}

//	public void add(FactoryAnnotatedClass toInsert) throws IdAlreadyUsedException {
//
//		FactoryAnnotatedClass existing = itemsMap.get(toInsert.getId());
//		if (existing != null) {
//			throw new IdAlreadyUsedException(existing);
//		}
//
//		itemsMap.put(toInsert.getId(), toInsert);
//	}
//
//	public void generateCode(Elements elementUtils, Filer filer) throws IOException {
//	}

}
