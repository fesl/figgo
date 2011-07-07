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
package br.octahedron.figgo.modules.users.controller;

import static br.octahedron.cotopaxi.CotopaxiProperty.APPLICATION_BASE_URL;
import static br.octahedron.cotopaxi.CotopaxiProperty.getProperty;
import static br.octahedron.cotopaxi.auth.AbstractGoogleAuthenticationInterceptor.CURRENT_USER_EMAIL;
import br.octahedron.cotopaxi.auth.AuthenticationRequired;
import br.octahedron.cotopaxi.auth.AuthenticationRequired.AuthenticationLevel;
import br.octahedron.cotopaxi.controller.Controller;
import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.cotopaxi.validation.RegexRule;
import br.octahedron.cotopaxi.validation.RequiredRule;
import br.octahedron.cotopaxi.validation.Validator;
import br.octahedron.figgo.modules.users.manager.UserManager;

/**
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public class UserController extends Controller {
	
	static final String BASE_DIR_TPL = "user/";
	static final String DASHBOARD_TPL = BASE_DIR_TPL + "dashboard.vm";
	static final String NEW_USER_TPL = BASE_DIR_TPL + "new.vm";
	static final String EDIT_USER_TPL = BASE_DIR_TPL + "edit.vm";
	
	@Inject
	private UserManager userManager;
	private Validator userValidator;
	
	private synchronized Validator getUserValidator() {
		if (this.userValidator == null) {
			this.userValidator = new Validator();
			this.userValidator.add("name", new RequiredRule(), "INVALID_NAME_MESSAGE");
			this.userValidator.add("name", new RegexRule("([a-zA-ZáéíóúÁÉÍÓÚÂÊÎÔÛâêîôûÃÕãõçÇ] *){2,}"), "INVALID_NAME_MESSAGE");
			this.userValidator.add("phoneNumber", new RequiredRule(), "INVALID_PHONE_MESSAGE");
			this.userValidator.add("phoneNumber", new RegexRule("^(([0-9]{2}|\\([0-9]{2}\\))[ ])?[0-9]{4}[-. ]?[0-9]{4}$"), "INVALID_PHONE_MESSAGE");
		}
		return this.userValidator;
	}
	
	/**
	 * @param userManager the userManager to set
	 */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	
	@AuthenticationRequired
	public void getDashboard() {
		out("user", this.userManager.getUser(in(CURRENT_USER_EMAIL)));
		success(DASHBOARD_TPL);
	}

	@AuthenticationRequired(authenticationLevel=AuthenticationLevel.AUTHENTICATE)
	public void getNew() {
		String userEmail = (String) session(CURRENT_USER_EMAIL);
		if (!this.userManager.existsUser(userEmail)) {
			out("email", userEmail);
			success(NEW_USER_TPL);
		} else {
			redirect("/edit");
		}
	}
	
	@AuthenticationRequired(authenticationLevel=AuthenticationLevel.AUTHENTICATE)
	public void postCreate() {
		Validator validator = this.getUserValidator();
		if (validator.isValid()) {
			this.userManager.createUser((String) session(CURRENT_USER_EMAIL), in("name"), in("phoneNumber"), in("description"));
			redirect(getProperty(APPLICATION_BASE_URL));
		} else {
			out("name", in("name"));
			out("phoneNumber", in("phoneNumber"));
			out("description", in("description"));
			invalid(NEW_USER_TPL);
		}
	}

	@AuthenticationRequired
	public void getEdit() {
		out("user", this.userManager.getUser(in(CURRENT_USER_EMAIL)));
		success(EDIT_USER_TPL);
	}
	
	@AuthenticationRequired
	public void putUpdate() {
		Validator validator = this.getUserValidator();
		if (validator.isValid()) {
			this.userManager.updateUser((String) session(CURRENT_USER_EMAIL), in("name"), in("phoneNumber"), in("description"));
			redirect(getProperty(APPLICATION_BASE_URL));
		} else {
			out("name", in("name"));
			out("phoneNumber", in("phoneNumber"));
			out("description", in("description"));
			invalid(EDIT_USER_TPL);
		}
	}
	
	
	/*

	def get_search() {
		def result = usersManager.getUsersStartingWith(params.term)
		renderJSON result, request, response
	}

	def get_upload() {
		request.user = usersManager.getUser(request.user.email)
		request.upload_url = blobstore.createUploadUrl('/blob/user/upload')
		render 'user/upload.vm', request, response
	}

	*/
}
