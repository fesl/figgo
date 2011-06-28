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
package br.octahedron.straight.modules.admin.manager;

import br.octahedron.commons.eventbus.Event;
import br.octahedron.straight.modules.configuration.data.DomainConfiguration;

/**
 * Event to inform that a domain configuration has created
 * 
 * @author Vítor Avelino
 */
public class DomainChangedEvent implements Event {

	private static final long serialVersionUID = -6348483502587001585L;
	
	private DomainConfiguration domainConfiguration;

	public DomainChangedEvent(DomainConfiguration domainConfiguration) { 
		this.domainConfiguration = domainConfiguration;
	}
	
	public DomainConfiguration getDomainConfiguration() {
		return this.domainConfiguration;
	}

}
