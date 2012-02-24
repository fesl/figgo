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
package br.octahedron.figgo.modules.domain.data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Represents the configurations for a given module. It contains the module facade class, and all
 * properties need by a module.
 * 
 * @author Danilo Penna Queiroz
 */
@PersistenceCapable
public class ModuleConfiguration implements Serializable {

	private static final long serialVersionUID = -2814532770225964507L;

	@PrimaryKey
	@Persistent
	private String moduleName;

	@Persistent(serialized = "true", defaultFetchGroup = "true")
	private Map<String, ModuleProperty> properties = new HashMap<String, ModuleProperty>();

	@Persistent(serialized = "true", defaultFetchGroup = "true")
	private Map<String, String> values = new HashMap<String, String>();
	
	@Persistent
	private Date timestamp;

	public ModuleConfiguration(String moduleName) {
		this.moduleName = moduleName;
		this.timestamp = new Date();
	}
	
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return this.timestamp;
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

	/**
	 * @return <code>true</code> if the module has an property with the given propertyKey,
	 *         <code>false</code> otherwise.
	 */
	public boolean existsProperty(String propertyKey) {
		return this.properties.containsKey(propertyKey);
	}

	/**
	 * @return a set with all configuration keys.
	 */
	public Set<String> getPropertiesKeys() {
		return this.properties.keySet();
	}

	/**
	 * @return The property's value or the default property's value.
	 */
	public String getPropertyValue(String propertyKey) {
		if (this.values.containsKey(propertyKey)) {
			return this.values.get(propertyKey);
		} else {
			return this.properties.get(propertyKey).getDefaultValue();
		}
	}

	/**
	 * @return the default value for a given property
	 */
	public String getPropertyDefaultValue(String propertyKey) {
		return this.properties.get(propertyKey).getDefaultValue();
	}

	/**
	 * @return the property's regex. This regex defines the acceptable formats for values. It should
	 *         be used to validade the values.
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.moduleName.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ModuleConfiguration) {
			ModuleConfiguration other = (ModuleConfiguration) obj;
			return this.moduleName.equals(other.getModuleName());
		} else {
			return false;
		}
	}
}
