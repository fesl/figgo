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
package br.octahedron.figgo.modules.services.data;

import java.util.Collection;

import javax.jdo.Query;

import br.octahedron.cotopaxi.datastore.GenericDAO;

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
		query.setFilter("providers == userId");
		query.declareParameters("java.lang.String userId");

		return (Collection<Service>) query.execute(userId);
	}

}
