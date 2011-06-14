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
package br.octahedron.straight.modules.users;

import br.octahedron.straight.modules.users.data.User;
import br.octahedron.straight.modules.users.data.UserView;

/**
 * @author Erick Moreno
 *
 */
public interface UsersIF {
	
	/**
	 * Verify if exists a {@link User} with passed userId registered in the system
	 * @param userId
	 * @return true if the {@link User} exists, false otherwise
	 */
	public boolean existsUser(String userId);
	
	/**
	 * Returns, if exists, the {@link User} represented by the passed userId. 
	 * @param userId
	 * @return the {@link User} with the given id, if exists, or <code>null</code>, if doesn't
	 *         exists a user with the given id.
	 */
	public UserView getUser(String userId);
	
	/**
	 * Creates a {@link User} with passed parameters
	 * 
	 * @param userId
	 * @param name
	 * @param phoneNumber
	 * @param avatar
	 * @param description
	 */
	public UserView createUser(String userId, String name, String phoneNumber, String description);
	
	/**
	 * Updates {@link User} properties with passed parameters
	 * 
	 * @param userId
	 * @param name
	 * @param phoneNumber
	 * @param avatar
	 * @param description
	 */
	public UserView updateUser(String userId, String name, String phoneNumber, String description);

}
