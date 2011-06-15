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

import br.octahedron.straight.modules.admin.data.ApplicationConfiguration;
import br.octahedron.straight.modules.admin.util.Route53Exception;

/**
 * @author danilo
 *
 */
public interface AdminIF {

	/**
	 * @return <code>true</code> if this applications is configured, <code>false</code> otherwise.
	 */
	public abstract boolean hasApplicationConfiguration();

	/**
	 * @return
	 */
	public abstract ApplicationConfiguration getApplicationConfiguration();

	/**
	 * Configures this application
	 */
	public abstract void configureApplication(String route53AccessKeyID, String route53AccessKeySecret, String route53ZoneID);

	/**
	 * Creates the domain at Rout53
	 * 
	 * @throws Route53Exception if some error occurs when accessing route53 API
	 */
	public abstract void createDomain(String domainName, String adminID) throws Route53Exception;

}