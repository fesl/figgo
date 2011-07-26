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
package br.octahedron.figgo.modules.user.controller.validation;

import br.octahedron.cotopaxi.validation.RegexRule;
import br.octahedron.cotopaxi.validation.RequiredRule;
import br.octahedron.cotopaxi.validation.Validator;

/**
 * @author vitoravelino
 *
 */
public class UserValidators {

	private static Validator userValidator;
	
	public static synchronized Validator getUserValidator() {
		if (userValidator == null) {
			userValidator = new Validator();
			userValidator.add("name", new RequiredRule(), "REQUIRED_USER_NAME_MESSAGE");
			userValidator.add("name", new RegexRule("([a-zA-ZáéíóúÁÉÍÓÚÂÊÎÔÛâêîôûÃÕãõçÇ] *){2,}"), "INVALID_USER_NAME_MESSAGE");
			userValidator.add("phoneNumber", new RequiredRule(), "REQUIRED_USER_PHONE_MESSAGE");
			userValidator.add("phoneNumber", new RegexRule("^(([0-9]{2}|\\([0-9]{2}\\))[ ])?[0-9]{4}[-. ]?[0-9]{4}$"), "INVALID_USER_PHONE_MESSAGE");
		}
		return userValidator;
	}
	
}
