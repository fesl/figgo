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
package br.octahedron.figgo.modules.service.controller;

import static br.octahedron.cotopaxi.controller.Converter.Builder.bigDecimalNumber;
import static br.octahedron.cotopaxi.controller.Converter.Builder.safeString;
import br.octahedron.cotopaxi.auth.AuthenticationRequired;
import br.octahedron.cotopaxi.auth.AuthorizationRequired;
import br.octahedron.cotopaxi.controller.Controller;
import br.octahedron.cotopaxi.datastore.namespace.NamespaceRequired;
import br.octahedron.cotopaxi.i18n.ControllerI18nHelper;
import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.cotopaxi.validation.Validator;
import br.octahedron.figgo.FiggoException;
import br.octahedron.figgo.OnlyForNamespaceControllerInterceptor.OnlyForNamespace;
import br.octahedron.figgo.modules.DataDoesNotExistsException;
import br.octahedron.figgo.modules.service.controller.validation.ServiceValidators;
import br.octahedron.figgo.modules.service.data.Service;
import br.octahedron.figgo.modules.service.data.ServiceContract;
import br.octahedron.figgo.modules.service.data.ServiceContract.ServiceContractStatus;
import br.octahedron.figgo.modules.service.manager.NotServiceProviderException;
import br.octahedron.figgo.modules.service.manager.ServiceManager;

/**
 * @author Vítor Avelino
 * 
 */
@OnlyForNamespace
@AuthenticationRequired
@NamespaceRequired
public class ServiceController extends Controller {

	private static final String BASE_DIR_TPL = "services/";
	private static final String NEW_SERVICE_TPL = BASE_DIR_TPL + "new.vm";
	private static final String LIST_SERVICE_TPL = BASE_DIR_TPL + "list.vm";
	private static final String SHOW_SERVICE_TPL = BASE_DIR_TPL + "show.vm";
	private static final String EDIT_SERVICE_TPL = BASE_DIR_TPL + "edit.vm";
	private static final String USER_SERVICES_TPL = BASE_DIR_TPL + "user.vm";
	private static final String CONTRACT_DIR_TPL = BASE_DIR_TPL + "contract/";
	private static final String EDIT_CONTRACT_TPL = CONTRACT_DIR_TPL + "edit.vm";
	private static final String LIST_CONTRACTS_TPL = CONTRACT_DIR_TPL + "list.vm";

	private static final String BASE_URL = "/services";
	private static final String SHOW_CONTRACTS_URL = BASE_URL + "/contracts";

	private ControllerI18nHelper i18n = new ControllerI18nHelper(this);

	@Inject
	private ServiceManager servicesManager;

	public void setServiceManager(ServiceManager serviceManager) {
		this.servicesManager = serviceManager;
	}

	/**
	 * Lists available services and categories
	 */
	@AuthorizationRequired
	public void getListServices() {
		this.out("services", this.servicesManager.getServices());
		this.out("categories", this.servicesManager.getServiceCategories());
		this.success(LIST_SERVICE_TPL);
	}

	/**
	 * Shows service details
	 * 
	 * Receives the service id as <code>id</code>
	 */
	@AuthorizationRequired
	public void getShowService() {
		try {
			Service service = this.servicesManager.getService(this.in("id"));
			this.out("service", service);
			this.success(SHOW_SERVICE_TPL);
		} catch (DataDoesNotExistsException e) {
			this.notFound();
		}
	}

	/**
	 * Show the form page for a new service
	 */
	@AuthorizationRequired
	public void getNewService() {
		this.success(NEW_SERVICE_TPL);
	}

	/**
	 * Process the new service form
	 * 
	 * Receives <code>name</code>, <code>amount</code>, <code>category</code> and
	 * <code>description</code>
	 */
	@AuthorizationRequired
	public void postNewService() {
		Validator validator = ServiceValidators.getServiceValidator();
		if (validator.isValid()) {
			this.servicesManager.createService(this.in("name", safeString()), this.in("amount", bigDecimalNumber()),
					this.in("category", safeString()), this.in("description", safeString()));
			this.redirect(BASE_URL);
		} else {
			this.echo();
			this.invalid(NEW_SERVICE_TPL);
		}
	}

	/**
	 * Show the form to edit an service
	 * 
	 * Receives the service id as <code>id</code>
	 */
	@AuthorizationRequired
	public void getEditService() {
		try {
			Service service = this.servicesManager.getService(this.in("id"));
			this.out("id", service.getId());
			this.out("name", service.getName());
			this.out("amount", service.getAmount());
			this.out("category", service.getCategory());
			this.out("description", service.getDescription());
			this.success(EDIT_SERVICE_TPL);
		} catch (DataDoesNotExistsException e) {
			this.notFound();
		}
	}

	/**
	 * Process the service edit form
	 * 
	 * Receives <code>name</code>, <code>amount</code>, <code>category</code> and
	 * <code>description</code>
	 */
	@AuthorizationRequired
	public void postEditService() {
		Validator validator = ServiceValidators.getServiceValidator();
		if (validator.isValid()) {
			try {
				this.servicesManager.updateService(this.in("id"), this.in("name", safeString()), this.in("amount", bigDecimalNumber()),
						this.in("category", safeString()), this.in("description", safeString()));
				this.redirect(BASE_URL);
			} catch (DataDoesNotExistsException e) {
				this.notFound();
			}
		} else {
			this.echo();
			this.invalid(EDIT_SERVICE_TPL);
		}
	}

