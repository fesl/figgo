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
package br.octahedron.straight.bank;

import java.util.Date;
import java.util.List;

import br.octahedron.straight.bank.data.BankTransaction;

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
	 * @param accountId
	 *            the account id
	 * @param lastUsedTransactionId
	 *            the last {@link BankTransaction}. All {@link BankTransaction} returned will have
	 *            id greater than this id.
	 * @return A list that contains all transactions for the given user, starting from the given
	 *         {@link BankTransaction} id.
	 */
	public List<BankTransaction> getLastTransactions(Long accountId, Long lastUsedTransactioId);
	
	/**
	 * Get the transactions for a accountId, by date range.
	 * TODO improve
	 * 
	 * @param accountId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<BankTransaction> getTransactionsByDateRange(Long accountId, Date startDate, Date endDate);
}
