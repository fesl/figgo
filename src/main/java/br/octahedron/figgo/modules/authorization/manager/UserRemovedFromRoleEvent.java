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
package br.octahedron.figgo.modules.authorization.manager;

import br.octahedron.cotopaxi.eventbus.Event;

/**
 * This event is used to notify that an user has one (or all) of it roles removed.
 * 
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
public class UserRemovedFromRoleEvent implements Event {
	
	private static final long serialVersionUID = 7866107378718727518L;
	
	private String domain;
	private String userId;

	/**
	 * @param userId The userId
	 * @param domain The domain which a role has changed
	 */
	public UserRemovedFromRoleEvent(String userId, String domain) {
		this.userId = userId;
		this.domain = domain;
	}

	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	
	@Override
	public int hashCode() {
		return this.userId.hashCode() ^ this.domain.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if( obj instanceof UserRemovedFromRoleEvent) {
			UserRemovedFromRoleEvent other = (UserRemovedFromRoleEvent) obj;
			return this.userId.equals(other.userId) && this.domain.equals(other.domain);
		} else {
			return false;
		}
	}
}
