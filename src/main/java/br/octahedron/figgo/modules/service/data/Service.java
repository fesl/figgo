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

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Order;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import br.octahedron.commons.util.TextNormalizer;

/**
 * @author Erick Moreno
 */
@PersistenceCapable
public class Service implements Serializable {

	private static final long serialVersionUID = -1270664240981744528L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String id;
	@Persistent
	private String name;
	@Persistent
	private BigDecimal amount;
	@Persistent
	private String description;
	@Persistent
	private String category;
	@Persistent
	private String categoryId;
	@Persistent
	private Set<String> providers;
	@Persistent(mappedBy = "service")
	@Order(extensions = @Extension(vendorName = "datanucleus", key = "list-ordering", value = "date desc"))
	private Set<ServiceContract> contracts;

	public Service(String name, BigDecimal amount, String category, String description) {
		this.name = name;
		this.amount = amount;
		this.setCategory(category);
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

	public String getId() {
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
	 * @return the category
	 */
	public String getCategoryId() {
		return this.categoryId;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(String category) {
		this.categoryId = TextNormalizer.normalize(category);
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
	 * @return the contracts
	 */
	public Set<ServiceContract> getContracts() {
		return contracts;
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
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Service) {
			return this.getId().equals(((Service) obj).getId());
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("%s (%s)", this.getName(), this.getId());
	}

}
