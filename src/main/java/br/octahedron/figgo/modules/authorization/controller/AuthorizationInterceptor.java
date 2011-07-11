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

import static br.octahedron.cotopaxi.auth.AbstractGoogleAuthenticationInterceptor.CURRENT_USER_EMAIL;
import br.octahedron.cotopaxi.auth.AbstractAuthorizationInterceptor;
import br.octahedron.cotopaxi.auth.AuthorizationRequired.NonAuthorizedConsequence;
import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.figgo.modules.authorization.manager.AuthorizationManager;

/**
 * {@link AbstractAuthorizationInterceptor} implementation for Figgo.
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public class AuthorizationInterceptor extends AbstractAuthorizationInterceptor {
	
	@Inject
	private AuthorizationManager authorizationManager;
	
	/**
	 * @param authorizationManager the authorizationManager to set
	 */
	public void setAuthorizationManager(AuthorizationManager authorizationManager) {
		this.authorizationManager = authorizationManager;
	}

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.auth.AbstractAuthorizationInterceptor#authorizeUser(java.lang.String, br.octahedron.cotopaxi.auth.AuthorizationRequired.NonAuthorizedConsequence)
	 */
	@Override
	protected void authorizeUser(String actionName, NonAuthorizedConsequence consequence, String redirectAddress) {
		String user = (String) session(CURRENT_USER_EMAIL);
		String domain = subDomain();
		if (!this.authorizationManager.isAuthorized(domain, user, actionName)) {
			if (consequence == NonAuthorizedConsequence.SET_RESTRICTED) {
				setRestricted();
			} else {
				redirect(redirectAddress);
			}
		}
	}

}
