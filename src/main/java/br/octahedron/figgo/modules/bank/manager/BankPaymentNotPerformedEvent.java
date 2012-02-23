/*
 *  Figgo - http://projeto.figgo.com.br
 *  Copyright (C) 2011 Octahedron - Coletivo Mundo
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
package br.octahedron.figgo.modules.bank.manager;

import java.math.BigDecimal;

import br.octahedron.cotopaxi.eventbus.Event;
import br.octahedron.figgo.modules.service.manager.ServiceContractPaidEvent;

/**
 * Event to notifies that a bank payment could not be performed.
 * 
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
public class BankPaymentNotPerformedEvent implements Event {

	private static final long serialVersionUID = -4703671294064118721L;

	private String serviceName;
	private BigDecimal value;
	private String contractor;
	private String provider;
	private String contractId;
	private Exception error;

	/**
	 * @param paidEvent
	 *            The paid event that bank was not able to perform the payment
	 * @param ex
	 *            The exception that avoid the payment - In general it is a
	 *            {@link DisabledBankAccountException} or a {@link InsufficientBalanceException}
	 */
	public BankPaymentNotPerformedEvent(ServiceContractPaidEvent paidEvent, Exception ex) {
		this.contractId = paidEvent.getContractId();
		this.provider = paidEvent.getProvider();
		this.contractor = paidEvent.getContractor();
		this.value = paidEvent.getValue();
		this.serviceName = paidEvent.getServiceName();
		this.error = ex;
	}

	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return this.serviceName;
	}

	/**
	 * @return the value
	 */
	public BigDecimal getValue() {
		return this.value;
	}

	/**
	 * @return the contractor
	 */
	public String getContractor() {
		return this.contractor;
	}

	/**
	 * @return the provider
	 */
	public String getProvider() {
		return this.provider;
	}

	/**
	 * @return the contractId
	 */
	public String getContractId() {
		return this.contractId;
	}

	/**
	 * @return the error
	 */
	public Exception getError() {
		return this.error;
	}

	@Override
	public int hashCode() {
		return this.contractId.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BankPaymentNotPerformedEvent) {
			BankPaymentNotPerformedEvent other = (BankPaymentNotPerformedEvent) obj;
			return this.contractId.equals(other.contractId);
		} else {
			return false;
		}
	}
}
