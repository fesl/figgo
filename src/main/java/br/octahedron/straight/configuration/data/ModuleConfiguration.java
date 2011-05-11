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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import br.octahedron.straight.configuration.ModuleConfigurationInfoService;

/**
 * Represents the configurations for a given module. It contains the module facade class, and all
 * properties need by a module.
 * 
 * @author Danilo Penna Queiroz
 */
@PersistenceCapable
public class ModuleConfiguration implements Serializable, ModuleConfigurationInfoService {

	private static final long serialVersionUID = -2814532770225964507L;

	@PrimaryKey
	@Persistent
	private String moduleName;

	@Persistent
	private Class<?> moduleFacade;

	@Persistent(serialized = "true", defaultFetchGroup = "true")
	private Map<String, ModuleProperty> properties = new HashMap<String, ModuleProperty>();

	@Persistent(serialized = "true", defaultFetchGroup = "true")
	private Map<String, String> values = new HashMap<String, String>();

	public ModuleConfiguration(String moduleName, Class<?> moduleFacade) {
		this.moduleName = moduleName;
		this.moduleFacade = moduleFacade;
	}

	/**
	 * Adds a property to this ModuleConfiguration This method should be called by the
	 * ModuleConfigurationBuilder
	 * 
	 * @param property
	 *            The property to be set
	 */
	public void addProperty(ModuleProperty property) {
		this.properties.put(property.getKey(), property);
	}

	/**
	 * @return the moduleName
	 */
	public String getModuleName() {
		return this.moduleName;
	}

	/* (non-Javadoc)
	 * @see br.octahedron.straight.configuration.data.ModuleConfigurationInfoService#getModuleFacade()
	 */
	public Class<?> getModuleFacade() {
		return this.moduleFacade;
	}

	/* (non-Javadoc)
	 * @see br.octahedron.straight.configuration.data.ModuleConfigurationInfoService#getPropertiesKeys()
	 */
	public Set<String> getPropertiesKeys() {
		return this.properties.keySet();
	}

	/* (non-Javadoc)
	 * @see br.octahedron.straight.configuration.data.ModuleConfigurationInfoService#getPropertyValue(java.lang.String)
	 */
	public String getPropertyValue(String propertyKey) {
		if (this.values.containsKey(propertyKey)) {
			return this.values.get(propertyKey);
		} else {
			return this.properties.get(propertyKey).getDefaultValue();
		}
	}

	/* (non-Javadoc)
	 * @see br.octahedron.straight.configuration.data.ModuleConfigurationInfoService#getPropertyDefaultValue(java.lang.String)
	 */
	public String getPropertyDefaultValue(String propertyKey) {
		return this.properties.get(propertyKey).getDefaultValue();
	}

	/* (non-Javadoc)
	 * @see br.octahedron.straight.configuration.data.ModuleConfigurationInfoService#getPropertyRegex(java.lang.String)
	 */
	public String getPropertyRegex(String propertyKey) {
		return this.properties.get(propertyKey).getRegex();
	}

	/**
	 * Sets the value for a given property.
	 * 
	 * <i>It assumes that the given is valid!</i>
	 */
	public void setConfigurationValue(String propertyKey, String propertyValue) {
		this.values.put(propertyKey, propertyValue);
	}

	/**
	 * Restore all properties to the default value.
	 */
	public void restoreDefaults() {
		this.values.clear();
	}

	/**
	 * It represents a module's configuration property.
	 */
	public static class ModuleProperty implements Serializable {

		private static final long serialVersionUID = 7010512484660278160L;
		private String key;
		private String defaultValue = "";
		private String regex = ".*";
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
}
