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

import br.octahedron.cotopaxi.validation.Validator;

/**
 * @author vitoravelino
 *
 *	TODO missing validators implementation
 */
public class ServiceValidators {

	private static Validator inexistentValidator;
	private static Validator valueValidator;
	private static Validator providerValidator;
	
	/**
	 * @return
	 */
	public static synchronized Validator getInexistentValidator() {
		if (inexistentValidator == null) {
			inexistentValidator = new Validator();
		}
		return inexistentValidator;
	}

	/**
	 * @return
	 */
	public static synchronized Validator getValueValidator() {
		if (valueValidator == null) {
			valueValidator = new Validator();
		}
		return valueValidator;
	}

	/**
	 * @return
	 */
	public static synchronized Validator getProviderValidator() {
		if (providerValidator == null) {
			providerValidator = new Validator();
		}
		return providerValidator;
	}

}
