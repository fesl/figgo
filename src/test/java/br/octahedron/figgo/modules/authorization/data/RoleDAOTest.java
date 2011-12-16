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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.octahedron.figgo.modules.authorization.data.Role;
import br.octahedron.figgo.modules.authorization.data.RoleDAO;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/**
 * @author Danilo Queiroz
 */
public class RoleDAOTest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private RoleDAO roleDAO = new RoleDAO();

	@Before
	public void setUp() {
		this.helper.setUp();
		this.initDB();
	}

	private void initDB() {
		Role role = new Role("admin");
		role.addUsers("developer");
		role.addActivities("commit_code", "create_tasks");
		this.roleDAO.save(role);
		role = new Role("user");
		role.addUsers("developer", "tester");
		role.addActivities("change_tasks", "pull_code");
		this.roleDAO.save(role);
	}

	@Test
	public void queryForUserRoles() {
		List<Role> roles = this.roleDAO.getUserRoles("none");
		assertTrue(roles.isEmpty());

		roles = this.roleDAO.getUserRoles("developer");
		assertEquals(2, roles.size());

		roles = this.roleDAO.getUserRoles("coach");
		assertEquals(0, roles.size());

		roles = this.roleDAO.getUserRoles("tester");
		assertEquals(1, roles.size());
	}
	
	@Test
	public void queryForUserRolesByDomain() {
		List<Role> roles = this.roleDAO.getUserRoles("none");
		assertTrue(roles.isEmpty());
		
		roles = this.roleDAO.getUserRoles("none");
		assertTrue(roles.isEmpty());

		roles = this.roleDAO.getUserRoles("developer");
		assertEquals(2, roles.size());
		
		roles = this.roleDAO.getUserRoles("tester");
		assertEquals(1, roles.size());
	}

	@Test
	public void queryForRole() {
		// False
		assertFalse(this.roleDAO.existsRoleFor("sysadmin", "commit_code"));
		assertFalse(this.roleDAO.existsRoleFor("tester", "commit_code"));

		// True
		assertTrue(this.roleDAO.existsRoleFor("tester", "change_tasks"));
	}
	
	@Test
	public void queryGetAll() {
		Collection<Role> roles = this.roleDAO.getAll();
		assertEquals(2, roles.size());
	}

}
