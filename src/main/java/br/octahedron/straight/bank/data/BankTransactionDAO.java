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

import java.util.Date;
import java.util.List;

import javax.jdo.Query;

import br.octahedron.straight.bank.TransactionInfoService;
import br.octahedron.straight.database.DatastoreFacade;
import br.octahedron.straight.database.GenericDAO;

/**
 * A DAO for transactions.
 * 
 * @author Danilo Queiroz
 */
public class BankTransactionDAO extends GenericDAO<BankTransaction> implements TransactionInfoService { 
	
	/*
	 * accountOrig == 'accID' && date >= 'startDate' && date < 'endDate'
	 * accountDest == 'accID' && date >= 'startDate' && date < 'endDate'
	 * 
	 * accountOrig == 'accID' && id > 'lastID'
	 * accountDest == 'accID' && id > 'lastID'
	 */

	public BankTransactionDAO() {
		super(BankTransaction.class);
	}

	/**
	 * Method to be used by tests. 
	 */
	protected void setDatastoreFacade(DatastoreFacade dsFacade) {
		this.datastoreFacade = dsFacade;
	}


	/* (non-Javadoc)
	 * @see br.octahedron.straight.bank.TransactionInfoService#getLastTransactions(java.lang.Long, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BankTransaction> getLastTransactions(Long accountId, Long lastUsedTransactionId) {
		/*
		 * (accountOri = accId || accountDest = accId) && tId > lastId
		 */
		Query query = this.datastoreFacade.createQueryForClass(BankTransaction.class);
		query.setFilter("id > transactionId && accountOrig == accId");
		query.declareParameters("Long transactionId");
		query.declareParameters("Long accId");
		query.setOrdering("id asc");
		List<BankTransaction> transactions1 = (List<BankTransaction>) query.execute(lastUsedTransactionId, accountId); 
		
		query = this.datastoreFacade.createQueryForClass(BankTransaction.class);
		query.setFilter("id > transactionId && accountDest == accId");
		query.declareParameters("Long transactionId");
		query.declareParameters("Long accId");
		query.setOrdering("id asc");
		List<BankTransaction> transactions2 = (List<BankTransaction>) query.execute(lastUsedTransactionId, accountId); 

		BankTransaction last1 = (!transactions1.isEmpty()) ? transactions1.get(transactions1.size() - 1) : null;
		BankTransaction last2 = (!transactions2.isEmpty()) ? transactions2.get(transactions2.size() - 1) : null;

		if (last1 != null && last2 != null) {
			Long id1 = last1.getId();
			Long id2 = last2.getId();
			if (id1.compareTo(id2) >= 0) {
				transactions2.addAll(transactions1);
				return transactions2;
			} else {
				transactions1.addAll(transactions2);
				return transactions1;
			}
		} else {
			transactions1.addAll(transactions2);
			return transactions1;
		}
	}

	/* (non-Javadoc)
	 * @see br.octahedron.straight.bank.TransactionInfoService#getTransactionsByDateRange(java.lang.Long, java.util.Date, java.util.Date)
	 */
	@Override
	public List<BankTransaction> getTransactionsByDateRange(Long accountId, Date startDate, Date endDate) {
//		List<BankTransaction> transactions = this.datastoreFacade.getObjectsByQuery(BankTransaction.class, "")
		
		
		return null;
	}
}
