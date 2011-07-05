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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import br.octahedron.figgo.modules.bank.TransactionInfoService;

/**
 * @author Danilo Queiroz
 */
@PersistenceCapable
public class BankAccount implements Serializable {

	private static final long serialVersionUID = 7892638707825018254L;

	@PrimaryKey
	@Persistent
	private String ownerId;
	@Persistent
	private String password;
	@Persistent
	private boolean enabled;
	@Persistent
	private BigDecimal value;
	@Persistent
	private Long lastTransactionId;
	@NotPersistent
	private transient TransactionInfoService transactionInfoService;

	public BankAccount(String ownerId) {
		this.ownerId = ownerId;
		this.value = new BigDecimal(0);
		this.lastTransactionId = null;
		this.enabled = true;
	}

	public void setTransactionInfoService(TransactionInfoService tInfoService) {
		this.transactionInfoService = tInfoService;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * @param enabled
	 *            the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the ownerId
	 */
	public String getOwnerId() {
		return this.ownerId;
	}

	/**
	 * @return the balance's value
	 */
	public BigDecimal getBalance() {
		if (this.transactionInfoService == null) {
			throw new IllegalStateException("TransactionInfoService cannot be null. Must be set before balance operations");
		}

		Collection<BankTransaction> transactions = this.transactionInfoService.getLastTransactions(this.ownerId, this.lastTransactionId);

		if (!transactions.isEmpty()) {
			BigDecimal transactionsBalance = new BigDecimal(0);

			for (BankTransaction bankTransaction : transactions) {
				if (bankTransaction.getAccountOrig().equals(this.ownerId)) {
					transactionsBalance = transactionsBalance.subtract(bankTransaction.getAmount());
				} else {
					transactionsBalance = transactionsBalance.add(bankTransaction.getAmount());
				}

			}

			// We need to know what happens if this sum result if lower than zero
			// TODO LOG this event as a warning
			this.value = this.value.add(transactionsBalance);
			Object[] tArray = transactions.toArray();
			this.lastTransactionId = ((BankTransaction) tArray[tArray.length - 1]).getId();
		}

		return this.value;
	}
}