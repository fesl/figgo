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

import br.octahedron.cotopaxi.datastore.jdo.PersistenceManagerPool;
import br.octahedron.cotopaxi.eventbus.Event;
import br.octahedron.cotopaxi.eventbus.EventBus;
import br.octahedron.cotopaxi.eventbus.InterestedEvent;
import br.octahedron.cotopaxi.eventbus.Subscriber;
import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.cotopaxi.inject.SelfInjectable;
import br.octahedron.figgo.modules.bank.data.BankTransaction.TransactionType;
import br.octahedron.figgo.modules.service.manager.ServiceContractPaidEvent;
import br.octahedron.util.Log;

/**
 * This subscriber listen for {@link ServiceContractPaidEvent} events and performs the bank payment
 * 
 * If the contractor has no funds to perform the payment, it publish a new event to inform about
 * that.
 * 
 * @author vitoravelino
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
@InterestedEvent(events = { ServiceContractPaidEvent.class })
public class ContractPaidSubscriber extends SelfInjectable implements Subscriber {

	private Log logger = new Log();

	@Inject
	private AccountManager accountManager;
	@Inject
	private EventBus eventBus;

	/**
	 * @param accountManager
	 *            the accountManager to set
	 */
	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	/**
	 * @param eventBus
	 *            the eventBus to set
	 */
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	public void eventPublished(Event event) {
		ServiceContractPaidEvent evt = (ServiceContractPaidEvent) event;
		try {
			this.accountManager.transact(evt.getContractor(), evt.getProvider(), evt.getValue(), evt.getServiceName(), TransactionType.PAYMENT);
		} catch (Exception e) {
			logger.error(e, "Unable to perform the payment from user %s to user %s regards to contract %s. Unpaid value: %f", evt.getContractor(),
					evt.getProvider(), evt.getServiceName(), evt.getValue());
			this.eventBus.publish(new BankPaymentNotPerformedEvent(evt, e));
		}

		// REVIEW not sure if it's necessary once we are creating (and not updating) an object
		PersistenceManagerPool.forceClose();
	}
}
