/*
 *  Figgo - http://projeto.figgo.com.br
 *  Copyright (C) 2011 Octahedron - Coletivo Mundo
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
package br.octahedron.figgo.modules.service.manager;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.*;

import br.octahedron.cotopaxi.eventbus.EventBus;
import br.octahedron.figgo.modules.DataDoesNotExistsException;
import br.octahedron.figgo.modules.service.data.Service;
import br.octahedron.figgo.modules.service.data.ServiceContract;
import br.octahedron.figgo.modules.service.data.ServiceContract.ServiceContractStatus;
import br.octahedron.figgo.modules.service.data.ServiceContractDAO;

/**
 * @author Danilo Queiroz
 */
public class ServiceManagerTest {

	private ServiceManager serviceManager = new ServiceManager();
	private EventBus eventBus = createMock(EventBus.class);
	private ServiceContractDAO contractDAO = createMock(ServiceContractDAO.class);
	private Service service = new Service("unitTest", new BigDecimal(10), "development", "create unit tests");
	private ServiceContract contract;

	@Before
	public void setUp() {
		this.serviceManager.setEventBus(this.eventBus);
		this.serviceManager.setServiceContractDAO(contractDAO);
		this.contract = new ServiceContract("1", service, "contractor@email", "provider@email");
	}

	@After
	public void tearDown() {
		reset(this.eventBus, this.contractDAO);
	}

	@Test
	public void testMakePayment1() throws CanceledServiceContractException, OnlyServiceContractorException{
		contract.setStatus(ServiceContractStatus.COMPLETED);
		expect(this.contractDAO.get("1")).andReturn(contract);
		this.eventBus.publish(eq(new ServiceContractPaidEvent(contract)));
		replay(this.contractDAO, this.eventBus);

		this.serviceManager.makePayment("1", "contractor@email");

		assertTrue(this.contract.isPaid());
		verify(this.contractDAO, this.eventBus);
	}

	@Test
	public void testMakePayment2() throws CanceledServiceContractException, OnlyServiceContractorException{
		contract.setStatus(ServiceContractStatus.COMPLETED);
		contract.setPaid(true);
		expect(this.contractDAO.get("1")).andReturn(contract);
		replay(this.contractDAO, this.eventBus);

		this.serviceManager.makePayment("1", "contractor@email");

		assertTrue(this.contract.isPaid());
		verify(this.contractDAO, this.eventBus);
	}

	@Test
	public void testMakePaymentUncompleted() throws CanceledServiceContractException, OnlyServiceContractorException {
		assertEquals(ServiceContractStatus.PENDING, this.contract.getStatus());
		expect(this.contractDAO.get("1")).andReturn(contract);
		this.eventBus.publish(eq(new ServiceContractPaidEvent(contract)));
		replay(this.contractDAO, this.eventBus);
		this.serviceManager.makePayment("1", "contractor@email");
		assertTrue(this.contract.isPaid());
		assertEquals(ServiceContractStatus.COMPLETED, this.contract.getStatus());
		verify(this.contractDAO, this.eventBus);
	}

	@Test(expected = CanceledServiceContractException.class)
	public void testMakePaymentFailed1() throws CanceledServiceContractException, OnlyServiceContractorException{
		try {
			contract.setStatus(ServiceContractStatus.CANCELED);
			expect(this.contractDAO.get("1")).andReturn(contract);
			replay(this.contractDAO, this.eventBus);

			this.serviceManager.makePayment("1", "other@email");
		} finally {
			assertFalse(this.contract.isPaid());
			verify(this.contractDAO, this.eventBus);
		}
	}
	
	@Test(expected = OnlyServiceContractorException.class)
	public void testMakePaymentFailed2() throws CanceledServiceContractException, OnlyServiceContractorException{
		try {
			contract.setStatus(ServiceContractStatus.COMPLETED);
			expect(this.contractDAO.get("1")).andReturn(contract);
			replay(this.contractDAO, this.eventBus);

			this.serviceManager.makePayment("1", "other@email");
		} finally {
			assertFalse(this.contract.isPaid());
			verify(this.contractDAO, this.eventBus);
		}
	}

	@Test
	public void testRollbackPayment1() {
		contract.setStatus(ServiceContractStatus.COMPLETED);
		contract.setPaid(true);
		expect(this.contractDAO.get("1")).andReturn(contract);
		replay(this.contractDAO, this.eventBus);

		this.serviceManager.rollbackPayment("1");

		assertFalse(this.contract.isPaid());
		verify(this.contractDAO, this.eventBus);
	}

	@Test
	public void testRollbackPayment2() {
		expect(this.contractDAO.get("1")).andReturn(contract);
		replay(this.contractDAO, this.eventBus);

		this.serviceManager.rollbackPayment("1");

		assertFalse(this.contract.isPaid());
		verify(this.contractDAO, this.eventBus);
	}

	@Test(expected = DataDoesNotExistsException.class)
	public void testRollbackPaymentNotFound() {
		expect(this.contractDAO.get("1")).andReturn(null);
		replay(this.contractDAO, this.eventBus);
		try {
			this.serviceManager.rollbackPayment("1");
		} finally {
			verify(this.contractDAO, this.eventBus);
		}
	}

}
