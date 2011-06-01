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
package br.octahedron.straight.inject;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;

import br.octahedron.straight.database.DatastoreFacade;

/**
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class InjectorTest {
	
	private InstanceHandler handler = new InstanceHandler();

	@Test
	public void testInjection() throws InstantiationException {
		UserFacade facade = handler.getInstance(UserFacade.class);
		assertNotNull(facade);
		UserService service = facade.getUserService();
		assertNotNull(service);
		UserDAO userDAO = service.getUserDAO();
		assertNotNull(userDAO);
		DatastoreFacade ds = userDAO.getDatastoreFacade();
		assertTrue(ds instanceof DatastoreFacade);
	}
}