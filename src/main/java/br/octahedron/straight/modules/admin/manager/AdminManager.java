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
package br.octahedron.straight.modules.admin.manager;

import static br.octahedron.straight.modules.admin.data.ApplicationConfiguration.APPLICATION_NAME;
import static br.octahedron.commons.eventbus.EventBus.*;
import br.octahedron.straight.modules.DataDoesNotExistsException;
import br.octahedron.straight.modules.admin.AdminIF;
import br.octahedron.straight.modules.admin.data.ApplicationConfiguration;
import br.octahedron.straight.modules.admin.data.ApplicationConfigurationDAO;
import br.octahedron.straight.modules.admin.data.ApplicationConfigurationView;
import br.octahedron.straight.modules.admin.util.Route53Exception;
import br.octahedron.straight.modules.admin.util.Route53Util;

/**
 * The manager for Application Configuration and admin operations
 * 
 * @author Danilo Penna Queiroz
 */
public class AdminManager implements AdminIF {
	
	private ApplicationConfigurationDAO applicationConfigurationDAO = new ApplicationConfigurationDAO();
	
	private void createApplicationConfiguration() {
		ApplicationConfiguration appConf = new ApplicationConfiguration();
		this.applicationConfigurationDAO.save(appConf);
	}

	/* (non-Javadoc)
	 * @see br.octahedron.straight.modules.admin.manager.AdminIF#hasApplicationConfiguration()
	 */
	public boolean hasApplicationConfiguration() {
		return this.applicationConfigurationDAO.exists(APPLICATION_NAME);
	}
	
	/* (non-Javadoc)
	 * @see br.octahedron.straight.modules.admin.manager.AdminIF#getApplicationConfiguration()
	 */
	public ApplicationConfigurationView getApplicationConfiguration() {
		if ( this.hasApplicationConfiguration() ) {
			return this.applicationConfigurationDAO.get(APPLICATION_NAME);
		} else {
			throw new DataDoesNotExistsException("This application isn't configured");
		}
	}
	
	/* (non-Javadoc)
	 * @see br.octahedron.straight.modules.admin.manager.AdminIF#configureApplication(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void configureApplication(String route53AccessKeyID, String route53AccessKeySecret, String route53ZoneID) {
		if (!this.hasApplicationConfiguration()) {
			this.createApplicationConfiguration();
		}
		
		ApplicationConfiguration appConf = this.applicationConfigurationDAO.get(APPLICATION_NAME);
		appConf.setRoute53AccessKeyID(route53AccessKeyID);
		appConf.setRoute53AccessKeySecret(route53AccessKeySecret);
		appConf.setRoute53ZoneID(route53ZoneID);
	}

	/* (non-Javadoc)
	 * @see br.octahedron.straight.modules.admin.manager.AdminIF#createDomain(java.lang.String, java.lang.String)
	 */
	public void createDomain(String domainName, String adminID) throws Route53Exception {
		if ( this.hasApplicationConfiguration() ) {
			ApplicationConfiguration appConf = this.applicationConfigurationDAO.get(APPLICATION_NAME);
			Route53Util.createDomain(domainName, appConf.getRoute53AccessKeyID(), appConf.getRoute53AccessKeySecret(), appConf.getRoute53ZoneID());
			publish(new DomainCreatedEvent(domainName, adminID));
		} else {
			throw new DataDoesNotExistsException("This application isn't configured");
		}
	}
}
