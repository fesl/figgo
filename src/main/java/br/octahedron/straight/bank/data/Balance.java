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

import java.io.Serializable;
import java.math.BigDecimal;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * @author Danilo Queiroz
 */
@PersistenceCapable
public class Balance implements Serializable {

	private static final long serialVersionUID = 1316539659486997084L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent(mappedBy = "balance")
	private BankAccount bankAccount;
	@Persistent
	private BigDecimal value;
	@Persistent
	private long lastTransactionId;

	/**
	 * 
	 */
	public Balance() {
		this.value = new BigDecimal(0);
		this.lastTransactionId = 0;
	}

	/**
	 * @return the value
	 */
	public BigDecimal getValue() {
		return this.value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	protected void setValue(BigDecimal value) {
		this.value = value;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * @return the bankAccount
	 */
	public BankAccount getBankAccount() {
		return this.bankAccount;
	}

	/**
	 * @param lastTransactionId
	 *            the lastTransactionId to set
	 */
	public void setLastTransactionId(long lastTransactionId) {
		this.lastTransactionId = lastTransactionId;
	}

	/**
	 * @return the lastTransactionId
	 */
	public long getLastTransactionId() {
		return this.lastTransactionId;
	}
}
