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
package br.octahedron.figgo.modules.domain.manager;

import br.octahedron.cotopaxi.datastore.jdo.PersistenceManagerPool;
import br.octahedron.cotopaxi.eventbus.AbstractNamespaceSubscriber;
import br.octahedron.cotopaxi.eventbus.Event;
import br.octahedron.cotopaxi.eventbus.InterestedEvent;
import br.octahedron.cotopaxi.eventbus.NamespaceEvent;
import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.figgo.modules.DataAlreadyExistsException;
import br.octahedron.figgo.modules.Module;
import br.octahedron.figgo.modules.ModuleSpec;
import br.octahedron.figgo.modules.ModuleSpec.Type;
import br.octahedron.figgo.modules.admin.manager.DomainCreatedEvent;
import br.octahedron.util.Log;

/**
 * Creates the configuration domain for a new domain
 * 
 * @author Danilo Queiroz
 */
@InterestedEvent(events = DomainCreatedEvent.class)
public class DomainCreatedSubscriber extends AbstractNamespaceSubscriber {

	private static final Log log = new Log(DomainCreatedSubscriber.class);

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
		log.info("Creating domain configuration for domain " + namespace);
		try {
			this.configurationManager.createDomainConfiguration(namespace);
			for (Module m : Module.values()) {
				ModuleSpec moduleSpec = m.getModuleSpec();
				if (moduleSpec.getModuleType() == Type.DOMAIN) {
					log.debug("Configuring module %s for domain %s", m.name(), namespace);
					this.configurationManager.enableModule(m);
				}
			}
		} catch (DataAlreadyExistsException e) {
			log.warning("Domain configuration already exists, nothing changed!");
		}
		PersistenceManagerPool.forceClose();
	}
}
