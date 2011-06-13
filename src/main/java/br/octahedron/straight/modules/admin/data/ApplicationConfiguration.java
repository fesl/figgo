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
package br.octahedron.straight.modules.admin.data;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * @author Danilo Queiroz
 */
@PersistenceCapable
public class ApplicationConfiguration {
	 
	@SuppressWarnings("unused")
	@PrimaryKey
	@Persistent
	private String applicationName = "figgo";
	
	@Persistent
	private String route53AccessKeyID;
	
	@Persistent
	private String route53AccessKeySecret;
	
	@Persistent
	private String route53ZoneId;

	/**
	 * @return the route53AccessKeyID
	 */
	public String getRoute53AccessKeyID() {
		return route53AccessKeyID;
	}

	/**
	 * @param route53AccessKeyID the route53AccessKeyID to set
	 */
	public void setRoute53AccessKeyID(String route53AccessKeyID) {
		this.route53AccessKeyID = route53AccessKeyID;
	}

	/**
	 * @return the route53AccessKeySecret
	 */
	public String getRoute53AccessKeySecret() {
		return route53AccessKeySecret;
	}

	/**
	 * @param route53AccessKeySecret the route53AccessKeySecret to set
	 */
	public void setRoute53AccessKeySecret(String route53AccessKeySecret) {
		this.route53AccessKeySecret = route53AccessKeySecret;
	}

	/**
	 * @return the route53ZoneId
	 */
	public String getRoute53ZoneId() {
		return route53ZoneId;
	}

	/**
	 * @param route53ZoneId the route53ZoneId to set
	 */
	public void setRoute53ZoneId(String route53ZoneId) {
		this.route53ZoneId = route53ZoneId;
	}


}