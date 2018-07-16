package com.tuacy.annotationlearning.annotation.abstractprocessor;

import com.tuacy.annotationprocess.Factory;

@Factory(
	id = "CalzonePizza",
	type = Meal.class
)
public class CalzonePizza implements Meal {

	@Override
	public float getPrice() {
		return 0;
	}
}
