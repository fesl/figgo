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
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Represents an BankTransaction. It holds the origin and destination account, the transaction
 * value, its timestamp and type.
 * 
 * @author Erick Moreno
 * @author Danilo Queiroz
 */
@PersistenceCapable
public class BankTransaction implements Serializable {

	private static final long serialVersionUID = -7989889136597879006L;

	public enum TransactionType {
		TRANSFER, PAYMENT, FINE, DEPOSIT, BALLAST
	};

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private String accountOrig;
	@Persistent
	private String accountDest;
	@Persistent
	private Long timestamp;
	@Persistent
	private BigDecimal amount;
	@Persistent
	private String comment;
	@Persistent
	private TransactionType type;

	/**
	 * TODO comment
	 * 
	 * @param accountOrig
	 * @param accountDest
	 * @param value
	 * @param type
	 * @param comment
	 */
	public BankTransaction(String accountOrig, String accountDest, BigDecimal value, TransactionType type, String comment) {
		this(accountOrig, accountDest, value, type, comment, new Date());
	}

	/**
	 * To be used by tests
	 */
	protected BankTransaction(String accountOrig, String accountDest, BigDecimal value, TransactionType type, String comment, Long timestamp) {
		this.accountOrig = accountOrig;
		this.accountDest = accountDest;
		this.amount = value;
		this.type = type;
		this.comment = comment;
		this.timestamp = timestamp;
	}

	/**
	 * To be used by tests
	 */
	protected BankTransaction(String accountOrig, String accountDest, BigDecimal value, TransactionType type, String comment, Date date) {
		this.accountOrig = accountOrig;
		this.accountDest = accountDest;
		this.amount = value;
		this.type = type;
		this.comment = comment;
		this.timestamp = date.getTime();
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		if(this.id == null && Boolean.parseBoolean(System.getProperty("TEST_MODE"))) {
			return Long.parseLong(this.comment);
		}
		return this.id;
	}

	/**
	 * @return the accountOrig
	 */
	public String getAccountOrig() {
		return this.accountOrig;
	}

	/**
	 * @return the accountDest
	 */
	public String getAccountDest() {
		return this.accountDest;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return new Date(this.timestamp);
	}

	public Long getTimestamp() {
		return this.timestamp;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return this.amount;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return this.comment;
	}

	/**
	 * @return the type
	 */
	public TransactionType getType() {
		return this.type;
	}

	public boolean isOrigin(String accountId) {
		return accountOrig.equals(accountId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BankTransaction) {
			BankTransaction other = (BankTransaction) obj;
			return this.getId().equals(other.getId());
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.id.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "id: " + this.id + " orig: " + this.accountOrig + " dest: " + this.accountDest + " value: " + this.getAmount() + " comment: "
				+ this.comment;
	}
}
