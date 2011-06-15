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
package br.octahedron.straight.modules.authorization;

import java.util.Collection;

import br.octahedron.straight.modules.DataAlreadyExistsException;
import br.octahedron.straight.modules.DataDoesNotExistsException;
import br.octahedron.straight.modules.authorization.data.Role;

/**
 * @author danilo
 *
 */
public interface AuthorizationIF {

	/**
	 * @return <code>true</code> if exists the given role at the given domain, <code>false</code>
	 *         otherwise.
	 */
	public abstract boolean existsRole(String domainName, String roleName);

	/**
	 * Creates a new role, for a given domain.
	 * 
	 * @throws DataAlreadyExistsException
	 *             if the role already exists
	 */
	public abstract Role createRole(String domainName, String roleName);

	/**
	 * Removes a role, for a given domain.
	 * 
	 * @throws DataDoesNotExistsException
	 *             if theres no role with the given name at the given domain.
	 */
	public abstract void removeRole(String domainName, String roleName);

	/**
	 * @return gets the given role
	 * 
	 * @throws DataDoesNotExistsException
	 *             if theres no such role
	 */
	public abstract Role getRole(String domainName, String roleName);

	/**
	 * Adds the given users to a specific role.
	 */
	public abstract void addUsersToRole(String domainName, String roleName, String... users);

	/**
	 * Adds the given activities to a specific role.
	 */
	public abstract void addActivitiesToRole(String domainName, String roleName, String... activities);

	/**
	 * @return All domains that the user has some role.
	 */
	public abstract Collection<String> getUserDomains(String username);

	/**
	 * @return <code>true</code> if the given user is authorized to perform the given activity at
	 *         the given domain, <code>false</code> otherwise.
	 */
	public abstract boolean isAuthorized(String domainName, String username, String activityName);

}