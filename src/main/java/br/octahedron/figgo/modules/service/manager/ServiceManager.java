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
	 * @throws ServiceNotFoundException
	 */
	public Service updateService(String serviceId, String name, BigDecimal value, String category, String description)
			throws ServiceNotFoundException {
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
	 * Gets the {@link Service} with the given id
	 * 
	 * @return the {@link Service} with the given id, if exists, or <code>null</code>, if doesn't
	 *         exists a user with the given id.
	 * @throws ServiceNotFoundException
	 */
	public Service getService(String serviceId) throws ServiceNotFoundException {
		Service service = this.serviceDAO.get(serviceId);
		if (service != null) {
			return service;
		} else {
			throw new ServiceNotFoundException();
		}
	}

	/**
	 * Adds a {@link User} that provides this service
	 * 
	 * @param serviceName
	 * @param userId
	 * @return
	 */
	public Service addProvider(String serviceId, String userId) {
		Service service = this.serviceDAO.get(serviceId);
		service.addProvider(userId);
		return service;
	}

	/**
	 * Removes a {@link User} from the providers list
	 * 
	 * @param serviceName
	 * @param userId
	 * @return
	 * @throws ServiceNotFoundException
	 */
	public Service removeProvider(String serviceId, String userId) throws ServiceNotFoundException {
		Service service = this.getService(serviceId);
		service.removeProvider(userId);
		return service;
	}

	/**
	 * This method retrieves all {@link User} that provides this service
	 * 
	 * @param serviceName
	 * @return An collection with userId
	 */
	public Collection<String> getServiceProviders(String serviceName) {
		Service serv = this.serviceDAO.getServiceByName(serviceName);
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
	 * @return all services
	 */
	public Collection<Service> getServices() {
		return this.serviceDAO.getAll();
	}

	/**
	 * @param serviceName
	 * @return
	 * @throws ServiceNotFoundException
	 */
	public void removeService(String serviceId) throws ServiceNotFoundException {
		Service service = this.getService(serviceId);
		this.serviceDAO.delete(service);
		this.eventBus.publish(new ServiceRemovedEvent(service));
	}

	// TODO Move this to ServiceCategoryManager
	/**
	 * 
	 * @return
	 */
	public Collection<ServiceCategory> getServiceCategories() {
		return this.serviceCategoryDAO.getAll();
	}

	/**
	 * @param categoryId
	 * @param categoryName
	 */
	public void createServiceCategory(String categoryId, String categoryName) {
		if (!this.serviceCategoryDAO.exists(categoryId)) {
			ServiceCategory serviceCategory = new ServiceCategory(categoryId, categoryName);
			this.serviceCategoryDAO.save(serviceCategory);
		}
	}

	/**
	 * @param category
	 */
	public void cleanCategory(String category) {
		Collection<Service> services = this.serviceDAO.getServicesByCategory(category);
		if (services.isEmpty()) {
			this.serviceCategoryDAO.delete(category);
		}
	}

	/**
	 * @param in
	 * @return
	 */
	public Collection<Service> getServicesByCategory(String category) {
		return this.serviceDAO.getServicesByCategory(category);
	}

	/**
	 * @param category
	 * @return
	 */
	public boolean existsServiceCategory(String category) {
		return this.serviceCategoryDAO.exists(category);
	}

	/**
	 * @param term
	 * @return
	 */
	public Collection<ServiceCategory> getCategoriesStartingWith(String term) {
		return this.serviceCategoryDAO.getAllStartsWith("id", term);
	}

	// TODO Move this to ServiceContractManager 300+ lines, wtf?

	/**
	 * @param in
	 * @return
	 * @throws ServiceContractNotFoundException
	 */
	public ServiceContract getServiceContract(String contractId) throws ServiceContractNotFoundException {
		ServiceContract serviceContract = this.serviceContractDAO.get(contractId);
		if (serviceContract != null) {
			return serviceContract;
		} else {
			throw new ServiceContractNotFoundException();
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
	 * @throws InexistentServiceProviderException
	 * @throws ServiceNotFoundException
	 */
	public void requestContract(String serviceId, String contractor, String provider) throws InexistentServiceProviderException,
			ServiceNotFoundException {
		Service service = this.getService(serviceId);
		if (service.hasProvider(provider)) {
			ServiceContract serviceContract = new ServiceContract(service, contractor, provider);
			this.serviceContractDAO.save(serviceContract);
			this.eventBus.publish(new ServiceContractRequestedEvent(serviceContract));
		} else {
			throw new InexistentServiceProviderException();
		}
	}

	/**
	 * Provider updates the service contract status.
	 * 
	 * @param contractId
	 * @param status
	 * @param providerId
	 * @throws NotServiceProviderException
	 * @throws ServiceContractNotFoundException
	 */
	public void updateContractStatus(String contractId, ServiceContractStatus status, String providerId) throws NotServiceProviderException,
			ServiceContractNotFoundException {
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
	 * @throws UncompletedServiceContractException
	 *             If the service contract isn't complete
	 * @throws OnlyServiceContractorException
	 *             If the user performing the payment isn't the contractor for given contract
	 * @throws ServiceContractNotFoundException
	 *             If there's no {@link ServiceContract} with the given contractId.
	 */
	public void makePayment(String contractId, String userId) throws UncompletedServiceContractException, OnlyServiceContractorException,
			ServiceContractNotFoundException {
		logger.debug("Trying to pay contract %s", contractId);
		ServiceContract serviceContract = this.getServiceContract(contractId);
		if (serviceContract.getStatus() != ServiceContractStatus.COMPLETED) {
			throw new UncompletedServiceContractException();
		} else if (!serviceContract.getContractor().equals(userId)) {
			throw new OnlyServiceContractorException();
		}

		if (!serviceContract.isPaid()) {
			logger.info("Contract %s has been paid by %s", contractId, userId);
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
	 * @throws ServiceContractNotFoundException
	 *             If there's no contract with the given Id
	 */
	public void rollbackPayment(String contractId) throws ServiceContractNotFoundException {
		ServiceContract serviceContract = this.getServiceContract(contractId);
		serviceContract.setPaid(false);
	}

}