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

import br.octahedron.commons.database.NamespaceCommons;
import br.octahedron.commons.inject.Inject;
import br.octahedron.straight.modules.configuration.data.DomainConfiguration;
import br.octahedron.straight.modules.configuration.data.DomainConfigurationView;
import br.octahedron.straight.modules.configuration.manager.ConfigurationManager;

/**
 * A facade for some of the {@link ConfigurationManager} methods. This facade should be used by the
 * application view.
 * 
 * @see ConfigurationManager
 * 
 * @author Erick Moreno
 */
public class ConfigurationInternalFacade {
	
	@Inject
	private ConfigurationManager configurationManager;

	public void setConfigurationManager(ConfigurationManager confManager) {
		this.configurationManager = confManager;
	}
	
	/**
	 * Returns an read-only {@link DomainConfiguration} specific for the passed namespace
	 * 
	 * @see ConfigurationManager#getDomainConfiguration()
	 */
	public DomainConfigurationView getDomainConfiguration(String namespace) {
		try {
			NamespaceCommons.changeToNamespace(namespace);
			return this.configurationManager.getDomainConfiguration();
		} finally{
			NamespaceCommons.backToPreviousNamespace();
		}
	}

}
