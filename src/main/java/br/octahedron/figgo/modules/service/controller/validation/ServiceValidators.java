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
package br.octahedron.figgo.modules.service.controller.validation;

import static br.octahedron.cotopaxi.validation.Rule.Builder.required;
import br.octahedron.cotopaxi.validation.Validator;
import br.octahedron.figgo.modules.bank.controller.validation.AmountRule;

/**
 * @author vitoravelino
 *
 *	TODO missing validators implementation
 */
public class ServiceValidators {

	private static Validator serviceValidator;
	private static Validator existentContractStatusValidator;
	private static Validator providerValidator;
	private static Validator existentContractValidator;
	private static Validator existentServiceValidator;
	
	/**
	 * @return
	 */
	public static synchronized Validator getServiceValidator() {
		if (serviceValidator == null) {
			serviceValidator = new Validator();
			serviceValidator.add("name", required("REQUIRED_SERVICE_NAME"));
			serviceValidator.add("amount", required("REQUIRED_SERVICE_AMOUNT"), new AmountRule());
			serviceValidator.add("category", required("REQUIRED_SERVICE_CATEGORY"));
		}
		return serviceValidator;
	}

	public static synchronized Validator getExistentServiceValidator() {
		if (existentServiceValidator == null) {
			existentServiceValidator = new Validator();
			existentServiceValidator.add("id", required("REQUIRED_SERVICE_ID"), new ExistentServiceRule());
		}
		return existentServiceValidator;
	}

	/**
	 * @return
	 */
	public static synchronized Validator getExistentContractValidator() {
		if (existentContractValidator == null) {
			existentContractValidator = new Validator();
			existentContractValidator.add("id", required("REQUIRED_CONTRACT_ID"), new ExistentContractRule());
		}
		return existentContractValidator;
	}
	
	
	public static synchronized Validator getExistentContractStatusValidator() {
		if (existentContractStatusValidator == null) {
			existentContractStatusValidator = new Validator();
			existentContractStatusValidator.add("status", required("REQUIRED_CONTRACT_STATUS"), new ExistentContractStatusRule());
		}
		return existentContractStatusValidator;
	}

	/**
	 * @return
	 */
	public static Validator getProviderValidator() {
		if (providerValidator == null) {
			providerValidator = new Validator();
			providerValidator.add("provider", required("REQUIRED_CONTRACT_PROVIDER"), new ExistentContractProviderRule());
		}
		return providerValidator;
	}


}
