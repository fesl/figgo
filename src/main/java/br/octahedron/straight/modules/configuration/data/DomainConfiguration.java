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
package br.octahedron.straight.modules.configuration.data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Represents an domain configuration, as the domain name, and the enabled modules.
 * 
 * @author Danilo Queiroz
 */
@PersistenceCapable
public class DomainConfiguration implements Serializable {

	/*
	 * TODO missing data as description, avatar, mail-list address, site url
	 */

	private static final long serialVersionUID = -1578869999667248765L;

	@PrimaryKey
	@Persistent
	private String domainName;

	@Persistent
	private HashSet<String> modulesEnabled = new HashSet<String>();

	public DomainConfiguration(String domainName) {
		this.domainName = domainName;
	}

	/**
	 * @return the domainName
	 */
	public String getDomainName() {
		return this.domainName;
	}

	public void addModule(String moduleName) {
		this.modulesEnabled.add(moduleName);
	}

	public void removeModule(String moduleName) {
		this.modulesEnabled.remove(moduleName);
	}

	@SuppressWarnings("unchecked")
	public Set<String> getModulesEnabled() {
		return (Set<String>) this.modulesEnabled.clone();
	}

	public boolean isModuleEnabled(String moduleName) {
		return this.modulesEnabled.contains(moduleName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.domainName.hashCode() << 3;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DomainConfiguration) {
			DomainConfiguration other = (DomainConfiguration) obj;
			return this.getDomainName().equals(other.getDomainName());
		} else {
			return false;
		}
	}

}
