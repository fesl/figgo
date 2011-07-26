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
package br.octahedron.figgo.modules.configuration;

import java.util.LinkedHashSet;
import java.util.Set;

import br.octahedron.figgo.modules.Module;
import br.octahedron.figgo.modules.Module.Type;
import br.octahedron.figgo.modules.configuration.data.DomainConfiguration;

/**
 * Retrieves the information about the modules existent and enabled for a domain.
 * 
 * @author Danilo Queiroz
 * @author Erick Moreno
 */
public class ModulesInfoService {

	private static final LinkedHashSet<String> modules = new LinkedHashSet<String>();

	static {
		for (Module module : Module.values()) {
			if (module.getModuleSpec().getModuleType() == Type.DOMAIN) {
				modules.add(module.name());
			}
		}
	}

	private DomainConfiguration domainConfiguration;

	public ModulesInfoService(DomainConfiguration domainConfiguration) {
		this.domainConfiguration = domainConfiguration;
	}

	/**
	 * @return A {@link Set} of all existent modules.
	 */
	public Set<String> getExistentModules() {
		return modules;
	}

	/**
	 * @see DomainConfiguration#isModuleEnabled(String)
	 */
	public boolean isModuleEnabled(String moduleName) {
		return this.domainConfiguration.isModuleEnabled(moduleName);
	}
}
