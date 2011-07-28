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
package br.octahedron.figgo.modules.bank.controller.validation;

import br.octahedron.cotopaxi.validation.RequiredRule;
import br.octahedron.cotopaxi.validation.Validator;

/**
 * @author Vítor Avelino
 *
 */
public class BankValidators {
	
	private static Validator destinationValidator;
	private static Validator valueValidator;
	private static Validator requiredValidator;
	
	/**
	 * A validator for transfers that checks the required fields
	 */
	public static synchronized Validator getRequiredValidator() {
		if (requiredValidator == null) {
			requiredValidator = new Validator();
			requiredValidator.add("userId", new RequiredRule(), "REQUIRED_TRANSASCTION_USERID_MESSAGE");
			requiredValidator.add("amount", new RequiredRule(), "REQUIRED_TRANSACTION_AMOUT_MESSAGE");
			requiredValidator.add("type", new RequiredRule(), "REQUIRED_TRANSACTION_TYPE_MESSAGE");
		}
		return requiredValidator;
	}
	
	/**
	 * A validator that check if the destination account exists and has authorization at the specified domain
	 */
	public static synchronized Validator getDestinationValidator() {
		if (destinationValidator == null) {
			destinationValidator = new Validator();
			destinationValidator.add("userId", new DestinationRule(), "INVALID_TRANSACTION_DESTINATION_MESSAGE");
		}
		return destinationValidator;
	}
	
	/**
	 * A validator that check if the transfer's amount is positive 
	 */
	public static synchronized Validator getAmountValidator() {
		if (valueValidator == null) {
			valueValidator = new Validator();
			valueValidator.add("amount", new AmountRule(), "INVALID_TRANSACTION_AMOUNT_MESSAGE");
		}
		return valueValidator;
	}
	
}
