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
package br.octahedron.figgo.modules.service.manager;

import br.octahedron.cotopaxi.eventbus.NamespaceEvent;
import br.octahedron.figgo.modules.service.data.Service;

/**
 * @author vitoravelino
 *
 */
public class ServiceUpdatedEvent extends NamespaceEvent {

	private static final long serialVersionUID = -45114603014772235L;
	
	private final String oldCategoryId;
	private final Service service;

	/**
	 * @param service
	 * @param oldCategoryId
	 */
	public ServiceUpdatedEvent(Service service, String oldCategoryId) {
		super();
		this.service = service;
		this.oldCategoryId = oldCategoryId;
	}
	
	public Service getService() {
		return this.service;
	}
	
	public String getOldCategoryId() {
		return this.oldCategoryId;
	}

}
