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
import br.octahedron.figgo.modules.service.controller.validation.ServiceValidators;
import br.octahedron.figgo.modules.service.data.Service;
import br.octahedron.figgo.modules.service.data.ServiceContract.ServiceContractStatus;
import br.octahedron.figgo.modules.service.manager.ServiceManager;
import br.octahedron.figgo.modules.service.manager.exception.OnlyProviderException;
import br.octahedron.figgo.modules.service.manager.exception.ProviderDoesNotExistException;

/**
 * @author Vítor Avelino
 * 
 */
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

	@AuthorizationRequired
	public void getListServices() {
		this.out("services", this.servicesManager.getServices());
		this.success(LIST_SERVICE_TPL);
	}

	@AuthorizationRequired
	public void getShowService() {
		Service service = this.servicesManager.getService(this.in("id"));
		if (service != null) {
			this.out("service", service);
			this.success(SHOW_SERVICE_TPL);
		} else {
			this.notFound();
		}
	}

	@AuthorizationRequired
	public void getNewService() {
		this.success(NEW_SERVICE_TPL);
	}

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

	@AuthorizationRequired
	public void getEditService() {
		Service service = this.servicesManager.getService(this.in("id"));
		this.out("id", service.getId());
		this.out("name", service.getName());
		this.out("amount", service.getAmount());
		this.out("category", service.getCategory());
		this.out("description", service.getDescription());
		this.success(EDIT_SERVICE_TPL);
	}

	@AuthorizationRequired
	public void postEditService() {
		Validator validator = ServiceValidators.getServiceValidator();
		if (validator.isValid()) {
			this.servicesManager.updateService(this.in("id"), this.in("name", safeString()), this.in("amount", bigDecimalNumber()),
					this.in("category", safeString()), this.in("description", safeString()));
			this.redirect(BASE_URL);
		} else {
			this.echo();
			this.invalid(EDIT_SERVICE_TPL);
		}
	}

	@AuthorizationRequired
	public void postAddProvider() {
		Service service = this.servicesManager.getService(this.in("id"));
		Validator existentServiceValidator = ServiceValidators.getExistentServiceValidator();
		if (existentServiceValidator.isValid()) {
			service.addProvider(this.currentUser());
			this.out("userId", this.currentUser());
			this.out("serviceId", this.in("id"));
			this.jsonSuccess();
		} else {
			this.jsonInvalid();
		}
	}

	@AuthorizationRequired
	public void postRemoveProvider() {
		Validator existentServiceValidator = ServiceValidators.getExistentServiceValidator();
		if (existentServiceValidator.isValid()) {
			this.out("service", this.servicesManager.removeProvider(this.in("id"), this.currentUser()));
			this.jsonSuccess();
		} else {
			this.jsonInvalid();
		}
	}

	@AuthorizationRequired
	public void postRemoveService() {
		Validator existentServiceValidator = ServiceValidators.getExistentServiceValidator();
		if (existentServiceValidator.isValid()) {
			this.servicesManager.removeService(this.in("id"));
			this.jsonSuccess();
		} else {
			this.jsonInvalid();
		}
	}

	public void getUserServices() {
		this.out("services", this.servicesManager.getUserServices(this.currentUser()));
		this.success(USER_SERVICES_TPL);
	}

	public void getShowContracts() {
		this.out("providerContracts", this.servicesManager.getProviderContracts(this.currentUser()));
		this.out("contractorContracts", this.servicesManager.getContractorContracts(this.currentUser()));
		this.success(LIST_CONTRACTS_TPL);
	}

	public void postRequestContract() {
		Validator existentServiceValidator = ServiceValidators.getExistentServiceValidator();
		if (existentServiceValidator.isValid()) {
			try {
				this.servicesManager.requestContract(this.in("id"), this.currentUser(), this.in("provider"));
				this.jsonSuccess();
			} catch (ProviderDoesNotExistException e) {
				this.out("exception", i18n.get(this.locales(), e.getMessage()));
				this.jsonInvalid();
			}
		} else {
			this.jsonInvalid();
		}
	}

	public void getShowHistory() {
		this.out("contracts", this.servicesManager.getContractsHistory(this.currentUser()));
		this.success(LIST_CONTRACTS_TPL);
	}

	public void getEditContract() {
		Validator contractsValidator = ServiceValidators.getExistentContractValidator();
		if (contractsValidator.isValid()) {
			this.out("contract", this.servicesManager.getServiceContract(this.in("id")));
			success(EDIT_CONTRACT_TPL);
		} else {
			this.invalid(EDIT_CONTRACT_TPL);
		}
	}

	public void postUpdateContract() {
		Validator existentContractValidator = ServiceValidators.getExistentContractValidator();
		Validator existentContractStatusValidator = ServiceValidators.getExistentContractStatusValidator();
		if (existentContractValidator.isValid() && existentContractStatusValidator.isValid()) {
			try {
				this.servicesManager.updateContractStatus(this.in("id"), ServiceContractStatus.valueOf(this.in("status")), this.currentUser());
				this.redirect(SHOW_CONTRACTS_URL);
			} catch (OnlyProviderException e) {
				this.out("warning", i18n.get(this.locales(), e.getMessage()));
				this.jsonInvalid();
			}
		} else {
			this.jsonInvalid();
		}
	}

	public void postPayContract() {
		Validator contractsValidator = ServiceValidators.getExistentContractValidator();
		if (contractsValidator.isValid()) {
			try {
				this.servicesManager.makePayment(this.in("id"), this.currentUser());
				this.jsonSuccess();
			} catch (FiggoException e) {
				this.out("exception", i18n.get(this.locales(), e.getMessage()));
				this.jsonInvalid();
			}
		} else {
			this.jsonInvalid();
		}
	}
}
