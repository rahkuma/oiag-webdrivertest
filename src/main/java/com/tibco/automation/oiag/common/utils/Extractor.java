package com.tibco.automation.oiag.common.utils;

import java.lang.reflect.*;


public class Extractor {
	public void extract(Class<?> clazz) {
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			System.out.println("Method: " + method.getName());
		}
		
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			System.out.println("Field: " + field.getName());
		}
	}
	
}
