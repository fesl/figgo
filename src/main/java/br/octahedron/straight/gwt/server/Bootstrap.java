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
package br.octahedron.straight.gwt.server;

import java.math.BigDecimal;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import br.octahedron.straight.bank.data.BankAccount;
import br.octahedron.straight.bank.data.BankAccountDAO;
import br.octahedron.straight.bank.data.BankTransaction;
import br.octahedron.straight.bank.manager.AccountManager;

/**
 * @author vitoravelino
 *
 */
public class Bootstrap implements ServletContextListener {

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		AccountManager accountManager = new AccountManager();
		BankAccountDAO accountDAO = new BankAccountDAO();
		BankAccount account1 = new BankAccount("joao", 2L);
		BankAccount account2 = new BankAccount("maria", 3L);
		accountDAO.save(account1);
		accountDAO.save(account2);
		accountManager.transact(0L, 2L, new BigDecimal(100), "", BankTransaction.TransactionType.DEPOSIT);
		accountManager.transact(0L, 3L, new BigDecimal(250.50), "", BankTransaction.TransactionType.DEPOSIT);
		accountManager.transact(2L, 3L, new BigDecimal(50), "", BankTransaction.TransactionType.DEPOSIT);
	}

}
