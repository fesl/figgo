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
package br.octahedron.figgo.modules.services.data;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/**
 * @author vitoravelino
 *
 */
public class ServiceContractDAOTest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private final ServiceContractDAO serviceContractDAO = new ServiceContractDAO();
	
	@Before
	public void setUp() throws InstantiationException {
		this.helper.setUp();
	}
	
	private void createServiceContracts() {
		this.serviceContractDAO.save(new ServiceContract("Serviço1", "Pessoa1", "Pessoa2", new BigDecimal(100)));
		this.serviceContractDAO.save(new ServiceContract("Serviço4", "Pessoa2", "Pessoa3", new BigDecimal(50)));
		this.serviceContractDAO.save(new ServiceContract("Serviço3", "Pessoa3", "Pessoa1", new BigDecimal(10)));
		this.serviceContractDAO.save(new ServiceContract("Serviço6", "Pessoa3", "Pessoa2", new BigDecimal(35)));
		this.serviceContractDAO.save(new ServiceContract("Serviço2", "Pessoa1", "Pessoa3", new BigDecimal(80)));
	}
	
	@Test
	public void getProviderHistory() {
		this.createServiceContracts();
		Collection<ServiceContract> contracts = this.serviceContractDAO.getProviderHistory("Pessoa1");
		Iterator<ServiceContract> iterator = contracts.iterator();
		assertEquals(1, contracts.size());
		assertEquals("Serviço3", iterator.next().getServiceName());
		
		contracts = this.serviceContractDAO.getProviderHistory("Pessoa2");
		iterator = contracts.iterator();
		assertEquals(2, contracts.size());
		assertEquals("Serviço6", iterator.next().getServiceName());
		assertEquals("Serviço1", iterator.next().getServiceName());
	}
	
	@Test
	public void getContractorHistory() {
		this.createServiceContracts();
		Collection<ServiceContract> contracts = this.serviceContractDAO.getContractorHistory("Pessoa1");
		Iterator<ServiceContract> iterator = contracts.iterator();
		assertEquals(2, contracts.size());
		assertEquals("Serviço2", iterator.next().getServiceName());
		assertEquals("Serviço1", iterator.next().getServiceName());
		
		contracts = this.serviceContractDAO.getContractorHistory("Pessoa2");
		iterator = contracts.iterator();
		assertEquals(1, contracts.size());
		assertEquals("Serviço4", iterator.next().getServiceName());
	}
	
	@Test
	public void getHistory() {
		this.createServiceContracts();
		Collection<ServiceContract> contracts = this.serviceContractDAO.getHistory("Pessoa1");
		Iterator<ServiceContract> iterator = contracts.iterator();
		assertEquals(3, contracts.size());
		assertEquals("Serviço2", iterator.next().getServiceName());
		assertEquals("Serviço3", iterator.next().getServiceName());
		assertEquals("Serviço1", iterator.next().getServiceName());
		
		contracts = this.serviceContractDAO.getHistory("Pessoa3");
		iterator = contracts.iterator();
		assertEquals(4, contracts.size());
		assertEquals("Serviço2", iterator.next().getServiceName());
		assertEquals("Serviço6", iterator.next().getServiceName());
		assertEquals("Serviço3", iterator.next().getServiceName());
		assertEquals("Serviço4", iterator.next().getServiceName());
	}
	
}
