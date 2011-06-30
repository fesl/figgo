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
package br.octahedron.figgo.modules.services.data;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * @author Erick Moreno
 */
public class Service implements Serializable, ServiceView {

	private static final long serialVersionUID = -1270664240981744528L;

	@PrimaryKey
	@Persistent
	private String name;
	@Persistent
	private String value;
	@Persistent
	private String description;
	@Persistent
	private Set<String> providers;

	public Service(String name, String value, String description) {
		this.name = name;
		this.value = value;
		this.description = description;
		this.providers = new TreeSet<String>();
	}

	public void addProvider(String userId) {
		this.providers.add(userId);
	}

	public void removeProvider(String userId) {
		this.providers.remove(userId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.modules.services.data.ServiceView#getProviders()
	 */
	@Override
	public Set<String> getProviders() {
		return this.providers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.modules.services.data.ServiceView#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.modules.services.data.ServiceView#getValue()
	 */
	@Override
	public String getValue() {
		return this.value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.modules.services.data.ServiceView#getDescription()
	 */
	@Override
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
