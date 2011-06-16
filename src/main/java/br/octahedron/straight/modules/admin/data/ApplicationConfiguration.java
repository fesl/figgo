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

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * @author Danilo Queiroz
 */
@PersistenceCapable
public class ApplicationConfiguration implements Serializable, ApplicationConfigurationView { 
	
	private static final long serialVersionUID = 6211848005905311102L;

	public static final String APPLICATION_NAME = "Figgo";
	 
	@SuppressWarnings("unused")
	@PrimaryKey
	@Persistent
	private String applicationName = APPLICATION_NAME;
	
	@Persistent
	private String route53AccessKeyID;
	
	@Persistent
	private String route53AccessKeySecret;
	
	@Persistent
	private String route53ZoneID;

	/* (non-Javadoc)
	 * @see br.octahedron.straight.modules.admin.data.ApplicationConfigurationView#getRoute53AccessKeyID()
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

	/* (non-Javadoc)
	 * @see br.octahedron.straight.modules.admin.data.ApplicationConfigurationView#getRoute53AccessKeySecret()
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

	/* (non-Javadoc)
	 * @see br.octahedron.straight.modules.admin.data.ApplicationConfigurationView#getRoute53ZoneID()
	 */
	public String getRoute53ZoneID() {
		return route53ZoneID;
	}

	/**
	 * @param route53ZoneId the route53ZoneId to set
	 */
	public void setRoute53ZoneID(String route53ZoneId) {
		this.route53ZoneID = route53ZoneId;
	}


}