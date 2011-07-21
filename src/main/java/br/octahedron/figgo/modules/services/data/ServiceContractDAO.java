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
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

import javax.jdo.Query;

import br.octahedron.cotopaxi.datastore.jdo.GenericDAO;

/**
 * @author Vítor Avelino
 *
 */
public class ServiceContractDAO extends GenericDAO<ServiceContract> {

	public ServiceContractDAO() {
		super(ServiceContract.class);
	}

	/**
	 * @param accountId
	 * @return
	 */
	public Collection<ServiceContract> getHistory(String accountId) {
		Collection<ServiceContract> providerContracts = this.getProviderHistory(accountId);
		Collection<ServiceContract> contractorContracts = this.getContractorHistory(accountId);
		return this.mergeServiceContracts(providerContracts, contractorContracts, Long.MIN_VALUE);
	}
	
	public Collection<ServiceContract> getHistory(String accountId, long count) {
		Collection<ServiceContract> providerContracts = this.getProviderHistory(accountId);
		Collection<ServiceContract> contractorContracts = this.getContractorHistory(accountId);
		return this.mergeServiceContracts(providerContracts, contractorContracts, count);
	}
	

	@SuppressWarnings("unchecked")
	protected Collection<ServiceContract> getProviderHistory(String accountId) {
		Query query = this.datastoreFacade.createQueryForClass(ServiceContract.class);
		query.setFilter("provider == :accountId");
		query.setOrdering("id desc");
		return (Collection<ServiceContract>) query.execute(accountId);
	}
	
	@SuppressWarnings("unchecked")
	protected Collection<ServiceContract> getContractorHistory(String accountId) {
		Query query = this.datastoreFacade.createQueryForClass(ServiceContract.class);
		query.setFilter("contractor == :accountId");
		query.setOrdering("id desc");
		return (Collection<ServiceContract>) query.execute(accountId);
	}

	/**
	 * Merges two service contracts list ordering service contracts by id (higher to lower)
	 * 
	 * @param count
	 * 
	 * @return a list with service contracts from the two lists, ordered by id.
	 */
	private Collection<ServiceContract> mergeServiceContracts(Collection<ServiceContract> providerContracts, Collection<ServiceContract> contractorContracts,
			long count) {
		TreeSet<ServiceContract> result = new TreeSet<ServiceContract>(new ServiceContractComparator());
		result.addAll(providerContracts);
		result.addAll(contractorContracts);

		if (count == Long.MIN_VALUE) {
			return result;
		} else {
			TreeSet<ServiceContract> other = new TreeSet<ServiceContract>(new ServiceContractComparator());
			Iterator<ServiceContract> itr = result.iterator();
			while (itr.hasNext() && count != 0) {
				other.add(itr.next());
				count--;
			}
			return other;
		}
	}

	private class ServiceContractComparator implements Comparator<ServiceContract> {
		public int compare(ServiceContract o1, ServiceContract o2) {
			return (int) (o2.getId() - o1.getId());
		}
	}

}
