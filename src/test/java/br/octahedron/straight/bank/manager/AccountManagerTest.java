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
import java.math.MathContext;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.octahedron.straight.bank.data.BankAccount;
import br.octahedron.straight.bank.data.BankAccountDAO;
import br.octahedron.straight.bank.data.BankTransaction;
import br.octahedron.straight.bank.data.BankTransaction.TransactionType;
import br.octahedron.straight.bank.data.BankTransactionDAO;

/**
 * @author Vítor Avelino
 * @author Erick Moreno
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
		this.accountDAO = createMock(BankAccountDAO.class);
		this.transactionDAO = createMock(BankTransactionDAO.class);
		this.accountManager = new AccountManager();
		this.accountManager.setAccountDAO(this.accountDAO);
		this.accountManager.setTransactionDAO(this.transactionDAO);
	}

	@Test
	public void getValidAccount() {
		BankAccount account = createMock(BankAccount.class);

		Long origin = new Long(1);
		expect(this.accountDAO.get(origin)).andReturn(account);
		expect(account.isEnabled()).andReturn(true);
		replay(this.accountDAO, account);

		this.accountManager.getValidAccount(origin);

		verify(this.accountDAO, account);
	}

	@Test(expected = DisabledBankAccountException.class)
	public void getValidAccountDisabled() {
		Long origin = new Long(1);
		BankAccount account = createMock(BankAccount.class);
		try {
			expect(this.accountDAO.get(origin)).andReturn(account);
			expect(account.isEnabled()).andReturn(false);
			replay(this.accountDAO, account);

			this.accountManager.getValidAccount(origin);
		} finally {
			verify(this.accountDAO, account);
		}
	}

	@Test(expected = InexistentBankAccountException.class)
	public void getValidAccountNull() {
		Long origin = new Long(1);
		BankAccount account = createMock(BankAccount.class);

		try {
			expect(this.accountDAO.get(origin)).andReturn(null);
			replay(this.accountDAO, account);

			this.accountManager.getValidAccount(origin);
		} finally {
			verify(this.accountDAO, account);
		}
	}

	@Test
	public void doSimpleTransaction() {
		Long originId = new Long(1);
		Long destId = new Long(2);

		BankAccount origin = createMock(BankAccount.class);
		BankAccount destination = createMock(BankAccount.class);

		expect(this.accountDAO.get(originId)).andReturn(origin);
		expect(origin.isEnabled()).andReturn(true);
		expect(this.accountDAO.get(destId)).andReturn(destination);
		expect(destination.isEnabled()).andReturn(true);

		origin.setTransactionInfoService(this.transactionDAO);
		expect(origin.getBalance()).andReturn(new BigDecimal(10));
		this.transactionDAO.save(notNull(BankTransaction.class));
		replay(this.accountDAO, this.transactionDAO, origin, destination);

		this.accountManager.transact(originId, destId, new BigDecimal(2), "", TransactionType.TRANSFER);
		verify(this.accountDAO, this.transactionDAO, origin, destination);
	}

	@Test(expected = InsufficientBalanceException.class)
	public void doSimpleInsufficientTransaction() {
		Long originId = new Long(1);
		Long destId = new Long(2);
		BankAccount origin = createMock(BankAccount.class);
		BankAccount destination = createMock(BankAccount.class);
		try {
			expect(this.accountDAO.get(originId)).andReturn(origin);
			expect(origin.isEnabled()).andReturn(true);
			expect(this.accountDAO.get(destId)).andReturn(destination);
			expect(destination.isEnabled()).andReturn(true);
			origin.setTransactionInfoService(this.transactionDAO);
			expect(origin.getBalance()).andReturn(new BigDecimal(10));
			replay(this.accountDAO, this.transactionDAO, origin, destination);

			this.accountManager.transact(originId, destId, new BigDecimal(20), "", TransactionType.TRANSFER);
		} finally {
			verify(this.accountDAO, this.transactionDAO, origin, destination);
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getBalanceInicialValue() {
		Long accID = new Long(12345);
		Long transID = new Long(0);
		BankAccount account = new BankAccount("teste", accID);
		account.setEnabled(true);
		expect(this.accountDAO.get(accID)).andReturn(account);
		expect(this.transactionDAO.getLastTransactions(accID, transID)).andReturn(Collections.EMPTY_LIST);
		replay(this.accountDAO, this.transactionDAO);

		assertEquals(new BigDecimal(0), this.accountManager.getBalance(accID));
		verify(this.accountDAO, this.transactionDAO);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void getBalanceSomeTransactions() {
		Long accID = new Long(12345);
		Long accID2 = new Long (54321);
		Long transID = new Long(0);
		Long transID1 = new Long(3);
		Long transID2 = new Long(6);
		Long transID3 = new Long(234);
		BigDecimal value1 = new BigDecimal(2.01);
		BigDecimal value2 = new BigDecimal(123456.91);
		BigDecimal value3 = new BigDecimal(23.31);
		
		BankAccount account = new BankAccount("teste", accID);
		account.setEnabled(true);
		expect(this.accountDAO.get(accID)).andReturn(account);
		List<BankTransaction> transactions = new LinkedList<BankTransaction>();
		
		transactions.add(new BankTransaction(accID, accID2, value1, BankTransaction.TransactionType.TRANSFER, "1", transID1));
		transactions.add(new BankTransaction(accID2, accID, value2, BankTransaction.TransactionType.TRANSFER, "2", transID2));
		transactions.add(new BankTransaction(accID2, accID, value3, BankTransaction.TransactionType.TRANSFER, "3", transID3));
		
		expect(this.transactionDAO.getLastTransactions(accID, transID)).andReturn(transactions);
		
		replay(this.accountDAO, this.transactionDAO);

		assertEquals(new BigDecimal(123478.21).round(new MathContext(2)), this.accountManager.getBalance(accID).round(new MathContext(2)));
		
		verify(this.accountDAO, this.transactionDAO);
	}
}
