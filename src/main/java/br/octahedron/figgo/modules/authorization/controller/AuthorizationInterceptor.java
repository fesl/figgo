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

import br.octahedron.cotopaxi.auth.AbstractAuthorizationInterceptor;
import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.figgo.modules.authorization.manager.AuthorizationManager;
import br.octahedron.util.Log;

/**
 * {@link AbstractAuthorizationInterceptor} implementation for Figgo.
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public class AuthorizationInterceptor extends AbstractAuthorizationInterceptor {
	
	private final Log log = new Log(AuthorizationInterceptor.class);
	
	@Inject
	private AuthorizationManager authorizationManager;
	
	/**
	 * @param authorizationManager the authorizationManager to set
	 */
	public void setAuthorizationManager(AuthorizationManager authorizationManager) {
		this.authorizationManager = authorizationManager;
	}

	@Override
	protected void authorizeUser(String actionName, String currentUser, boolean showForbiddenPage) {
		String user = this.currentUser();
		String domain = subDomain();
		if (user!= null && !this.authorizationManager.isAuthorized(domain, user, actionName)) {
			log.debug("User %s is not authorized to perform action %s on domain %s", user, actionName, domain);
			if( showForbiddenPage) { 
				forbidden();
			}
		} else {
			this.authorized();
			log.debug("User %s is authorized to perform action %s on domain %s", user, actionName, domain);
		}		
	}
}
