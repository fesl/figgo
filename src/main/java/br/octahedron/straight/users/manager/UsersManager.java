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
package br.octahedron.straight.users.manager;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;

import br.octahedron.straight.users.data.User;
import br.octahedron.straight.users.data.UserDAO;

/**
 * @author Erick Moreno
 *
 */
@PersistenceCapable
public class UsersManager implements Serializable{

	private static final long serialVersionUID = -8418976983506795060L;
	
	private UserDAO userDAO = new UserDAO();
	
	/**
	 * Creates a system user
	 * 
	 * @param userId
	 * @param name
	 * @param phoneNumber
	 * @param avatar
	 * @return
	 */
	public User createUser(String userId, String name, String phoneNumber, String avatar, String description){
		
		User user = new User(userId, name, phoneNumber, avatar, description);
		
		userDAO.save(user);
		return user;
	}
	
	/**
	 * Updates a system user parameters
	 * 
	 * @param userId
	 * @param name
	 * @param phoneNumber
	 * @param avatar
	 * @param description
	 */
	public void update(String userId, String name, String phoneNumber, String avatar, String description){
		User user = userDAO.get(userId);
		
		user.setName(name);
		user.setPhoneNumber(phoneNumber);
		user.setAvatar(avatar);
		user.setDescription(description);
		//This object will be updated to the DB by JDO persistence manager
	}
}
