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
package br.octahedron.straight.bank.data;

import static junit.framework.Assert.*;
import static org.easymock.EasyMock.*;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.octahedron.straight.bank.data.BankTransaction.TransactionType;
import br.octahedron.straight.database.DatastoreFacade;

/**
 * @author Danilo Queiroz
 * 
 */
public class BankTransactionDAOTest {

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
		Long myId = new Long(10);
		Long otherId = new Long(9);
		List<BankTransaction> transactions1 = new LinkedList<BankTransaction>();
		for(int i = 1; i < 6; i++) {
			transactions1.add(new BankTransaction(myId, otherId, new BigDecimal(1), TransactionType.TRANSFER, "", new Long(i)));
		}
		List<BankTransaction> transactions2 = new LinkedList<BankTransaction>();
		for(int i = 6; i < 11; i++) {
			transactions2.add(new BankTransaction(otherId, myId, new BigDecimal(1), TransactionType.TRANSFER, "", new Long(i)));
		}
		expect(this.datastore.getObjectsByQuery(BankTransaction.class, "id > '0' && accountOrig == '10'", "id asc")).andReturn(transactions1);
		expect(this.datastore.getObjectsByQuery(BankTransaction.class, "id > '0' && accountDest == '10'", "id asc")).andReturn(transactions2);
		replay(this.datastore);
		
		List<BankTransaction> result = this.transactionDAO.getLastTransactions(myId, new Long(0));
		assertEquals(10, result.size());
		assertEquals(new BankTransaction(myId, otherId, new BigDecimal(1), TransactionType.TRANSFER, "", new Long(1)), result.get(0));
		assertEquals(new BankTransaction(otherId, myId, new BigDecimal(1), TransactionType.TRANSFER, "", new Long(10)), result.get(9));
		verify(this.datastore);
	}
	
	@Test
	public void testGetLastTransactions2() {
		/*
		 * Equals the anterior test, but changing the list deliver order
		 */
		Long myId = new Long(10);
		Long otherId = new Long(9);
		List<BankTransaction> transactions2 = new LinkedList<BankTransaction>();
		for(int i = 1; i < 6; i++) {
			transactions2.add(new BankTransaction(otherId, myId, new BigDecimal(1), TransactionType.TRANSFER, "", new Long(i)));
		}
		List<BankTransaction> transactions1 = new LinkedList<BankTransaction>();
		for(int i = 6; i < 11; i++) {
			transactions1.add(new BankTransaction(myId, otherId, new BigDecimal(1), TransactionType.TRANSFER, "", new Long(i)));
		}
		expect(this.datastore.getObjectsByQuery(BankTransaction.class, "id > '0' && accountOrig == '10'", "id asc")).andReturn(transactions1);
		expect(this.datastore.getObjectsByQuery(BankTransaction.class, "id > '0' && accountDest == '10'", "id asc")).andReturn(transactions2);
		replay(this.datastore);
		
		List<BankTransaction> result = this.transactionDAO.getLastTransactions(myId, new Long(0));
		assertEquals(10, result.size());
		assertEquals(new BankTransaction(myId, otherId, new BigDecimal(1), TransactionType.TRANSFER, "", new Long(1)), result.get(0));
		assertEquals(new BankTransaction(otherId, myId, new BigDecimal(1), TransactionType.TRANSFER, "", new Long(10)), result.get(9));
		verify(this.datastore);
	}
}