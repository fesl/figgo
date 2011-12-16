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
 * Indicates that an user has been activated in a given domain
 * 
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
public class UserActivatedEvent implements Event {

	private static final long serialVersionUID = -178797810500703607L;
	
	private String userId;
	private String domain;

	/**
	 * @param userId
	 *            The activated user's id
	 * @param domain
	 *            The domain
	 */
	public UserActivatedEvent(String userId, String domain) {
		this.userId = userId;
		this.domain = domain;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

}
