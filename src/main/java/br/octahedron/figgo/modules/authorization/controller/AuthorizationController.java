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
package br.octahedron.figgo.modules.authorization.controller;

import br.octahedron.cotopaxi.auth.AuthenticationRequired;
import br.octahedron.cotopaxi.controller.Controller;
import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.figgo.modules.authorization.manager.AuthorizationManager;
import br.octahedron.util.Log;

/**
 * @author Vítor Avelino
 *
 */
@AuthenticationRequired
public class AuthorizationController extends Controller {

	private static final String BASE_DIR_TPL = "domain/roles/";
	private static final String LIST_ROLES_TPL = BASE_DIR_TPL + "list.vm";
	private static final String LIST_USER_TPL = BASE_DIR_TPL + "users.vm";
	private static final String EDIT_USER_ROLES = BASE_DIR_TPL + "edit_user.vm";
	private static final String BASE_URL = "/domain/role";
	private static final String LIST_USER_URL = "/domain/users";
	
	@Inject
	private AuthorizationManager authorizationManager;
	
	public void setAuthorizationManager(AuthorizationManager authorizationManager) {
		this.authorizationManager = authorizationManager;
	}
	
	public void getListRoles() {
		out("roles", this.authorizationManager.getRoles(subDomain()));
		success(LIST_ROLES_TPL);
	}

	public void postUpdateRoles() {
		redirect(BASE_URL);
	}
	
	public void getListUsers() {
		// out("users", this.authorizationManager.getRoles(subDomain()));
		// out("pending", this.authorizationManager.getRoles(subDomain()));
		success(LIST_USER_TPL);
	}
	
	public void getEditUserRoles() {
		out("userRoles", this.authorizationManager.getUserRoles(currentUser()));
		out("roles", this.authorizationManager.getRoles(subDomain()));
		success(EDIT_USER_ROLES);
	}
	
	private static final Log log = new Log(AuthorizationController.class);
	
	public void postAddUserRole() {
		this.authorizationManager.addUsersToRole(subDomain(), in("role"), in("user"));
		jsonSuccess();
	}
	
	public void postRemoveUserRole() {
		this.authorizationManager.removeUserFromRole(subDomain(), in("role"), in("user"));
		jsonSuccess();
	}
	
	public void postRemoveUserRoles() {
		// this.authorizationManager.removeUserFromRoles(subDomain(), in("user"));
		jsonSuccess();
	}
	
	
}
