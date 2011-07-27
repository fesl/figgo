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
package br.octahedron.figgo.modules.authorization.data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * @author Danilo Penna Queiroz
 */
@PersistenceCapable
public class Role implements Serializable, RoleView {

	private static final String SEPARATOR = "#";

	public static String createRoleKey(String domain, String name) {
		return domain + SEPARATOR + name;
	}

	private static final long serialVersionUID = -7580021620330781535L;

	@PrimaryKey
	@Persistent
	private String id;

	@Persistent
	private String domain;

	@Persistent
	private String name;

	@Persistent
	private Set<String> activities = new TreeSet<String>();

	@Persistent
	private Set<String> users = new TreeSet<String>();

	public Role(String domain, String name) {
		this.domain = domain;
		this.name = name;
		this.fixId();
	}

	/**
	 * Fix the Role ID, it means, check if the id is set and if necessary, sets it.
	 */
	private void fixId() {
		if (this.id == null || this.id.isEmpty()) {
			this.id = createRoleKey(this.domain, this.name);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.modules.authorization.data.RoleView#getId()
	 */
	public String getId() {
		this.fixId();
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.modules.authorization.data.RoleView#getDomain()
	 */
	public String getDomain() {
		return this.domain;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.modules.authorization.data.RoleView#getName()
	 */
	public String getName() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.modules.authorization.data.RoleView#getActivities()
	 */
	public Set<String> getActivities() {
		return this.activities;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.modules.authorization.data.RoleView#getUsers()
	 */
	public Set<String> getUsers() {
		return this.users;
	}

	public void addUsers(Collection<String> users) {
		this.users.addAll(users);
	}

	public void addUsers(String... users) {
		this.addUsers(Arrays.asList(users));
	}

	public void addActivities(Collection<String> activities) {
		this.activities.addAll(activities);
	}

	public void addActivities(String... activities) {
		this.addActivities(Arrays.asList(activities));
	}
	
	public void removeUser(String user) {
		this.users.remove(user);
	}
	
	public boolean containsActivity(String activity) {
		return this.activities.contains(activity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.domain.hashCode() & this.name.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Role) {
			Role other = (Role) obj;
			return this.domain.equals(other.getDomain()) && this.name.equals(other.getName());
		} else {
			return false;
		}
	}

	/**
	 * @param activities
	 */
	public void updateActivities(Collection<String> activities) {
		// FIXME maybe another option?
		this.activities.addAll(activities);
		this.activities.retainAll(activities);
	}
}
