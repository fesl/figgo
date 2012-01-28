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
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.Query;

import org.junit.Before;
import org.junit.Test;

import br.octahedron.cotopaxi.datastore.jdo.DatastoreFacade;
import br.octahedron.figgo.modules.bank.data.BankTransaction.TransactionType;

/**
 * @author Danilo Queiroz
 * 
 */
public class BankTransactionDAOMockTest {
	
	static {
		System.setProperty("TEST_MODE", "true");
	}

	private DatastoreFacade datastore;
	private BankTransactionDAO transactionDAO;

	@Before
	public void setUp() {
		this.datastore = createMock(DatastoreFacade.class);
		this.transactionDAO = new BankTransactionDAO();
		this.transactionDAO.setDatastoreFacade(this.datastore);
	}

	@Test
	public void testGetLastTransactions() {
		String myId = "Conta10";
		String otherId = "Conta9";
		Long lastTransactionId = new Long(0);

		Query query1 = createMock(Query.class);
		List<BankTransaction> transactions1 = new LinkedList<BankTransaction>();
		for (int i = 1; i < 6; i++) {
			transactions1.add(new BankTransaction(myId, otherId, new BigDecimal(1), TransactionType.TRANSFER, String.valueOf(i)));
		}
		expect(this.datastore.createQueryForClass(BankTransaction.class)).andReturn(query1);
		query1.setFilter("timestamp >= :timestamp && accountOrig == :accId");
		query1.setOrdering("timestamp asc");
		expect(query1.execute(lastTransactionId, myId)).andReturn(transactions1);

		Query query2 = createMock(Query.class);
		List<BankTransaction> transactions2 = new LinkedList<BankTransaction>();
		for (int i = 6; i < 11; i++) {
			transactions2.add(new BankTransaction(otherId, myId, new BigDecimal(1), TransactionType.TRANSFER, String.valueOf(i)));
		}
		expect(this.datastore.createQueryForClass(BankTransaction.class)).andReturn(query2);
		query2.setFilter("timestamp >= :timestamp && accountDest == :accId");
		query2.setOrdering("timestamp asc");
		expect(query2.execute(lastTransactionId, myId)).andReturn(transactions2);

		replay(this.datastore, query1, query2);

		Collection<BankTransaction> result = this.transactionDAO.getLastTransactions(myId, new Long(0));
		assertEquals(10, result.size());
		assertTrue(result.contains(new BankTransaction(myId, otherId, new BigDecimal(1), TransactionType.TRANSFER, "1")));
		assertTrue(result.contains(new BankTransaction(otherId, myId, new BigDecimal(1), TransactionType.TRANSFER, "2")));
		verify(this.datastore, query1, query2);
	}

	@Test
	public void testGetLastTransactions2() {
		String myId = "Conta10";
		String otherId = "Conta9";
		Long lastTransactionId = new Long(0);

		Query query1 = createMock(Query.class);
		List<BankTransaction> transactions1 = new LinkedList<BankTransaction>();
		for (int i = 6; i < 11; i++) {
			transactions1.add(new BankTransaction(myId, otherId, new BigDecimal(1), TransactionType.TRANSFER, String.valueOf(i)));
		}
		expect(this.datastore.createQueryForClass(BankTransaction.class)).andReturn(query1);
		query1.setFilter("timestamp >= :timestamp && accountOrig == :accId");
		query1.setOrdering("timestamp asc");
		expect(query1.execute(lastTransactionId, myId)).andReturn(transactions1);

		Query query2 = createMock(Query.class);
		List<BankTransaction> transactions2 = new LinkedList<BankTransaction>();
		for (int i = 1; i < 6; i++) {
			transactions2.add(new BankTransaction(otherId, myId, new BigDecimal(1), TransactionType.TRANSFER, String.valueOf(i)));
		}
		expect(this.datastore.createQueryForClass(BankTransaction.class)).andReturn(query2);
		query2.setFilter("timestamp >= :timestamp && accountDest == :accId");
		query2.setOrdering("timestamp asc");
		expect(query2.execute(lastTransactionId, myId)).andReturn(transactions2);

		replay(this.datastore, query1, query2);

		Collection<BankTransaction> result = this.transactionDAO.getLastTransactions(myId, new Long(0));
		assertEquals(10, result.size());
		assertTrue(result.contains(new BankTransaction(myId, otherId, new BigDecimal(1), TransactionType.TRANSFER, "1")));
		assertTrue(result.contains(new BankTransaction(otherId, myId, new BigDecimal(1), TransactionType.TRANSFER, "10")));
		verify(this.datastore, query1, query2);
	}
}
