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

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import br.octahedron.figgo.modules.service.data.ServiceContract;
import br.octahedron.figgo.modules.service.data.ServiceContractDAO;
import br.octahedron.figgo.modules.service.data.ServiceContract.ServiceContractStatus;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/**
 * @author vitoravelino
 *
 */
public class ServiceContractDAOTest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private final ServiceContractDAO serviceContractDAO = new ServiceContractDAO();
	private final ServiceDAO serviceDAO = new ServiceDAO();
	
	private Service service1 = new Service("test1", new BigDecimal(10), "Test", "some service");
	private Service service2 = new Service("test2", new BigDecimal(10), "Test", "some service");
	private Service service3 = new Service("test3", new BigDecimal(10), "Test", "some service");
	private Service service4 = new Service("test4", new BigDecimal(10), "Test", "some service");
	
	@Before
	public void setUp() throws InstantiationException {
		this.helper.setUp();
	}
	
	private void createServices() {
		this.serviceDAO.save(service1);
		this.serviceDAO.save(service2);
		this.serviceDAO.save(service3);
		this.serviceDAO.save(service4);
	}
	
	private void createServiceContracts() {
		this.serviceContractDAO.save(new ServiceContract(this.serviceDAO.get(service1.getId()), "Pessoa1", "Pessoa2"));
		this.serviceContractDAO.save(new ServiceContract(this.serviceDAO.get(service2.getId()), "Pessoa2", "Pessoa3"));
		this.serviceContractDAO.save(new ServiceContract(this.serviceDAO.get(service3.getId()), "Pessoa3", "Pessoa1"));
		this.serviceContractDAO.save(new ServiceContract(this.serviceDAO.get(service4.getId()), "Pessoa3", "Pessoa2"));
	}
	
	private void createHistoryServiceContracts() {
		ServiceContract serviceContract1 = new ServiceContract(this.serviceDAO.get(service1.getId()), "Pessoa1", "Pessoa2");
		serviceContract1.setStatus(ServiceContractStatus.COMPLETED);
		this.serviceContractDAO.save(serviceContract1);
		ServiceContract serviceContract2 = new ServiceContract(this.serviceDAO.get(service4.getId()), "Pessoa2", "Pessoa3");
		serviceContract2.setStatus(ServiceContractStatus.COMPLETED);
		this.serviceContractDAO.save(serviceContract2);
	}
	
	@Test
	public void getProviderContracts() {
		this.createServices();
		this.createServiceContracts();
		Collection<ServiceContract> contracts = this.serviceContractDAO.getProviderContracts("Pessoa1");
		Iterator<ServiceContract> iterator = contracts.iterator();
		assertEquals(1, contracts.size());
		assertEquals(service3, iterator.next().getService());
		
		contracts = this.serviceContractDAO.getProviderContracts("Pessoa2");
		iterator = contracts.iterator();
		assertEquals(2, contracts.size());
		assertEquals(service4, iterator.next().getService());
		assertEquals(service1, iterator.next().getService());
	}
	
	@Test
	public void getContractorContracts() {
		this.createServices();
		this.createServiceContracts();
		Collection<ServiceContract> contracts = this.serviceContractDAO.getContractorContracts("Pessoa1");
		Iterator<ServiceContract> iterator = contracts.iterator();
		assertEquals(1, contracts.size());
		assertEquals(service1, iterator.next().getService());
		
		contracts = this.serviceContractDAO.getContractorContracts("Pessoa2");
		iterator = contracts.iterator();
		assertEquals(1, contracts.size());
		assertEquals(service2, iterator.next().getService());
	}
	
	@Test
	public void getContracts() {
		this.createServices();
		this.createServiceContracts();
		Collection<ServiceContract> contracts = this.serviceContractDAO.getContracts("Pessoa1");
		Iterator<ServiceContract> iterator = contracts.iterator();
		assertEquals(2, contracts.size());
		assertEquals(service1, iterator.next().getService());
		assertEquals(service3, iterator.next().getService());
		
		contracts = this.serviceContractDAO.getContracts("Pessoa3");
		iterator = contracts.iterator();
		assertEquals(3, contracts.size());
		assertEquals(service2, iterator.next().getService());
		assertEquals(service3, iterator.next().getService());
		assertEquals(service4, iterator.next().getService());
	}
	
	@Test
	public void getHistory() {
		this.createServices();
		this.createHistoryServiceContracts();
		Collection<ServiceContract> contracts = this.serviceContractDAO.getHistory("Pessoa2");
		Iterator<ServiceContract> iterator = contracts.iterator();
		assertEquals(1, contracts.size());
		assertEquals(service1, iterator.next().getService());
	}
	
}
