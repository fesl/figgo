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
package br.octahedron.figgo.modules.bank.manager;

import br.octahedron.cotopaxi.eventbus.Event;
import br.octahedron.cotopaxi.eventbus.InterestedEvent;
import br.octahedron.cotopaxi.eventbus.Subscriber;
import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.cotopaxi.inject.SelfInjectable;
import br.octahedron.figgo.modules.bank.data.BankTransaction.TransactionType;
import br.octahedron.figgo.modules.service.data.ServiceContract;
import br.octahedron.figgo.modules.service.manager.ServiceContractPaidEvent;

/**
 * @author vitoravelino
 * 
 */
@InterestedEvent(events = { ServiceContractPaidEvent.class })
public class ContractPaidSubscriber extends SelfInjectable implements Subscriber {

	@Inject
	private AccountManager accountManager;

	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.octahedron.cotopaxi.eventbus.Subscriber#eventPublished(br.octahedron.cotopaxi.eventbus
	 * .Event)
	 */
	@Override
	public void eventPublished(Event event) {
		try {
			ServiceContractPaidEvent paidEvent = (ServiceContractPaidEvent) event;
			ServiceContract serviceContract = paidEvent.getServiceContract();
			this.accountManager.transact(serviceContract.getContractor(), serviceContract.getProvider(), serviceContract.getAmount(), "",
					TransactionType.PAYMENT);
		} catch (Exception e) {
			// TODO ROLLBACK PAYMENT
			// FIRE EVENT TO ROLL BACK PAYMENT, SENDING THE CAUSE.
			// TODO notifies the origin account owner 
		}
	}

}
