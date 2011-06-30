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
package br.octahedron.figgo.modules.bank.data;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import br.octahedron.figgo.modules.bank.data.BankTransaction.TransactionType;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/**
 * @author Danilo Queiroz
 */
public class BankTransactionDAOTest  {

	
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private BankTransactionDAO bankTransactionDAO = new BankTransactionDAO();

	@Before
	public void setUp() throws InstantiationException {
		this.helper.setUp();
	}

	public void createTransactions() {
		// storing transactions
		this.bankTransactionDAO.save(new BankTransaction("FiggoBank", "Conta1", new BigDecimal(100), TransactionType.DEPOSIT, "1"));
		this.bankTransactionDAO.save(new BankTransaction("FiggoBank", "Conta1", new BigDecimal(100), TransactionType.DEPOSIT, "2"));
		this.bankTransactionDAO.save(new BankTransaction("Conta1", "FiggoBank", new BigDecimal(50), TransactionType.DEPOSIT, "3"));
	}

	@Test
	public void getLastNTransactionsTest() {
		this.createTransactions();
		String conta1 = "Conta1";
		Collection<BankTransaction> transactions = this.bankTransactionDAO.getLastNTransactions(conta1, 1);
		assertEquals(1, transactions.size());

		transactions = this.bankTransactionDAO.getLastNTransactions(conta1, 10);
		assertEquals(3, transactions.size());

		transactions = this.bankTransactionDAO.getLastNTransactions(conta1, 2);
		assertEquals(2, transactions.size());

		transactions = this.bankTransactionDAO.getLastNTransactions("helloworld", 10);
		assertTrue(transactions.isEmpty());
	}

	@Test
	public void getLastTransactionsTest() {
		this.createTransactions();
		String conta1 = "Conta1";
		// recovering all last transactions for account 1l
		Collection<BankTransaction> transactions = this.bankTransactionDAO.getLastTransactions(conta1, null);
		assertEquals(3, transactions.size());
		// recovering last transactions for account 1l
		transactions = this.bankTransactionDAO.getLastTransactions(conta1, 1l);
		assertEquals(2, transactions.size());
		// recovering last transactions for account 1l
		transactions = this.bankTransactionDAO.getLastTransactions(conta1, 3l);
		assertTrue(transactions.isEmpty());
	}

	@Test(expected = IllegalStateException.class)
	public void illegalStateTest() {
		this.createTransactions();
		BankAccount account = new BankAccount("tester");
		assertEquals(new BigDecimal(150), account.getBalance());
	}

	@Test
	public void getBalanceTest() {
		this.createTransactions();
		BankAccount account = new BankAccount("Conta1");
		account.setTransactionInfoService(this.bankTransactionDAO);
		assertEquals(new BigDecimal(150), account.getBalance());
		this.bankTransactionDAO.save(new BankTransaction("Conta1", "FiggoBank", new BigDecimal(50), TransactionType.DEPOSIT, "4"));
		assertEquals(new BigDecimal(100), account.getBalance());
		this.bankTransactionDAO.save(new BankTransaction("Conta1", "FiggoBank", new BigDecimal(50), TransactionType.DEPOSIT, "5"));
		this.bankTransactionDAO.save(new BankTransaction("Conta1", "FiggoBank", new BigDecimal(50), TransactionType.DEPOSIT, "6"));
		assertEquals(new BigDecimal(0), account.getBalance());
	}
}
