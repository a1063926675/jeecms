package com.jeecms.common.jpa;

/*
 * Copyright 2011-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Optional;

import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import com.querydsl.core.types.EntityPath;

/**
 * Simple implementation of {@link EntityPathResolver} to lookup a query class
 * by reflection and using the static field of the same type.
 * 
 * @author Oliver Gierke
 */
public enum SimpleEntityPathResolver implements EntityPathResolver {

	/**
	 * 实例对象
	 */
	INSTANCE;

	private static final String NO_CLASS_FOUND_TEMPLATE = "Did not find a query class %s for domain class %s!";
	private static final String NO_FIELD_FOUND_TEMPLATE = "Did not find a static field of the same type in %s!";

	/**
	 * Creates an {@link EntityPath} instance for the given domain class. Tries
	 * to lookup a class matching the naming convention (prepend Q to the simple
	 * name of the class, same package) and find a static field of the same type
	 * in it.
	 * 
	 * @param domainClass
	 *            domainClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> EntityPath<T> createPath(Class<T> domainClass) {

		String pathClassName = getQueryClassName(domainClass);

		try {

			Class<?> pathClass = ClassUtils.forName(pathClassName, domainClass.getClassLoader());

			return getStaticFieldOfType(pathClass).map(it -> (EntityPath<T>) 
					ReflectionUtils.getField(it, null))
					.orElseThrow(() -> 
					new IllegalStateException(String.format(NO_FIELD_FOUND_TEMPLATE, pathClass)));

		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(
					String.format(NO_CLASS_FOUND_TEMPLATE,
							pathClassName, domainClass.getName()), e);
		}
	}

	/**
	 * Returns the first static field of the given type inside the given type.
	 * @param type Class
	 * @return
	 */
	private Optional<Field> getStaticFieldOfType(Class<?> type) {

		for (Field field : type.getDeclaredFields()) {

			boolean isStatic = Modifier.isStatic(field.getModifiers());
			boolean hasSameType = type.equals(field.getType());

			if (isStatic && hasSameType) {
				return Optional.of(field);
			}
		}

		Class<?> superclass = type.getSuperclass();
		return Object.class.equals(superclass) ? Optional.empty() : getStaticFieldOfType(superclass);
	}

	/**
	 * Returns the name of the query class for the given domain class.
	 * 
	 * @param domainClass domainClass
	 * @return
	 */
	private String getQueryClassName(Class<?> domainClass) {

		String simpleClassName = ClassUtils.getShortName(domainClass);
		return String.format("%s.querydsl.Q%s%s", 
				domainClass.getPackage().getName(), getClassBase(simpleClassName),
				domainClass.getSimpleName());
	}

	/**
	 * Analyzes the short class name and potentially returns the outer class.
	 * @param shortName domain类名
	 * @return
	 */
	private String getClassBase(String shortName) {

		String[] parts = shortName.split("\\.");
		int shortNameLen = 2;

		if (parts.length < shortNameLen) {
			return "";
		}

		return parts[0] + "_";
	}
}
