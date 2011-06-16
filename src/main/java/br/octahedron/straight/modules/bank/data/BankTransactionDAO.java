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
package br.octahedron.straight.modules.bank.data;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.Query;

import br.octahedron.commons.database.GenericDAO;
import br.octahedron.straight.modules.bank.TransactionInfoService;

/**
 * A DAO for transactions.
 * 
 * @author Danilo Queiroz
 */
public class BankTransactionDAO extends GenericDAO<BankTransaction> implements TransactionInfoService {

	public BankTransactionDAO() {
		super(BankTransaction.class);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.bank.TransactionInfoService#getLastTransactions(java.lang.Long,
	 * java.lang.Long)
	 */
	@Override
	public List<BankTransaction> getLastTransactions(String accountId, Long lastUsedTransactionId) {
		if (lastUsedTransactionId == null) {
			return this.getAllTransactions(accountId);
		} else {
			return this.getLastTransactionsFrom(accountId, lastUsedTransactionId);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.octahedron.straight.bank.TransactionInfoService#getTransactionsByDateRange(java.lang.Long,
	 * java.util.Date, java.util.Date)
	 */
	@Override
	public List<BankTransaction> getTransactionsByDateRange(Long accountId, Date startDate, Date endDate) {
		// List<BankTransaction> transactions =
		// this.datastoreFacade.getObjectsByQuery(BankTransaction.class, "")

		return null;
	}

	/**
	 * Get last transactions with id greater than the given lastUsedTransactionId
	 */
	@SuppressWarnings("unchecked")
	private List<BankTransaction> getLastTransactionsFrom(String accountId, Long lastUsedTransactionId) {
		Query query = this.datastoreFacade.createQueryForClass(BankTransaction.class);
		query.setFilter("id > transactionId && accountOrig == accId");
		query.declareParameters("java.lang.Long transactionId, java.lang.Long accId");
		query.setOrdering("id asc");
		List<BankTransaction> transactions1 = (List<BankTransaction>) query.execute(lastUsedTransactionId, accountId);

		query = this.datastoreFacade.createQueryForClass(BankTransaction.class);
		query.setFilter("id > transactionId && accountDest == accId");
		query.declareParameters("java.lang.Long transactionId, java.lang.Long accId");
		query.setOrdering("id asc");
		List<BankTransaction> transactions2 = (List<BankTransaction>) query.execute(lastUsedTransactionId, accountId);

		return this.mergeTransactions(transactions1, transactions2);
	}

	/**
	 * Get all transactions for an account
	 */
	@SuppressWarnings("unchecked")
	private List<BankTransaction> getAllTransactions(String accountId) {
		Query query = this.datastoreFacade.createQueryForClass(BankTransaction.class);
		query.setFilter("accountOrig == accId");
		query.declareParameters("java.lang.Long accId");
		query.setOrdering("id asc");
		List<BankTransaction> transactions1 = (List<BankTransaction>) query.execute(accountId);

		query = this.datastoreFacade.createQueryForClass(BankTransaction.class);
		query.setFilter("accountDest == accId");
		query.declareParameters("java.lang.Long accId");
		query.setOrdering("id asc");
		List<BankTransaction> transactions2 = (List<BankTransaction>) query.execute(accountId);

		return this.mergeTransactions(transactions1, transactions2);
	}

	/**
	 * Merges two transactions list ordering transactions by id (lower to higher)
	 * 
	 * @return a list with transactions from the two lists, ordered by id.
	 */
	private List<BankTransaction> mergeTransactions(List<BankTransaction> transactions1, List<BankTransaction> transactions2) {
		List<BankTransaction> result = new LinkedList<BankTransaction>();

		BankTransaction last1 = (!transactions1.isEmpty()) ? transactions1.get(transactions1.size() - 1) : null;
		BankTransaction last2 = (!transactions2.isEmpty()) ? transactions2.get(transactions2.size() - 1) : null;
		if (last1 != null && last2 != null) {
			Long id1 = last1.getId();
			Long id2 = last2.getId();
			if (id1.compareTo(id2) >= 0) {
				result.addAll(transactions2);
				result.addAll(transactions1);
			} else {
				result.addAll(transactions1);
				result.addAll(transactions2);
			}
		} else {
			result.addAll(transactions1);
			result.addAll(transactions2);
		}
		return result;
	}
}
