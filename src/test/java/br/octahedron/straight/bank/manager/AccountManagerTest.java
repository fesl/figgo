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

import static org.easymock.EasyMock.*;

import java.math.BigDecimal;

import org.junit.Test;

import br.octahedron.straight.bank.data.Balance;
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

	@Test
	public void doSimpleTransaction() {
		BankAccount origin = createMock(BankAccount.class);
		BankAccount destination = createMock(BankAccount.class);
		BankAccountDAO accountDAO = createMock(BankAccountDAO.class);
		BankTransactionDAO transactionDAO = createMock(BankTransactionDAO.class);
		AccountManager accountManager = new AccountManager();
		accountManager.setAccountDAO(accountDAO);
		accountManager.setTransactionDAO(transactionDAO);
		
		expect(accountDAO.get("origin")).andReturn(origin);
		expect(accountDAO.get("destination")).andReturn(destination);
		expect(origin.getBalance()).andReturn(new BigDecimal(10));
		transactionDAO.save(notNull(BankTransaction.class));
		replay(accountDAO, transactionDAO, origin, destination);
		
		accountManager.transact("origin", "destination", new BigDecimal(2), "", TransactionType.TRANSFER);
	}
	
}
