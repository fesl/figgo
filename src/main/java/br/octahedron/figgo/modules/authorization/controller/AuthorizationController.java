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
import br.octahedron.cotopaxi.auth.AuthorizationRequired;
import br.octahedron.cotopaxi.controller.Controller;
import static br.octahedron.cotopaxi.controller.Converter.Builder.*;
import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.figgo.modules.authorization.manager.AuthorizationManager;

/**
 * @author Vítor Avelino
 * 
 */
@AuthenticationRequired
@AuthorizationRequired
public class AuthorizationController extends Controller {

	private static final String BASE_DIR_TPL = "domain/roles/";
	private static final String LIST_ROLES_TPL = BASE_DIR_TPL + "roles.vm";
	private static final String LIST_USER_TPL = BASE_DIR_TPL + "users.vm";
	private static final String EDIT_USERS_ROLES_TPL = BASE_DIR_TPL + "users_edit.vm";
	private static final String NEW_ROLE_TPL = BASE_DIR_TPL + "role_new.vm";
	private static final String EDIT_ROLE_TPL = BASE_DIR_TPL + "role_edit.vm";
	private static final String BASE_ROLES_URL = "/roles";
	private static final String BASE_USERS_URL = "/users";

	@Inject
	private AuthorizationManager authorizationManager;

	public void setAuthorizationManager(AuthorizationManager authorizationManager) {
		this.authorizationManager = authorizationManager;
	}

	public void getListRoles() {
		this.out("roles", this.authorizationManager.getRoles(this.subDomain()));
		this.out("activities", this.authorizationManager.getAcitivities());
		this.success(LIST_ROLES_TPL);
	}

	public void postAddRoleActivity() {
		this.authorizationManager.addActivitiesToRole(this.subDomain(), this.in("role"), this.in("activity"));
		this.jsonSuccess();
	}

	public void postRemoveRoleActivity() {
		this.authorizationManager.removeActivitiesToRole(this.subDomain(), this.in("role"), this.in("activity"));
		this.jsonSuccess();
	}

	public void postAddUserRole() {
		this.authorizationManager.addUsersToRole(this.subDomain(), this.in("role"), this.in("user"));
		this.jsonSuccess();
	}

	public void postRemoveUserRole() {
		this.authorizationManager.removeUserFromRole(this.subDomain(), this.in("role"), this.in("user"));
		this.jsonSuccess();
	}

	/**
	 * /roles/user/del
	 * 
	 * Remove all roles of user
	 */
	public void postRemoveUserRoles() {
		this.authorizationManager.removeUserFromRoles(this.subDomain(), this.in("user"));
		this.jsonSuccess();
	}

	public void getNewRole() {
		this.out("activities", this.authorizationManager.getAcitivities());
		this.success(NEW_ROLE_TPL);
	}

	public void postNewRole() {
		this.authorizationManager.createRole(this.subDomain(), this.in("name"), this.values("activities"));
		this.redirect(BASE_ROLES_URL);
	}

	public void getEditRole() {
		this.out("activities", this.authorizationManager.getAcitivities());
		this.out("role", this.authorizationManager.getRole(this.subDomain(), this.in("role")));
		this.success(EDIT_ROLE_TPL);
	}

	public void postEditRole() {
		this.authorizationManager.updateRoleActivities(this.subDomain(), this.in("role"), this.values("activities"));
		this.redirect(BASE_ROLES_URL);
	}

	public void postRemoveRole() {
		this.authorizationManager.removeRole(this.subDomain(), this.in("role"));
		this.redirect(BASE_ROLES_URL);
	}
	
	/**
	 *	GET /users
	 */
	public void getListUsers() {
		// TODO verificar se é subdomínio válido
		this.out("users", this.authorizationManager.getActiveUsers(this.subDomain()));
		this.out("pending", this.authorizationManager.getNonActiveUsers(this.subDomain()));
		this.success(LIST_USER_TPL);
	}
	
	/**
	 *	GET /roles/users
	 */
	public void getListUsersAndRoles() {
		// TODO verificar se é subdomínio válido
		this.out("roles", this.authorizationManager.getRoles(this.subDomain()));
		this.out("users", this.authorizationManager.getActiveUsers(this.subDomain()));
		this.success(EDIT_USERS_ROLES_TPL);
	}
	
	/**
	 * POST /user/request
	 */
	public void postRequestUser() {
		// TODO verificar se é subdomínio válido
		this.authorizationManager.createDomainUser(this.subDomain(), this.currentUser(), false);
		this.redirect("/");
	}
	
	/**
	 * POST /user/request/accept
	 */
	public void postAcceptRequestUser() {
		// TODO verificar se é subdomínio válido
		this.authorizationManager.activateDomainUser(this.subDomain(), this.in("user"));
		this.redirect(BASE_USERS_URL);
	}
	
	/**
	 * POST /user/request/reject
	 */
	public void postRejectRequestUser() {
		// TODO verificar se é subdomínio válido
		this.authorizationManager.removeDomainUser(this.subDomain(), this.in("user"));
		this.redirect(BASE_USERS_URL);
	}
	
	public void getUserRoles() {
		this.out("result", this.authorizationManager.getUsersRoles(this.subDomain(), this.in("users", strArray(","))));
		this.jsonSuccess();
	}
	
}
