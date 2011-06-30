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
package br.octahedron.figgo.test;

import static junit.framework.Assert.assertEquals;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import br.octahedron.figgo.test.Entity;
import br.octahedron.figgo.test.EntityDAO;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/**
 * Tests for Datastore Behavior
 * 
 * @author Danilo Queiroz
 */
public class TestEntity {

	private EntityDAO dao = new EntityDAO();

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

	@Before
	public void setUp() {
		this.helper.setUp();
		this.populate();
	}

	/**
	 * 
	 */
	private void populate() {
		Entity e = new Entity("1");
		e.add("a", "b", "c");
		this.dao.save(e);
		e = new Entity("2");
		e.add("a", "b");
		this.dao.save(e);
		e = new Entity("3");
		e.add("b");
		this.dao.save(e);
		e = new Entity("4");
		e.add("b", "c");
		this.dao.save(e);

	}

	@Test
	public void testGetEntityWithElement() {
		Collection<Entity> entities = this.dao.getEntitiesWithElement("b");
		assertEquals(4, entities.size());
		System.out.println(entities);

		entities = this.dao.getEntitiesWithElement("a");
		assertEquals(2, entities.size());
		System.out.println(entities);
	}
}
