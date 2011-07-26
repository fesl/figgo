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

import br.octahedron.cotopaxi.eventbus.Event;
import br.octahedron.figgo.modules.service.data.ServiceContract;

/**
 * @author vitoravelino
 *
 */
public class ServiceContractUpdatedEvent implements Event {

	private static final long serialVersionUID = 1079739165236621842L;

	private ServiceContract serviceContract;

	public ServiceContractUpdatedEvent(ServiceContract serviceContract) {
		this.serviceContract = serviceContract;
	}

	public ServiceContract getServiceContract() {
		return this.serviceContract;
	}

}
