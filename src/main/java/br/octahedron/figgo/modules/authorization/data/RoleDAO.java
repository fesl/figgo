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

import java.util.List;

import javax.jdo.Query;

import br.octahedron.cotopaxi.datastore.jdo.GenericDAO;

/**
 * @author Danilo Queiroz
 * @author Vítor Avelino
 */
public class RoleDAO extends GenericDAO<Role> {

	public RoleDAO() {
		super(Role.class);
	}

	/**
	 * Returns all roles of a user in a specific domain the whole system.
	 * 
	 * @param domain
	 *            domain that the roles belongs to
	 * @param username
	 *            username username of the user
	 * 
	 * @return a list of {@link Role} that belongs to user
	 */
	@SuppressWarnings("unchecked")
	public List<Role> getUserRoles(String domain, String username) {
		Query query = this.datastoreFacade.createQueryForClass(Role.class);
		query.setFilter("domain == :domain && users == :username");
		return (List<Role>) query.execute(domain, username);
	}

	/**
	 * Returns all roles of a user in the whole system.
	 * 
	 * @param username
	 *            username of the user
	 * 
	 * @return a list of {@link Role} that belongs to user
	 */
	@SuppressWarnings("unchecked")
	public List<Role> getUserRoles(String username) {
		Query query = this.datastoreFacade.createQueryForClass(Role.class);
		query.setFilter("users == :username");
		return (List<Role>) query.execute(username);
	}

	/**
	 * Returns all existent roles of a specific domain.
	 * 
	 * @param domain
	 *            domain that the roles belong to
	 * 
	 * @return a list of {@link Role} that belongs to domain
	 */
	@SuppressWarnings("unchecked")
	public List<Role> getAll(String domain) {
		Query query = this.datastoreFacade.createQueryForClass(Role.class);
		query.setFilter("domain == :domain");
		return (List<Role>) query.execute(domain);
	}

	/**
	 * Checks if exists at least a role that matches the given domain, user and activity.
	 *  
	 * @return <code>true</code> if exists at least one role that match the given domain, user and
	 *         activity, <code>false</code> if doesn't exists such role.
	 */
	public boolean existsRoleFor(String domain, String username, String activity) {
		Query query = this.datastoreFacade.createQueryForClass(Role.class);
		query.setFilter("domain == :domainName && users == :username && activities == :activity");
		query.setResult("count(this)");
		return ((Integer) query.execute(domain, username, activity)) != 0;
	}
}
