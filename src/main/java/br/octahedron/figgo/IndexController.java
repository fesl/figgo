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
package br.octahedron.figgo;

import static br.octahedron.cotopaxi.validation.Rule.Builder.required;
import br.octahedron.commons.util.Mailer;
import br.octahedron.cotopaxi.controller.Controller;
import br.octahedron.cotopaxi.validation.Validator;

/**
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public class IndexController extends Controller {

	private static final String INDEX_TPL = "index.vm";
	private static final String CONTACT_TPL = "contact.vm";
	private static final String ABOUT_TPL = "about.vm";

	/**
	 * Shows maintenance land page
	 */
	public void getIndex() {
		this.success(INDEX_TPL);
	}

	/**
	 * Shows About page
	 */
	public void getAbout() {
		success(ABOUT_TPL);
	}

	/**
	 * Shows contact
	 */
	public void getContact() {
		success(CONTACT_TPL);
	}

	/**
	 * Process contact form
	 */
	public void postContact() {
		Validator validator = getValidator();
		if (validator.isValid()) {
			Mailer.send(in("name"), in("from"), in("subject"), in("message"));
			this.out("notice", "MESSAGE_SENT");
			this.success(CONTACT_TPL);
		} else {
			this.echo();
			this.invalid(CONTACT_TPL);
		}
	}

	/*
	 * Validator stuff for contact form
	 */
	private static Validator validator;

	protected static synchronized Validator getValidator() {
		if (validator == null) {
			validator = new Validator();
			validator.add("name", required("REQUIRED_NAME"));
			validator.add("from", required("REQUIRED_EMAIL"));
			validator.add("subject", required("REQUIRED_SUBJECT"));
			validator.add("message", required("REQUIRED_MESSAGE_TYPE"));
		}
		return validator;
	}
}
