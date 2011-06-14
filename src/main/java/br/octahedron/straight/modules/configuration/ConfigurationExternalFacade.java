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
package br.octahedron.straight.modules.configuration;

import java.util.Map;
import java.util.Map.Entry;

import br.octahedron.commons.inject.Inject;
import br.octahedron.straight.modules.Module;
import br.octahedron.straight.modules.configuration.data.DomainConfiguration;
import br.octahedron.straight.modules.configuration.data.DomainConfigurationView;
import br.octahedron.straight.modules.configuration.data.DomainSpecificModuleConfigurationView;
import br.octahedron.straight.modules.configuration.manager.ConfigurationManager;

/**
 * A facade for some of the {@link ConfigurationManager} methods. This facade should be used by the
 * application view.
 * 
 * @see ConfigurationManager
 * 
 * @author Danilo Queiroz
 * @author Erick Moreno
 */
public class ConfigurationExternalFacade {

	@Inject
	private ConfigurationManager configurationManager;

	public void setConfigurationManager(ConfigurationManager confManager) {
		this.configurationManager = confManager;
	}

	// TODO configure domain

	/**
	 * @see ConfigurationManager#createDomainConfiguration(String)
	 */
	public void createDomainConfiguration(String domainName) {
		this.configurationManager.createDomainConfiguration(domainName);
	}

	/**
	 * Returns an read-only {@link DomainConfiguration}
	 * 
	 * @see ConfigurationManager#getDomainConfiguration()
	 */
	public DomainConfigurationView getDomainConfiguration() {
		return this.configurationManager.getDomainConfiguration();
	}

	/**
	 * @return the {@link ModulesInfoService} for this domain.
	 */
	public ModulesInfoService getModulesInfoService() {
		return new ModulesInfoService(this.getDomainConfiguration());
	}

	/**
	 * Changes a {@link Module} state for this domain. It means, it can enable or disable a Module.
	 * 
	 * @param module The module to be enabled or disabled.
	 * @param enabled <code>true</code> to enable the module, <code>false</code> to disable
	 * 
	 * @see ConfigurationManager#enableModule(Module)
	 * @see ConfigurationManager#disableModule(Module)
	 */
	public void changeModuleState(Module module, boolean enabled) {
		if (enabled) {
			this.configurationManager.enableModule(module);
		} else {
			this.configurationManager.disableModule(module);
		}
	}

	/**
	 * @see ConfigurationManager#getModuleConfiguration(Module)
	 */
	public DomainSpecificModuleConfigurationView getModuleConfiguration(Module module) {
		return this.configurationManager.getModuleConfiguration(module);
	}

	/**
	 * @see ConfigurationManager#restoreModuleProperties(Module)
	 */
	public void restoreModuleConfiguration(Module module) {
		this.configurationManager.restoreModuleProperties(module);
	}

	/**
	 * Updates the configurations parameters for a given {@link Module}.
	 * 
	 * @see ConfigurationManager#setModuleProperty(Module, String, String)
	 */
	public void updateModuleConfiguration(Module module, Map<String, String> moduleProperties) {
		for (Entry<String, String> entry : moduleProperties.entrySet()) {
			this.configurationManager.setModuleProperty(module, entry.getKey(), entry.getValue());
		}
	}
}
