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
import java.util.Collection;

import br.octahedron.cotopaxi.eventbus.EventBus;
import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.figgo.modules.DataDoesNotExistsException;
import br.octahedron.figgo.modules.service.data.Service;
import br.octahedron.figgo.modules.service.data.ServiceCategory;
import br.octahedron.figgo.modules.service.data.ServiceCategoryDAO;
import br.octahedron.figgo.modules.service.data.ServiceContract;
import br.octahedron.figgo.modules.service.data.ServiceContract.ServiceContractStatus;
import br.octahedron.figgo.modules.service.data.ServiceContractDAO;
import br.octahedron.figgo.modules.service.data.ServiceDAO;
import br.octahedron.figgo.modules.user.data.User;
import br.octahedron.util.Log;

/**
 * This entity is responsible by manage services
 * 
 * @author Erick Moreno
 * @author Vítor Avelino
 */
public class ServiceManager {

	private static final Log logger = new Log(ServiceManager.class);

	private ServiceDAO serviceDAO = new ServiceDAO();
	private ServiceCategoryDAO serviceCategoryDAO = new ServiceCategoryDAO();
	private ServiceContractDAO serviceContractDAO = new ServiceContractDAO();
	@Inject
	private EventBus eventBus;

	/*
	 * for tests purpose
	 */
	protected void setServiceCategoryDAO(ServiceCategoryDAO serviceCategoryDAO) {
		this.serviceCategoryDAO = serviceCategoryDAO;
	}

	/*
	 * for tests purpose
	 */
	protected void setServiceContractDAO(ServiceContractDAO serviceContractDAO) {
		this.serviceContractDAO = serviceContractDAO;
	}

	/*
	 * for tests purpose
	 */
	protected void setServiceDAO(ServiceDAO serviceDAO) {
		this.serviceDAO = serviceDAO;
	}

	/**
	 * @param eventBus
	 *            the eventBus to set
	 */
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	/**
	 * Gets the {@link Service} with the given id
	 * 
	 * @return the {@link Service} with the given id, if exists, or <code>null</code>, if doesn't
	 *         exists a user with the given id.
	 * @throws DataDoesNotExistsException
	 *             If there's no such service
	 */
	public Service getService(String serviceId) {
		Service service = this.serviceDAO.get(serviceId);
		if (service != null) {
			return service;
		} else {
			throw new DataDoesNotExistsException();
		}
	}

	/**
	 * Creates and saves a new service
	 * 
	 * @return The {@link Service} created
	 */
	public Service createService(String name, BigDecimal value, String category, String description) {
		Service service = new Service(name, value, category, description);
		this.serviceDAO.save(service);
		this.eventBus.publish(new ServiceCreatedEvent(service.getCategoryId(), service.getCategory()));
		return service;
	}

	/**
	 * Updates an service
	 * 
	 * @throws DataDoesNotExistsException
	 *             If there's no such service
	 */
	public Service updateService(String serviceId, String name, BigDecimal value, String category, String description) {
		Service service = this.getService(serviceId);
		String oldCategoryId = service.getCategoryId();
		service.setName(name);
		service.setAmount(value);
		service.setDescription(description);
		service.setCategory(category);
		this.eventBus.publish(new ServiceUpdatedEvent(service, oldCategoryId));
		// This object will be updated to the DB by JDO persistence manager
		return service;
	}

	/**
	 * Adds a {@link User} that provides this service
	 * 
	 * @param serviceId
	 *            The service's id
	 * @param userId
	 *            The user's id
	 * @return
	 * 
	 * @throws DataDoesNotExistsException
	 *             If there's no such service
	 */
	public void addProvider(String serviceId, String userId) {
		Service service = this.serviceDAO.get(serviceId);
		service.addProvider(userId);
	}

