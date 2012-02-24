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
package br.octahedron.figgo.modules.domain.controller.validation;

import static br.octahedron.cotopaxi.validation.Rule.Builder.*;
import br.octahedron.cotopaxi.validation.Validator;

/**
 * @author Danilo Penna Queiroz - danilo.queiroz@octahedron.com.br
 */
public class DomainValidator {

	
	private static Validator domainValidator;

	public static Validator getDomainValidator() {
		if ( domainValidator == null) {
			domainValidator = new Validator();
			domainValidator.add("name", required("REQUIRED_DOMAIN_NAME"), minLength(4, "MINIMUM_LENGTH_DOMAIN_NAME"));
			domainValidator.add("url", regex("https?://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", "INVALID_URL"));
			domainValidator.add("maillist", minLength(5, "MINIMUM_LENGTH_DOMAIN_LIST"));
			// TODO validate desc (avoid html tags)
		}
		return domainValidator;
	}
}
