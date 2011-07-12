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

import static br.octahedron.cotopaxi.auth.AbstractAuthenticationInterceptor.CURRENT_USER_EMAIL;
import br.octahedron.cotopaxi.controller.Controller;
import br.octahedron.cotopaxi.datastore.NamespaceManager;
import br.octahedron.cotopaxi.datastore.NamespaceRequired;
import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.figgo.modules.authorization.manager.AuthorizationManager;
import br.octahedron.figgo.modules.configuration.manager.ConfigurationManager;

/**
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
@NamespaceRequired
public class IndexController extends Controller {

	@Inject
	private ConfigurationManager configurationManager;
	@Inject
	private AuthorizationManager authorizationManager;
	@Inject
	private NamespaceManager namespaceManager;

	public void setNamespaceManager(NamespaceManager namespaceManager) {
		this.namespaceManager = namespaceManager;
	}
	public void setConfigurationManager(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}
	
	public void setAuthorizationManager(AuthorizationManager authorizationManager) {
		this.authorizationManager = authorizationManager;
	}
	
	public void getIndex() {
		if (serverName().equalsIgnoreCase("www.figgo.com.br") || serverName().equalsIgnoreCase("localhost")) {
			redirect("/dashboard");
		} else {
			boolean userExists;
			out("domain", this.configurationManager.getDomainConfiguration());
			try {
				namespaceManager.changeToGlobalNamespace();
				userExists = this.authorizationManager.getUserDomains((String) session(CURRENT_USER_EMAIL)).contains(subDomain());
			} finally {
				namespaceManager.changeToPreviousNamespace();
			}

			if (userExists) {
				success("domain/index.vm");
			} else {
				success("domain/public_index.vm");
			}
		}
	}
}
