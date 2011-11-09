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

import static br.octahedron.cotopaxi.controller.Converter.Builder.number;

import java.math.BigDecimal;

import br.octahedron.cotopaxi.auth.AuthenticationRequired;
import br.octahedron.cotopaxi.auth.AuthorizationRequired;
import br.octahedron.cotopaxi.controller.Controller;
import br.octahedron.cotopaxi.controller.ConvertionException;
import br.octahedron.cotopaxi.controller.converter.NumberConverter.NumberType;
import br.octahedron.cotopaxi.datastore.namespace.NamespaceRequired;
import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.cotopaxi.validation.Validator;
import br.octahedron.figgo.modules.service.controller.validation.ServiceValidators;
import br.octahedron.figgo.modules.service.data.Service;
import br.octahedron.figgo.modules.service.data.ServiceContract.ServiceContractStatus;
import br.octahedron.figgo.modules.service.manager.ServiceManager;

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
	
	@Inject
	private ServiceManager servicesManager;
	
	public void setServiceManager(ServiceManager serviceManager) {
		this.servicesManager = serviceManager;
	}
	
	@AuthorizationRequired
	public void getListServices() {
		out("services", this.servicesManager.getServices());
		success(LIST_SERVICE_TPL);
	}
	
	@AuthorizationRequired
	public void getShowService() throws ConvertionException {
		Service service = this.servicesManager.getService((Long) in("id", number(NumberType.LONG)));
		if (service != null) {
			out("service", service);
			success(SHOW_SERVICE_TPL);
		} else {
			notFound();
		}
	}
	
	@AuthorizationRequired
	public void getNewService() {
		success(NEW_SERVICE_TPL);
	}
	
	@AuthorizationRequired
	public void postNewService() throws ConvertionException {
		Validator inexistentValidator = ServiceValidators.getInexistentValidator();
		Validator valueValidator = ServiceValidators.getValueValidator();
		if (inexistentValidator.isValid() && valueValidator.isValid()) {
			this.servicesManager.createService(in("name"), (BigDecimal) in("value", number(NumberType.BIG_DECIMAL)), in("category"), in("description"));
			redirect(BASE_URL);
		} else {
			echo();
			invalid(NEW_SERVICE_TPL);
		}
	}
	
	@AuthorizationRequired
	public void getEditService() throws ConvertionException {
		Service service = this.servicesManager.getService((Long) in("id", number(NumberType.LONG)));
		out("id", service.getId());
		out("name", service.getName());
		out("value", service.getAmount());
		out("category", service.getCategory());
		out("description", service.getDescription());
		success(EDIT_SERVICE_TPL);
	}
	
	@AuthorizationRequired
	public void postEditService() throws ConvertionException {
		Validator inexistentValidator = ServiceValidators.getInexistentValidator();
		Validator valueValidator = ServiceValidators.getValueValidator();
		if (inexistentValidator.isValid() && valueValidator.isValid()) {
			this.servicesManager.updateService((Long) in("id", number(NumberType.LONG)), in("name"), (BigDecimal) in("value", number(NumberType.BIG_DECIMAL)), in("category"), in("description"));
			redirect(BASE_URL);
		} else {
			echo();
			invalid(EDIT_SERVICE_TPL);
		}
	}
	
	@AuthorizationRequired
	public void postAddProvider() throws ConvertionException {
		Service service = this.servicesManager.getService((Long) in("id", number(NumberType.LONG)));
		String userId = this.currentUser();
		Validator inexistentValidator = ServiceValidators.getInexistentValidator();
		if (inexistentValidator.isValid()) {
			service.addProvider(this.currentUser());
			out("html", "<li id=\"" + userId.split("@")[0] +"\">" + userId + "- <a class=\"contract-link\" href=\"/service/" + service.getId() + "/contract/" + userId + "\">contratar</a></li>");
			jsonSuccess();
		} else {
			jsonInvalid();
		}
	}
	
	@AuthorizationRequired
	public void postRemoveProvider() throws ConvertionException {
		Validator inexistentValidator = ServiceValidators.getInexistentValidator();
		if (inexistentValidator.isValid()) {
			out("service", this.servicesManager.removeProvider((Long) in("id", number(NumberType.LONG)), currentUser()));
			jsonSuccess();
		} else {
			jsonInvalid();
		}
	}
	
	@AuthorizationRequired
	public void postRemoveService() throws ConvertionException {
		Validator inexistentValidator = ServiceValidators.getInexistentValidator();
		if (inexistentValidator.isValid()) {
			this.servicesManager.removeService((Long) in("id", number(NumberType.LONG)));
			jsonSuccess();
		} else {
			jsonInvalid();
		}
	}
	
	public void getUserServices() {
		out("services", this.servicesManager.getUserServices(this.currentUser()));
		success(USER_SERVICES_TPL);
	}
	
	public void getShowContracts() {
		out("providerContracts", this.servicesManager.getProviderContracts(this.currentUser()));
		out("contractorContracts", this.servicesManager.getContractorContracts(this.currentUser()));
		success(LIST_CONTRACTS_TPL);
	}
	
	public void postRequestContract() throws ConvertionException {
		Validator inexistentValidator = ServiceValidators.getInexistentValidator();
		Validator providerValidator = ServiceValidators.getProviderValidator();
		if (inexistentValidator.isValid() && providerValidator.isValid()) {
			this.servicesManager.requestContract((Long) in("id", number(NumberType.LONG)), this.currentUser(), in("provider"));
			jsonSuccess();
		} else {
			jsonInvalid();
		}
	}
	
	public void getShowHistory() {
		out("contracts", this.servicesManager.getContractsHistory(currentUser()));
		success(LIST_CONTRACTS_TPL);
	}
	
	public void getEditContract() throws ConvertionException {
		Validator inexistentValidator = ServiceValidators.getInexistentValidator();
		if (inexistentValidator.isValid()) {
			out("contract", this.servicesManager.getServiceContract((Long) in("id", number(NumberType.LONG))));
			success(EDIT_CONTRACT_TPL);
		} else {
			invalid(EDIT_CONTRACT_TPL);
		}
	}
	
	public void postUpdateContract() throws ConvertionException {
		Validator inexistentValidator = ServiceValidators.getInexistentValidator();
		Validator providerValidator = ServiceValidators.getProviderValidator();
		if (inexistentValidator.isValid() && providerValidator.isValid()) {
			this.servicesManager.updateContractStatus((Long) in("id", number(NumberType.LONG)), ServiceContractStatus.valueOf(in("status")));
			redirect(SHOW_CONTRACTS_URL);
		} else {
			jsonInvalid();
		}
	}
	
	public void postPayContract() throws ConvertionException {
		// todo validator
		this.servicesManager.makePayment((Long) in("id", number(NumberType.LONG)));
	}
}
