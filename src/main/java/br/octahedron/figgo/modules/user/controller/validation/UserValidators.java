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

import static br.octahedron.cotopaxi.validation.Rule.Builder.*;

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
			userValidator.add("name", required("REQUIRED_USER_NAME"), regex("([a-zA-ZáéíóúÁÉÍÓÚÂÊÎÔÛâêîôûÃÕãõçÇ] *){2,}", "INVALID_USER_NAME"));
			userValidator.add("phoneNumber", required("REQUIRED_USER_PHONE"), regex("^(([0-9]{2}|\\([0-9]{2}\\))[ ])?[0-9]{4}[-. ]?[0-9]{4}$", "INVALID_USER_PHONE"));
		}
		return userValidator;
	}
	
}
