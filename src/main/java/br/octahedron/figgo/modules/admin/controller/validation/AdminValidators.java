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
package br.octahedron.figgo.modules.admin.controller.validation;

import static br.octahedron.cotopaxi.validation.Rule.Builder.*;
import br.octahedron.cotopaxi.validation.Validator;

/**
 * @author vitoravelino
 */
public class AdminValidators {

	private static Validator domainValidator;
	private static Validator configValidator;

	/**
	 * 	Validator for domain creation input
	 */
	public static synchronized Validator getDomainValidator() {
		if (domainValidator == null) {
			domainValidator = new Validator();
			domainValidator.add("name", new InexistentDomainRule());
		}
		return domainValidator;
	}
	
	/**
	 * Validator for admin configuration input
	 */
	public static synchronized Validator getConfigValidator() {
		if (configValidator == null) {
			configValidator = new Validator();
			configValidator.add("accessKey", required("FIELD_REQUIRED"));
			configValidator.add("keySecret", required("FIELD_REQUIRED"));
			configValidator.add("zone", required("FIELD_REQUIRED"));
		}
		return domainValidator;
	}
}

