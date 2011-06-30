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

import br.octahedron.cotopaxi.inject.InstanceHandler;
import br.octahedron.straight.modules.admin.manager.AdminManager;
import br.octahedron.straight.modules.authorization.manager.AuthorizationManager;
import br.octahedron.straight.modules.bank.manager.AccountManager;
import br.octahedron.straight.modules.configuration.manager.ConfigurationManager;
import br.octahedron.straight.modules.users.manager.UsersManager;

/**
 * This Builder knows how to create managers for each module.
 * 
 * @author Erick Moreno
 * 
 */
public class ManagerBuilder {

	private static InstanceHandler instanceHandler = new InstanceHandler();

	private static <T> T getInstance(Class<T> klass) {
		try {
			return instanceHandler.getInstance(klass);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		}
	}

	public static UsersManager getUserManager() {
		return getInstance(UsersManager.class);
	}

	public static AuthorizationManager getAuthorizationManager() {
		return getInstance(AuthorizationManager.class);
	}

	public static AdminManager getAdminManager() {
		return getInstance(AdminManager.class);
	}

	public static AccountManager getAccountManager() {
		return getInstance(AccountManager.class);
	}

	public static ConfigurationManager getConfigurationManager() {
		return getInstance(ConfigurationManager.class);
	}

}
