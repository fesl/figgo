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
package br.octahedron.figgo.modules.authorization.data;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * @author Vítor Avelino
 *
 */
@PersistenceCapable
public class DomainUser implements Serializable {

	/**
	 * Creates the {@link DomainUser} primary key
	 * @return the primary key for the {@link DomainUser} for the given user/domain pair. 
	 */
	protected static String createDomainUserKey(String userId, String domain) {
		return userId + "#" + domain;
	}

	private static final long serialVersionUID = -1706113967230945694L;
	
	@SuppressWarnings("unused")
	@PrimaryKey
	@Persistent
	private String id;
	
	@Persistent
	private String userId;
	
	@Persistent
	private String domain;
	
	@Persistent
	private boolean isActive = false;

	public DomainUser(String userId, String domain, boolean active) {
		this.userId = userId;
		this.domain = domain;
		this.isActive = active;
		this.id = createDomainUserKey(this.userId, this.domain);
	}

	
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return this.userId;
	}
	
	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}
	
	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return this.isActive;
	}
	
	/**
	 * @param isActive the isActive to set
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}	
	
	public void markAsActive() {
		this.isActive = true;
	}
}
