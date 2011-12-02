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

import br.octahedron.cotopaxi.datastore.namespace.NamespaceManager;
import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.cotopaxi.inject.SelfInjectable;
import br.octahedron.cotopaxi.interceptor.TemplateInterceptor;
import br.octahedron.cotopaxi.view.response.TemplateResponse;
import br.octahedron.figgo.modules.authorization.manager.AuthorizationManager;


/**
 * A {@link TemplateIntercetor} that adds an AuthorizationHelper to 
 * view. 
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public class AuthorizationHelperTemplateInterceptor extends TemplateInterceptor {

    /*
     * (non-Javadoc)
     */
    @Override
    public void preRender(TemplateResponse response) {
        response.addOutput("auth", new AuthorizationHelper(this.currentUser()));
    }

    /**
     * This class encapsulates the authorization manager and provides a helper
     * method to be used by the view to check user authorizations.
     * 
     * When using velocity, the view can checks if an user has authorization for
     * a given activity just by using $auth.activity
     * 
     * @author Danilo Queiroz
     */
    public class AuthorizationHelper extends SelfInjectable {

    	@Inject
        private AuthorizationManager authorizationManager;

        @Inject
        private NamespaceManager namespaceManager;

        private String userId;
        private String domain;

        /**
         * @param userManager
         *            the userManager to set
         */
        public void setAuthorizationManager(AuthorizationManager authorizationManager) {
            this.authorizationManager = authorizationManager;
        }

        /**
         * @param namespaceManager
         *            the namespaceManager to set
         */
        public void setNamespaceManager(NamespaceManager namespaceManager) {
            this.namespaceManager = namespaceManager;
            this.domain = namespaceManager.currentNamespace();
        }
        

        public AuthorizationHelper(String userId) {
            this.userId = userId;
        }

		public boolean get(String activity) {
            try {
                this.namespaceManager.changeToGlobalNamespace();
                return authorizationManager.isAuthorized(this.domain, this.userId, activity);
            } finally {
                this.namespaceManager.changeToPreviousNamespace();
            }
        }
    }
}