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
	private Long accountOrig;
	@Persistent
	private Long accountDest;
	@Persistent
	private Date date;
	@Persistent
	private BigDecimal value;
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
	public BankTransaction(Long accountOrig, Long accountDest, BigDecimal value, TransactionType type, String comment) {
		this.accountOrig = accountOrig;
		this.accountDest = accountDest;
		this.value = value;
		this.type = type;
		this.comment = comment;

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
	public Long getAccountOrig() {
		return this.accountOrig;
	}

	/**
	 * @return the accountDest
	 */
	public Long getAccountDest() {
		return this.accountDest;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return this.date;
	}

	/**
	 * @return the value
	 */
	public BigDecimal getValue() {
		return this.value;
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

}
