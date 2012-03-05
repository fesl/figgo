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
import br.octahedron.cotopaxi.controller.Converter;
import br.octahedron.cotopaxi.datastore.namespace.NamespaceManager;
import br.octahedron.cotopaxi.datastore.namespace.NamespaceRequired;
import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.figgo.OnlyForNamespaceControllerInterceptor.OnlyForNamespace;
import br.octahedron.figgo.modules.authorization.manager.AuthorizationManager;
import br.octahedron.figgo.util.SafeStringConverter;

/**
 * @author Vítor Avelino
 * 
 */
@AuthenticationRequired
@AuthorizationRequired
@NamespaceRequired
@OnlyForNamespace
public class AuthorizationController extends Controller {
	
	// TODO move this converter to cotopaxi.
	private static Converter<String> safeStringConverter = new SafeStringConverter();

	private static final String BASE_DIR_TPL = "domain/roles/";
	private static final String LIST_ROLES_TPL = BASE_DIR_TPL + "roles.vm";
	private static final String EDIT_USERS_ROLES_TPL = BASE_DIR_TPL + "users_edit.vm";
	private static final String BASE_ROLES_URL = "/roles";

	@Inject
	private AuthorizationManager authorizationManager;

	public void setAuthorizationManager(AuthorizationManager authorizationManager) {
		this.authorizationManager = authorizationManager;
	}

	/**
	 * Show the roles page with all roles
	 */
	public void getListRoles() {
		this.out("roles", this.authorizationManager.getRoles());
		this.out("activities", this.authorizationManager.getActivities());
		this.success(LIST_ROLES_TPL);
	}

	/**
	 * Creates a new role
	 */
	public void postNewRole() {
		this.authorizationManager.createRole(this.in("name", safeStringConverter), this.values("activities"));
		this.redirect(BASE_ROLES_URL);
	}

	/**
	 * Removes an role
	 */
	public void postRemoveRole() {
		this.authorizationManager.removeRole(this.in("role", safeStringConverter));
		this.redirect(BASE_ROLES_URL);
	}

	/**
	 * Adds an user to a role
	 * 
	 * <b>AjaxMethod</b>
	 */
	public void postAddUserRole() {
		this.authorizationManager.addUsersToRole(this.in("role", safeStringConverter), this.in("user", safeStringConverter));
		this.jsonSuccess();
	}

	/**
	 * Removes a role from an user
	 * 
	 * <b>AjaxMethod</b>
	 */
	public void postRemoveUserRole() {
		this.authorizationManager.removeUserFromRole(this.in("role", safeStringConverter), this.in("user", safeStringConverter), this.subDomain());
		this.jsonSuccess();
	}

	/**
	 * /roles/user/del
	 * 
	 * Remove all roles of user
	 * 
	 * <b>AjaxMethod</b>
	 */
	public void postRemoveUserRoles() {
		this.authorizationManager.removeUserFromRoles(this.in("user", safeStringConverter), this.subDomain());
		this.jsonSuccess();
	}

	/**
	 * Add activity to a role.
	 * 
	 * <b>AjaxMethod</b>
	 */
	public void postAddRoleActivity() {
		this.authorizationManager.addActivitiesToRole(this.in("role", safeStringConverter), this.in("activity", safeStringConverter));
		this.jsonSuccess();
	}

	/**
	 * Remove an activity from a role
	 * 
	 * <b>AjaxMethod</b>
	 */
	public void postRemoveRoleActivity() {
		this.authorizationManager.removeActivitiesToRole(this.in("role", safeStringConverter), this.in("activity", safeStringConverter));
		this.jsonSuccess();
	}

	/**
	 * Get given users roles.
	 * 
	 * <b>AjaxMethod</b>
	 * 
	 * REVIEW This action is not mapped and action isn't added to spec
	 */
//	public void getUserRoles() {
//		this.out("result", this.authorizationManager.getUsersRoles(this.in("users", strArray(","))));
//		this.jsonSuccess();
//	}

	// REVIEW change how this works - possible using async/ajax call
	@Inject
	private NamespaceManager namespaceManager;
	
	/**
	 * @param namespaceManager the namespaceManager to set
	 */
	public void setNamespaceManager(NamespaceManager namespaceManager) {
		this.namespaceManager = namespaceManager;
	}

	/**
	 * GET /roles/users
	 */
	public void getListUsersAndRoles() {
		// TODO verificar se é subdomínio válido
		this.out("roles", this.authorizationManager.getRoles());
		// This called should occurs into global namespace
		this.namespaceManager.changeToGlobalNamespace();
		this.out("users", this.authorizationManager.getActiveUsers(this.subDomain()));
		this.namespaceManager.changeToPreviousNamespace();
		this.success(EDIT_USERS_ROLES_TPL);
	}
}
