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
package br.octahedron.straight.bank.manager;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.notNull;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import br.octahedron.straight.bank.data.BankAccount;
import br.octahedron.straight.bank.data.BankAccountDAO;
import br.octahedron.straight.bank.data.BankTransaction;
import br.octahedron.straight.bank.data.BankTransactionDAO;
import br.octahedron.straight.bank.data.BankTransaction.TransactionType;

/**
 * @author Vítor Avelino
 *
 */
public class AccountManagerTest {

	private BankAccountDAO accountDAO;
	private BankTransactionDAO transactionDAO;
	private AccountManager accountManager;

	/**
	 * 
	 */
	@Before
	public void setUp() {
		accountDAO = createMock(BankAccountDAO.class);
		transactionDAO = createMock(BankTransactionDAO.class);
		accountManager = new AccountManager();
		accountManager.setAccountDAO(accountDAO);
		accountManager.setTransactionDAO(transactionDAO);
	}
	
	@Test
	public void doSimpleTransaction() throws InsufficientBalanceException {
		BankAccount origin = createMock(BankAccount.class);
		BankAccount destination = createMock(BankAccount.class);
		expect(accountDAO.get("origin")).andReturn(origin);
		expect(origin.getBalance()).andReturn(new BigDecimal(10));
		transactionDAO.save(notNull(BankTransaction.class));
		replay(accountDAO, transactionDAO, origin, destination);
		
		accountManager.transact("origin", "destination", new BigDecimal(2), "", TransactionType.TRANSFER);
		verify(accountDAO, transactionDAO, origin, destination);
	}
	
	@Test(expected = InsufficientBalanceException.class)
	public void doSimpleInsufficientTransaction() throws InsufficientBalanceException {
		BankAccount origin = createMock(BankAccount.class);
		BankAccount destination = createMock(BankAccount.class);
		try {
			expect(accountDAO.get("origin")).andReturn(origin);
			expect(origin.getBalance()).andReturn(new BigDecimal(10));
			replay(accountDAO, transactionDAO, origin, destination);

			accountManager.transact("origin", "destination", new BigDecimal(20), "", TransactionType.TRANSFER);
		} finally {
			verify(accountDAO, transactionDAO, origin, destination);
		}
	}


	
	public void getBalance(){
		
	}
	
}
