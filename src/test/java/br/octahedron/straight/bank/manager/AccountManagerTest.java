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

import static junit.framework.Assert.assertEquals;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.notNull;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import br.octahedron.straight.bank.data.BankAccount;
import br.octahedron.straight.bank.data.BankAccountDAO;
import br.octahedron.straight.bank.data.BankTransaction;
import br.octahedron.straight.bank.data.BankTransaction.TransactionType;
import br.octahedron.straight.bank.data.BankTransactionDAO;

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
	public void getValidAccount() {
		BankAccount account = createMock(BankAccount.class);

		expect(accountDAO.get("origin")).andReturn(account);
		expect(account.isEnabled()).andReturn(true);
		replay(accountDAO, account);

		accountManager.getValidAccount("origin");

		verify(accountDAO, account);
	}

	@Test(expected = DisabledBankAccountException.class)
	public void getValidAccountDisabled() {
		BankAccount account = createMock(BankAccount.class);
		try {
			expect(accountDAO.get("origin")).andReturn(account);
			expect(account.isEnabled()).andReturn(false);
			replay(accountDAO, account);

			accountManager.getValidAccount("origin");
		} finally {
			verify(accountDAO, account);
		}
	}

	@Test(expected = InexistentBankAccountException.class)
	public void getValidAccountNull() {
		BankAccount account = createMock(BankAccount.class);

		try {
			expect(accountDAO.get("origin")).andReturn(null);
			replay(accountDAO, account);

			accountManager.getValidAccount("origin");
		} finally{
			verify(accountDAO, account);
		}
	}

	@Test
	public void doSimpleTransaction() {
		BankAccount origin = createMock(BankAccount.class);
		BankAccount destination = createMock(BankAccount.class);
		
		expect(accountDAO.get("origin")).andReturn(origin);
		expect(origin.isEnabled()).andReturn(true);
		expect(accountDAO.get("destination")).andReturn(destination);
		expect(destination.isEnabled()).andReturn(true);

		expect(origin.getBalance()).andReturn(new BigDecimal(10));
		transactionDAO.save(notNull(BankTransaction.class));
		replay(accountDAO, transactionDAO, origin, destination);

		accountManager.transact("origin", "destination", new BigDecimal(2), "", TransactionType.TRANSFER);
		verify(accountDAO, transactionDAO, origin, destination);
	}

	@Test(expected = InsufficientBalanceException.class)
	public void doSimpleInsufficientTransaction() {
		BankAccount origin = createMock(BankAccount.class);
		BankAccount destination = createMock(BankAccount.class);
		try {
			expect(accountDAO.get("origin")).andReturn(origin);
			expect(origin.isEnabled()).andReturn(true);
			expect(accountDAO.get("destination")).andReturn(destination);
			expect(destination.isEnabled()).andReturn(true);
			
			expect(origin.getBalance()).andReturn(new BigDecimal(10));
			replay(accountDAO, transactionDAO, origin, destination);

			accountManager.transact("origin", "destination", new BigDecimal(20), "", TransactionType.TRANSFER);
		} finally {
			verify(accountDAO, transactionDAO, origin, destination);
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getBalance(){
		Long accID = new Long(12345);
		Long transID = new Long(0);
		BankAccount account = new BankAccount("teste", accID); 
		expect(accountDAO.get(accID)).andReturn(account);
		expect(transactionDAO.getLastTransactions(accID, transID)).andReturn(Collections.EMPTY_LIST);
		replay(accountDAO, transactionDAO);
		
		assertEquals(new BigDecimal(0), accountManager.getBalance(accID));
		verify(accountDAO, transactionDAO);
	}	
}
