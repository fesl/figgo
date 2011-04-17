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

import java.util.List;

import br.octahedron.straight.bank.TransactionInfoService;
import br.octahedron.straight.database.DatastoreFacade;
import br.octahedron.straight.database.GenericDAO;

/**
 * @author Danilo Queiroz
 */
public class BankTransactionDAO extends GenericDAO<BankTransaction> implements TransactionInfoService {

	public BankTransactionDAO() {
		super(BankTransaction.class);
	}
	
	protected void setDatastoreFacade(DatastoreFacade dsFacade) {
		this.datastoreFacade = dsFacade;
	}

	/**
	 * @param i
	 * @param j
	 * @return
	 */
	@Override
	public List<BankTransaction> getLastTransactions(Long accountId, Long lastUsedTransactionId) {
		/*
		 * (accountOri = accId || accountDest = accId) && tId > lastId
		 */
		List<BankTransaction> transactions1 = this.datastoreFacade.getObjectsByQuery(BankTransaction.class, "id > '" + lastUsedTransactionId + "' && accountOrig == '" + accountId + "'",
				"id asc");
		List<BankTransaction> transactions2 = this.datastoreFacade.getObjectsByQuery(BankTransaction.class, "id > '" + lastUsedTransactionId + "' && accountDest == '" + accountId + "'",
				"id asc");

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
}
