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

import java.util.LinkedHashSet;
import java.util.Set;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * @author Erick Moreno
 *
 */
@PersistenceCapable
public class Bank {
	
	@Persistent
	private String currency;
	@PrimaryKey @Persistent
	private String name;
	@Persistent
	private String currencyAbreviation;
	@Persistent
	private Set<String> operatorsIds;
	
	/**
	 * 
	 * @param name
	 * @param currency
	 * @param operatorId
	 */
	public Bank(String name, String currency, String operatorId){
		this.name = name;
		this.currency = currency;
		this.operatorsIds = new LinkedHashSet<String>();
		this.operatorsIds.add(operatorId);
	}
	
	
	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}
	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the currencyAbreviation
	 */
	public String getCurrencyAbreviation() {
		return currencyAbreviation;
	}
	/**
	 * @param currencyAbreviation the currencyAbreviation to set
	 */
	public void setCurrencyAbreviation(String currencyAbreviation) {
		this.currencyAbreviation = currencyAbreviation;
	}
	/**
	 * @return the operatorsIds
	 */
	public Set<String> getOperatorsIds() {
		return operatorsIds;
	}
	
	/**
	 * 
	 * @param operatorId
	 */
	public void addOperator(String operatorId){
		this.operatorsIds.add(operatorId);
	}
	
	/**
	 * 
	 * @param operatorId
	 */
	public void removeOperator(String operatorId){
		this.operatorsIds.remove(operatorId);
	}

}
