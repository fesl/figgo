/*
 *  Straight - A system to manage financial demands for small and decentralized
 *  organizations.
 *  Copyright (C) 2011  Octahedron 
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.octahedron.commons.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;

/**
 * This class provides reflection utilities methods.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
class ReflectionUtil {

	/**
	 * Get all {@link Field}s annotated with the given {@link Annotation}
	 */
	public static <T extends Annotation> Collection<Field> getAnnotatedFields(Class<?> klass, Class<T> annClass) {
		Collection<Field> result = new LinkedList<Field>();
		Field[] fields = klass.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(annClass)) {
				result.add(field);
			}
		}
		Class<?> superClass = klass.getSuperclass();
		if ( superClass != null ) {
			result.addAll(getAnnotatedFields(superClass, annClass));
		}
		return result;
	}

	/**
	 * Gets the "set" method for a field with the given name. Eg.: if the given field name is
	 * "name", it will look for a method called setName.
	 */
	public static Method getSetMethod(String fieldName, Class<?> klass, Class<?> paramType) throws SecurityException, NoSuchMethodException {
		String name = (fieldName.length() > 2) ? "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1) : fieldName.toUpperCase();
		return klass.getMethod(name, paramType);
	}

	/**
	 * Gets a field's value using the "get" method. Eg.: for a field named "name" it will use the
	 * method getName to recover the field's value.
	 */
	public static Object getFieldValue(Field field, Object instance) {
		// getField method name
		String lower = field.getName();
		StringBuilder buf = new StringBuilder();
		buf.append("get");
		buf.append(Character.toUpperCase(lower.charAt(0)));
		buf.append(lower.substring(1));
		// find the method
		try {
			Method met = instance.getClass().getMethod(buf.toString(), new Class[0]);
			return invoke(instance, met, new Object[0]);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Get an annotated field's value.
	 * 
	 * @see ReflectionUtil#getFieldValue(Field, Object)
	 */
	public static <T extends Annotation> Object getAnnotatedFieldValue(Object instance, Class<T> annClass) {
		Object result = null;
		Collection<Field> fields = getAnnotatedFields(instance.getClass(), annClass);
		if (fields.size() > 0) {
			Field f = fields.iterator().next();
			result = getFieldValue(f, instance);
		}
		return result;
	}

	/**
	 * Invokes the given object {@link Method}.
	 */
	protected static Object invoke(Object instance, Method method, Object... params) throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		return method.invoke(instance, params);
	}
}
