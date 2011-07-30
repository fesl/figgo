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

/**
 * @author Vítor Avelino
 *
 */
@AuthenticationRequired
public class AuthorizationController extends Controller {

	private static final String BASE_DIR_TPL = "domain/roles/";
	private static final String LIST_ROLES_TPL = BASE_DIR_TPL + "roles.vm";
	private static final String LIST_USER_TPL = BASE_DIR_TPL + "users.vm";
	private static final String EDIT_USER_ROLES = BASE_DIR_TPL + "user_edit.vm";
	private static final String NEW_ROLE_TPL = BASE_DIR_TPL + "role_new.vm";
	private static final String EDIT_ROLE_TPL = BASE_DIR_TPL + "role_edit.vm";
	private static final String BASE_URL = "/roles";
	
	@Inject
	private AuthorizationManager authorizationManager;
	
	public void setAuthorizationManager(AuthorizationManager authorizationManager) {
		this.authorizationManager = authorizationManager;
	}
	
	public void getListRoles() {
		out("roles", this.authorizationManager.getRoles(subDomain()));
		out("activities", this.authorizationManager.getAcitivities());
		success(LIST_ROLES_TPL);
	}

	public void postUpdateRoles() {
		redirect(BASE_URL);
	}
	
	public void getListUsers() {
		// out("users", this.authorizationManager.getUsersFromDomain(subDomain()));
		// out("pending", this.authorizationManager.getRoles(subDomain()));
		success(LIST_USER_TPL);
	}
	
	public void getEditUserRoles() {
		out("userRoles", this.authorizationManager.getUserRoles(subDomain(), currentUser()));
		out("roles", this.authorizationManager.getRoles(subDomain()));
		success(EDIT_USER_ROLES);
	}
	
	public void postAddUserRole() {
		this.authorizationManager.addUsersToRole(subDomain(), in("role"), in("user"));
		jsonSuccess();
	}
	
	public void postRemoveUserRole() {
		this.authorizationManager.removeUserFromRole(subDomain(), in("role"), in("user"));
		jsonSuccess();
	}
	
	public void postRemoveUserRoles() {
		this.authorizationManager.removeUserFromRoles(subDomain(), in("user"));
		jsonSuccess();
	}
	
	public void getNewRole() {
		out("activities", this.authorizationManager.getAcitivities());
		success(NEW_ROLE_TPL);
	}
	
	public void postNewRole() {
		this.authorizationManager.createRole(subDomain(), in("name"), values("activities"));
		redirect(BASE_URL);
	}
	
	public void getEditRole() {
		out("activities", this.authorizationManager.getAcitivities());
		out("role", this.authorizationManager.getRole(subDomain(), in("role")));
		success(EDIT_ROLE_TPL);
	}
	
	public void postEditRole() {
		this.authorizationManager.updateRoleActivities(subDomain(), in("role"), values("activities"));
		redirect(BASE_URL);
	}
	
	public void postRemoveRole() {
		this.authorizationManager.removeRole(subDomain(), in("role"));
		jsonSuccess();
	}
}
