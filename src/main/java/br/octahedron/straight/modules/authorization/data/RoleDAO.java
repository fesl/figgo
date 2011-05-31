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
package br.octahedron.straight.modules.authorization.data;

import java.util.List;

import javax.jdo.Query;

import br.octahedron.straight.database.GenericDAO;

/**
 * @author Danilo Queiroz
 */
public class RoleDAO extends GenericDAO<Role> {

	public RoleDAO() {
		super(Role.class);
	}

	/**
	 * @return A list with all user's roles
	 */
	@SuppressWarnings("unchecked")
	public List<Role> getUserRoles(String username) {
		Query query = this.datastoreFacade.createQueryForClass(Role.class);
		query.setFilter("users == username");
		query.declareParameters("java.lang.String username");
		return (List<Role>) query.execute(username);
	}

	/**
	 * @return <code>true</code> if exists at least one role that match the given domain, user and
	 *         activity, <code>false</code> if doesn't exists such role.
	 */
	public boolean existsRoleFor(String domainName, String username, String activityName) {
		Query query = this.datastoreFacade.createQueryForClass(Role.class);
		query.setFilter("domain == domainName && users == username && activities == activity");
		query.declareParameters("java.lang.String domainName, java.lang.String username, java.lang.String activity");
		query.setResult("count(this)");
		return ((Integer) query.execute(domainName, username, activityName)) != 0;
	}
}