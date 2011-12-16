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

import br.octahedron.cotopaxi.datastore.namespace.NamespaceManager;
import br.octahedron.cotopaxi.eventbus.Event;
import br.octahedron.cotopaxi.eventbus.InterestedEvent;
import br.octahedron.cotopaxi.eventbus.Subscriber;
import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.figgo.modules.authorization.data.DomainUser;

/**
 * Listen for {@link UserRoleRemovedSubscriber}. When a user has one of his roles for a given domain
 * removed, this subscribers checks if the user still has any role at such domain, and , if
 * necessary, deletes the {@link DomainUser} entry for this user/domain pair.
 * 
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
@InterestedEvent(events = UserRemovedFromRoleEvent.class)
public class UserRoleRemovedSubscriber implements Subscriber {

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
		UserRemovedFromRoleEvent evt = (UserRemovedFromRoleEvent) event;
		this.namespaceManager.changeToNamespace(evt.getDomain());
		boolean hasntRole = this.authorizationManager.getUserRoles(evt.getUserId()).isEmpty();
		this.namespaceManager.changeToGlobalNamespace();
		if(hasntRole) {
			this.authorizationManager.removeDomainUser(evt.getDomain(), evt.getUserId());
		}

	}

}
