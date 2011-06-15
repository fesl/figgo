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
package br.octahedron.straight.modules.admin;

import br.octahedron.commons.database.NamespaceCommons;
import br.octahedron.commons.inject.Inject;
import br.octahedron.straight.modules.admin.data.ApplicationConfiguration;
import br.octahedron.straight.modules.admin.manager.AdminManager;
import br.octahedron.straight.modules.admin.util.Route53Exception;

/**
 * A decorator for the {@link AdminManager} class, that adjust to use the global namespace
 * 
 * @see AdminManager
 * @author Danilo Queiroz
 */
public class AdminDecorator implements AdminIF {
	
	@Inject
	private AdminManager adminManager;
	
	/**
	 * @param adminManager the adminManager to set
	 */
	public void setAdminManager(AdminManager adminManager) {
		this.adminManager = adminManager;
	}

		
	/**
	 * @see AdminManager#hasApplicationConfiguration()
	 */
	public boolean hasApplicationConfiguration() {
		try {
			NamespaceCommons.changeToGlobalNamespace();
			return this.adminManager.hasApplicationConfiguration();
		} finally {
			NamespaceCommons.backToPreviousNamespace();
		}
	}
	
	/**
	 * @see AdminManager#getApplicationConfiguration()
	 */
	public ApplicationConfiguration getApplicationConfiguration() {
		try {
			NamespaceCommons.changeToGlobalNamespace();
			return this.adminManager.getApplicationConfiguration();
		} finally {
			NamespaceCommons.backToPreviousNamespace();
		}
	}
	
	/**
	 * @see AdminManager#configureApplication(String, String, String)
	 */
	public void configureApplication(String route53AccessKeyID, String route53AccessKeySecret, String route53ZoneId) {
		try {
			NamespaceCommons.changeToGlobalNamespace();
			this.adminManager.configureApplication(route53AccessKeyID, route53AccessKeySecret, route53ZoneId);
		} finally {
			NamespaceCommons.backToPreviousNamespace();
		}
	}

	/**
	 * @see AdminManager#createDomain(String, String)
	 */
	public void createDomain(String domainName, String adminID) throws Route53Exception {
		try {
			NamespaceCommons.changeToGlobalNamespace();
			this.adminManager.createDomain(domainName, adminID);
		} finally {
			NamespaceCommons.backToPreviousNamespace();
		}
	}

}
