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
package br.octahedron.straight.modules.users.data;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/**
 * @author Vítor Avelino
 *
 */
public class UsersDAOTest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private final UserDAO userDAO = new UserDAO();

	@Before
	public void setUp() {
		this.helper.setUp();
	}
	
	public void createUsers() {
		// storing users
		this.userDAO.save(new User("test3@example.com", "Name3", "8888 8888", ""));
		this.userDAO.save(new User("test@example.com", "name1", "8888 8888", ""));
		this.userDAO.save(new User("test2@example.com", "Name2", "8888 8888", ""));
		this.userDAO.save(new User("lalala@example.com", "User", "8888 8888", ""));
		this.userDAO.save(new User("user2@example.com", "Lalala", "8888 8888", ""));
	}
	
	@Test
	public void searchUser() {
		this.createUsers();
		Collection<User> result = this.userDAO.getUsersStartingWith("Name3");
		assertEquals(1, result.size());
		result  = this.userDAO.getUsersStartingWith("name");
		assertEquals(3, result.size());
		assertEquals("[[name1 test@example.com], [Name2 test2@example.com], [Name3 test3@example.com]]", result.toString());
		
		result = this.userDAO.getUsersStartingWith("test3");
		assertEquals(1, result.size());
		assertEquals("[[Name3 test3@example.com]]", result.toString());
		
		result  = this.userDAO.getUsersStartingWith("test");
		assertEquals(3, result.size());
		assertEquals("[[name1 test@example.com], [Name2 test2@example.com], [Name3 test3@example.com]]", result.toString());
		
		result  = this.userDAO.getUsersStartingWith("user");
		assertEquals(2, result.size());
		assertEquals("[[Lalala user2@example.com], [User lalala@example.com]]", result.toString());
		
		result  = this.userDAO.getUsersStartingWith("lal");
		assertEquals(2, result.size());
		assertEquals("[[Lalala user2@example.com], [User lalala@example.com]]", result.toString());
	}
}
