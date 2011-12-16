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

import static br.octahedron.figgo.modules.authorization.manager.AuthorizationManager.*;
import br.octahedron.cotopaxi.datastore.jdo.PersistenceManagerPool;
import br.octahedron.cotopaxi.datastore.namespace.NamespaceManager;
import br.octahedron.cotopaxi.eventbus.Event;
import br.octahedron.cotopaxi.eventbus.InterestedEvent;
import br.octahedron.cotopaxi.eventbus.Subscriber;
import br.octahedron.cotopaxi.inject.Inject;

/**
 * Listen for {@link UserActivatedEvent}. When a user is activated at a given domain, this
 * subscriber adds him to USERS role.
 * 
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
@InterestedEvent(events = UserActivatedEvent.class)
public class UserActivatedSubscriber implements Subscriber {

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
	 * @param namespaceManager
	 *            the namespaceManager to set
	 */
	public void setNamespaceManager(NamespaceManager namespaceManager) {
		this.namespaceManager = namespaceManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.octahedron.cotopaxi.eventbus.Subscriber#eventPublished(br.octahedron.cotopaxi.eventbus
	 * .Event)
	 */
	@Override
	public void eventPublished(Event event) {
		UserActivatedEvent evt = (UserActivatedEvent) event;
		this.namespaceManager.changeToNamespace(evt.getDomain());
		authorizationManager.addUsersToRole(USERS_ROLE_NAME, evt.getUserId());

		PersistenceManagerPool.forceClose();
	}

}
