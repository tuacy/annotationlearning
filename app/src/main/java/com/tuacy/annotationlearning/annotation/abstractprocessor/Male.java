package com.tuacy.annotationlearning.annotation.abstractprocessor;

import com.tuacy.annotationprocess.Factory;

@Factory(id = "Male", type = People.class)
public class Male extends People{

	@Override
	public String getName() {
		return "男生";
	}

	@Override
	public int getAge() {
		return 28;
	}

	@Override
	public int getSex() {
		return 0;
	}
}
