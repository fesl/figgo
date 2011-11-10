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
package br.octahedron.figgo.modules.service.data;

import static junit.framework.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/**
 * @author Danilo Queiroz
 */
public class ServiceDAOTest {
	
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private final ServiceDAO serviceDAO = new ServiceDAO();
	
	@Before
	public void setUp() throws InstantiationException {
		this.helper.setUp();
	}
	
	@Test
	public void testGetServiceByName() {
		assertNull(this.serviceDAO.getServiceByName("somename"));
		
		Service service = new Service("somename", new BigDecimal(10), "test", "");
		this.serviceDAO.save(service);
		assertNotNull(this.serviceDAO.getServiceByName("somename"));
		
		service = new Service("othername", new BigDecimal(10), "test", "");
		this.serviceDAO.save(service);
		assertNotNull(this.serviceDAO.getServiceByName("somename"));
		
		
		service = new Service("somename", new BigDecimal(20), "test", "");
		this.serviceDAO.save(service);
		// There's no real guarantee about this order
		assertEquals(new BigDecimal(10), this.serviceDAO.getServiceByName("somename").getAmount());
	}

}
