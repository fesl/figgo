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
package br.octahedron.straight.modules.configuration;

import java.util.Set;

/**
 * @author danilo
 * 
 */
public interface ModuleConfigurationInfoService {

	/**
	 * @return the moduleName
	 */
	public abstract String getModuleName();

	/**
	 * @return <code>true</code> if the module has an property with the given propertyKey,
	 *         <code>false</code> otherwise.
	 */
	public abstract boolean existsProperty(String propertyKey);

	/**
	 * @return the moduleFacade
	 */
	public abstract Class<?> getModuleFacade();

	/**
	 * @return a set with all configuration keys.
	 */
	public abstract Set<String> getPropertiesKeys();

	/**
	 * @return The property's value or the default property's value.
	 */
	public abstract String getPropertyValue(String propertyKey);

	/**
	 * @return the default value for a given property
	 */
	public abstract String getPropertyDefaultValue(String propertyKey);

	/**
	 * @return the property's regex. This regex defines the acceptable formats for values. It should
	 *         be used to validade the values.
	 */
	public abstract String getPropertyRegex(String propertyKey);

}