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
package br.octahedron.straight.inject;

import static br.octahedron.straight.inject.DependencyManager.containsImplementation;
import static br.octahedron.straight.inject.DependencyManager.getImplementation;
import static br.octahedron.straight.inject.DependencyManager.registerImplementation;
import static br.octahedron.straight.inject.DependencyManager.resolveDependency;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This entity handles classes' instances. It provide access to unique instances of the classes, and
 * it's responsible to perform dependency injection.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class InstanceHandler {

	private static final Logger logger = Logger.getLogger(InstanceHandler.class.getName());

	/**
	 * Gets a <T> instance for the given {@link Class} ready to be used.
	 * 
	 * @param klass
	 *            the T's class
	 * @return The T instance.
	 * @throws InstantiationException
	 */
	public <T> T getInstance(Class<T> klass) throws InstantiationException {
		try {
			if (!containsImplementation(klass)) {
				T instance = createInstance(klass);
				registerImplementation(klass, instance);
			}
			return getImplementation(klass);
		} catch (Exception e) {
			throw new InstantiationException("Unable to load class " + klass);
		}
	}

	/**
	 * Creates a new instance of the given class.
	 */
	public <T> T createInstance(Class<T> klass) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class<? extends T> implClass = resolveDependency(klass);
		T instance = implClass.newInstance();
		inject(instance);
		return instance;
	}

	/**
	 * Checks if should inject any attribute and inject if necessary.
	 */
	protected void inject(Object instance) {
		Collection<Field> fields = ReflectionUtil.getAnnotatedFields(instance.getClass(), Inject.class);
		// for each annotated field
		for (Field f : fields) {
			try {
				Inject inject = f.getAnnotation(Inject.class);
				// create the object to inject
				Class<?> klass = f.getType();
				Object obj;
				if (inject.newInstance()) {
					obj = getInstance(klass);
				} else {
					obj = createInstance(klass);
				}
				// gets the instance's method to inject the object created above
				logger.info("Injecting object " + obj.getClass().getSimpleName() + " into object " + instance.getClass().getSimpleName());
				Method set = ReflectionUtil.getSetMethod(klass.getSimpleName(), instance.getClass(), klass);
				set.invoke(instance, obj);
			} catch (Exception ex) {
				logger.log(Level.SEVERE, "Unable to performe injection: " + ex.getLocalizedMessage(), ex);
			}
		}
	}
}