	/**
	 * Removes a {@link User} from the providers list
	 * 
	 * @param serviceId
	 *            The service's Id
	 * @param userId
	 *            The user's Id
	 * @return
	 * @throws DataDoesNotExistsException
	 *             If there's no such service
	 * 
	 *             TODO really need to return the service?
	 */
	public Service removeProvider(String serviceId, String userId) {
		Service service = this.getService(serviceId);
		service.removeProvider(userId);
		return service;
	}

	// /**
	// * This method retrieves all {@link User} that provides this service
	// *
	// * @param serviceName
	// * @return An collection with userId
	// */
	// public Collection<String> getServiceProviders(String serviceName) {
	// Service serv = this.serviceDAO.getServiceByName(serviceName);
	// return serv.getProviders();
	// }

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
	 * @return all services
	 */
	public Collection<Service> getServices() {
		return this.serviceDAO.getAll();
	}

	/**
	 * Removes a service.
	 * 
	 * @param serviceId
	 *            The service's id
	 * @throws DataDoesNotExistsException
	 *             If there's no such service
	 */
	public void removeService(String serviceId) {
		Service service = this.getService(serviceId);
		this.serviceDAO.delete(service);
		this.eventBus.publish(new ServiceRemovedEvent(service));
	}

	// TODO Move this to ServiceCategoryManager
	/**
	 * Gets all service's categories
	 * 
	 * @return All service's categories
	 */
	public Collection<ServiceCategory> getServiceCategories() {
		return this.serviceCategoryDAO.getAll();
	}

	/**
	 * Creates a new ServiceCategory
	 * 
	 * @param categoryId
	 *            The category's Id
	 * @param categoryName
	 *            The category's name
	 */
	protected void createServiceCategory(String categoryId, String categoryName) {
		if (!this.serviceCategoryDAO.exists(categoryId)) {
			ServiceCategory serviceCategory = new ServiceCategory(categoryId, categoryName);
			this.serviceCategoryDAO.save(serviceCategory);
		}
	}

	/**
	 * Cleans a category, it means, remove this category only if there's no service with this
	 * category
	 * 
	 * @param categoryId
	 *            The category's id
	 */
	protected void cleanCategory(String categoryId) {
		Collection<Service> services = this.serviceDAO.getServicesByCategory(categoryId);
		if (services.isEmpty()) {
			this.serviceCategoryDAO.delete(categoryId);
		}
	}

	/**
	 * Get all services of a given category.
	 * 
	 * @param categoryId
	 *            The category's Id
	 * @return All services of a given category
	 */
	public Collection<Service> getServicesByCategory(String categoryId) {
		return this.serviceDAO.getServicesByCategory(categoryId);
	}

	/**
	 * Checks if exists a category with the given id
	 * 
	 * @param categoryId
	 *            The category's Id.
	 * @return <code>true</code> if exists, <code>false</code> otherwise.
	 */
	public boolean existsServiceCategory(String categoryId) {
		return this.serviceCategoryDAO.exists(categoryId);
	}

	/**
	 * Get all categories if id starting with the given term
	 * 
	 * @param term
	 *            The category prefix
	 * @return all categories if id starting with the given term
	 */
	public Collection<ServiceCategory> getCategoriesStartingWith(String term) {
		return this.serviceCategoryDAO.getAllStartsWith("id", term);
	}

	// TODO Move this to ServiceContractManager 300+ lines, wtf?

	/**
	 * @param in
	 * @return
	 * @throws DataDoesNotExistsException
	 *             If there's no such service
	 */
	public ServiceContract getServiceContract(String contractId) {
		ServiceContract serviceContract = this.serviceContractDAO.get(contractId);
		if (serviceContract != null) {
			return serviceContract;
		} else {
			throw new DataDoesNotExistsException();
		}
	}

	/**
	 * @param userId
	 * @return
	 */
	public Collection<ServiceContract> getProvidedContracts(String userId) {
		return this.serviceContractDAO.getProvidedContracts(userId);
	}

