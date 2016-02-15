package com.d2c;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class MainApplication extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		System.out.println("MainApplication/getClasses");
		Set<Class<?>> s = new HashSet<>();
		s.add(MainPageResource.class);
		return s;
	}
}
