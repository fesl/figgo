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
 * A role specify a set of activities that can be performed by a set of users for a given domain.
 * 
 * @author Danilo Penna Queiroz
 */
@PersistenceCapable
public class Role implements Serializable {

	private static final long serialVersionUID = -7580021620330781535L;

	@PrimaryKey
	@Persistent
	private String name;

	@Persistent
	private Set<String> activities = new TreeSet<String>();

	@Persistent
	private Set<String> users = new TreeSet<String>();

	public Role(String name) {
		this.name = name;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the activities
	 */
	public Set<String> getActivities() {
		return this.activities;
	}

	/**
	 * @return the users
	 */
	public Set<String> getUsers() {
		return this.users;
	}

	/**
	 * @param users
	 *            Adds a bunch of users to this role.
	 */
	public void addUsers(Collection<String> users) {
		this.users.addAll(users);
	}

	/**
	 * @param users
	 *            Adds a bunch of users to this role.
	 */
	public void addUsers(String... users) {
		this.addUsers(Arrays.asList(users));
	}

	public void removeUser(String user) {
		this.users.remove(user);
	}
	
	public boolean containsUser(String user) {
		return this.users.contains(user);
	}

	/**
	 * @param activities
	 *            Adds a bunch of activities to this role.
	 */
	public void addActivities(Collection<String> activities) {
		this.activities.addAll(activities);
	}

	/**
	 * @param activities
	 *            Adds a bunch of activities to this role.
	 */
	public void addActivities(String... activities) {
		this.addActivities(Arrays.asList(activities));
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
		return this.name.hashCode();
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
			return this.name.equals(other.getName());
		} else {
			return false;
		}
	}

	/**
	 * @param activities
	 */
	public void updateActivities(Collection<String> activities) {
		// FIXME maybe another option?
		this.activities.clear();
		this.activities.addAll(activities);
	}

	/**
	 * @param activities
	 *            Adds a bunch of activities to this role.
	 */
	public void removeActivities(String[] activities) {
		this.removeActivities(Arrays.asList(activities));
	}
	
	/**
	 * @param activities
	 *            Adds a bunch of activities to this role.
	 */
	public void removeActivities(Collection<String> activities) {
		this.activities.removeAll(activities);
	}

}