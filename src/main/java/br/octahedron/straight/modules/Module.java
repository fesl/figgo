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

import br.octahedron.straight.modules.authorization.AuthorizationSpec;
import br.octahedron.straight.modules.bank.BankSpec;

/**
 * All the Modules existent at the project
 * 
 * @author Danilo Queiroz
 */
public enum Module {

	BANK(new BankSpec()), AUTHORIZATION(new AuthorizationSpec());

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
