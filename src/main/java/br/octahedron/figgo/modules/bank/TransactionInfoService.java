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
package br.octahedron.figgo.modules.bank;

import java.util.Collection;
import java.util.Date;

import br.octahedron.figgo.modules.bank.data.BankTransaction;

/**
 * An Information Service to recover {@link BankTransaction} informations.
 * 
 * @author Erick Moreno
 * @author Danilo Queiroz
 */
public interface TransactionInfoService {

	/**
	 * Return the last transactions for an account, with id greater with the given last used
	 * transaction id.
	 * 
	 * This method ensures that the list's last element will be the one with the greater id.
	 * However, there is no order guarantee for elements.
	 * 
	 * @param accountId
	 *            the account id
	 * @param lastUsedTransactionId
	 *            the last {@link BankTransaction}. All {@link BankTransaction} returned will have
	 *            id greater than this id. If you want to get all transactions for an account,
	 *            lastUsedTransactionId should be <code>null</code>;
	 * @return A list that contains all transactions for the given user, starting from the given
	 *         {@link BankTransaction} id.
	 */
	public Collection<BankTransaction> getLastTransactions(String accountId, Long lastUsedTransactioId);

	/**
	 * Get the transactions for a accountId, by date range. TODO improve
	 * 
	 * @param accountId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public Collection<BankTransaction> getTransactionsByDateRange(Long accountId, Date startDate, Date endDate);
}