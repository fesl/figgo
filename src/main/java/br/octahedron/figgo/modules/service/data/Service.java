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
package br.octahedron.figgo.modules.service.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import java.util.TreeSet;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * @author Erick Moreno
 */
@PersistenceCapable
public class Service implements Serializable {

	private static final long serialVersionUID = -1270664240981744528L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private String name;
	@Persistent
	private BigDecimal amount;
	@Persistent
	private String description;
	@Persistent
	private String category;
	@Persistent
	private Set<String> providers;

	public Service(String name, BigDecimal amount, String category, String description) {
		this.name = name;
		this.amount = amount;
		this.category = category;
		this.description = description;
		this.providers = new TreeSet<String>();
	}

	public void addProvider(String userId) {
		this.providers.add(userId);
	}

	public void removeProvider(String userId) {
		this.providers.remove(userId);
	}
	
	public boolean hasProvider(String userId) {
		return this.providers.contains(userId);
	}
	
	public Long getId() {
		return this.id;
	}

	/**
	 * @return the providers
	 */
	public Set<String> getProviders() {
		return this.providers;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return this.category;
	}
	
	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	
	/**
	 * @return the value
	 */
	public BigDecimal getAmount() {
		return this.amount;
	}

	/**
	 * @param amount
	 *            the value to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param name
	 * 			the name to set
	 */
	public void setName(String name) {
		this.name = name;
		
	}

}
