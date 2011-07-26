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
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * @author Vítor Avelino
 */
@PersistenceCapable
public class ServiceContract implements Serializable {

	private static final long serialVersionUID = -3731664858017404625L;

	public enum ServiceContractStatus {
		PENDING, IN_PROGRESS, COMPLETED;
	}
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private String serviceName;
	@Persistent
	private String contractor;
	@Persistent
	private String provider;
	@Persistent
	private BigDecimal amount;
	@Persistent
	private ServiceContractStatus status;
	@Persistent
	private Boolean paid;
	@Persistent
	private Date date;
	
	public ServiceContract(String serviceName, String contractor, String provider, BigDecimal amount) {
		this.serviceName = serviceName;
		this.contractor = contractor;
		this.provider = provider;
		this.amount = amount;
		this.status = ServiceContractStatus.PENDING;
		this.paid = false;
		this.date = new Date();
	}
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * @return the status
	 */
	public ServiceContractStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(ServiceContractStatus status) {
		this.status = status;
	}

	/**
	 * @return the paid
	 */
	public Boolean isPaid() {
		return paid;
	}
	
	/**
	 * @param paid the paid to set
	 */
	public void setPaid(Boolean paid) {
		this.paid = paid;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return this.amount;
	}

	/**
	 * @return the contractor id
	 */
	public String getContractor() {
		return this.contractor;
	}

	/**
	 * @return the provider id
	 */
	public String getProvider() {
		return this.provider;
	}

	/**
	 * @return the service name
	 */
	public String getServiceName() {
		return this.serviceName;
	}
	
	/**
	 * @return the date
	 */
	public Date getDate() {
		return this.date;
	}

	public String toString() {
		return "[" + id + ", " + serviceName + ", " + contractor + ", " + provider + "]";
	}

}
