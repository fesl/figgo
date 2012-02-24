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
package br.octahedron.figgo.modules.authorization.manager;

import static br.octahedron.figgo.modules.authorization.manager.AuthorizationManager.ADMINS_ROLE_NAME;
import static br.octahedron.figgo.modules.authorization.manager.AuthorizationManager.USERS_ROLE_NAME;
import br.octahedron.cotopaxi.datastore.jdo.PersistenceManagerPool;
import br.octahedron.cotopaxi.datastore.namespace.NamespaceManager;
import br.octahedron.cotopaxi.eventbus.Event;
import br.octahedron.cotopaxi.eventbus.InterestedEvent;
import br.octahedron.cotopaxi.eventbus.Subscriber;
import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.figgo.modules.ApplicationDomainModuleSpec;
import br.octahedron.figgo.modules.ApplicationDomainModuleSpec.ActionSpec;
import br.octahedron.figgo.modules.DataAlreadyExistsException;
import br.octahedron.figgo.modules.Module;
import br.octahedron.figgo.modules.ModuleSpec;
import br.octahedron.figgo.modules.ModuleSpec.Type;
import br.octahedron.figgo.modules.admin.manager.DomainCreatedEvent;
import br.octahedron.util.Log;

/**
 * 
 * @author Danilo Penna Queiroz
 */
@InterestedEvent(events = { DomainCreatedEvent.class })
public class CreateDomainAuthorizationSubscriber implements Subscriber {



	private static final Log logger = new Log(CreateDomainAuthorizationSubscriber.class);

	@Inject
	private AuthorizationManager authorizationManager;
	
	@Inject
	private NamespaceManager namespaceManager;

	/**
	 * @param authorizationManager
	 *            the authorizationManager to set
	 */
	public void setAuthorizationManager(AuthorizationManager authorizationManager) {
		this.authorizationManager = authorizationManager;
	}
	
	/**
	 * @param namespaceManager the namespaceManager to set
	 */
	public void setNamespaceManager(NamespaceManager namespaceManager) {
		this.namespaceManager = namespaceManager;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.octahedron.commons.eventbus.Subscriber#eventPublished(br.octahedron.commons.eventbus.Event
	 * )
	 */
	@Override
	public void eventPublished(Event event) {
		DomainCreatedEvent domainEvent = (DomainCreatedEvent) event;
		String domainName = domainEvent.getNamespace();
		this.namespaceManager.changeToNamespace(domainName);
		
		String domainAdmin = domainEvent.getAdminID();
		logger.info("Creating the default roles authorizations for domain: " + domainName + ". The domain admin is " + domainAdmin);

		// create roles
		for(String role : new String[]{ USERS_ROLE_NAME, ADMINS_ROLE_NAME}) {
			try {
				this.authorizationManager.createRole(role);
			} catch (DataAlreadyExistsException ex) {
				// Role already exists? ok, remove all activities from it
				this.authorizationManager.removeActivitiesFromRole(role);
			}
		}

		// add actions to roles
		for (Module m : Module.values()) {
			ModuleSpec moduleSpec = m.getModuleSpec();
			if (moduleSpec.getModuleType() == Type.DOMAIN || moduleSpec.getModuleType() == Type.APPLICATION_DOMAIN) {
				logger.debug("Adding actions for module %s for domain %s", m.name(), domainName);
				ApplicationDomainModuleSpec spec = (ApplicationDomainModuleSpec) moduleSpec;
				for (ActionSpec action : spec.getModuleActions()) {
					String name = action.getAction();
					logger.debug("Module %s; Action %s", m.toString(), name);
					this.authorizationManager.addActivitiesToRole(ADMINS_ROLE_NAME, name);
					if (!action.isAdministrativeOnly()) {
						this.authorizationManager.addActivitiesToRole(USERS_ROLE_NAME, name);
					}
				}
			}
		}

		// add admin user to admin role
		logger.info("Configuring the admin for domain %s. Admin: %s", domainName, domainAdmin);
		this.authorizationManager.addUsersToRole(ADMINS_ROLE_NAME, domainAdmin);
		this.authorizationManager.addUsersToRole(USERS_ROLE_NAME, domainAdmin);
		
		// persist DomainUser information
		this.namespaceManager.changeToGlobalNamespace();
		this.authorizationManager.createDomainUser(domainName, domainAdmin, true);
		
		// Interceptors don't work for tasks, forcing PersistenceManager to close
		PersistenceManagerPool.forceClose();
	}
}
