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
package br.octahedron.figgo.modules.user.manager;

import br.octahedron.cotopaxi.datastore.jdo.PersistenceManagerPool;
import br.octahedron.cotopaxi.eventbus.Event;
import br.octahedron.cotopaxi.eventbus.InterestedEvent;
import br.octahedron.cotopaxi.eventbus.Subscriber;
import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.figgo.modules.upload.controller.UserUploadEvent;

/**
 * @author vitoravelino
 * 
 */
@InterestedEvent(events = { UserUploadEvent.class })
public class UsersUploadSubscriber implements Subscriber {

	@Inject
	private UserManager usersManager;

	/**
	 * @param usersManager
	 *            the usersManager to set
	 */
	public void setUserManager(UserManager usersManager) {
		this.usersManager = usersManager;
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
		UserUploadEvent uploadEvent = (UserUploadEvent) event;
		this.usersManager.updateAvatarKey(uploadEvent.getTarget(), uploadEvent.getBlobKey());
		PersistenceManagerPool.forceClose();
	}

}
