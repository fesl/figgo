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
package br.octahedron.figgo.modules.services.manager;

import java.math.BigDecimal;
import java.util.Collection;

import br.octahedron.cotopaxi.eventbus.EventBus;
import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.figgo.modules.services.data.Service;
import br.octahedron.figgo.modules.services.data.ServiceContract;
import br.octahedron.figgo.modules.services.data.ServiceContractDAO;
import br.octahedron.figgo.modules.services.data.ServiceDAO;
import br.octahedron.figgo.modules.services.data.ServiceContract.ServiceContractStatus;
import br.octahedron.figgo.modules.users.data.User;

/**
 * This entity is responsible by manage services
 * 
 * @author Erick Moreno
 * @author Vítor Avelino
 */
public class ServiceManager {

	private final ServiceDAO serviceDAO = new ServiceDAO();
	private final ServiceContractDAO serviceContractDAO = new ServiceContractDAO();
	
	@Inject
	private EventBus eventBus;
	
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}
	/**
	 * Creates and saves a new service
	 * 
	 * @return The {@link Service} created
	 */
	public Service createService(String name, BigDecimal value, String category, String description) {
		Service service = new Service(name, value, category, description);
		this.serviceDAO.save(service);
		return service;
	}

	/**
	 * Updates an service
	 */
	public Service updateService(String name, BigDecimal value, String description) {
		Service serv = this.serviceDAO.get(name);
		serv.setAmount(value);
		serv.setDescription(description);
		// This object will be updated to the DB by JDO persistence manager
		return serv;
	}

	/**
	 * Gets the {@link Service} with the given id
	 * 
	 * @return the {@link Service} with the given id, if exists, or <code>null</code>, if doesn't
	 *         exists a user with the given id.
	 */
	public Service getService(String name) {
		return this.serviceDAO.get(name);
	}

	/**
	 * Checks if exists a {@link Service} with the given id.
	 * 
	 * @return <code>true</code> if exists, <code>false</code> otherwise.
	 */
	public boolean existsService(String name) {
		return this.serviceDAO.exists(name);
	}

	/**
	 * Adds a {@link User} that provides this service
	 * 
	 * @param serviceName
	 * @param userId
	 */
	public void addProvider(String serviceName, String userId) {
		Service serv = this.serviceDAO.get(serviceName);
		serv.addProvider(userId);
	}

	/**
	 * Removes a {@link User} from the providers list
	 * 
	 * @param serviceName
	 * @param userId
	 */
	public void removeProvider(String serviceName, String userId) {
		Service serv = this.serviceDAO.get(serviceName);
		serv.removeProvider(userId);
	}

	/**
	 * This method retrieves all {@link User} that provides this service
	 * 
	 * @param serviceName
	 * @return An collection with userId
	 */
	public Collection<String> getServiceProviders(String serviceName) {
		Service serv = this.serviceDAO.get(serviceName);
		return serv.getProviders();
	}

	/**
	 * This method returns all {@link Service} that has the specified {@link User} (represented by
	 * his userId) as a provider.
	 * 
	 * @param userId
	 *            The key that identifies a {@link User}
	 * @return A collection with all services that has the passed user as provider.
	 */
	public Collection<Service> getUserServices(String userId) {
		return this.serviceDAO.getUserServices(userId);
	}
	
	/**
	 * creates a new contract TODO
	 * 
	 * @param serviceName
	 * @param contractor
	 * @param provider
	 * @param amount
	 */
	public void requestContract(String serviceName, String contractor, String provider, BigDecimal amount) {
		ServiceContract serviceContract = new ServiceContract(serviceName, contractor, provider, amount);
		this.serviceContractDAO.save(serviceContract);
		this.eventBus.publish(new ServiceContractRequestedEvent(serviceContract));
	}
	
	public void updateContractStatus(Long id, ServiceContractStatus status) {
		ServiceContract serviceContract = this.serviceContractDAO.get(id);
		serviceContract.setStatus(status);
		this.eventBus.publish(new ServiceContractUpdatedEvent(serviceContract));
	}
	
	public void makePayment(Long id) {
		ServiceContract serviceContract = this.serviceContractDAO.get(id);
		serviceContract.setPaid(true);
		this.eventBus.publish(new ServiceContractPaidEvent(serviceContract));
	}
	
	public Collection<ServiceContract> getContractsHistory(String userId) {
		return this.serviceContractDAO.getHistory(userId);
	}

}
