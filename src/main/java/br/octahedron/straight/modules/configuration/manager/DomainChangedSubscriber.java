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
package br.octahedron.straight.modules.configuration.manager;

import java.util.logging.Logger;

import br.octahedron.commons.eventbus.Event;
import br.octahedron.commons.eventbus.InterestedEvent;
import br.octahedron.commons.eventbus.Subscriber;
import br.octahedron.straight.modules.admin.manager.DomainChangedEvent;
import br.octahedron.straight.modules.configuration.data.DomainConfigurationDAO;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

/**
 * Deletes the domains configuration list on {@link MemcacheService} for all existing namespaces
 * when a domain configuration has changed to force regeneration on the next request of it.
 * 
 * @author Vítor Avelino
 */
@InterestedEvent(events = { DomainChangedEvent.class })
public class DomainChangedSubscriber implements Subscriber {

	private static final Logger logger = Logger.getLogger(DomainChangedSubscriber.class.getName());

	private MemcacheService memcacheService = MemcacheServiceFactory.getMemcacheService();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.octahedron.commons.eventbus.Subscriber#eventPublished(br.octahedron.commons.eventbus.Event
	 * )
	 */
	@Override
	public void eventPublished(Event event) {
		logger.info("Regenerating domains configuration list based on existing namespaces");
		memcacheService.delete(DomainConfigurationDAO.NAMESPACE_KEY);
	}

}
