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

import java.math.BigDecimal;

import br.octahedron.cotopaxi.eventbus.Event;
import br.octahedron.figgo.modules.service.data.ServiceContract;

/**
 * Event to notify that an {@link ServiceContract} has been paid.
 * 
 * @author vitoravelino
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
public class ServiceContractPaidEvent implements Event {

	private static final long serialVersionUID = -6579332745876992770L;

	private String provider;
	private String contractor;
	private BigDecimal value;
	private String serviceName;

	private String contractId;

	/**
	 * @param srvContract
	 *            The paid service contract
	 */
	public ServiceContractPaidEvent(ServiceContract srvContract) {
		this(srvContract.getId(), srvContract.getContractor(), srvContract.getProvider(), srvContract.getAmount(), srvContract.getService().getName());
	}

	/**
	 * @param contractId The {@link ServiceContract} id
	 * @param contractor The contractor id
	 * @param provider The provider Id
	 * @param value The {@link ServiceContract} value 
	 * @param serviceName The service name
	 */
	public ServiceContractPaidEvent(String contractId, String contractor, String provider, BigDecimal value, String serviceName) {
		this.contractId = contractId;
		this.contractor = contractor;
		this.provider = provider;
		this.value = value;
		this.serviceName = serviceName;
	}

	/**
	 * @return the contractId
	 */
	public String getContractId() {
		return contractId;
	}

	/**
	 * @return the provider
	 */
	public String getProvider() {
		return provider;
	}

	/**
	 * @return the contractor
	 */
	public String getContractor() {
		return contractor;
	}

	/**
	 * @return the value
	 */
	public BigDecimal getValue() {
		return value;
	}

	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}
	
	@Override
	public int hashCode() {
		return this.contractId.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ServiceContractPaidEvent) {
			ServiceContractPaidEvent other = (ServiceContractPaidEvent) obj;
			return this.contractId.equals(other.contractId);
		} else {
			return false;
		}
	}
}
