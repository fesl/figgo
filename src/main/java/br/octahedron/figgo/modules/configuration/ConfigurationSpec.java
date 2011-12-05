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
package br.octahedron.figgo.modules.configuration;

import java.util.HashSet;
import java.util.Set;

import br.octahedron.cotopaxi.eventbus.Subscriber;
import br.octahedron.figgo.modules.ApplicationDomainModuleSpec;
import br.octahedron.figgo.modules.configuration.manager.DomainCreatedSubscriber;
import br.octahedron.figgo.modules.configuration.manager.DomainChangedSubscriber;
import br.octahedron.figgo.modules.configuration.manager.DomainUploadSubscriber;

/**
 * @author vitoravelino
 * 
 */
public class ConfigurationSpec implements ApplicationDomainModuleSpec {


	@Override
	public Type getModuleType() {
		return Type.APPLICATION_DOMAIN;
	}

	@Override
	public boolean hasSubscribers() {
		return true;
	}

	@Override
	public Set<Class<? extends Subscriber>> getSubscribers() {
		Set<Class<? extends Subscriber>> subscribers = new HashSet<Class<? extends Subscriber>>();
		subscribers.add(DomainCreatedSubscriber.class);
		subscribers.add(DomainUploadSubscriber.class);
		subscribers.add(DomainChangedSubscriber.class);
		return subscribers;
	}

	@Override
	public Set<ActionSpec> getModuleActions() {
		Set<ActionSpec> actions = new HashSet<ActionSpec>();
		
		actions.add(new ActionSpec("DomainUpload", true));
		actions.add(new ActionSpec("EditDomain", true));
		actions.add(new ActionSpec("ModuleDomain", true));
		actions.add(new ActionSpec("EnableModuleDomain", true));
		actions.add(new ActionSpec("DisableModuleDomain", true));

		return actions;
	}


}
