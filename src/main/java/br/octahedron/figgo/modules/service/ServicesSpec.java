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
package br.octahedron.figgo.modules.service;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import br.octahedron.cotopaxi.eventbus.Subscriber;
import br.octahedron.figgo.modules.DomainModuleSpec;
import br.octahedron.figgo.modules.configuration.data.ModuleConfiguration;
import br.octahedron.figgo.modules.service.manager.PaymentFailedSubscriber;
import br.octahedron.figgo.modules.service.manager.ServiceCreatedSubscriber;
import br.octahedron.figgo.modules.service.manager.ServiceUpdatedSubscriber;

/**
 * @author Vítor Avelino
 */
public class ServicesSpec implements DomainModuleSpec {

	@Override
	public Type getModuleType() {
		return Type.DOMAIN;
	}

	@Override
	public boolean hasDomainSpecificConfiguration() {
		return false;
	}

	@Override
	public ModuleConfiguration getDomainSpecificModuleConfiguration() {
		return null;
	}

	@Override
	public boolean hasSubscribers() {
		return true;
	}

	@Override
	public Set<Class<? extends Subscriber>> getSubscribers() {
		Set<Class<? extends Subscriber>> subscribers = new LinkedHashSet<Class<? extends Subscriber>>();
		subscribers.add(ServiceCreatedSubscriber.class);
		subscribers.add(ServiceUpdatedSubscriber.class);
		subscribers.add(PaymentFailedSubscriber.class);
		return subscribers;
	}

	/* (non-Javadoc)
	 * @see br.octahedron.figgo.modules.ApplicationDomainModuleSpec#getModuleActions()
	 */
	@Override
	public Set<ActionSpec> getModuleActions() {
		Set<ActionSpec> actions = new HashSet<ActionSpec>();
		
		actions.add(new ActionSpec("ListServices"));
		actions.add(new ActionSpec("ShowService"));
		actions.add(new ActionSpec("AddProvider"));
		actions.add(new ActionSpec("RemoveProvider"));
		actions.add(new ActionSpec("UserServices"));

		actions.add(new ActionSpec("ShowContracts"));
		actions.add(new ActionSpec("ShowHistory"));
		actions.add(new ActionSpec("EditContract"));
		actions.add(new ActionSpec("PayContract"));
		actions.add(new ActionSpec("RequestContract"));
		actions.add(new ActionSpec("ServicesByCategory"));
		
		actions.add(new ActionSpec("NewService", true));
		actions.add(new ActionSpec("EditService", true));
		actions.add(new ActionSpec("RemoveService", true));
		
		return actions;
	}
}
