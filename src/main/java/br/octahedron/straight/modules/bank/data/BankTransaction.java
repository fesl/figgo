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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * @author Erick Moreno
 * 
 */
@PersistenceCapable
public class BankTransaction implements Serializable {

	private static final long serialVersionUID = -7989889136597879006L;
	
	public enum TransactionType {
		TRANSFER, PAYMENT, FINE, DEPOSIT
	};

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private String accountOrig;
	@Persistent
	private String accountDest;
	@Persistent
	private Date date;
	@Persistent
	private BigDecimal amount;
	@Persistent
	private String comment;
	@Persistent
	private TransactionType type;

	/**
	 * 
	 * @param accountOrig
	 * @param accountDest
	 * @param value
	 * @param type
	 * @param comment
	 */
	public BankTransaction(String accountOrig, String accountDest, BigDecimal value, TransactionType type, String comment) {
		this.accountOrig = accountOrig;
		this.accountDest = accountDest;
		this.amount = value;
		this.type = type;
		this.comment = comment;
		this.date = new Date();
	}

	/**
	 * Created only for tests purposes
	 * 
	 * @param accountOrig
	 * @param accountDest
	 * @param value
	 * @param type
	 * @param comment
	 */
	public BankTransaction(String accountOrig, String accountDest, BigDecimal value, TransactionType type, String comment, Long transactionId) {
		this(accountOrig, accountDest, value, type, comment);
		this.id = transactionId;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
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
		return this.date;
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
