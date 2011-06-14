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
package br.octahedron.straight.modules.configuration.manager;

import java.util.logging.Logger;

import br.octahedron.commons.eventbus.AbstractNamespaceSubscriber;
import br.octahedron.commons.eventbus.Event;
import br.octahedron.commons.eventbus.InterestedEvent;
import br.octahedron.commons.eventbus.NamespaceEvent;
import br.octahedron.commons.inject.Inject;
import br.octahedron.straight.modules.Module;
import br.octahedron.straight.modules.ModuleSpec;
import br.octahedron.straight.modules.admin.manager.DomainCreatedEvent;

/**
 * Creates the configuration domain for a new domain
 * 
 * @author Danilo Queiroz
 */
@InterestedEvent(events = DomainCreatedEvent.class)
public class CreateDomainConfigurationSubscriber extends AbstractNamespaceSubscriber {

	private static final Logger logger = Logger.getLogger(CreateDomainConfigurationSubscriber.class.getName());

	@Inject
	private ConfigurationManager configurationManager;

	/**
	 * @param configurationManager
	 *            the configurationManager to set
	 */
	public void setConfigurationManager(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.octahedron.commons.eventbus.AbstractNamespaceSubscriber#processEvent(br.octahedron.commons
	 * .eventbus.Event)
	 */
	@Override
	protected void processEvent(Event event) {
		String namespace = ((NamespaceEvent) event).getNamespace();
		logger.info("Creating domain configuration for domain " + namespace);
		this.configurationManager.createDomainConfiguration(namespace);
		for (Module m : Module.values()) {
			ModuleSpec moduleSpec = m.getModuleSpec();
			if (moduleSpec.isDomainSpecificModule()) {
				logger.fine("Configuring module " + m.name() + " for domain " + namespace);
				this.configurationManager.enableModule(m);
			}
		}
	}
}
