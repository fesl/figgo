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
public class BankAccount implements Serializable {

	private static final long serialVersionUID = 7892638707825018254L;

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private long id;
	@Persistent
	private String ownerId;
	@Persistent
	private String password;
	@Persistent
	private boolean enabled;
	@Persistent
	private Balance balance;
	
	public BankAccount(String ownerId) {
		this.ownerId = ownerId;
		this.balance = new Balance();
	}
	
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}
	
	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * @return the ownerId
	 */
	public String getOwnerId() {
		return ownerId;
	}
	
	/**
	 * @return the balance's value
	 */
	public BigDecimal getBalance() {
		return balance.getValue();
	}
}
