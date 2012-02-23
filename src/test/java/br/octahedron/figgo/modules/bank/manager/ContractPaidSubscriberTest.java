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
package br.octahedron.figgo.modules.bank.manager;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.octahedron.cotopaxi.eventbus.EventBus;
import br.octahedron.figgo.modules.bank.data.BankTransaction.TransactionType;
import br.octahedron.figgo.modules.service.manager.ServiceContractPaidEvent;

/**
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
public class ContractPaidSubscriberTest {
	
	private AccountManager account = createMock(AccountManager.class);
	private EventBus bus = createMock(EventBus.class);
	
	private ContractPaidSubscriber subscriber = new ContractPaidSubscriber();
	
	@Before
	public void setUp() {
		this.subscriber.setAccountManager(this.account);
		this.subscriber.setEventBus(this.bus);
	}
	
	@After
	public void tearDown() {
		reset(this.account, this.bus);
	}

	@Test
	public void testPaymentOk() throws InsufficientBalanceException, DisabledBankAccountException {
		this.account.transact("contractor@email", "provider@email", new BigDecimal("10"), "unitTest", TransactionType.PAYMENT);
		replay(this.account, this.bus);
		
		this.subscriber.eventPublished(new ServiceContractPaidEvent("1", "contractor@email", "provider@email", new BigDecimal("10"), "unitTest"));
		
		verify(this.account, this.bus);
	}
	
	@Test
	public void testPaymentFailed() throws InsufficientBalanceException, DisabledBankAccountException {
		Exception e = new InsufficientBalanceException("contractor@email has insufficient balance to make transaction");
		ServiceContractPaidEvent evt = new ServiceContractPaidEvent("1", "contractor@email", "provider@email", new BigDecimal("10"), "unitTest");
		
		this.account.transact("contractor@email", "provider@email", new BigDecimal("10"), "unitTest", TransactionType.PAYMENT);
		expectLastCall().andThrow(e);
		this.bus.publish(eq(new BankPaymentNotPerformedEvent(evt, e)));
		replay(this.account, this.bus);
		
		this.subscriber.eventPublished(evt);
		
		verify(this.account, this.bus);
	}

}
