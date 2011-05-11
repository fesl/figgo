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
package br.octahedron.straight.configuration.data;

import java.io.Serializable;

/**
 * It represents a module's configuration property.
 * 
 * @author Danilo Queiroz
 */
public class ModuleProperty implements Serializable {

	private static final long serialVersionUID = 7010512484660278160L;
	private String key;
	private String defaultValue = "";
	private String regex = "";
	private String description = "";

	/**
	 * @param key
	 *            The property key
	 */
	public ModuleProperty(String key) {
		this.key = key;
	}

	/**
	 * @param key
	 *            The property key
	 * @param defaultValue
	 *            The property default value
	 */
	public ModuleProperty(String key, String defaultValue) {
		this(key);
		this.defaultValue = defaultValue;
	}

	/**
	 * @param key
	 *            The property key
	 * @param defaultValue
	 *            The property default value
	 * @param regex
	 *            The property regex (used to validation)
	 * @param description
	 *            The property description
	 */
	public ModuleProperty(String key, String defaultValue, String regex, String description) {
		this(key, defaultValue);
		this.regex = regex;
		this.description = description;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return this.key;
	}

	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return this.defaultValue;
	}

	/**
	 * @return the regex
	 */
	public String getRegex() {
		return this.regex;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Sets the property regex (used to validation)
	 * 
	 * @param regex
	 *            the regex to set
	 */
	public void setRegex(String regex) {
		this.regex = regex;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
