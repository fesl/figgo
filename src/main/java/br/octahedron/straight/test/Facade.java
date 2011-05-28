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
package br.octahedron.straight.test;

import java.util.logging.Logger;

import br.octahedron.straight.modules.users.manager.UsersManager;

/**
 * @author vitoravelino
 *
 */
@SuppressWarnings("unused")
public class Facade {
	
	private static final Logger logger = Logger.getLogger(Facade.class.getName());
	
	private static Facade instance;
	
	private UsersManager usersManager;
	
	private Facade() {
		this.usersManager = new UsersManager();
	}
	
	public static Facade getInstance() {
		if (instance == null) {
			instance = new Facade();
		}
		return instance;
	}
	
	public void createUser(String userId, String name, String phoneNumber, String avatar, String description) {
		this.usersManager.createUser(userId, name, phoneNumber, avatar, description);
	}
	
	public boolean existsUser(String userId) {
		return this.usersManager.existsUser(userId);
	}
	
}
