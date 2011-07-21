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
package br.octahedron.figgo.modules;

import br.octahedron.figgo.modules.admin.AdminSpec;
import br.octahedron.figgo.modules.authorization.AuthorizationSpec;
import br.octahedron.figgo.modules.bank.BankSpec;
import br.octahedron.figgo.modules.configuration.ConfigurationSpec;
import br.octahedron.figgo.modules.services.ServicesSpec;
import br.octahedron.figgo.modules.users.UsersSpec;

/**
 * All the Modules existent at the project
 * 
 * @author Danilo Queiroz
 */
public enum Module {

	BANK(new BankSpec()), SERVICES(new ServicesSpec()), AUTHORIZATION(new AuthorizationSpec()), USER(new UsersSpec()), ADMIN(new AdminSpec()), DOMAIN(new ConfigurationSpec());

	/**
	 * Indicates the module Type
	 */
	public enum Type {
		/**
		 * Indicates that the module is an Application Module and has a global scope.
		 * 
		 * An application module is an internal module to the application and provides
		 * features needed by the system. This kind of module can't be enabled/disabled.
		 */
		APPLICATION_GLOBAL,
		/**
		 * Indicates that the module is an Application Module and has domain scope.
		 * 
		 * An application module is an internal module to the application and provides
		 * features needed by the system. This kind of module can't be enabled/disabled.
		 */
		APPLICATION_DOMAIN,
		/**
		 * Indicates that the module is a domain module, and provides functionalities specific for a
		 * domain needs. This kind of module can be enable/disabled per domain.
		 */
		DOMAIN;
	}

	private ModuleSpec moduleSpec;

	private Module(ModuleSpec moduleSpecification) {
		this.moduleSpec = moduleSpecification;
	}

	public ModuleSpec getModuleSpec() {
		return this.moduleSpec;
	}

	public static ModuleSpec getModuleSpec(Module module) {
		return module.getModuleSpec();
	}

	public static ModuleSpec getModuleSpec(String moduleName) {
		return Module.valueOf(moduleName).getModuleSpec();
	}
}