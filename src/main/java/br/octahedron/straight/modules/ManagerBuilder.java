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
package br.octahedron.straight.modules;

import br.octahedron.commons.inject.DependencyManager;
import br.octahedron.commons.inject.InstanceHandler;
import br.octahedron.straight.modules.authorization.AuthorizationDecorator;
import br.octahedron.straight.modules.authorization.AuthorizationIF;
import br.octahedron.straight.modules.users.UsersDecorator;
import br.octahedron.straight.modules.users.UsersIF;

/**
 * This Builder knows how to create managers for each module.
 * 
 * @author Erick Moreno
 * 
 */
public class ManagerBuilder {
	
	static {
		/* Register Dependencies */
		DependencyManager.registerDependency(UsersIF.class, UsersDecorator.class);
		DependencyManager.registerDependency(AuthorizationIF.class, AuthorizationDecorator.class);
	}

	private static InstanceHandler instanceHandler = new InstanceHandler();

	public static UsersIF getUserManager() throws InstantiationException {
		return (UsersIF) instanceHandler.getInstance(UsersIF.class);
	}
	
	public static AuthorizationIF getAuthorizationManager() throws InstantiationException {
		return instanceHandler.getInstance(AuthorizationIF.class);
	}
}
