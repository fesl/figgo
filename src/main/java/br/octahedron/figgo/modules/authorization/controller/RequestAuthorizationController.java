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
import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.figgo.OnlyForNamespaceControllerInterceptor.OnlyForNamespace;
import br.octahedron.figgo.modules.authorization.manager.AuthorizationManager;

/**
 * @author Danilo Queiroz
 */
@AuthenticationRequired
@OnlyForNamespace
public class RequestAuthorizationController extends Controller {
	
	private static final String BASE_DIR_TPL = "domain/roles/";
	private static final String LIST_USER_TPL = BASE_DIR_TPL + "users.vm";
	private static final String BASE_USERS_URL = "/users";
	
	@Inject
	private AuthorizationManager authorizationManager;

	public void setAuthorizationManager(AuthorizationManager authorizationManager) {
		this.authorizationManager = authorizationManager;
	}
	

	/**
	 * GET /users
	 */
	public void getListUsers() {
		// TODO verificar se é subdomínio válido
		this.out("users", this.authorizationManager.getActiveUsers(this.subDomain()));
		this.out("pending", this.authorizationManager.getNonActiveUsers(this.subDomain()));
		this.success(LIST_USER_TPL);
	}
	
	/**
	 * GET /users.json
	 */
	public void getListUsersJSON() {
		// TODO verificar se é subdomínio válido
		this.out("active", this.authorizationManager.getActiveUsers(this.subDomain()));
		this.out("pending", this.authorizationManager.getNonActiveUsers(this.subDomain()));
		this.jsonSuccess();
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
	@AuthorizationRequired
	public void postAcceptRequestUser() {
		// TODO verificar se é subdomínio válido
		this.authorizationManager.activateDomainUser(this.subDomain(), this.in("user"));
		this.redirect(BASE_USERS_URL);
	}

	/**
	 * POST /user/request/reject
	 */
	@AuthorizationRequired
	public void postRejectRequestUser() {
		// TODO verificar se é subdomínio válido
		this.authorizationManager.removeDomainUser(this.subDomain(), this.in("user"));
		this.redirect(BASE_USERS_URL);
	}
}
