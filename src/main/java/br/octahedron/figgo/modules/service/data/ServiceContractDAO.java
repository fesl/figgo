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
import br.octahedron.figgo.modules.service.data.ServiceContract.ServiceContractStatus;

/**
 * @author Vítor Avelino
 *
 */
public class ServiceContractDAO extends GenericDAO<ServiceContract> {

	public ServiceContractDAO() {
		super(ServiceContract.class);
	}

	/**
	 * TODO
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Collection<ServiceContract> getOpenedProvidedContracts(String userId) {
		Query query = this.datastoreFacade.createQueryForClass(ServiceContract.class);
		query.setFilter("provider == :userId && paid == :paid");
		query.setOrdering("id desc");
		return (Collection<ServiceContract>) query.execute(userId, false);
	}
	
	/**
	 * TODO
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Collection<ServiceContract> getOpenedHiredContracts(String userId) {
		Query query = this.datastoreFacade.createQueryForClass(ServiceContract.class);
		query.setFilter("contractor == :userId && paid == :paid");
		query.setOrdering("id desc");
		return (Collection<ServiceContract>) query.execute(userId, false);
	}
	
	/**
	 * TODO
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Collection<ServiceContract> getProvidedContracts(String userId) {
		Query query = this.datastoreFacade.createQueryForClass(ServiceContract.class);
		query.setFilter("provider == :userId && status == :status && paid == true");
		query.setOrdering("id desc");
		return (Collection<ServiceContract>) query.execute(userId, ServiceContractStatus.COMPLETED, true);
	}
	
	/**
	 * TODO
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Collection<ServiceContract> getHiredContracts(String userId) {
		Query query = this.datastoreFacade.createQueryForClass(ServiceContract.class);
		query.setFilter("contractor == :userId && status == :status && paid == :paid");
		query.setOrdering("id desc");
		return (Collection<ServiceContract>) query.execute(userId, ServiceContractStatus.COMPLETED, true);
	}

}
