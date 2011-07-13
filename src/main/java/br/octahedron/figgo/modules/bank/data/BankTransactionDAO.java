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

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.jdo.Query;

import br.octahedron.cotopaxi.datastore.GenericDAO;
import br.octahedron.figgo.modules.bank.TransactionInfoService;

/**
 * A DAO for transactions.
 * 
 * @author Danilo Queiroz
 */
public class BankTransactionDAO extends GenericDAO<BankTransaction> implements TransactionInfoService {

	public BankTransactionDAO() {
		super(BankTransaction.class);
	}

	public Collection<BankTransaction> getLastNTransactions(String accountId, int n) {
		return this.getAllTransactions(accountId, n);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.bank.TransactionInfoService#getLastTransactions(java.lang.Long,
	 * java.lang.Long)
	 */
	@Override
	public Collection<BankTransaction> getLastTransactions(String accountId, Long lastUsedTransactionId) {
		if (lastUsedTransactionId == null) {
			return this.getAllTransactions(accountId, Long.MIN_VALUE);
		} else {
			return this.getLastTransactionsFrom(accountId, lastUsedTransactionId);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.octahedron.straight.bank.TransactionInfoService#getTransactionsByDateRange(java.lang.String,
	 * java.util.Date, java.util.Date)
	 */
	@Override
	public Collection<BankTransaction> getTransactionsByDateRange(String accountId, Date startDate, Date endDate) {
		Collection<BankTransaction> transactions = this.getTransactionsByDateRange(startDate, endDate);
		TreeSet<BankTransaction> result = new TreeSet<BankTransaction>(new BankTransactionComparator());
		Iterator<BankTransaction> iterator = transactions.iterator();
		BankTransaction current;
		while (iterator.hasNext()) {
			current = iterator.next();
			if (current.belongsTo(accountId)) {
				result.add(current);
			}
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	protected Collection<BankTransaction> getTransactionsByDateRange(Date startDate, Date endDate) {
		Query query = this.datastoreFacade.createQueryForClass(BankTransaction.class);
		query.setFilter("date >= :startDate && date <= :endDate");
		return (List<BankTransaction>) query.execute(startDate, endDate);
	}

	/**
	 * Get last transactions with id greater than the given lastUsedTransactionId
	 */
	@SuppressWarnings("unchecked")
	private Collection<BankTransaction> getLastTransactionsFrom(String accountId, Long lastUsedTransactionId) {
		Query query = this.datastoreFacade.createQueryForClass(BankTransaction.class);
		query.setFilter("id > :transactionId && accountOrig == :accId");
		query.setOrdering("id asc");
		List<BankTransaction> transactions1 = (List<BankTransaction>) query.execute(lastUsedTransactionId, accountId);

		query = this.datastoreFacade.createQueryForClass(BankTransaction.class);
		query.setFilter("id > :transactionId && accountDest == :accId");
		query.setOrdering("id asc");
		List<BankTransaction> transactions2 = (List<BankTransaction>) query.execute(lastUsedTransactionId, accountId);

		return this.mergeTransactions(transactions1, transactions2, Long.MIN_VALUE);
	}

	/**
	 * Get all transactions for an account
	 */
	@SuppressWarnings("unchecked")
	private Collection<BankTransaction> getAllTransactions(String accountId, long count) {
		Query query = this.datastoreFacade.createQueryForClass(BankTransaction.class);
		query.setFilter("accountOrig == :accId");
		query.setOrdering("id asc");
		if (count > 0) {
			query.setRange(0, count);
		}
		List<BankTransaction> transactions1 = (List<BankTransaction>) query.execute(accountId);

		query = this.datastoreFacade.createQueryForClass(BankTransaction.class);
		query.setFilter("accountDest == :accId");
		query.setOrdering("id asc");
		if (count > 0) {
			query.setRange(0, count);
		}
		List<BankTransaction> transactions2 = (List<BankTransaction>) query.execute(accountId);

		return this.mergeTransactions(transactions1, transactions2, count);
	}

	/**
	 * Merges two transactions list ordering transactions by id (lower to higher)
	 * 
	 * @param count
	 * 
	 * @return a list with transactions from the two lists, ordered by id.
	 */
	private Collection<BankTransaction> mergeTransactions(Collection<BankTransaction> transactions1, Collection<BankTransaction> transactions2,
			long count) {
		TreeSet<BankTransaction> result = new TreeSet<BankTransaction>(new BankTransactionComparator());
		result.addAll(transactions1);
		result.addAll(transactions2);

		if (count == Long.MIN_VALUE) {
			return result;
		} else {
			TreeSet<BankTransaction> other = new TreeSet<BankTransaction>(new BankTransactionComparator());
			Iterator<BankTransaction> itr = result.descendingIterator();
			while (itr.hasNext() && count != 0) {
				other.add(itr.next());
				count--;
			}
			return other;
		}
	}

	private class BankTransactionComparator implements Comparator<BankTransaction> {
		public int compare(BankTransaction o1, BankTransaction o2) {
			return (int) (o1.getId() - o2.getId());
		}
	}

	/**
	 * @param string
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BigDecimal getBallast() {
		Query query = this.datastoreFacade.createQueryForClass(BankTransaction.class);
		query.setFilter("accountOrig == :accOrig");
		List<BankTransaction> transactions = (List<BankTransaction>) query.execute(SystemAccount.ID);
		BigDecimal sum = new BigDecimal(0.0);
		for (BankTransaction transaction : transactions) {
			sum = sum.add(transaction.getAmount());
		}
		return sum;
	}

	/**
	 * @param parse
	 * @param parse2
	 * @return
	 */
	public Collection<BankTransaction> getAmountByDateRange(String accountId, Date startDate, Date endDate) {
		Collection<BankTransaction> transactions = this.getTransactionsByDateRange(accountId, startDate, endDate);
		BigDecimal debit = new BigDecimal(0.0);
		BigDecimal credit = new BigDecimal(0.0);
		for (BankTransaction transaction : transactions) {
			if (transaction.isOrigin(accountId)) {
				debit = debit.add(transaction.getAmount());
			} else {
				credit = credit.add(transaction.getAmount());
			}
		}
		return null;
	}

}
