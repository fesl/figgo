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

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

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

	private String[] servicesIds = new String[4];

	@Before
	public void setUp() throws InstantiationException {
		this.helper.setUp();
	}

	private void createServices() {
		Service service = new Service("test1", new BigDecimal(10), "Test", "some service");
		this.serviceDAO.save(service);
		this.servicesIds[0] = service.getId();
		service = new Service("test2", new BigDecimal(10), "Test", "some service");
		this.serviceDAO.save(service);
		this.servicesIds[1] = service.getId();
		service = new Service("test3", new BigDecimal(10), "Test", "some service");
		this.serviceDAO.save(service);
		this.servicesIds[2] = service.getId();
		service = new Service("test4", new BigDecimal(10), "Test", "some service");
		this.serviceDAO.save(service);
		this.servicesIds[3] = service.getId();
	}

	private void createServiceContracts() {
		// service1 completed but not paid
		ServiceContract serviceContract = new ServiceContract(this.serviceDAO.get(this.servicesIds[0]), "Pessoa1", "Pessoa2");
		serviceContract.setStatus(ServiceContractStatus.COMPLETED);
		this.serviceContractDAO.save(serviceContract);
		// service2 pending
		serviceContract = new ServiceContract(this.serviceDAO.get(this.servicesIds[1]), "Pessoa2", "Pessoa3");
		serviceContract.setStatus(ServiceContractStatus.PENDING);
		this.serviceContractDAO.save(serviceContract);
		// service3 completed but not paid
		serviceContract = new ServiceContract(this.serviceDAO.get(this.servicesIds[2]), "Pessoa3", "Pessoa1");
		serviceContract.setStatus(ServiceContractStatus.IN_PROGRESS);
		this.serviceContractDAO.save(serviceContract);
		// service4 completed but not paid
		serviceContract = new ServiceContract(this.serviceDAO.get(this.servicesIds[3]), "Pessoa3", "Pessoa2");
		serviceContract.setStatus(ServiceContractStatus.COMPLETED);
		serviceContract.setPaid(true);
		this.serviceContractDAO.save(serviceContract);
	}

	@Test
	public void getProvidedContracts() {
		this.createServices();
		this.createServiceContracts();
		Collection<ServiceContract> contracts = this.serviceContractDAO.getProvidedContracts("Pessoa2");
		Iterator<ServiceContract> iterator = contracts.iterator();
		assertEquals(1, contracts.size());
		assertEquals("test4", iterator.next().getService().getName());

		// Pessoa3 has one provided contract but it's not paid yet
		contracts = this.serviceContractDAO.getProvidedContracts("Pessoa3");
		iterator = contracts.iterator();
		assertEquals(0, contracts.size());
	}

	@Test
	public void getHiredContracts() {
		this.createServices();
		this.createServiceContracts();
		// Pessoa3 has two hired contracts but only one is paid
		Collection<ServiceContract> contracts = this.serviceContractDAO.getHiredContracts("Pessoa3");
		Iterator<ServiceContract> iterator = contracts.iterator();
		assertEquals(1, contracts.size());
		assertEquals("test4", iterator.next().getService().getName());

		// Pessoa1 has one hired contract but it's not paid yet
		contracts = this.serviceContractDAO.getHiredContracts("Pessoa1");
		iterator = contracts.iterator();
		assertEquals(0, contracts.size());
	}

	@Test
	public void getOpenedProvidedContracts() {
		this.createServices();
		this.createServiceContracts();
		// Pessoa2 has two provided contracts but only one is not paid
		Collection<ServiceContract> contracts = this.serviceContractDAO.getOpenedProvidedContracts("Pessoa2");
		Iterator<ServiceContract> iterator = contracts.iterator();
		assertEquals(1, contracts.size());
		assertEquals("test1", iterator.next().getService().getName());

		// Pessoa1 has one provided contract and it's not paid
		contracts = this.serviceContractDAO.getOpenedProvidedContracts("Pessoa1");
		iterator = contracts.iterator();
		assertEquals(1, contracts.size());
		assertEquals("test3", iterator.next().getService().getName());
	}

	@Test
	public void getOpenedHiredContracts() {
		this.createServices();
		this.createServiceContracts();
		// Pessoa3 has two hired contracts but only one is not paid
		Collection<ServiceContract> contracts = this.serviceContractDAO.getOpenedHiredContracts("Pessoa3");
		Iterator<ServiceContract> iterator = contracts.iterator();
		assertEquals(1, contracts.size());
		assertEquals("test3", iterator.next().getService().getName());

		// Pessoa1 has one hired contract and it's not paid
		contracts = this.serviceContractDAO.getOpenedHiredContracts("Pessoa1");
		iterator = contracts.iterator();
		assertEquals(1, contracts.size());
		assertEquals("test1", iterator.next().getService().getName());
	}

}
