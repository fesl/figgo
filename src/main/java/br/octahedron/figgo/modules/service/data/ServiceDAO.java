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
package br.octahedron.figgo.modules.service.data;

import java.util.Collection;

import javax.jdo.Query;

import br.octahedron.cotopaxi.datastore.jdo.GenericDAO;
import br.octahedron.figgo.modules.user.data.User;

/**
 * @author Erick Moreno
 */
public class ServiceDAO extends GenericDAO<Service> {
	
	public ServiceDAO() {
		super(Service.class);
	}

	/**
	 * This method returns all services that has the specified {@link User} (represented by his
	 * userId) as a provider.
	 * 
	 * @param userId
	 *            The key that identifies a {@link User}
	 * @return A collection with all services that has the passed user as provider.
	 */
	@SuppressWarnings("unchecked")
	public Collection<Service> getUserServices(String userId) {
		Query query = this.datastoreFacade.createQueryForClass(Service.class);
		query.setFilter("providers == :userId");
		return (Collection<Service>) query.execute(userId);
	}

	/**
	 * Gets a service by name. If there's more then one service with same name, it returns the
	 * first, if there's no service with the given name, returns null.
	 * 
	 * @param serviceName The service's name
	 * 
	 * @return The first service found with the given name, or <code>null</code> if theres no service with the given name
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Service getServiceByName(String serviceName) {
		Query query = this.datastoreFacade.createQueryForClass(Service.class);
		query.setFilter("name == :serviceName");
		Collection<Service> results = (Collection<Service>) query.execute(serviceName);
		return (results.size() > 0) ? results.iterator().next() : null;
	}

	/**
	 * @param category
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Collection<Service> getServicesByCategory(String category) {
		Query query = this.datastoreFacade.createQueryForClass(Service.class);
		query.setFilter("categoryId == :categoryId");
		return (Collection<Service>) query.execute(category);
	}
	
	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.datastore.jdo.GenericDAO#getAll()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Service> getAll() {
		Query query = this.datastoreFacade.createQueryForClass(Service.class);
		query.setOrdering("name ascending");
		return (Collection<Service>) query.execute();
	}
}
