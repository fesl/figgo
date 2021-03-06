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
package br.octahedron.figgo.modules.service.manager;

import br.octahedron.cotopaxi.datastore.jdo.PersistenceManagerPool;
import br.octahedron.cotopaxi.eventbus.Event;
import br.octahedron.cotopaxi.eventbus.InterestedEvent;
import br.octahedron.cotopaxi.eventbus.Subscriber;
import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.cotopaxi.inject.SelfInjectable;
import br.octahedron.figgo.modules.DataDoesNotExistsException;
import br.octahedron.figgo.modules.bank.manager.BankPaymentNotPerformedEvent;
import br.octahedron.util.Log;

/**
 * This subscriber listen for {@link BankPaymentNotPerformedEvent} events and performs a rollback of
 * the service payment, marking service contract as not paid.
 * 
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
@InterestedEvent(events = BankPaymentNotPerformedEvent.class)
public class PaymentFailedSubscriber extends SelfInjectable implements Subscriber {

	private Log logger = new Log();

	@Inject
	private ServiceManager serviceManager;

	/**
	 * @param serviceManager
	 *            the serviceManager to set
	 */
	public void setServiceManager(ServiceManager serviceManager) {
		this.serviceManager = serviceManager;
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	public void eventPublished(Event event) {
		BankPaymentNotPerformedEvent bankEvt = (BankPaymentNotPerformedEvent) event;
		try {
			this.serviceManager.rollbackPayment(bankEvt.getContractId());
		} catch (DataDoesNotExistsException e) {
			logger.error(e, "Error rolling service contract payment back. There's no contract with id %s", bankEvt.getContractId());
		}

		PersistenceManagerPool.forceClose();
	}
}
