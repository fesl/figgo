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
package br.octahedron.straight.modules.authorization.data;

import static junit.framework.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

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
		Role role = new Role("domain1", "admin");
		role.addUsers("developer");
		role.addActivities("commit_code", "create_tasks");
		roleDAO.save(role);
		role = new Role("domain1", "user");
		role.addUsers("developer", "tester");
		role.addActivities("change_tasks", "pull_code");
		roleDAO.save(role);
		role = new Role("domain2", "admin");
		role.addUsers("developer");
		role.addActivities("commit_code", "create_tasks");
		roleDAO.save(role);
		role = new Role("domain2", "user");
		role.addUsers("developer", "coach");
		role.addActivities("pull_code", "change_tasks");
		roleDAO.save(role);
		role = new Role("domain3", "admin");
		role.addUsers("developer", "coach");
		role.addActivities("commit_code", "create_tasks");
		roleDAO.save(role);
		role = new Role("domain3", "user");
		role.addUsers("developer", "coach", "tester");
		role.addActivities("pull_code", "change_tasks");
		roleDAO.save(role);
	}

	@Test
	public void queryRolesTest() {
		List<Role> roles = this.roleDAO.getUserRoles("none");
		assertTrue(roles.isEmpty());

		roles = this.roleDAO.getUserRoles("developer");
		assertEquals(6, roles.size());

		roles = this.roleDAO.getUserRoles("coach");
		assertEquals(3, roles.size());

		roles = this.roleDAO.getUserRoles("tester");
		assertEquals(2, roles.size());
	}

	@Test
	public void queryForRole() {
		// False
		assertFalse(this.roleDAO.existsRoleFor("domain1", "sysadmin", "commit_code"));
		assertFalse(this.roleDAO.existsRoleFor("domain1", "tester", "commit_code"));
		assertFalse(this.roleDAO.existsRoleFor("domain2", "coach", "commit_code"));

		// True
		assertTrue(this.roleDAO.existsRoleFor("domain3", "coach", "pull_code"));
		assertTrue(this.roleDAO.existsRoleFor("domain1", "tester", "change_tasks"));
		assertTrue(this.roleDAO.existsRoleFor("domain3", "coach", "create_tasks"));
	}

}
