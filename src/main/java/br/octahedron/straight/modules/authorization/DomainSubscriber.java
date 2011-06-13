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
package br.octahedron.straight.modules.authorization;

import br.octahedron.commons.eventbus.Event;
import br.octahedron.commons.eventbus.InterestedEvent;
import br.octahedron.commons.eventbus.Subscriber;
import br.octahedron.commons.inject.Inject;
import br.octahedron.straight.modules.admin.manager.DomainCreatedEvent;
import br.octahedron.straight.modules.authorization.manager.AuthorizationManager;

/**
 * 
 * @author Danilo Penna Queiroz
 */
@InterestedEvent(events={DomainCreatedEvent.class})
public class DomainSubscriber implements Subscriber {

	private static final long serialVersionUID = 3902021955159483119L;
	
	@Inject
	private AuthorizationManager authorizationManager;
	
	/**
	 * @param authorizationManager the authorizationManager to set
	 */
	public void setAuthorizationManager(AuthorizationManager authorizationManager) {
		this.authorizationManager = authorizationManager;
	}

	/* (non-Javadoc)
	 * @see br.octahedron.commons.eventbus.Subscriber#eventPublished(br.octahedron.commons.eventbus.Event)
	 */
	@Override
	public void eventPublished(Event event) {
		DomainCreatedEvent domainEvent = (DomainCreatedEvent) event;
		// TODO finish
	}
}
