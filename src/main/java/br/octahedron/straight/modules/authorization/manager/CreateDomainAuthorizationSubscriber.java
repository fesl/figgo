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
package br.octahedron.straight.modules.authorization.manager;

import java.util.Set;
import java.util.logging.Logger;

import br.octahedron.commons.eventbus.Event;
import br.octahedron.commons.eventbus.InterestedEvent;
import br.octahedron.commons.eventbus.Subscriber;
import br.octahedron.commons.inject.Inject;
import br.octahedron.straight.modules.Module;
import br.octahedron.straight.modules.ModuleSpec;
import br.octahedron.straight.modules.admin.manager.DomainCreatedEvent;

/**
 * 
 * @author Danilo Penna Queiroz
 */
@InterestedEvent(events = { DomainCreatedEvent.class })
public class CreateDomainAuthorizationSubscriber implements Subscriber {

	private static final String USERS_ROLE_NAME = "usuários";
	private static final String ADMINS_ROLE_NAME = "administradores";

	private static final Logger logger = Logger.getLogger(CreateDomainAuthorizationSubscriber.class.getName());

	@Inject
	private AuthorizationManager authorizationManager;

	/**
	 * @param authorizationManager
	 *            the authorizationManager to set
	 */
	public void setAuthorizationManager(AuthorizationManager authorizationManager) {
		this.authorizationManager = authorizationManager;
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
		String domainAdmin = domainEvent.getAdminID();
		logger.info("Creating the default roles authorizations for domain: " + domainName + ". The domain admin is " + domainAdmin);

		// create roles
		logger.fine("Creating roles for domain " + domainName);
		this.authorizationManager.createRole(domainName, USERS_ROLE_NAME);
		this.authorizationManager.createRole(domainName, ADMINS_ROLE_NAME);
		// add actions to roles
		for (Module m : Module.values()) {
			ModuleSpec moduleSpec = m.getModuleSpec();
			if (moduleSpec.getModuleType() == Module.Type.DOMAIN || moduleSpec.getModuleType() == Module.Type.APPLICATION_DOMAIN) {
				logger.fine("Adding actions for module " + m.name() + " for domain " + domainName);
				Set<String> adminActions = moduleSpec.getModuleAdministrativeActions();
				Set<String> userActions = moduleSpec.getModuleActions();
				userActions.removeAll(adminActions);
				this.authorizationManager.addActivitiesToRole(domainName, ADMINS_ROLE_NAME, adminActions);
				this.authorizationManager.addActivitiesToRole(domainName, USERS_ROLE_NAME, userActions);
			}
		}
		// add admin user to admin role
		logger.fine("Configuring the admin for domain " + domainName + ". Admin: " + domainAdmin);
		this.authorizationManager.addUsersToRole(domainName, ADMINS_ROLE_NAME, domainAdmin);
		this.authorizationManager.addUsersToRole(domainName, USERS_ROLE_NAME, domainAdmin);
	}
}
