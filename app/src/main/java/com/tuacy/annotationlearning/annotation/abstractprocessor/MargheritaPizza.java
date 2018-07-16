package com.tuacy.annotationlearning.annotation.abstractprocessor;

import com.tuacy.annotationprocess.Factory;

@Factory(id = "MargheritaPizza", type = Meal.class)
public class MargheritaPizza implements Meal {

	@Override
	public float getPrice() {
		return 0;
	}
}