	/**
	 * @param userId
	 * @return
	 */
	public Collection<ServiceContract> getHiredContracts(String userId) {
		return this.serviceContractDAO.getHiredContracts(userId);
	}

	/**
	 * @param userId
	 * @return
	 */
	public Collection<ServiceContract> getOpenedProvidedContracts(String userId) {
		return this.serviceContractDAO.getOpenedProvidedContracts(userId);
	}

	/**
	 * @param userId
	 * @return
	 */
	public Collection<ServiceContract> getOpenedHiredContracts(String userId) {
		return this.serviceContractDAO.getOpenedHiredContracts(userId);
	}

	/**
	 * Creates a new contract of a service between a contractor and a provider.
	 * 
	 * @param serviceName
	 * @param contractor
	 * @param provider
	 * @throws NotServiceProviderException
	 *             If the given provider doesn't provide the given service
	 * @throws DataDoesNotExistsException
	 *             If there's no such service
	 */
	public void requestContract(String serviceId, String contractor, String provider) throws NotServiceProviderException {
		Service service = this.getService(serviceId);
		if (service.hasProvider(provider)) {
			ServiceContract serviceContract = new ServiceContract(service, contractor, provider);
			this.serviceContractDAO.save(serviceContract);
			this.eventBus.publish(new ServiceContractRequestedEvent(serviceContract));
		} else {
			throw new NotServiceProviderException();
		}
	}

	/**
	 * Provider updates the service contract status.
	 * 
	 * @param contractId
	 * @param status
	 * @param providerId
	 * @throws NotServiceProviderException
	 *             If the given providerId isn't the provider for the given contract
	 * @throws DataDoesNotExistsException
	 *             If there's no such service
	 */
	public void updateContractStatus(String contractId, ServiceContractStatus status, String providerId) throws NotServiceProviderException {
		ServiceContract serviceContract = this.getServiceContract(contractId);
		if (serviceContract.getProvider().equals(providerId)) {
			serviceContract.setStatus(status);
			this.eventBus.publish(new ServiceContractUpdatedEvent(serviceContract));
		} else {
			throw new NotServiceProviderException();
		}
	}

	/**
	 * Pays a service. It mark the service as paid and publishes a notification about this event.
	 * 
	 * If the given {@link ServiceContract} is already paid, nothing happens.
	 * 
	 * @param contractId
	 *            The contract's id being paid.
	 * @param userId
	 *            The user's id who is performing the payment.
	 * 
	 * @throws CanceledServiceContractException
	 *             If the service contract is canceled
	 * @throws OnlyServiceContractorException
	 *             If the user performing the payment isn't the contractor for given contract
	 * @throws DataDoesNotExistsException
	 *             If there's no such service
	 */
	public void makePayment(String contractId, String userId) throws CanceledServiceContractException, OnlyServiceContractorException {
		logger.debug("Trying to pay contract %s", contractId);
		ServiceContract serviceContract = this.getServiceContract(contractId);
		if (serviceContract.getStatus() == ServiceContractStatus.CANCELED) {
			throw new CanceledServiceContractException();
		} else if (!serviceContract.getContractor().equals(userId)) {
			throw new OnlyServiceContractorException();
		}

		if (!serviceContract.isPaid()) {
			logger.info("Contract %s has been paid by %s", contractId, userId);
			serviceContract.setStatus(ServiceContractStatus.COMPLETED);
			serviceContract.setPaid(true);
			this.eventBus.publish(new ServiceContractPaidEvent(serviceContract));
		}
	}

	/**
	 * Performs a payment rollback for the given contract. In other words, it marks the given
	 * {@link ServiceContract} as no paid.
	 * 
	 * @param contractId
	 *            The ServiceContract's Id
	 * @throws DataDoesNotExistsException
	 *             If there's no such service
	 */
	public void rollbackPayment(String contractId) {
		ServiceContract serviceContract = this.getServiceContract(contractId);
		serviceContract.setPaid(false);
	}

}