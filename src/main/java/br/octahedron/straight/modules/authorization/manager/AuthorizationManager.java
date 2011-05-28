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
package br.octahedron.straight.modules.authorization.manager;

import static br.octahedron.straight.modules.authorization.data.Role.createRoleKey;
import br.octahedron.straight.modules.DataAlreadyExistsException;
import br.octahedron.straight.modules.DataDoesNotExistsException;
import br.octahedron.straight.modules.authorization.data.Role;
import br.octahedron.straight.modules.authorization.data.RoleDAO;
import br.octahedron.straight.modules.authorization.data.RoleView;

/**
 * This entity is responsible to manage authorization issues, such as roles operations
 * (create/delete role, add/remove user, add/remove activity) and check user authorizations.
 * 
 * It's a global module, working in the default namespace.
 * 
 * @author Danilo Queiroz
 */
public class AuthorizationManager {

	/*
	 * open questions
	 * 
	 * will it add default roles for domains (admin, users) how will it get the activities for each
	 * module?
	 */

	private RoleDAO roleDAO = new RoleDAO();

	/**
	 * @param roleDAO
	 *            the roleDAO to set
	 */
	public void setRoleDAO(RoleDAO roleDAO) {
		this.roleDAO = roleDAO;
	}

	/**
	 * @return <code>true</code> if exists the given role at the given domain, <code>false</code>
	 *         otherwise.
	 */
	public boolean existsRole(String domainName, String roleName) {
		return this.roleDAO.exists(createRoleKey(domainName, roleName));
	}

	/**
	 * Creates a new role, for a given domain.
	 * 
	 * @throws DataAlreadyExistsException
	 *             if the role already exists
	 */
	public Role createRole(String domainName, String roleName) {
		if (!this.existsRole(domainName, roleName)) {
			Role role = new Role(domainName, roleName);
			this.roleDAO.save(role);
			return role;
		} else {
			throw new DataAlreadyExistsException("Already exists role " + roleName + " at domain " + domainName);
		}
	}

	/**
	 * Removes a role, for a given domain.
	 * 
	 * @throws DataDoesNotExistsException
	 *             if theres no role with the given name at the given domain.
	 */
	public void removeRole(String domainName, String roleName) {
		if (this.existsRole(domainName, roleName)) {
			this.roleDAO.delete(createRoleKey(domainName, roleName));
		} else {
			throw new DataDoesNotExistsException("There's no role " + roleName + " at domain " + domainName);
		}
	}

	/**
	 * @return gets the given role
	 * 
	 * @throws DataDoesNotExistsException
	 *             if theres no such role
	 */
	public RoleView getRole(String domainName, String roleName) {
		if (this.existsRole(domainName, roleName)) {
			return this.roleDAO.get(createRoleKey(domainName, roleName));
		} else {
			throw new DataDoesNotExistsException("There's no role " + roleName + " at domain " + domainName);
		}
	}

	/**
	 * 
	 */
	public void addUsersToRole(String domainName, String roleName, String... users) {
		((Role) this.getRole(domainName, roleName)).addUsers(users);
	}

	/**
	 */
	public void addActivitiesToRole(String domainName, String roleName, String... activities) {
		((Role) this.getRole(domainName, roleName)).addActivities(activities);
	}

	/*
	 * get domains(user)
	 * 
	 * authorize(domain, user, activity)
	 */

}
