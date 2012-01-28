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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import br.octahedron.figgo.modules.bank.data.BankTransaction.TransactionType;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/**
 * @author Danilo Queiroz
 */
public class BankTransactionDAOTest {

	private static final Long date1;
	private static final Long date2;
	private static final Long date3;
	
	static {
		date1 = new Date().getTime();
		date2 = date1+2*1000;
		date3 = date2 +10;
		try {
			Thread.sleep(1000*5);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private final DateFormat formatter = new SimpleDateFormat("dd/MM/yy");
	private final BankTransactionDAO bankTransactionDAO = new BankTransactionDAO();

	@Before
	public void setUp() throws InstantiationException {
		this.helper.setUp();
	}

	public void createTransactions() {
		// storing transactions
		this.bankTransactionDAO.save(new BankTransaction("FiggoBank", "Conta1", new BigDecimal(100), TransactionType.DEPOSIT, "1", date1));
		this.bankTransactionDAO.save(new BankTransaction("FiggoBank", "Conta1", new BigDecimal(100), TransactionType.DEPOSIT, "2", date2));
		this.bankTransactionDAO.save(new BankTransaction("Conta1", "FiggoBank", new BigDecimal(50), TransactionType.DEPOSIT, "3", date3));
	}

	private void createDateTransactions() throws ParseException {
		this.bankTransactionDAO.save(new BankTransaction("FiggoBank", "Conta1", new BigDecimal(100), TransactionType.DEPOSIT, "2", formatter
				.parse("16/01/2011")));
		this.bankTransactionDAO.save(new BankTransaction("Conta1", "FiggoBank", new BigDecimal(100), TransactionType.DEPOSIT, "2", formatter
				.parse("10/01/2011")));
		this.bankTransactionDAO.save(new BankTransaction("FiggoBank", "Conta1", new BigDecimal(100), TransactionType.DEPOSIT, "2", formatter
				.parse("11/01/2011")));
		this.bankTransactionDAO.save(new BankTransaction("FiggoBank", "Conta3", new BigDecimal(100), TransactionType.DEPOSIT, "2", formatter
				.parse("11/01/2011")));
		this.bankTransactionDAO.save(new BankTransaction("FiggoBank", "Conta2", new BigDecimal(100), TransactionType.DEPOSIT, "2", formatter
				.parse("12/01/2011")));
		this.bankTransactionDAO.save(new BankTransaction("Conta1", "FiggoBank", new BigDecimal(100), TransactionType.DEPOSIT, "2", formatter
				.parse("13/01/2011")));
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
		transactions = this.bankTransactionDAO.getLastTransactions(conta1, date1);
		assertEquals(3, transactions.size());
		// recovering last transactions for account 1l
		transactions = this.bankTransactionDAO.getLastTransactions(conta1, date2);
		assertEquals(2, transactions.size());
		// recovering last transactions for account 1l
		transactions = this.bankTransactionDAO.getLastTransactions(conta1, date3);
		assertEquals(1, transactions.size());
	}

	@Test(expected = IllegalStateException.class)
	public void illegalStateTest() {
		this.createTransactions();
		BankAccount account = new BankAccount("tester");
		assertEquals(new BigDecimal(150), account.getBalance());
	}

	@Test
	public void getBalanceTest() throws InterruptedException {
		this.createTransactions();
		BankAccount account = new BankAccount("Conta1");
		account.setTransactionInfoService(this.bankTransactionDAO);
		assertEquals(new BigDecimal(150), account.getBalance());
		this.bankTransactionDAO.save(new BankTransaction("Conta1", "FiggoBank", new BigDecimal(50), TransactionType.DEPOSIT, "4"));
		assertEquals(new BigDecimal(100), account.getBalance());
		Thread.sleep(1000);
		this.bankTransactionDAO.save(new BankTransaction("Conta1", "FiggoBank", new BigDecimal(50), TransactionType.DEPOSIT, "5"));
		this.bankTransactionDAO.save(new BankTransaction("Conta1", "FiggoBank", new BigDecimal(50), TransactionType.DEPOSIT, "6"));
		assertEquals(new BigDecimal(0), account.getBalance());
	}
	
	@Test
	public void getBalanceTest2() throws InterruptedException {
		this.createTransactions();
		BankAccount account = new BankAccount("Conta1");
		account.setTransactionInfoService(this.bankTransactionDAO);
		assertEquals(new BigDecimal(150), account.getBalance());
		this.bankTransactionDAO.save(new BankTransaction("Conta1", "FiggoBank", new BigDecimal(50), TransactionType.DEPOSIT, "4"));
		assertEquals(new BigDecimal(100), account.getBalance());
		Date now = new Date();
		this.bankTransactionDAO.save(new BankTransaction("Conta1", "FiggoBank", new BigDecimal(50), TransactionType.DEPOSIT, "5", now));
		this.bankTransactionDAO.save(new BankTransaction("Conta1", "FiggoBank", new BigDecimal(50), TransactionType.DEPOSIT, "6", now));
		assertEquals(new BigDecimal(0), account.getBalance());
		this.bankTransactionDAO.save(new BankTransaction("FiggoBank", "Conta1", new BigDecimal(50), TransactionType.DEPOSIT, "7", now));
		this.bankTransactionDAO.save(new BankTransaction("Conta1", "FiggoBank", new BigDecimal(10), TransactionType.DEPOSIT, "8"));
		assertEquals(new BigDecimal(40), account.getBalance());
	}

	@Test
	public void getBallast() {
		this.bankTransactionDAO.save(new BankTransaction("FiggoBank", "mundo", new BigDecimal(100), TransactionType.DEPOSIT, "2"));
		this.bankTransactionDAO.save(new BankTransaction("FiggoBank", "mundo", new BigDecimal(100), TransactionType.DEPOSIT, "2"));
		assertEquals(new BigDecimal(200), this.bankTransactionDAO.getBallast());

		this.bankTransactionDAO.save(new BankTransaction("FiggoBank", "mundo", new BigDecimal(100), TransactionType.DEPOSIT, "2"));
		this.bankTransactionDAO.save(new BankTransaction("FiggoBank", "mundo", new BigDecimal(100), TransactionType.DEPOSIT, "2"));
		assertEquals(new BigDecimal(400), this.bankTransactionDAO.getBallast());
	}

	@Test
	public void getTransactionsByDateRange() throws ParseException {
		this.createDateTransactions();
		assertEquals(1, this.bankTransactionDAO.getTransactionsByDateRange("Conta1", formatter.parse("12/01/11"), formatter.parse("15/01/11")).size());
		assertEquals(2, this.bankTransactionDAO.getTransactionsByDateRange("Conta1", formatter.parse("11/01/11"), formatter.parse("15/01/11")).size());
	}

	@Test
	public void getAmountCreditByDateRange() throws ParseException {
		this.createDateTransactions();
		assertEquals(BigDecimal.ZERO,
				this.bankTransactionDAO.getAmountCreditByDateRange("Conta1", formatter.parse("12/01/11"), formatter.parse("15/01/11")));
		assertEquals(new BigDecimal(100),
				this.bankTransactionDAO.getAmountCreditByDateRange("Conta1", formatter.parse("11/01/11"), formatter.parse("15/01/11")));
	}

	@Test
	public void getAmountTransactionsByDateRange() throws ParseException {
		this.createDateTransactions();
		assertEquals(new BigDecimal(200), this.bankTransactionDAO.getAmountByDateRange(formatter.parse("12/01/11"), formatter.parse("15/01/11")));
		assertEquals(new BigDecimal(400), this.bankTransactionDAO.getAmountByDateRange(formatter.parse("11/01/11"), formatter.parse("15/01/11")));
	}

}