	/**
	 * Removes a Service
	 * 
	 * Receives the service id as <code>id</code>
	 */
	@AuthorizationRequired
	public void postRemoveService() {
		try {
			this.servicesManager.removeService(this.in("id"));
			this.redirect(BASE_URL);
		} catch (DataDoesNotExistsException e) {
			this.notFound();
		}
	}

	/**
	 * Gets all services provided by current user
	 */
	public void getUserServices() {
		this.out("services", this.servicesManager.getUserServices(this.currentUser()));
		this.success(USER_SERVICES_TPL);
	}

	/**
	 * Shows all contracts (opened, hired and provided) for current user
	 */
	public void getShowContracts() {
		this.out("providedOpenedContracts", this.servicesManager.getOpenedProvidedContracts(this.currentUser()));
		this.out("hiredOpenedContracts", this.servicesManager.getOpenedHiredContracts(this.currentUser()));
		this.out("hiredContracts", this.servicesManager.getHiredContracts(this.currentUser()));
		this.out("providedContracts", this.servicesManager.getProvidedContracts(this.currentUser()));
		this.success(LIST_CONTRACTS_TPL);
	}

	/**
	 * Shows the form tho edit a contract
	 * 
	 * Receives the contract id as <code>id</code>
	 */
	public void getEditContract() {
		try {
			ServiceContract serviceContract = this.servicesManager.getServiceContract(this.in("id"));
			this.out("contract", serviceContract);
			this.out("status", serviceContract.getStatus());
			this.success(EDIT_CONTRACT_TPL);
		} catch (DataDoesNotExistsException e) {
			this.notFound();
		}
	}

	/**
	 * Process the edit contract form.
	 * 
	 * Receives the <code>id</code>, <code>status</code>.
	 */
	public void postEditContract() {
		Validator existentContractStatusValidator = ServiceValidators.getExistentContractStatusValidator();
		if (existentContractStatusValidator.isValid()) {
			try {
				this.servicesManager.updateContractStatus(this.in("id"), ServiceContractStatus.valueOf(this.in("status", safeString())), this.currentUser());
				this.redirect(SHOW_CONTRACTS_URL);
			} catch (NotServiceProviderException e) {
				this.echo();
				this.out("exception", i18n.get(this.locales(), e.getMessage()));
				this.invalid(EDIT_CONTRACT_TPL);
			} catch (DataDoesNotExistsException e) {
				this.notFound();
			}
		} else {
			// REVIEW is it correct?
			this.jsonInvalid();
		}
	}

	/**
	 * List all services for a given category.
	 * 
	 * Receives the <code>category</code>
	 */
	public void getServicesByCategory() {
		String category = this.in("category", safeString());
		if (this.servicesManager.existsServiceCategory(category)) {
			this.out("services", this.servicesManager.getServicesByCategory(category));
			this.out("categories", this.servicesManager.getServiceCategories());
			this.out("currentCategory", category);
			this.success(LIST_SERVICE_TPL);
		} else {
			this.notFound();
		}
	}

	/**
	 * Posts the payment for a contract
	 * 
	 * Receives the contract id as <code>id</code>
	 */
	public void postPayContract() {
		// FIXME use json
		try {
			this.servicesManager.makePayment(this.in("id"), this.currentUser());
			this.redirect(SHOW_CONTRACTS_URL);
		} catch (DataDoesNotExistsException e) {
			this.notFound();
		} catch (FiggoException e) {
			this.out("exception", i18n.get(this.locales(), e.getMessage()));
			this.out("providerContracts", this.servicesManager.getProvidedContracts(this.currentUser()));
			this.out("contractorContracts", this.servicesManager.getHiredContracts(this.currentUser()));
			this.invalid(LIST_CONTRACTS_TPL);
		}
	}

	/**
	 * JSON method
	 * 
	 * Adds a new provider to a service.
	 * 
	 * Receives the service id as <code>id</code> and gets the current user to add as provider.
	 */
	@AuthorizationRequired
	public void postAddProvider() {
		try {
			this.servicesManager.addProvider(this.in("id"), this.currentUser());
			this.out("userId", this.currentUser());
			this.out("serviceId", this.in("id"));
			this.jsonSuccess();
		} catch (DataDoesNotExistsException e) {
			this.notFound();
		}
	}

	/**
	 * JSON method
	 * 
	 * Removes a provider.
	 * 
	 * Receives the service id as <code>id</code> and gets the current user to remove as provider.
	 * 
	 */
	@AuthorizationRequired
	public void postRemoveProvider() {
		try {
			this.out("service", this.servicesManager.removeProvider(this.in("id"), this.currentUser()));
			this.jsonSuccess();
		} catch (DataDoesNotExistsException e) {
			this.notFound();
		}
	}

	/**
	 * JSON method
	 * 
	 * Requests a service from a provides.
	 * 
	 * Receives the service id as <code>id</code> and the service provider as <code>provider</code>
	 */
	public void postRequestContract() {
		try {
			Validator contractValidator = ServiceValidators.getContractValidator();
			if (contractValidator.isValid()) {
				this.servicesManager.requestContract(this.in("id"), this.currentUser(), this.in("provider", safeString()));
				this.jsonSuccess();
			} else {
				this.jsonInvalid();
			}
		} catch (NotServiceProviderException e) {
			this.out("exception", i18n.get(this.locales(), e.getMessage()));
			this.jsonInvalid();
		} catch (DataDoesNotExistsException e) {
			this.notFound();
		}
	}

	/**
	 * JSON method
	 * 
	 * List categories starting with the given prefix.
	 * 
	 * Receives the category prefix as <code>term</code>
	 */
	public void getSearchCategory() {
		this.out("result", servicesManager.getCategoriesStartingWith(this.in("term", safeString())));
		jsonSuccess();
	}
}
